package com.techlabs.app.dto;

import java.time.LocalDateTime;

import com.techlabs.app.entity.Account;

public class TransactionResponse implements ITransactionResponse{

	private int id;
	private String type;
	private LocalDateTime date;
	private Account account;
	private int amount;
	
	public TransactionResponse(int id, String type, LocalDateTime date, Account sender_account, Account receiver_account, int amount) {
		super();
		this.id = id;
		this.type = type;
		this.date = date;
		this.account = sender_account;
		this.amount = amount;
	}

	public TransactionResponse() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account sender_account) {
		this.account = sender_account;
	}

	

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
	
}
