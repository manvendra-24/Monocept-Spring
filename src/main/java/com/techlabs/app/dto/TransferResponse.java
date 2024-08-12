package com.techlabs.app.dto;

import java.time.LocalDateTime;

import com.techlabs.app.entity.Account;

public class TransferResponse implements ITransactionResponse{

	private int id;
	private String type;
	private LocalDateTime date;
	private Account sender;
	private Account receiver;
	private int amount;
	
	public TransferResponse(int id, String type, LocalDateTime date, Account sender, Account receiver, int amount) {
		super();
		this.id = id;
		this.type = type;
		this.date = date;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}
	public TransferResponse() {
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
	public Account getSender() {
		return sender;
	}
	public void setSender(Account sender) {
		this.sender = sender;
	}
	public Account getReceiver() {
		return receiver;
	}
	public void setReceiver(Account receiver) {
		this.receiver = receiver;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
