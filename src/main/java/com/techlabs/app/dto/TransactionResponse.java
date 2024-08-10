package com.techlabs.app.dto;

import java.time.LocalDateTime;

import com.techlabs.app.entity.Account;

public class TransactionResponse {

	private int id;
	private String type;
	private LocalDateTime date;
	private Account sender_account;
	private Account receiver_account;
	private int amount;
	
	public TransactionResponse(int id, String type, LocalDateTime date, Account sender_account, Account receiver_account, int amount) {
		super();
		this.id = id;
		this.type = type;
		this.date = date;
		this.sender_account = sender_account;
		this.receiver_account = receiver_account;
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

	public Account getSender_account() {
		return sender_account;
	}

	public void setSender_account(Account sender_account) {
		this.sender_account = sender_account;
	}

	public Account getReceiver_account() {
		return receiver_account;
	}

	public void setReceiver_account(Account receiver_account) {
		this.receiver_account = receiver_account;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
	
}
