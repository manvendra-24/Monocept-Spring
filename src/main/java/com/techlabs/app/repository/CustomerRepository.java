package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	List<Customer> findAllCustomersAndBankById(int bank_id);

}
