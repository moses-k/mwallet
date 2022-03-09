package com.mwallet.tps.service;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mwallet.tps.exception.PricingNotFoundException;
import com.mwallet.tps.exception.WalletNotFoundException;
import com.mwallet.tps.modal.Pricing;
import com.mwallet.tps.modal.Transactions;
import com.mwallet.tps.modal.User;
import com.mwallet.tps.modal.Wallet;
import com.mwallet.tps.payload.request.LoadWalletRequest;
import com.mwallet.tps.payload.request.TransferFundRequest;
import com.mwallet.tps.payload.response.LoadWalletResponse;
import com.mwallet.tps.payload.response.TransferFundResponse;
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

	@Transactional
	public LoadWalletResponse loadAccount(LoadWalletRequest loadRequest) {

		BigDecimal amount;
		String walletid;
		String paymode = "C";
		String txnType;
		double txnCharges = 0;
		String txnCode;
		LoadWalletResponse response = new LoadWalletResponse();
		try {
			amount = loadRequest.getAmount();
			walletid = loadRequest.getWalletId();
			// get the txn mode
			txnType = Utilities.getLoadAccountCode();

			// get txn charges
			System.out.println("before here ================================ " + txnType);

			Optional<Pricing> txn_Price = pricingRepository.findByTxnType(txnType);
			Pricing txnPricing = txn_Price.orElseThrow(
					() -> new PricingNotFoundException("No user pricing details for paymode : " + txnType));

			System.out.println("Reaching here ================================");

			if (txnPricing.getFixedFee().doubleValue() != 0) {
				txnCharges = txnPricing.getFixedFee().doubleValue();
			} else if (txnPricing.getVarFee().doubleValue() != 0) {
				txnCharges = (txnPricing.getVarFee().doubleValue()) * amount.doubleValue();
			}

			// generate transaction code
			txnCode = Utilities.generateTransactionCode(12);

			// check minimal transaction amount
			if (amount.doubleValue() < txnPricing.getMinAmount().doubleValue()) {

				// throw new Exception(
				// "Transaction amount is less than the minimum amount allowed: KES " +
				// txnPricing.getMinAmount());

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
				// throw new Exception(
				// "Transaction amount is more than the maximum amount allowed: KES " +
				// txnPricing.getMaxAmount());

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
			transactions.setTxnCode(txnCode);
			transactions.setTxnType(txnType);
			transactions.setPayMode(paymode);
			transactions.setDateTime(Utilities.getCurrentTimeStamp());
			transactions.setWalletId(walletid);
			transactions.setTxnAmount(amount);

			// Business commissions that is the transaction charges (txnCharges) to be saved
			// in the business ledger.
			// This is TO BE DONE. as it out of scope as per now.

			txnrepository.save(transactions);

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
		String senderPaymode = "D"; //Debit
		String receiverPaymode = "C";  //Credit
		String txnType;
		double txnCharges = 0;
		String txnCode;
		TransferFundResponse response = new TransferFundResponse();
		try {
			sendAmount = transferFundsRequest.getSendAmount();
			senderWalletId = transferFundsRequest.getSenderWallet();
			receiverPhoneNo = transferFundsRequest.getReceiverPhoneNumber();
			// get the txn mode
			txnType = Utilities.getTransferFundCode();
			// generate transaction code
			txnCode = Utilities.generateTransactionCode(12);

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
			senderTxn.setTxnCode(txnCode);
			senderTxn.setTxnType(txnType);
			senderTxn.setPayMode(receiverPaymode);  //Debit account
			senderTxn.setDateTime(Utilities.getCurrentTimeStamp());
			senderTxn.setWalletId(senderWalletId);
			senderTxn.setTxnAmount(sendAmount);

			txnrepository.save(senderTxn);

			// Save sender transaction
			Transactions receiverTxn = new Transactions();
			receiverTxn.setTxnCode(txnCode);
			receiverTxn.setTxnType(txnType);
			receiverTxn.setPayMode(senderPaymode); //Credit account
			receiverTxn.setDateTime(Utilities.getCurrentTimeStamp());
			receiverTxn.setWalletId(senderWalletId);
			receiverTxn.setTxnAmount(sendAmount);
			txnrepository.save(receiverTxn);

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

}
