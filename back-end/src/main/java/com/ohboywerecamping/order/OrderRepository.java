package com.ohboywerecamping.order;

import java.time.LocalDate;
import java.util.List;

import com.ohboywerecamping.common.Repository;
import com.ohboywerecamping.domain.Order;

public interface OrderRepository extends Repository<Order, String> {
    default List<Order> findByCustomer(String customerId) {
        return findByCustomer(customerId, LocalDate.MIN);
    }

    List<Order> findByCustomer(String customerId, LocalDate start);
}
