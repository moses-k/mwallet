package com.mwallet.tps.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mwallet.tps.modal.Transactions;
import com.mwallet.tps.payload.request.WalletTransactionsRequest;
import com.mwallet.tps.service.AccountTransactionsService;

@RestController
@RequestMapping(path = "/mwallet/account")
public class WalletTransactionsController {

	@Autowired
	AccountTransactionsService accountTransactionsService;

	/* Customer profile */
	@GetMapping(path = "/transactions", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Transactions>> registerCustomer(
			@Valid @NotNull @RequestBody WalletTransactionsRequest transactionRequest) {
		List<Transactions> arrTxns = accountTransactionsService.getWalletTransactions(transactionRequest);
		return new ResponseEntity<List<Transactions>>(arrTxns, HttpStatus.OK);
	}

}
