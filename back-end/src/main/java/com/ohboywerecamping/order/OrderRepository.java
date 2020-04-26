package com.ohboywerecamping.order;

import java.util.List;

import com.ohboywerecamping.common.Repository;
import com.ohboywerecamping.domain.Order;

public interface OrderRepository extends Repository<Order, String> {
    List<Order> findByCustomer(String customerId);
}
