package com.techlabs.app.service;

import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AccountRequest;
import com.techlabs.app.dto.AccountResponse;
import com.techlabs.app.dto.CustomerResponse;
import com.techlabs.app.dto.ITransactionResponse;
import com.techlabs.app.dto.MyAccountResponse;
import com.techlabs.app.dto.TransactionRequest;
import com.techlabs.app.dto.TransferRequest;
import com.techlabs.app.util.PagedResponse;



@Service
public interface IService {
	
	PagedResponse<AccountResponse> getAllAccounts(int page, int size, String sortBy, String direction);
	PagedResponse<CustomerResponse> getAllCustomers(int page, int size, String sortBy, String direction);
	AccountResponse getAccountById(int account_id);
	int getTotalBalance(int customer_id);
	int getBalance(int account_id);
	String createAnAccountForCustomer(AccountRequest accountRequest);
	CustomerResponse getCustomerById(int customer_id);
	PagedResponse<AccountResponse> getAllAccountByCustomer(int page, int size, String sortBy, String direction, int customer_id);
	ITransactionResponse getTransactionById(int transaction_id);
	String deleteAnAccount(int account_id);
	PagedResponse<ITransactionResponse> getTransactionsOfAccount(int account_id, int page, int size, String sortBy, String direction);
	String reActivateAccount(int account_id);
	PagedResponse<AccountResponse> getAllAccountsOfBank(int bank_id, int page, int size, String sortBy, String direction);

	

	
	int getBalanceByToken(String token);
	int getAccountBalanceByToken(String token, int account_id);
	PagedResponse<MyAccountResponse> getAllAccountByToken(String token, int page, int size, String sortBy, String direction);
	String doTransaction(String type, String token, TransactionRequest transactionRequest);
	String deleteMyAccount(String token,int account_id);
	PagedResponse<ITransactionResponse> getMyAccountTransactions(String token, int account_id, int page, int size, String sortBy, String direction);
	String doTransfer(String token, TransferRequest transferRequest);
	
}
