package com.mwallet.tps.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mwallet.tps.exception.TestException;
import com.mwallet.tps.modal.Transactions;
import com.mwallet.tps.payload.request.WalletTransactionsRequest;
import com.mwallet.tps.repository.TransactionRepository;

@Service
public class AccountTransactionsService {

	@Autowired
	TransactionRepository transactionRepository;

	@Transactional(rollbackFor = TestException.class)
	public List<Transactions> getWalletTransactions(WalletTransactionsRequest transactionsRequest) throws TestException {
		List<Transactions> arrTransactions = transactionRepository.findLastTenTxn(transactionsRequest.getWalletId());

		return arrTransactions;

	}

}
