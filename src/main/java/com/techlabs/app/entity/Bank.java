package com.techlabs.app.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "banks")
public class Bank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	private String bank_name;
	private String bank_abbr;
	
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true, mappedBy = "bank")
	@JsonIgnore
	private List<Account> accounts;


	public Bank(int id, String bank_name, String bank_abbr, List<Account> accounts) {
		super();
		this.id = id;
		this.bank_name = bank_name;
		this.bank_abbr = bank_abbr;
		this.accounts = accounts;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getBank_name() {
		return bank_name;
	}


	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}


	public String getBank_abbr() {
		return bank_abbr;
	}


	public void setBank_abbr(String bank_abbr) {
		this.bank_abbr = bank_abbr;
	}


	public List<Account> getAccounts() {
		return accounts;
	}


	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}


	public Bank() {
		super();
	}
	
	
}
