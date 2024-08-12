package com.techlabs.app.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String type;
	
	private LocalDateTime date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountNumber")
	@JsonIgnore
	private Account senderAccount;
	
	private int receiverAccount;
	
	private int amount;
	
	
	private int senderBalance;
	
	private int receiverBalance;

	public Transaction() {
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

	public Account getSenderAccount() {
		return senderAccount;
	}

	public void setSenderAccount(Account senderAccount) {
		this.senderAccount = senderAccount;
	}

	public int getReceiverAccount() {
		return receiverAccount;
	}

	public void setReceiverAccount(int receiverAccount) {
		this.receiverAccount = receiverAccount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getSenderBalance() {
		return senderBalance;
	}

	public void setSenderBalance(int senderBalance) {
		this.senderBalance = senderBalance;
	}

	public int getReceiverBalance() {
		return receiverBalance;
	}

	public void setReceiverBalance(int receiverBalance) {
		this.receiverBalance = receiverBalance;
	}

	public Transaction(int id, String type, LocalDateTime date, Account senderAccount, int receiverAccount, int amount,
			int senderBalance, int receiverBalance) {
		super();
		this.id = id;
		this.type = type;
		this.date = date;
		this.senderAccount = senderAccount;
		this.receiverAccount = receiverAccount;
		this.amount = amount;
		this.senderBalance = senderBalance;
		this.receiverBalance = receiverBalance;
	}

}
