package com.ohboywerecamping.customer;

import java.util.List;

import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.test.InMemoryRepository;

public class InMemoryCustomerRepository extends InMemoryRepository<Customer, Long> implements CustomerRepository {
    public InMemoryCustomerRepository() {
        super(i -> (long) i, Customer::setId, List.of(customer()));
    }

    private static Customer customer() {
        final Customer customer = new Customer();
        customer.setFirstName("George");
        customer.setLastName("Washington");
        customer.setEmail("gwashington@whitehouse.gov");
        customer.setPhone("2025551234");
        return customer;
    }
}
