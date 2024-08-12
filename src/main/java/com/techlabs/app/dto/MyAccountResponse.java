package com.techlabs.app.dto;

public class MyAccountResponse {

	private int account_number;
	private int balance;
	private String bank_details;
	
	public MyAccountResponse(int account_number, int balance, String bank_details) {
		super();
		this.account_number = account_number;
		this.balance = balance;
		this.bank_details = bank_details;
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
	
	
	public String getBank_details() {
		return bank_details;
	}
	public void setBank_details(String bank_details) {
		this.bank_details = bank_details;
	}
	public MyAccountResponse() {
		super();
	}
	
	
}
