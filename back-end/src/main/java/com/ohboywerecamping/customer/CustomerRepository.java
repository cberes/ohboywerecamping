package com.ohboywerecamping.customer;

import java.util.Optional;

import com.ohboywerecamping.common.Repository;
import com.ohboywerecamping.domain.Customer;

public interface CustomerRepository extends Repository<Customer, String> {
    Optional<Customer> findByEmail(String email);
}
