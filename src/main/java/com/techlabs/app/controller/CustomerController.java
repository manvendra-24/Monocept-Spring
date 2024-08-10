package com.techlabs.app.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.MyAccountResponse;
import com.techlabs.app.dto.TransactionRequest;
import com.techlabs.app.dto.TransactionResponse;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.service.IService;
import com.techlabs.app.util.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CustomerController {

	IService service;

	public CustomerController(IService service) {
		super();
		this.service = service;
	}
	
	
	//get my account
	@GetMapping("/myaccounts")
	public ResponseEntity<PagedResponse<MyAccountResponse>> getMyAccount(
	        HttpServletRequest request,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
	        @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
	    String authorizationHeader = request.getHeader("Authorization");
	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	        String token = authorizationHeader.substring(7);
	        PagedResponse<MyAccountResponse> pagedResponse = service.getAllAccountByToken(token, page, size, sortBy, direction);
	        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
	    }

	    throw new ApiException("Unauthorized");
	}

		
		//get my balance
		@GetMapping("/mybalance")
		public ResponseEntity<Integer> getMyBalance(HttpServletRequest request) {
	        String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            String token = authorizationHeader.substring(7);
	            int balance = service.getBalanceByToken(token);
	            return new ResponseEntity<>(balance,HttpStatus.OK);
	        }
	        throw new ApiException("Unauthorized");
	    }
		
		//get my account balance
		@GetMapping("/myaccounts/{account_id}/balance")
		public ResponseEntity<Integer> getMyAccountBalance(HttpServletRequest request, @PathVariable int account_id) {
	        String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            String token = authorizationHeader.substring(7);
	            int balance = service.getAccountBalanceByToken(token, account_id);
	            return new ResponseEntity<>(balance, HttpStatus.OK);
	        }
	        throw new ApiException("Unauthorized");
	    }
		
		//get my transactions
		@GetMapping("/myaccounts/{account_id}/transactions")
		public ResponseEntity<PagedResponse<TransactionResponse>> getMyAccountTransactions(
		        HttpServletRequest request,
		        @PathVariable int account_id,
		        @RequestParam(value = "page", defaultValue = "0") int page,
		        @RequestParam(value = "size", defaultValue = "10") int size,
		        @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
		        @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

		    String authorizationHeader = request.getHeader("Authorization");
		    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		        String token = authorizationHeader.substring(7);
		        PagedResponse<TransactionResponse> pagedResponse = service.getMyAccountTransactions(token, account_id, page, size, sortBy, direction);
		        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
		    }
		    throw new ApiException("Unauthorized");
		}

		
		//delete my account
		@DeleteMapping("/myaccounts/{account_id}")
		public ResponseEntity<String> inactiveMyAccount(HttpServletRequest request, @PathVariable int account_id) {
			 String authorizationHeader = request.getHeader("Authorization");
		        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		            String token = authorizationHeader.substring(7);
		            String response = service.deleteMyAccount(token, account_id);
		            return new ResponseEntity<>(response, HttpStatus.OK);
		        }
		        throw new ApiException("Unauthorized");
		}
		
		//add a transaction
		@PostMapping("/mytransactions")
		public ResponseEntity<String> doTransaction(HttpServletRequest request,@Valid @RequestBody TransactionRequest transactionRequest) {
			String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            String token = authorizationHeader.substring(7);
	            String response = service.doTransaction(token, transactionRequest);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	        throw new ApiException("Unauthorized");
		}
}
