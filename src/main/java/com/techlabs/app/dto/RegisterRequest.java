package com.techlabs.app.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
	
	@NotBlank(message = "First name is missing")
	private String firstname;
	
	@NotBlank(message = "Last name is missing")
	private String lastname;
	
	@NotBlank(message = "Username is missing")
	private String username;
	
	@NotBlank(message = "Email is missing")
	private String email;
	
	@NotBlank(message = "Password is missing")
	private String password;
	
	public RegisterRequest() {
		super();
	}

	public RegisterRequest(String firstname, String lastname, String username, String email, String password) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
