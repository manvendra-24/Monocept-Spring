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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "First name is missing")
	private String firstname;
	
	@NotNull(message = "Last name is missing")
	private String lastname;
	
	private int totalbalance;
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true, mappedBy = "customer")
	@JsonIgnore
	private List<Account> accounts;

	public Customer() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int customer_id) {
		this.id = customer_id;
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
