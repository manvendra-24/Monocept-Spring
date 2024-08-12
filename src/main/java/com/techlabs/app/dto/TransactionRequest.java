package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionRequest {

	
	@NotNull(message = "Account is missing")
	private int account;
	
	@NotNull(message = "Amount is missing")
	private int amount;

	public TransactionRequest() {
		super();
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int sender_account) {
		this.account = sender_account;
	}

	

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public TransactionRequest(
			@NotBlank(message = "Sender account is missing") int sender_account,
			@NotNull(message = "Amount is missing") int amount) {
		super();
		this.account = sender_account;
		this.amount = amount;
	}

	
	
	
	
}
