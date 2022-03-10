package com.mwallet.tps.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class WalletTransactionsRequest {
	@NotBlank
	@NotNull
	private String walletId;

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

}
