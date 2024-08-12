package com.techlabs.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Bank;
import com.techlabs.app.entity.Customer;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

	Page<Account> findByCustomer(Customer customer, Pageable pageable);

	Page<Account> findAll(Pageable pageable);

	Page<Account> findByBank(Bank bank, Pageable pageable);

}
