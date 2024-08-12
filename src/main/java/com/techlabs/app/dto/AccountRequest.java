package com.techlabs.app.dto;

import jakarta.validation.constraints.NotNull;

public class AccountRequest {
	
	@NotNull(message="Customer id is missing")
	private int customer_id;
	
	@NotNull(message = "bank_id is missing")
	private int bank_id;
	
	public AccountRequest(int customer_id, int bank_id) {
		super();
		this.customer_id = customer_id;
		this.bank_id = bank_id;
	}
	
	public AccountRequest() {
		super();
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getBank_id() {
		return bank_id;
	}

	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	
	
}
