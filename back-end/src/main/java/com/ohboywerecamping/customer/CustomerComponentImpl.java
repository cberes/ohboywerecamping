package com.ohboywerecamping.customer;

import java.util.Optional;

import com.ohboywerecamping.domain.Customer;

public class CustomerComponentImpl implements CustomerComponent {
    private final CustomerRepository customers;

    public CustomerComponentImpl(final CustomerRepository customers) {
        this.customers = customers;
    }

    @Override
    public Optional<Customer> findCustomerByEmail(final String email) {
        return customers.findByEmail(email);
    }

    @Override
    public Customer findOrCreateCustomer(final String email) {
        return customers.findByEmail(email)
                .orElseGet(() -> create(email));
    }

    private Customer create(final String email) {
        final Customer customer = new Customer();
        customer.setEmail(email);
        customers.save(customer);
        return customer;
    }
}
