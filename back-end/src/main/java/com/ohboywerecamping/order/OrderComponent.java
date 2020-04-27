package com.ohboywerecamping.order;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.domain.Order;

public interface OrderComponent {
    Order create(String email, String campsiteId, Collection<LocalDate> days);

    Order create(Customer customer, String campsiteId, Collection<LocalDate> days);

    Optional<Order> findOrderById(String orderId);

    default List<Order> findOrdersByCustomer(String customerId) {
        return findOrdersByCustomerAfterDate(customerId, LocalDate.MIN);
    }

    List<Order> findOrdersByCustomerAfterDate(String customerId, LocalDate date);
}
