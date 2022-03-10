package com.mwallet.tps.service;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mwallet.tps.exception.PricingNotFoundException;
import com.mwallet.tps.exception.TestException;
import com.mwallet.tps.exception.WalletNotFoundException;
import com.mwallet.tps.modal.ATMTransactions;
import com.mwallet.tps.modal.ATMWallet;
import com.mwallet.tps.modal.Pricing;
import com.mwallet.tps.modal.Transactions;
import com.mwallet.tps.modal.User;
import com.mwallet.tps.modal.Wallet;
import com.mwallet.tps.payload.request.LoadWalletRequest;
import com.mwallet.tps.payload.request.TransferFundRequest;
import com.mwallet.tps.payload.request.WithdrawFundRequest;
import com.mwallet.tps.payload.response.LoadWalletResponse;
import com.mwallet.tps.payload.response.TransferFundResponse;
import com.mwallet.tps.payload.response.WithdrawFundResponse;
import com.mwallet.tps.repository.AtmWalletRepository;
import com.mwallet.tps.repository.AtmTransactionRepository;
import com.mwallet.tps.repository.CustomerRepository;
import com.mwallet.tps.repository.PricingRepository;
import com.mwallet.tps.repository.TransactionRepository;
import com.mwallet.tps.repository.WalletRepository;
import com.mwallet.tps.utility.Utilities;

@Service
public class PaymentService {

	@Autowired
	TransactionRepository txnrepository;
	@Autowired
	PricingRepository pricingRepository;
	@Autowired
	WalletRepository walletRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	AtmWalletRepository atmWalletRepository;
	@Autowired
	AtmTransactionRepository atmTransactionRepository;

	@Transactional
	public LoadWalletResponse loadAccount(LoadWalletRequest loadRequest) {

		BigDecimal amount;
		String walletid;
		String paymode = "C";
		String txnType;
		double txnCharges = 0;
		String txnCode;
		String systemRef;
		LoadWalletResponse response = new LoadWalletResponse();
		try {
			amount = loadRequest.getAmount();
			walletid = loadRequest.getWalletId();
			// get the txn mode
			txnType = Utilities.getLoadAccountCode();
			// generate transaction codes
			txnCode = Utilities.generateTransactionCode(12);
			systemRef = Utilities.getLoadAccountCode() + "-" + Utilities.generateTransactionCode(14);

			Optional<Pricing> txn_Price = pricingRepository.findByTxnType(txnType);
			Pricing txnPricing = txn_Price.orElseThrow(
					() -> new PricingNotFoundException("No user pricing details for paymode : " + txnType));

			if (txnPricing.getFixedFee().doubleValue() != 0) {
				txnCharges = txnPricing.getFixedFee().doubleValue();
			} else if (txnPricing.getVarFee().doubleValue() != 0) {
				txnCharges = (txnPricing.getVarFee().doubleValue()) * amount.doubleValue();
			}

			// check minimal transaction amount
			if (amount.doubleValue() < txnPricing.getMinAmount().doubleValue()) {
				response.setStatusCode("057"); // Transaction not allowed
				response.setTransactionCode(txnCode);
				response.setMessage(
						"Transaction amount is less than the minimum amount allowed: KES " + txnPricing.getMinAmount());
				response.setAmount(amount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			// check max amount per transaction
			if (amount.doubleValue() > txnPricing.getMaxAmount().doubleValue()) {
				response.setStatusCode("057"); // Transaction not allowed
				response.setTransactionCode(txnCode);
				response.setMessage(
						" Amount is more than the maximum amount allowed: KES " + txnPricing.getMaxAmount());
				response.setAmount(amount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			// Update customer wallet
			Optional<Wallet> walletOptional = walletRepository.findById(walletid);
			Wallet m_Wallet = walletOptional.orElseThrow(
					() -> new WalletNotFoundException("No Wallet Details found for walletId : " + walletid));

			double newBalance = ((m_Wallet.getCurrentBalance().doubleValue()) + amount.doubleValue()) - txnCharges;

			walletRepository.setUpdateWallet(new BigDecimal(newBalance), walletid);

			// Save the transaction

			Transactions transactions = new Transactions();
			transactions.setSysReference(systemRef);
			transactions.setTxnCode(txnCode);
			transactions.setTxnType(txnType);
			transactions.setPayMode(paymode);
			transactions.setDateTime(Utilities.getCurrentTimeStamp());
			transactions.setWalletId(walletid);
			transactions.setTxnAmount(amount);
			txnrepository.save(transactions);

			// Save transaction charges
			if (txnCharges > 0) {
				Transactions senderTxnCharges = new Transactions();
				senderTxnCharges.setSysReference(systemRef + "-C");
				senderTxnCharges.setTxnCode(txnCode);
				senderTxnCharges.setTxnType(txnType);
				senderTxnCharges.setPayMode(paymode); // Debit account
				senderTxnCharges.setDateTime(Utilities.getCurrentTimeStamp());
				senderTxnCharges.setWalletId(walletid);
				senderTxnCharges.setTxnAmount(new BigDecimal(txnCharges));
				txnrepository.save(senderTxnCharges);

			}
			
			// Business commissions that is the transaction charges (txnCharges) to be saved
			// in the business ledger.
			// This is TO BE DONE. as it out of scope as per now.

			response.setStatusCode("201");
			response.setTransactionCode(txnCode);
			response.setMessage("Wallet Loaded successful");
			response.setAmount(amount);
			response.setTxnmode("Credit");
			response.setTimestamp(Utilities.getCurrentTimeStamp());

		} catch (Exception e) {
			System.out.println("Load Wallet Account Failed" + e.getMessage());
			// throw new LoadWalletFailedException("Load Wallet Account Failed"+
			// e.getMessage());
		}

		return response;

	}

	@Transactional
	public TransferFundResponse transferFunds(TransferFundRequest transferFundsRequest) {

		BigDecimal sendAmount;
		String senderWalletId;
		String receiverPhoneNo;
		String senderPaymode = "D"; // Debit
		String receiverPaymode = "C"; // Credit
		String txnType;
		double txnCharges = 0;
		String txnCode;
		String senderSystemRef;
		String receiverSystemRef;
		TransferFundResponse response = new TransferFundResponse();
		try {
			sendAmount = transferFundsRequest.getSendAmount();
			senderWalletId = transferFundsRequest.getSenderWallet();
			receiverPhoneNo = transferFundsRequest.getReceiverPhoneNumber();
			// get the txn mode
			txnType = Utilities.getTransferFundCode();
			// generate transaction code
			txnCode = Utilities.generateTransactionCode(12);
			senderSystemRef = Utilities.getLoadAccountCode() + "-" + Utilities.generateTransactionCode(14);
			receiverSystemRef = Utilities.getLoadAccountCode() + "-" + Utilities.generateTransactionCode(14);
			/** step 1 get transaction charges */
			Optional<Pricing> txn_Price = pricingRepository.findByTxnType(txnType);
			Pricing txnPricing = txn_Price.orElseThrow(
					() -> new PricingNotFoundException("No user pricing details for paymode : " + txnType));

			if (txnPricing.getFixedFee().doubleValue() != 0) {
				txnCharges = txnPricing.getFixedFee().doubleValue();
			} else if (txnPricing.getVarFee().doubleValue() != 0) {
				txnCharges = (txnPricing.getVarFee().doubleValue()) * sendAmount.doubleValue();
			}

			/** Step 2 Transaction Authorization check minimal transaction amount */
			if (sendAmount.doubleValue() < txnPricing.getMinAmount().doubleValue()) {
				response.setStatusCode("057"); // Transaction not allowed
				response.setTransactionCode(txnCode);
				response.setMessage(
						"Send amount is less than the minimum amount allowed: KES " + txnPricing.getMinAmount());
				response.setAmount(sendAmount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			// check max amount per transaction
			if (sendAmount.doubleValue() > txnPricing.getMaxAmount().doubleValue()) {
				// throw new Exception(
				// "Transaction amount is more than the maximum amount allowed: KES " +
				// txnPricing.getMaxAmount());
				response.setStatusCode("057"); // Transaction not allowed
				response.setTransactionCode(txnCode);
				response.setMessage(" Send amount should not be more than KES " + txnPricing.getMaxAmount());
				response.setAmount(sendAmount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			/** Step 3 Update sender wallet */
			Optional<Wallet> walletOptional = walletRepository.findById(senderWalletId);
			Wallet m_Wallet = walletOptional.orElseThrow(() -> new WalletNotFoundException(
					"No Wallet Details found for Sender walletId : " + senderWalletId));

			// Check if sender has sufficient funds

			if (m_Wallet.getCurrentBalance().doubleValue() < (sendAmount.doubleValue() + txnCharges)) {

				response.setStatusCode("051"); // Transaction not allowed
				response.setTransactionCode(txnCode);
				response.setMessage("Insufficinet Funds. Kindly topup your account");
				response.setAmount(sendAmount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			double newSenderBalance = ((m_Wallet.getCurrentBalance().doubleValue()) - sendAmount.doubleValue())
					- txnCharges;

			walletRepository.setUpdateWallet(new BigDecimal(newSenderBalance), senderWalletId);

			/** Step 4 Update Receiver wallet */
			// get receiver wallet details
			Optional<User> receiverUserOptional = customerRepository.findByPhoneNumber(receiverPhoneNo);
			User m_User = receiverUserOptional.orElseThrow(
					() -> new WalletNotFoundException("No User Details found for Sender walletId : " + senderWalletId));

			Optional<Wallet> receiverWalletOptional = walletRepository.findByRelationshipNo(m_User.getRelationshipNo());
			Wallet m_WalletReceiver = receiverWalletOptional.orElseThrow(() -> new WalletNotFoundException(
					"No Wallet Details found for Receiver walletId : " + receiverPhoneNo));

			double newReceiverBalance = (m_WalletReceiver.getCurrentBalance().doubleValue()) + sendAmount.doubleValue();

			walletRepository.setUpdateWallet(new BigDecimal(newReceiverBalance), m_WalletReceiver.getWalletId());

			// Save sender transaction
			Transactions senderTxn = new Transactions();
			senderTxn.setSysReference(senderSystemRef);
			senderTxn.setTxnCode(txnCode);
			senderTxn.setTxnType(txnType);
			senderTxn.setPayMode(receiverPaymode); // Debit account
			senderTxn.setDateTime(Utilities.getCurrentTimeStamp());
			senderTxn.setWalletId(senderWalletId);
			senderTxn.setTxnAmount(sendAmount);
			txnrepository.save(senderTxn);

			// Save receiver transaction
			Transactions receiverTxn = new Transactions();
			receiverTxn.setSysReference(receiverSystemRef);
			receiverTxn.setTxnCode(txnCode);
			receiverTxn.setTxnType(txnType);
			receiverTxn.setPayMode(senderPaymode); // Credit account
			receiverTxn.setDateTime(Utilities.getCurrentTimeStamp());
			receiverTxn.setWalletId(m_WalletReceiver.getWalletId());
			receiverTxn.setTxnAmount(sendAmount);
			txnrepository.save(receiverTxn);

			// Save sender transaction charges
			if (txnCharges > 0) {
				Transactions senderTxnCharges = new Transactions();
				senderTxnCharges.setSysReference(senderSystemRef + "-C");
				senderTxnCharges.setTxnCode(txnCode);
				senderTxnCharges.setTxnType(txnType);
				senderTxnCharges.setPayMode(receiverPaymode); // Debit account
				senderTxnCharges.setDateTime(Utilities.getCurrentTimeStamp());
				senderTxnCharges.setWalletId(senderWalletId);
				senderTxnCharges.setTxnAmount(new BigDecimal(txnCharges));
				txnrepository.save(senderTxnCharges);

			}

			// Business commissions that is the transaction charges (txnCharges) to be saved
			// in the business ledger.
			// This is TO BE DONE. as it out of scope as per now.

			response.setStatusCode("200");
			response.setTransactionCode(txnCode);
			response.setMessage("Transfer fund successful");
			response.setAmount(sendAmount);
			response.setTxnmode("Debit");
			response.setReceiver(m_User.getName());
			response.setTimestamp(Utilities.getCurrentTimeStamp());

		} catch (Exception e) {
			System.out.println("Transfer Funds Failed" + e.getMessage());
			// throw new LoadWalletFailedException("Load Wallet Account Failed"+
			// e.getMessage());
		}

		return response;
	}


	@Transactional(rollbackFor = TestException.class)
	public WithdrawFundResponse withdrawFromATM(WithdrawFundRequest withdrawRequest) throws TestException {

		BigDecimal withdrawAmount;
		String walletid;
		String atmCode;
		String paymode = "D";
		String atmPayMode = "C";
		String txnType;
		double txnCharges = 0;
		String txnCode;
		String systemRef;
		WithdrawFundResponse response = new WithdrawFundResponse();
		//try {
			withdrawAmount = withdrawRequest.getAmount();
			walletid = withdrawRequest.getWalletId();
			atmCode = withdrawRequest.getAtmCode();
			// get the txn mode
			txnType = Utilities.getATMWithdrawFundsCode();
			// generate transaction code
			txnCode = Utilities.generateTransactionCode(12);
			systemRef = Utilities.getATMWithdrawFundsCode() + "-" + Utilities.generateTransactionCode(14);

			// transaction price
			Optional<Pricing> txn_Price = pricingRepository.findByTxnType(txnType);
			Pricing txnPricing = txn_Price.orElseThrow(
					() -> new PricingNotFoundException("No user pricing details for paymode : " + txnType));

			if (txnPricing.getFixedFee().doubleValue() != 0) {
				txnCharges = txnPricing.getFixedFee().doubleValue();
			} else if (txnPricing.getVarFee().doubleValue() != 0) {
				txnCharges = (txnPricing.getVarFee().doubleValue()) * withdrawAmount.doubleValue();
			}

			/** Step 2 Transaction Authorization check minimal transaction amount */
			if (withdrawAmount.doubleValue() < txnPricing.getMinAmount().doubleValue()) {

				response.setStatusCode("057"); // Transaction not allowed
				response.setTransactionCode(txnCode);
				response.setMessage(
						"Withdrawal amount is less than the minimum amount allowed: KES " + txnPricing.getMinAmount());
				response.setAmount(withdrawAmount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			// check max amount per transaction
			if (withdrawAmount.doubleValue() > txnPricing.getMaxAmount().doubleValue()) {

				response.setStatusCode("057"); // Transaction not allowed
				response.setTransactionCode(txnCode);
				response.setMessage(
						" Withdrawal amount is more than the maximum amount allowed: KES " + txnPricing.getMaxAmount());
				response.setAmount(withdrawAmount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			/** get ATM details */

			Optional<ATMWallet> atmOptional = atmWalletRepository.findByAtmCode(atmCode);
			ATMWallet m_ATMWallet = atmOptional
					.orElseThrow(() -> new WalletNotFoundException("No ATM Details found for atmCode : " + atmCode));

			// check ATM cash balance

			if (m_ATMWallet.getCashBalance().doubleValue() < (withdrawAmount.doubleValue() + txnCharges)) {
				response.setStatusCode("051"); // Insufficient funds
				response.setTransactionCode(txnCode);
				response.setMessage("ATM has insufficient cash for your Withdrawal");
				response.setAmount(withdrawAmount);
				response.setTimestamp(Utilities.getCurrentTimeStamp());

				return response;
			}

			double newATMFloatBalance = (m_ATMWallet.getFloatBalance().doubleValue())
					+ (withdrawAmount.doubleValue() + txnCharges);
			double newATMFCashBalance = m_ATMWallet.getCashBalance().doubleValue() - withdrawAmount.doubleValue();

			atmWalletRepository.setUpdateWallet(new BigDecimal(newATMFCashBalance), new BigDecimal(newATMFloatBalance),
					m_ATMWallet.getWalletId());

			/** Update customer wallet */
			Optional<Wallet> walletOptional = walletRepository.findById(walletid);
			Wallet m_Wallet = walletOptional.orElseThrow(
					() -> new WalletNotFoundException("No Wallet Details found for walletId : " + walletid));

			double newBalance = ((m_Wallet.getCurrentBalance().doubleValue()) - ((withdrawAmount.doubleValue())
					+ txnCharges) );

			walletRepository.setUpdateWallet(new BigDecimal(newBalance), walletid);

			// Save the wallet transaction
			Transactions transactions = new Transactions();
			transactions.setSysReference(systemRef);
			transactions.setTxnCode(txnCode);
			transactions.setTxnType(txnType);
			transactions.setPayMode(paymode); // Debit
			transactions.setDateTime(Utilities.getCurrentTimeStamp());
			transactions.setWalletId(walletid);
			transactions.setTxnAmount(withdrawAmount);
			txnrepository.save(transactions);

			// update atm ledger
			ATMTransactions atmTransactions = new ATMTransactions();
			atmTransactions.setSysReference(systemRef);
			atmTransactions.setTxnCode(txnCode);
			atmTransactions.setTxnType(txnType);
			atmTransactions.setPayMode(atmPayMode); // Credit
			atmTransactions.setDateTime(Utilities.getCurrentTimeStamp());
			atmTransactions.setWalletId(walletid);
			atmTransactions.setTxnAmount(withdrawAmount);
			atmTransactionRepository.save(atmTransactions);

			if (txnCharges > 0) {
				// Save the wallet transaction
				Transactions chargesTxn = new Transactions();
				chargesTxn.setSysReference(systemRef + "-C");
				chargesTxn.setTxnCode(txnCode);
				chargesTxn.setTxnType("Charges");
				chargesTxn.setPayMode(paymode); // Debit
				chargesTxn.setDateTime(Utilities.getCurrentTimeStamp());
				chargesTxn.setWalletId(walletid);
				chargesTxn.setTxnAmount(new BigDecimal(txnCharges));
				txnrepository.save(chargesTxn);

				// update atm ledger commission
				ATMTransactions atmcommissionTxn = new ATMTransactions();
				atmcommissionTxn.setSysReference(systemRef + "-C");
				atmcommissionTxn.setTxnCode(txnCode);
				atmcommissionTxn.setTxnType("Commission");
				atmcommissionTxn.setPayMode(atmPayMode); // Credit
				atmcommissionTxn.setDateTime(Utilities.getCurrentTimeStamp());
				atmcommissionTxn.setWalletId(walletid);
				atmcommissionTxn.setTxnAmount(new BigDecimal(txnCharges));
				atmTransactionRepository.save(atmcommissionTxn);

			}

			// Business commissions that is the transaction charges (txnCharges) to be saved
			// in the business ledger.
			// This is TO BE DONE. as it out of scope as per now.

			response.setStatusCode("201");
			response.setTransactionCode(txnCode);
			response.setMessage("Withdrawal successful");
			response.setAmount(withdrawAmount);
			response.setAtmcode(withdrawRequest.getAtmCode());
			response.setTimestamp(Utilities.getCurrentTimeStamp());

		/*} catch (Exception e) {
			System.out.println("Load Wallet Account Failed" + e.getMessage());
			 throw new TestException("Load Wallet Account Failed");
			// e.getMessage());
		}*/

		return response;

	}

}
