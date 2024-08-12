package com.techlabs.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.app.controller.AdminController;
import com.techlabs.app.dto.AccountRequest;
import com.techlabs.app.dto.AccountResponse;
import com.techlabs.app.dto.CustomerResponse;
import com.techlabs.app.dto.ITransactionResponse;
import com.techlabs.app.dto.MyAccountResponse;
import com.techlabs.app.dto.TransactionRequest;
import com.techlabs.app.dto.TransactionResponse;
import com.techlabs.app.dto.TransferRequest;
import com.techlabs.app.dto.TransferResponse;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Bank;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.BankRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.TransactionRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;
import com.techlabs.app.util.DtoConversion;
import com.techlabs.app.util.PagedResponse;

@Service
public class ServiceImpl implements IService{

	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	CustomerRepository customerRepository;
	UserRepository userRepository;
	AccountRepository accountRepository;
	TransactionRepository transactionRepository;
	BankRepository bankRepository;
	JwtTokenProvider jwtTokenProvider;
	DtoConversion dtoConversion;
	
	public ServiceImpl(
					CustomerRepository customerRepository, 
					UserRepository userRepository,
					AccountRepository accountRepository,
					TransactionRepository transactionRepository,
					BankRepository bankRepository,
					DtoConversion dtoConversion,
					JwtTokenProvider jwtTokenProvider) {
		super();
		this.customerRepository = customerRepository;
		this.userRepository = userRepository;
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.bankRepository = bankRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.dtoConversion = dtoConversion;
	}
	
	
	
	@Override
	public PagedResponse<AccountResponse> getAllAccounts(int page, int size, String sortBy, String direction) {
		
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
	
		
		Page<Account> page1 = accountRepository.findAll(pageable);
		
		List<Account> accounts = page1.getContent();
		List<AccountResponse> accountsResponse = new ArrayList<>();
		for(Account account:accounts) {
			AccountResponse accountResponse = dtoConversion.convertAccountToResponse(account);
			accountsResponse.add(accountResponse);
		}
		
		
		logger.info("Admin successfully getting all accounts");
		return new PagedResponse<>(accountsResponse,page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	}
	
	@Override
	public PagedResponse<CustomerResponse> getAllCustomers(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Customer> page1 = customerRepository.findAll(pageable);
		List<Customer> customers = page1.getContent();
		List<CustomerResponse> customersResponse = new ArrayList<>();
		for(Customer customer:customers) {
			CustomerResponse customerResponse = dtoConversion.conversionCustomerToResponse(customer);
			customersResponse.add(customerResponse);
		}
		
		logger.info("Admin successfully getting all customers");
		return new PagedResponse<>(customersResponse,page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	}
	
	@Override
	public String createAnAccountForCustomer(AccountRequest accountRequest) {
		Account account = new Account();
		account.setBalance(1000);
		Optional<Customer> oCustomer = customerRepository.findById(accountRequest.getCustomer_id());
		if(oCustomer.isEmpty()){
			throw new ResourceNotFoundException("Customer not available");
		}
		Customer customer = oCustomer.get();
		customer.setTotalbalance(customer.getTotalbalance() + 1000);
		account.setCustomer(customer);
		account.setActive(true);
		Optional<Bank> oBank = bankRepository.findById(accountRequest.getBank_id());
		if(oBank.isEmpty()) {
			throw new ResourceNotFoundException("Bank not available");
		}
		account.setBank(oBank.get());
		accountRepository.save(account);
		
		logger.info("Admin successfully created a account");
		return "Account created successfully";
	}

	@Override
	public AccountResponse getAccountById(int account_id) {
		Optional<Account> oAccount = accountRepository.findById(account_id);
		if(oAccount.isEmpty()) {
			throw new ResourceNotFoundException("Account not exists");
		}
		Account account = oAccount.get();
		AccountResponse accountResponse = dtoConversion.convertAccountToResponse(account);
		
		logger.info("Admin successfully getting account by its id");
		return accountResponse;
	}
	
	@Override
	public int getTotalBalance(int customer_id) {
		Optional<Customer> oCustomer = customerRepository.findById(customer_id);
		if(oCustomer.isEmpty()){
			throw new ResourceNotFoundException("Customer not available");
		}
		Customer customer = oCustomer.get();
		
		logger.info("Admin successfully getting total balance of a customer");
		return customer.getTotalbalance();
	}

	@Override
	public int getBalance(int account_id) {
		Optional<Account> oAccount = accountRepository.findById(account_id);
		if(oAccount.isEmpty()) {
			throw new ResourceNotFoundException("Account not exists");
		}
		Account account = oAccount.get();
		
		logger.info("Admin successfully getting balance of account ");
		return account.getBalance();
	}

	@Override
	public CustomerResponse getCustomerById(int customer_id) {
		Optional<Customer> oCustomer = customerRepository.findById(customer_id);
		if(oCustomer.isEmpty()){
			throw new ResourceNotFoundException("Customer not available");
		}
		Customer customer = oCustomer.get();
		CustomerResponse customerResponse = dtoConversion.conversionCustomerToResponse(customer);
		logger.info("Admin successfully getting customer by id");
		return customerResponse;
	}

	@Override
	public PagedResponse<AccountResponse> getAllAccountByCustomer(int page, int size, String sortBy, String direction, int customer_id) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		
		
		Optional<Customer> oCustomer = customerRepository.findById(customer_id);
		if(oCustomer.isEmpty()){
			throw new ResourceNotFoundException("Customer not available");
		}
		Customer customer = oCustomer.get();
		
		System.out.println(customer.getId());
		Page<Account> page1 = accountRepository.findByCustomer(customer, pageable);
		List<Account> accounts = page1.getContent();
		List<AccountResponse> accountResponses = new ArrayList<>();
		for(Account account: accounts) {
			System.out.println(account);
			AccountResponse accountResponse = dtoConversion.convertAccountToResponse(account);
			accountResponses.add(accountResponse);
		}
		
		logger.info("Admin successfully getting all accounts of a customer");

		return new PagedResponse<>(accountResponses, page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	}

	@Override
	public ITransactionResponse getTransactionById(int transaction_id) {
		Optional<Transaction> oTransaction = transactionRepository.findById(transaction_id);
		if(oTransaction.isEmpty()) {
			throw new ResourceNotFoundException("Transaction not exists");
		}
		Transaction transaction = oTransaction.get();
		if(transaction.getType().equalsIgnoreCase("Transfer")) {
			TransferResponse transferResponse = dtoConversion.convertTransactionToTransferResponse(transaction);
			return transferResponse;
		}
		TransactionResponse transactionResponse = dtoConversion.convertTransactionToTransactionResponse(transaction);
		return transactionResponse;
	}


	public PagedResponse<ITransactionResponse> getTransactionsOfAccount(int accountId, int page, int size, String sortBy, String direction) {
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
	    Optional<Account> oAccount = accountRepository.findById(accountId);
	    if (oAccount.isEmpty()) {
	        throw new ResourceNotFoundException("Account not available");
	    }
	    Account account = oAccount.get();
	    Page<Transaction> transactionPage = transactionRepository.findBySenderAccountOrReceiverAccount(account, account.getAccountNumber(), pageable);
	    
	    List<Transaction> transactions = transactionPage.getContent(); 
		List<ITransactionResponse> transactionResponses = new ArrayList<>();
		for(Transaction transaction: transactions) {
			if(transaction.getType().equalsIgnoreCase("Transfer")) {
				TransferResponse transferResponse = dtoConversion.convertTransactionToTransferResponse(transaction);
				transactionResponses.add(transferResponse);
			}
			else {
				TransactionResponse transactionResponse = dtoConversion.convertTransactionToTransactionResponse(transaction);
				transactionResponses.add(transactionResponse);
			}
		}
	    return new PagedResponse<>(transactionResponses, transactionPage.getNumber(), transactionPage.getSize(),
	            transactionPage.getTotalElements(), transactionPage.getTotalPages(), transactionPage.isLast());
	}


	@Override
	public String deleteAnAccount(int account_id) {
		Optional<Account> oAccount = accountRepository.findById(account_id);
		if(oAccount.isEmpty()) {
			throw new ResourceNotFoundException("Account not exists");
		}
		Account account = oAccount.get();
		account.setActive(false);
		accountRepository.save(account);
		
		Customer customer = account.getCustomer();
		int newBalance = customer.getTotalbalance() - account.getBalance();
		customer.setTotalbalance(newBalance);
		customerRepository.save(customer);
		return "Account Deleted";
	}
	
	@Override
	public String reActivateAccount(int account_id) {
		
		Optional<Account> oAccount = accountRepository.findById(account_id);
		if(oAccount.isEmpty()) {
			throw new ResourceNotFoundException("Account not exists");
		}
		Account account = oAccount.get();
		if(account.isActive()) {
			throw new ApiException("Account already active.");
		}
		account.setActive(true);
		accountRepository.save(account);
		
		Customer customer = account.getCustomer();
		int newBalance = customer.getTotalbalance() + account.getBalance();
		customer.setTotalbalance(newBalance);
		customerRepository.save(customer);
		
		return "Account activated";
	}
	
	
	
	

	
	
	
	
	
	
	

	@Override
	public int getBalanceByToken(String token) {
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsername(username);
		if(oUser.isEmpty()) {
			oUser = userRepository.findByEmail(username);
		}
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("User is not available");
		}
		User user = oUser.get();
		Customer customer = user.getCustomer();
		return customer.getTotalbalance();
	}

	@Override
	public int getAccountBalanceByToken(String token, int account_id) {
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsername(username);
		if(oUser.isEmpty()) {
			oUser = userRepository.findByEmail(username);
		}
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("User is not available");
		}
		User user = oUser.get();
		Customer customer = user.getCustomer();
		List<Account> accounts = customer.getAccounts();
		for(Account account:accounts) {
			if(account.getAccountNumber() == account_id && account.isActive()) {
				return account.getBalance();
			}
		}
		throw new ApiException("Account not exists");
	}


	@Override
	public PagedResponse<MyAccountResponse> getAllAccountByToken(String token, int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
	    
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsername(username);
		if(oUser.isEmpty()) {
			oUser = userRepository.findByEmail(username);
		}
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("User is not available");
		}
		User user = oUser.get();
		Customer customer = user.getCustomer();
		
		Page<Account> page1 = accountRepository.findByCustomer(customer, pageable);
		List<Account> accounts = page1.getContent();
		
		List<MyAccountResponse> myAccountsResponse = new ArrayList<>();
		for(Account account:accounts) {
			if(account.isActive()) {
				MyAccountResponse myAccountResponse = dtoConversion.convertAccountToMyAccount(account);
				myAccountsResponse.add(myAccountResponse);
			}
		}
		return new PagedResponse<>(myAccountsResponse,page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
		
	}


	@Override
	public String deleteMyAccount(String token, int account_id) {
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsername(username);
		if(oUser.isEmpty()) {
			oUser = userRepository.findByEmail(username);
		}
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("User is not available");
		}
		User user = oUser.get();
		Customer customer = user.getCustomer();
		List<Account> accounts = customer.getAccounts();
		
		for(Account a:accounts) {
			if(a.getAccountNumber() == account_id && a.isActive()) {
				a.setActive(false);
				accountRepository.save(a);
				
				Customer c = a.getCustomer();
				int newBalance = c.getTotalbalance() - a.getBalance();
				c.setTotalbalance(newBalance);
				customerRepository.save(c);
				return "Account Deleted";
			}
		}
		throw new ApiException("Account not exists");
		
	}


	@Override
	public String doTransaction(String type, String token, TransactionRequest transactionRequest) {
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsername(username);
		if(oUser.isEmpty()) {
			oUser = userRepository.findByEmail(username);
		}
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("User is not available");
		}
		User user = oUser.get();
		Customer customer1 = user.getCustomer();
		List<Account> myaccounts = customer1.getAccounts();
		
		Account sender = new Account();
		int temp = 0;
		for(Account a:myaccounts) {
			if(a.getAccountNumber() == transactionRequest.getAccount() && a.isActive()) {
				temp = 1;
				sender = a;
			}
		}
		if(temp == 0) {
			throw new ApiException("Account not exists");
		}
		if(type.equalsIgnoreCase("DEPOSIT")){
			int amount = transactionRequest.getAmount();
			sender.setBalance(sender.getBalance() + amount);
			customer1.setTotalbalance(customer1.getTotalbalance() + amount);
			customerRepository.save(customer1);
			accountRepository.save(sender);
			Transaction transaction = dtoConversion.convertTransactionRequestToTransaction(type, transactionRequest);
			transactionRepository.save(transaction);
		}
		else if(type.equalsIgnoreCase("WITHDRAWAL")) {
			int amount = transactionRequest.getAmount();
			if(amount > sender.getBalance()) {
				throw new ResourceNotFoundException("Amount is not available to withdraw");
			}
			sender.setBalance(sender.getBalance() - amount);
			customer1.setTotalbalance(customer1.getTotalbalance() - amount);
			customerRepository.save(customer1);
			accountRepository.save(sender);
			Transaction transaction = dtoConversion.convertTransactionRequestToTransaction(type, transactionRequest);
			transactionRepository.save(transaction);
		}
		
		return "Transaction successful";
	}


	@Override
	public PagedResponse<ITransactionResponse> getMyAccountTransactions(String token, int account_id, int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
		
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsername(username);
		if(oUser.isEmpty()) {
			oUser = userRepository.findByEmail(username);
		}
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("User is not available");
		}
		User user = oUser.get();
		Customer customer = user.getCustomer();
		List<Account> accounts = customer.getAccounts();
		
		for(Account a:accounts) {
			if(a.getAccountNumber() == account_id && a.isActive()) {
		
				Page<Transaction> page1 = transactionRepository.findBySenderAccountOrReceiverAccount(a,a.getAccountNumber(), pageable);
				List<Transaction> transactions = page1.getContent();
				List<ITransactionResponse> transactionResponses = new ArrayList<>();
				for(Transaction t: transactions) {
					if(t.getType().equalsIgnoreCase("Transfer")) {
						TransferResponse transferResponse = dtoConversion.convertTransactionToTransferResponse(t);
						transactionResponses.add(transferResponse);
					}
					else {
						TransactionResponse transactionResponse = dtoConversion.convertTransactionToTransactionResponse(t);
						transactionResponses.add(transactionResponse);
					}
				}
				return new PagedResponse<>(transactionResponses,page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
			}
		}
		throw new ApiException("Account not exists");
		
	}



	@Override
	public String doTransfer(String token, TransferRequest transferRequest) {
		String username = jwtTokenProvider.getUsername(token);
		Optional<User> oUser = userRepository.findByUsername(username);
		if(oUser.isEmpty()) {
			oUser = userRepository.findByEmail(username);
		}
		if(oUser.isEmpty()) {
			throw new ResourceNotFoundException("User is not available");
		}
		User user = oUser.get();
		Customer customer1 = user.getCustomer();
		List<Account> myaccounts = customer1.getAccounts();
		
		Account sender = new Account();
		int temp = 0;
		for(Account a:myaccounts) {
			if(a.getAccountNumber() == transferRequest.getSender_account() && a.isActive()) {
				temp = 1;
				sender = a;
			}
		}
		if(temp == 0) {
			throw new ApiException("Sender Account not exists");
		}
		
		
		int amount = transferRequest.getAmount();
		int temp2 = 0;
		Account receiver = new Account();
		List<Account> accounts = accountRepository.findAll();
		for(Account a:accounts) {
			if(a.getAccountNumber() == transferRequest.getReceiver_account() && a.isActive()) {
				temp2 =1;
				receiver = a;
			}
		}
		if(temp2 == 0) {
			throw new ApiException("Receiver Account not exists");
		}
		Transaction transaction = dtoConversion.convertTransferRequestToTransaction(transferRequest);
		transactionRepository.save(transaction);
		Customer customer2 = receiver.getCustomer();
		sender.setBalance(sender.getBalance() - amount);
		customer1.setTotalbalance(customer1.getTotalbalance() - amount);
		receiver.setBalance(receiver.getBalance() + amount);
		customer2.setTotalbalance(customer2.getTotalbalance() + amount);
		customerRepository.save(customer1);
		accountRepository.save(sender);
		customerRepository.save(customer2);
		accountRepository.save(receiver);
		return "Transaction successful";
	}



	@Override
	public PagedResponse<AccountResponse> getAllAccountsOfBank(int bank_id, int page, int size, String sortBy,
			String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
	
		Optional<Bank> oBank = bankRepository.findById(bank_id);
		if(oBank.isEmpty()) {
			throw new ResourceNotFoundException("Bank is not available");
		}
		Bank bank = oBank.get();
		Page<Account> page1 = accountRepository.findByBank(bank, pageable);
		
		List<Account> accounts = page1.getContent();
		List<AccountResponse> accountsResponse = new ArrayList<>();
		for(Account account:accounts) {
			AccountResponse accountResponse = dtoConversion.convertAccountToResponse(account);
			accountsResponse.add(accountResponse);
		}
		
		return new PagedResponse<>(accountsResponse,page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	}
	
	
}
