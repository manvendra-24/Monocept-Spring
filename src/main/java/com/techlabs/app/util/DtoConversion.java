package com.techlabs.app.util;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.techlabs.app.dto.AccountResponse;
import com.techlabs.app.dto.CustomerResponse;
import com.techlabs.app.dto.MyAccountResponse;
import com.techlabs.app.dto.TransactionRequest;
import com.techlabs.app.dto.TransactionResponse;
import com.techlabs.app.dto.TransferRequest;
import com.techlabs.app.dto.TransferResponse;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Transaction;
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
		accountResponse.setAccount_number(account.getAccountNumber());
		accountResponse.setBalance(account.getBalance());
		accountResponse.setCustomer(account.getCustomer());
		accountResponse.setActive(account.isActive());
		accountResponse.setBank_details( account.getBank().getId() + " " + account.getBank().getBank_name());

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

	public TransactionResponse convertTransactionToTransactionResponse(Transaction transaction) {
		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setId(transaction.getId());
		transactionResponse.setType(transaction.getType());
		transactionResponse.setAccount(transaction.getSenderAccount());
		transactionResponse.setAmount(transaction.getAmount());
		transactionResponse.setDate(transaction.getDate());
		return transactionResponse;
	}

	public TransferResponse convertTransactionToTransferResponse(Transaction transaction) {
		TransferResponse transactionResponse = new TransferResponse();
		transactionResponse.setId(transaction.getId());
		transactionResponse.setType(transaction.getType());
		transactionResponse.setSender(transaction.getSenderAccount());
		Account receiver = accountRepository.findById(transaction.getReceiverAccount()).get();
		transactionResponse.setReceiver(receiver);
		transactionResponse.setAmount(transaction.getAmount());
		transactionResponse.setDate(transaction.getDate());
		return transactionResponse;
	}
	public MyAccountResponse convertAccountToMyAccount(Account account) {
		MyAccountResponse myAccount = new MyAccountResponse();
		myAccount.setAccount_number(account.getAccountNumber());
		myAccount.setBalance(account.getBalance());
		myAccount.setBank_details( account.getBank().getId() + " " + account.getBank().getBank_name());
		return myAccount;
	}

	public Transaction convertTransactionRequestToTransaction(String type,TransactionRequest transactionRequest) {
		Transaction transaction = new Transaction();
		Account sender = accountRepository.findById(transactionRequest.getAccount()).get();
		transaction.setAmount(transactionRequest.getAmount());
		transaction.setDate(LocalDateTime.now());
		transaction.setSenderAccount(sender);
		transaction.setSenderBalance(sender.getBalance());
		transaction.setType(type);
		return transaction;
	}

	public Transaction convertTransferRequestToTransaction(TransferRequest transferRequest) {
		Transaction transaction = new Transaction();
		Account sender = accountRepository.findById(transferRequest.getSender_account()).get();
		transaction.setAmount(transferRequest.getAmount());
		transaction.setDate(LocalDateTime.now());
		transaction.setSenderAccount(sender);
		transaction.setSenderBalance(sender.getBalance());
		transaction.setReceiverAccount(transferRequest.getReceiver_account());
		Account receiver = accountRepository.findById(transaction.getReceiverAccount()).get();
		transaction.setReceiverBalance(receiver.getBalance());
		transaction.setType("Transfer");
		return transaction;
	}
}
