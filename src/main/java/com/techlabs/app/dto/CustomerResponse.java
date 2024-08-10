package com.techlabs.app.dto;

import java.util.List;

import com.techlabs.app.entity.Account;

public class CustomerResponse {

	private int id;
	private String firstname;
	private String lastname;
	private int totalbalance;
	private List<Account> accounts;
	
	public CustomerResponse(int id, String firstname, String lastname, int totalbalance, List<Account> accounts) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.totalbalance = totalbalance;
		this.accounts = accounts;
	}

	public CustomerResponse() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getTotalbalance() {
		return totalbalance;
	}

	public void setTotalbalance(int totalbalance) {
		this.totalbalance = totalbalance;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	
}
