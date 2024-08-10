package com.techlabs.app.dto;

import jakarta.validation.constraints.NotNull;

public class AccountRequest {
	
	@NotNull(message="Customer id is missing")
	private int customer_id;
	
	public AccountRequest(int customer_id) {
		super();
		this.customer_id = customer_id;
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
}
