package com.ohboywerecamping.customer;

import java.util.Optional;

import com.ohboywerecamping.domain.Customer;

public interface CustomerComponent {
    Optional<Customer> findCustomerByEmail(String email);

    Customer findOrCreateCustomer(String email);
}
