package com.techlabs.app.controller;


import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.techlabs.app.dto.ITransactionResponse;
import com.techlabs.app.dto.MyAccountResponse;
import com.techlabs.app.dto.TransactionRequest;
import com.techlabs.app.dto.TransferRequest;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.service.IService;
import com.techlabs.app.service.PdfGeneratorService;
import com.techlabs.app.util.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CustomerController {
	 
	 @Value("${file.download-dir}")
	 private String downloadDir;

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	IService service;
	PdfGeneratorService pdfGeneratorService;

	public CustomerController(IService service, PdfGeneratorService pdfGeneratorService) {
		super();
		this.service = service;
		this.pdfGeneratorService = pdfGeneratorService;
	}
	
	
	//get my account
	@GetMapping("/myaccounts")
	public ResponseEntity<PagedResponse<MyAccountResponse>> getMyAccount(
	        HttpServletRequest request,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        @RequestParam(value = "sortBy", defaultValue = "accountNumber") String sortBy,
	        @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
	    String authorizationHeader = request.getHeader("Authorization");
	    
		logger.info("Customer fetching all accounts");

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
			logger.info("Customer fetching total balance");

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
			logger.info("Customer fetching account's balance");
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
		public ResponseEntity<PagedResponse<ITransactionResponse>> getMyAccountTransactions(
		        HttpServletRequest request,
		        @PathVariable int account_id,
		        @RequestParam(value = "page", defaultValue = "0") int page,
		        @RequestParam(value = "size", defaultValue = "10") int size,
		        @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
		        @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
			logger.info("Customer fetching all transactions of account");
		    String authorizationHeader = request.getHeader("Authorization");
		    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		        String token = authorizationHeader.substring(7);
		        PagedResponse<ITransactionResponse> pagedResponse = service.getMyAccountTransactions(token, account_id, page, size, sortBy, direction);
		        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
		    }
		    throw new ApiException("Unauthorized");
		}

		
		//delete my account
		@DeleteMapping("/myaccounts/{account_id}/delete")
		public ResponseEntity<String> inactiveMyAccount(HttpServletRequest request, @PathVariable int account_id) {
			logger.info("Customer deactivating account");
			 String authorizationHeader = request.getHeader("Authorization");
		        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		            String token = authorizationHeader.substring(7);
		            String response = service.deleteMyAccount(token, account_id);
		            return new ResponseEntity<>(response, HttpStatus.OK);
		        }
		        throw new ApiException("Unauthorized");
		}
		
		//add a transaction
		@PostMapping("/mytransactions/transfer")
		public ResponseEntity<String> doTransfer(HttpServletRequest request,@Valid @RequestBody TransferRequest transferRequest) {
			logger.info("Customer trying to do a transaction");
			String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            String token = authorizationHeader.substring(7);
	            String response = service.doTransfer(token, transferRequest);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	        throw new ApiException("Unauthorized");
		}
		
		//add a transaction
		@PostMapping("/mytransactions/deposit")
		public ResponseEntity<String> doDeposit(HttpServletRequest request,@Valid @RequestBody TransactionRequest transactionRequest) {
			logger.info("Customer trying to do a transaction");

			String authorizationHeader = request.getHeader("Authorization");
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            String token = authorizationHeader.substring(7);
	            String response = service.doTransaction("Deposit",token, transactionRequest);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	        throw new ApiException("Unauthorized");
		}
		
		//add a transaction
		@PostMapping("/mytransactions/withdrawal")
		public ResponseEntity<String> doWithdrawal(HttpServletRequest request,@Valid @RequestBody TransactionRequest transactionRequest) {
			logger.info("Customer trying to do a transaction");

			String authorizationHeader = request.getHeader("Authorization");
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			      String token = authorizationHeader.substring(7);
			      String response = service.doTransaction("Withdrawal",token, transactionRequest);
			      return new ResponseEntity<>(response, HttpStatus.OK);
			}
			throw new ApiException("Unauthorized");
		}
		

		@GetMapping("/myaccounts/{account_id}/passbook/download")
	    public ResponseEntity<Resource> downloadFile(@PathVariable int account_id) throws FileNotFoundException {
			String accountNumber = Integer.toString(account_id);
			String filename = "";
			try {
				filename = pdfGeneratorService.generateStatement(accountNumber, downloadDir);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
	        File file = new File(filename);
	        if (!file.exists()) {
	            return ResponseEntity.notFound().build();
	        }
	        
	        InputStream inputStream = new FileInputStream(file);
	        InputStreamResource resource = new InputStreamResource(inputStream);

	        return ResponseEntity.ok()
	                .contentType(MediaType.APPLICATION_PDF)
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
	                .body(resource);
	    }
}


