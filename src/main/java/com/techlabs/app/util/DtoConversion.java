package com.techlabs.app.util;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.techlabs.app.dto.AccountResponse;
import com.techlabs.app.dto.CustomerResponse;
import com.techlabs.app.dto.MyAccountResponse;
import com.techlabs.app.dto.TransactionRequest;
import com.techlabs.app.dto.TransactionResponse;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AccountRepository;

@Component
public class DtoConversion {

	
	AccountRepository accountRepository;
	

	public DtoConversion(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	public AccountResponse convertAccountToResponse(Account account) {
		AccountResponse accountResponse = new AccountResponse();
		accountResponse.setAccount_number(account.getAccount_number());
		accountResponse.setBalance(account.getBalance());
		accountResponse.setCustomer(account.getCustomer());
		accountResponse.setActive(account.isActive());
		return accountResponse;
	}

	public CustomerResponse conversionCustomerToResponse(Customer customer) {
		CustomerResponse customerResponse = new CustomerResponse();
		customerResponse.setFirstname(customer.getFirstname());
		customerResponse.setLastname(customer.getLastname());
		customerResponse.setId(customer.getId());
		customerResponse.setTotalbalance(customer.getTotalbalance());
		customerResponse.setAccounts(customer.getAccounts());
		return customerResponse;
	}

	public TransactionResponse convertTransactionToResponse(Transaction transaction) {
		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setId(transaction.getId());
		transactionResponse.setType(transaction.getType());
		transactionResponse.setSender_account(transaction.getSenderAccount());
		Optional<Account> oAccount = accountRepository.findById(transaction.getReceiverAccount());
		if(oAccount.isEmpty()) {
			throw new ResourceNotFoundException("Receiver Account not exists");
		}
		Account account = oAccount.get();
		transactionResponse.setReceiver_account(account);
		transactionResponse.setAmount(transaction.getAmount());
		transactionResponse.setDate(transaction.getDate());
		return transactionResponse;
	}

	public MyAccountResponse convertAccountToMyAccount(Account account) {
		MyAccountResponse myAccount = new MyAccountResponse();
		myAccount.setAccount_number(account.getAccount_number());
		myAccount.setBalance(account.getBalance());
		return myAccount;
	}

	public Transaction convertRequestToTransaction(TransactionRequest transactionRequest) {
		Transaction transaction = new Transaction();
		Account sender = accountRepository.findById(transactionRequest.getSender_account()).get();
		transaction.setAmount(transactionRequest.getAmount());
		transaction.setDate(LocalDateTime.now());
		transaction.setSenderAccount(sender);
		transaction.setSenderBalance(sender.getBalance());
		if(transactionRequest.getType().equalsIgnoreCase("Transfer")) {
			Account receiver = accountRepository.findById(transactionRequest.getReceiver_account()).get();
			transaction.setReceiverAccount(transactionRequest.getReceiver_account());
			transaction.setReceiverBalance(receiver.getBalance());
		}
		transaction.setType(transactionRequest.getType());
		return transaction;
	}

	
}
