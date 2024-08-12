package com.techlabs.app.dto;

import com.techlabs.app.entity.Customer;

public class AccountResponse {

	private int account_number;
	private int balance;
	private Customer customer;
	private boolean active;
	private String bank_details;
	
	public AccountResponse(int account_number, int balance, Customer customer, boolean active, String bank_details) {
		super();
		this.account_number = account_number;
		this.balance = balance;
		this.customer = customer;
		this.active = active;
		this.bank_details = bank_details;
	}

	public AccountResponse() {
		super();
	}

	public int getAccount_number() {
		return account_number;
	}

	public void setAccount_number(int account_number) {
		this.account_number = account_number;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getBank_details() {
		return bank_details;
	}

	public void setBank_details(String bank_details) {
		this.bank_details = bank_details;
	}
	
	

	
	
}
