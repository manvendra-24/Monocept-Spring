package com.techlabs.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AccountRequest;
import com.techlabs.app.dto.AccountResponse;
import com.techlabs.app.dto.CustomerResponse;
import com.techlabs.app.dto.MyAccountResponse;
import com.techlabs.app.dto.TransactionRequest;
import com.techlabs.app.dto.TransactionResponse;
import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.ApiException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AccountRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.TransactionRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;
import com.techlabs.app.util.DtoConversion;
import com.techlabs.app.util.PagedResponse;

@Service
public class ServiceImpl implements IService{

	CustomerRepository customerRepository;
	UserRepository userRepository;
	AccountRepository accountRepository;
	TransactionRepository transactionRepository;
	JwtTokenProvider jwtTokenProvider;
	DtoConversion dtoConversion;
	
	public ServiceImpl(
					CustomerRepository customerRepository, 
					UserRepository userRepository,
					AccountRepository accountRepository,
					TransactionRepository transactionRepository, 
					DtoConversion dtoConversion,
					JwtTokenProvider jwtTokenProvider) {
		super();
		this.customerRepository = customerRepository;
		this.userRepository = userRepository;
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.dtoConversion = dtoConversion;
	}
	
	
	@Override
	public PagedResponse<AccountResponse> getAllAccounts(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		
		Page<Account> page1 = accountRepository.findAll(pageable);
		List<Account> accounts = page1.getContent();
		List<AccountResponse> accountsResponse = new ArrayList<>();
		for(Account account:accounts) {
			AccountResponse accountResponse = dtoConversion.convertAccountToResponse(account);
			accountsResponse.add(accountResponse);
		}
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
		accountRepository.save(account);
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
		return accountResponse;
	}
	
	@Override
	public int getTotalBalance(int customer_id) {
		Optional<Customer> oCustomer = customerRepository.findById(customer_id);
		if(oCustomer.isEmpty()){
			throw new ResourceNotFoundException("Customer not available");
		}
		Customer customer = oCustomer.get();
		return customer.getTotalbalance();
	}

	@Override
	public int getBalance(int account_id) {
		Optional<Account> oAccount = accountRepository.findById(account_id);
		if(oAccount.isEmpty()) {
			throw new ResourceNotFoundException("Account not exists");
		}
		Account account = oAccount.get();
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
		
		System.out.println(customer);
		Page<Account> page1 = accountRepository.findByCustomer(customer, pageable);
		List<Account> accounts = page1.getContent();
		List<AccountResponse> accountResponses = new ArrayList<>();
		for(Account account: accounts) {
			AccountResponse accountResponse = dtoConversion.convertAccountToResponse(account);
			accountResponses.add(accountResponse);
		}
		return new PagedResponse<>(accountResponses, page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
	}

	@Override
	public TransactionResponse getTransactionById(int transaction_id) {
		Optional<Transaction> oTransaction = transactionRepository.findById(transaction_id);
		if(oTransaction.isEmpty()) {
			throw new ResourceNotFoundException("Transaction not exists");
		}
		Transaction transaction = oTransaction.get();
		TransactionResponse transactionResponse = dtoConversion.convertTransactionToResponse(transaction);
		return transactionResponse;
	}


	public PagedResponse<TransactionResponse> getTransactionsOfAccount(int accountId, int page, int size, String sortBy, String direction) {
	    Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();
	    PageRequest pageable = PageRequest.of(page, size, sort);
	    Optional<Account> oAccount = accountRepository.findById(accountId);
	    if (oAccount.isEmpty()) {
	        throw new ResourceNotFoundException("Account not available");
	    }
	    Account account = oAccount.get();
	    Page<Transaction> transactionPage = transactionRepository.findBySenderAccount(account, pageable);
	    
	    List<Transaction> transactions = transactionPage.getContent(); 
		List<TransactionResponse> transactionResponses = new ArrayList<>();
		for(Transaction transaction: transactions) {
			TransactionResponse transactionResponse = dtoConversion.convertTransactionToResponse(transaction);
			transactionResponses.add(transactionResponse);
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
			if(account.getAccount_number() == account_id && account.isActive()) {
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
			if(a.getAccount_number() == account_id && a.isActive()) {
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
	public String doTransaction(String token, TransactionRequest transactionRequest) {
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
			if(a.getAccount_number() == transactionRequest.getSender_account() && a.isActive()) {
				temp = 1;
				sender = a;
			}
		}
		if(temp == 0) {
			throw new ApiException("Sender Account not exists");
		}
		String type = transactionRequest.getType();
		if(type.equalsIgnoreCase("DEPOSIT")){
			int amount = transactionRequest.getAmount();
			sender.setBalance(sender.getBalance() + amount);
			customer1.setTotalbalance(customer1.getTotalbalance() + amount);
			customerRepository.save(customer1);
			accountRepository.save(sender);
			Transaction transaction = dtoConversion.convertRequestToTransaction(transactionRequest);
			transactionRepository.save(transaction);
		}
		else if(type.equalsIgnoreCase("WITHDRAWAL")) {
			int amount = transactionRequest.getAmount();
			if(amount > sender.getBalance()) {
				throw new ResourceNotFoundException("Amount is not available to withdraw");
			}
			sender.setBalance(sender.getBalance() - amount);
			customer1.setTotalbalance(customer1.getTotalbalance() + amount);
			customerRepository.save(customer1);
			accountRepository.save(sender);
			Transaction transaction = dtoConversion.convertRequestToTransaction(transactionRequest);
			transactionRepository.save(transaction);
		}
		else if(type.equalsIgnoreCase("TRANSFER")) {
			int amount = transactionRequest.getAmount();
			int temp2 = 0;
			Account receiver = new Account();
			List<Account> accounts = accountRepository.findAll();
			for(Account a:accounts) {
				if(a.getAccount_number() == transactionRequest.getReceiver_account() && a.isActive()) {
					temp2 =1;
					receiver = a;
				}
			}
			if(temp2 == 0) {
				throw new ApiException("Receiver Account not exists");
			}
			Transaction transaction = dtoConversion.convertRequestToTransaction(transactionRequest);
			transactionRepository.save(transaction);
			Customer customer2 = receiver.getCustomer();
			sender.setBalance(sender.getBalance() - amount);
			customer1.setTotalbalance(customer1.getTotalbalance() - amount);
			receiver.setBalance(receiver.getBalance() + amount);
			receiver.getTransactions().add(transaction);
			customer2.setTotalbalance(customer2.getTotalbalance() + amount);
			customerRepository.save(customer1);
			accountRepository.save(sender);
			customerRepository.save(customer2);
			accountRepository.save(receiver);
		}
		return "Transaction successful";
	}


	@Override
	public PagedResponse<TransactionResponse> getMyAccountTransactions(String token, int account_id, int page, int size, String sortBy, String direction) {
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
			if(a.getAccount_number() == account_id && a.isActive()) {
		
				Page<Transaction> page1 = transactionRepository.findBySenderAccount(a, pageable);
				List<Transaction> transactions = page1.getContent();
				List<TransactionResponse> transactionResponses = new ArrayList<>();
				for(Transaction t: transactions) {
					TransactionResponse transactionResponse = dtoConversion.convertTransactionToResponse(t);
					transactionResponses.add(transactionResponse);
				}
				return new PagedResponse<>(transactionResponses,page1.getNumber(), page1.getSize(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
			}
		}
		throw new ApiException("Account not exists");
		
	}
	
	
}
