package com.mwallet.tps.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mwallet.tps.payload.request.LoadWalletRequest;
import com.mwallet.tps.payload.request.TransferFundRequest;
import com.mwallet.tps.payload.request.WithdrawFundRequest;
import com.mwallet.tps.payload.response.LoadWalletResponse;
import com.mwallet.tps.payload.response.TransferFundResponse;
import com.mwallet.tps.payload.response.WithdrawFundResponse;
import com.mwallet.tps.service.PaymentService;

@RestController
@RequestMapping(path = "/mwallet/payment")
public class PaymentController {

	@Autowired
	PaymentService paymentService;

	/* Load wallet */
	@PostMapping(path = "/loadwallet", consumes = "application/json", produces = "application/json")
	public ResponseEntity<LoadWalletResponse> registerCustomer(
			@Valid @NotNull @RequestBody LoadWalletRequest loadRegRequest) {
		LoadWalletResponse loadWalletResponse = paymentService.loadAccount(loadRegRequest);
		return new ResponseEntity<LoadWalletResponse>(loadWalletResponse, HttpStatus.OK);
	}

	/* Transafer Funds */
	@PostMapping(path = "/transfer", consumes = "application/json", produces = "application/json")
	public ResponseEntity<TransferFundResponse> registerCustomer(
			@Valid @NotNull @RequestBody TransferFundRequest transferFundRegRequest) {
		TransferFundResponse transferFundResponse = paymentService.transferFunds(transferFundRegRequest);
		return new ResponseEntity<TransferFundResponse>(transferFundResponse, HttpStatus.OK);
	}

	/* Withdraw Funds */
	@PostMapping(path = "/withdraw", consumes = "application/json", produces = "application/json")
	public ResponseEntity<WithdrawFundResponse> registerCustomer(
			@Valid @NotNull @RequestBody WithdrawFundRequest withdrawFundRegRequest) {
		WithdrawFundResponse withdrawFundResponse = paymentService.withdrawFromATM(withdrawFundRegRequest);
		return new ResponseEntity<WithdrawFundResponse>(withdrawFundResponse, HttpStatus.OK);
	}

	

}
