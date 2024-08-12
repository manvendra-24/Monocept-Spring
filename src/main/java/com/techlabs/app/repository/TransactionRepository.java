package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{


	Page<Transaction> findBySenderAccount(Account account, PageRequest pageable);

	Page<Transaction> findBySenderAccountOrReceiverAccount(Account account, int accountNumber, PageRequest pageable);

	List<Transaction> findBySenderAccountOrReceiverAccount(Account account, int number);

	
}

