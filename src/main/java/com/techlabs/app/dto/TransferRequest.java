package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransferRequest {

	@NotBlank(message = "Type of Transaction is missing")
	private String type;
	
	@NotNull(message = "Sender Account is missing")
	private int sender_account;
	
	@NotNull(message = "Receiver Account is missing")
	private int receiver_account;
	
	@NotNull(message = "Amount is missing")
	private int amount;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSender_account() {
		return sender_account;
	}

	public void setSender_account(int sender_account) {
		this.sender_account = sender_account;
	}

	public int getReceiver_account() {
		return receiver_account;
	}

	public void setReceiver_account(int receiver_account) {
		this.receiver_account = receiver_account;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public TransferRequest(@NotBlank(message = "Type of Transaction is missing") String type,
			@NotNull(message = "Sender Account is missing") int sender_account,
			@NotNull(message = "Receiver Account is missing") int receiver_account,
			@NotNull(message = "Amount is missing") int amount) {
		super();
		this.type = type;
		this.sender_account = sender_account;
		this.receiver_account = receiver_account;
		this.amount = amount;
	}

	public TransferRequest() {
		super();
	}
	
	
	
}
