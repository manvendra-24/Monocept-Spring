package com.techlabs.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AccountRequest;
import com.techlabs.app.dto.AccountResponse;
import com.techlabs.app.dto.CustomerResponse;
import com.techlabs.app.dto.ITransactionResponse;
import com.techlabs.app.service.IService;
import com.techlabs.app.util.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	IService service;

	public AdminController(IService service) {
		super();
		this.service = service;
	}
	
	
	
	//get all customers
	@GetMapping("/customers")
	public ResponseEntity<PagedResponse<CustomerResponse>> getAllCustomers(
			@RequestParam (name="page", defaultValue="0") int page,
			@RequestParam (name="size", defaultValue="5") int size,
			@RequestParam (name = "sortBy", defaultValue="id") String sortBy,
			@RequestParam (name = "direction", defaultValue="asc") String direction
			){
		
		logger.info("Admin fetching all customers");
		PagedResponse<CustomerResponse> customersResponse = service.getAllCustomers(page, size, sortBy, direction);
		return new ResponseEntity<>(customersResponse, HttpStatus.OK);
	}
	
	//get total balance of a customer
	@GetMapping("/customers/{customer_id}/balance")
	public ResponseEntity<Integer> getTotalBalance(@PathVariable(name= "customer_id") int customer_id) {
		logger.info("Admin fetching a customer's total balance");
		return new ResponseEntity<>(service.getTotalBalance(customer_id), HttpStatus.OK);	
	}
		
	//get customer by id
	@GetMapping("/customers/{customer_id}")
	public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable int customer_id) {
		logger.info("Admin fetching a customer by id");
		return new ResponseEntity<>(service.getCustomerById(customer_id), HttpStatus.OK);
	}
	
	//get all accounts of a customer
	@GetMapping("/customers/{customer_id}/accounts")
	public ResponseEntity<PagedResponse<AccountResponse>> getAllAccountByCustomer(
			@RequestParam (name="page", defaultValue="0") int page,
			@RequestParam (name="size", defaultValue="5") int size,
			@RequestParam (name = "sortBy", defaultValue="accountNumber") String sortBy,
			@RequestParam (name = "direction", defaultValue="asc") String direction,
			@PathVariable int customer_id){
		logger.info("Admin fetching a customer's all accounts");
		PagedResponse<AccountResponse> accountsResponse = service.getAllAccountByCustomer(page, size, sortBy, direction, customer_id);
		return new ResponseEntity<>(accountsResponse,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	//get all accounts
	@GetMapping("/accounts")
	public ResponseEntity<PagedResponse<AccountResponse>> getAllAccounts(
			@RequestParam (name="page", defaultValue="0") int page,
			@RequestParam (name="size", defaultValue="5") int size,
			@RequestParam (name = "sortBy", defaultValue="accountNumber") String sortBy,
			@RequestParam (name = "direction", defaultValue="asc") String direction
			){
		logger.info("Admin fetching all accounts");
		PagedResponse<AccountResponse> accountResponses = service.getAllAccounts(page, size, sortBy, direction);
		return ResponseEntity.ok(accountResponses);
	}
	
	//add an account
	@PostMapping("/accounts")
	public ResponseEntity<String> createAnAccountForCustomer(@Valid @RequestBody AccountRequest accountRequest) {
		logger.info("Admin creating an account for customer");
		return new ResponseEntity<>(service.createAnAccountForCustomer(accountRequest),HttpStatus.OK);
	}
	
	//get account by id
	@GetMapping("/accounts/{account_id}")
	public ResponseEntity<AccountResponse> getAccountById(@PathVariable int account_id) {
		logger.info("Admin fetching account by id");
		return new ResponseEntity<>(service.getAccountById(account_id),HttpStatus.OK);
	}
	
	//get balance of an account
	@GetMapping("/accounts/{account_id}/balance")
	public ResponseEntity<Integer> getBalance(@PathVariable(name = "account_id") int account_id) {
		logger.info("Admin fetching account's balance");
		return new ResponseEntity<>(service.getBalance(account_id), HttpStatus.OK);
	}
	
	//delete an account
	@DeleteMapping("/accounts/{account_id}/delete")
	public ResponseEntity<String> deleteAnAccount(@PathVariable int account_id) {
		logger.info("Admin deleting account");
		return new ResponseEntity<>(service.deleteAnAccount(account_id), HttpStatus.OK);
	}
	
	//get all transactions of an account
	@GetMapping("/accounts/{account_id}/transactions")
	public ResponseEntity<PagedResponse<ITransactionResponse>> getTransactionsOfAccount(
	        @PathVariable int account_id,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
	        @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
		
		logger.info("Admin getting all transactions of an account");
	    PagedResponse<ITransactionResponse> pagedResponse = service.getTransactionsOfAccount(account_id, page, size, sortBy, direction);
	    return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
	}

	
	//reactivate account 
	@PutMapping("/accounts/{account_id}/reactivate")
	public ResponseEntity<String> reActivateAccount(@PathVariable int account_id) {
		logger.info("Admin reactivating account");
		return new ResponseEntity<>(service.reActivateAccount(account_id), HttpStatus.OK);
	} 
	
	
	//get transaction by id
	@GetMapping("/transactions/{transaction_id}")
	public ResponseEntity<ITransactionResponse> getTransactionById(@PathVariable int transaction_id) {
		logger.info("Admin fetching a transaction by id");
		return new ResponseEntity<>(service.getTransactionById(transaction_id), HttpStatus.OK);
	}
	
	
	//get bank's accounts
	@GetMapping("/banks/{bank_id}/accounts")
	public ResponseEntity<PagedResponse<AccountResponse>> getAllAccountsOfBank(
			@PathVariable int bank_id,
			@RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        @RequestParam(value = "sortBy", defaultValue = "accountNumber") String sortBy,
	        @RequestParam(value = "direction", defaultValue = "DESC") String direction){
		logger.info("Admin fetching all accounts in a bank");
		return new ResponseEntity<>(service.getAllAccountsOfBank(bank_id, page, size, sortBy, direction), HttpStatus.OK);
	}
	
	
	
}
