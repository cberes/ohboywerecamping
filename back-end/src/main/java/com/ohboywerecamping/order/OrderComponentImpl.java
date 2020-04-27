package com.ohboywerecamping.order;

import java.time.LocalDate;
import java.util.*;

import com.ohboywerecamping.campsite.CampsiteRepository;
import com.ohboywerecamping.customer.CustomerRepository;
import com.ohboywerecamping.domain.Campsite;
import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.domain.Order;
import com.ohboywerecamping.domain.Reservation;
import com.ohboywerecamping.reservation.ReservationRepository;

public class OrderComponentImpl implements OrderComponent {
    private final CustomerRepository customers;
    private final OrderRepository orders;
    private final CampsiteRepository campsites;
    private final ReservationRepository reservations;

    public OrderComponentImpl(final CustomerRepository customers,
                              final OrderRepository orders,
                              final CampsiteRepository campsites,
                              final ReservationRepository reservations) {
        this.customers = customers;
        this.orders = orders;
        this.campsites = campsites;
        this.reservations = reservations;
    }

    @Override
    public Order create(final String email, final String campsiteId, final Collection<LocalDate> days) {
        return create(customers.findByEmail(email).get(), campsiteId, days);
    }

    @Override
    public Order create(final Customer customer, final String campsiteId, final Collection<LocalDate> days) {
        validateDays(days);

        final Campsite campsite = campsites.findById(campsiteId).get();

        final Order order = saveOrder(customer);

        reserveDaysWithRollback(order, campsite, days);

        return order;
    }

    private void validateDays(final Collection<LocalDate> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("days are required");
        }

        final LocalDate start = days.stream().min(LocalDate::compareTo).get();
        final LocalDate end = days.stream().max(LocalDate::compareTo).get();

        final Set<LocalDate> daySet = new HashSet<>(days);
        final boolean allDaysPresent = start.datesUntil(end.plusDays(1)).allMatch(daySet::contains);

        if (!allDaysPresent) {
            throw new IllegalArgumentException("days must be consecutive");
        }
    }

    private Order saveOrder(final Customer customer) {
        final Order order = new Order();
        order.setCustomer(customer);
        orders.save(order);
        return order;
    }

    private void reserveDaysWithRollback(final Order order, final Campsite campsite, final Collection<LocalDate> days) {
        final List<Reservation> reserved = new LinkedList<>();
        for (LocalDate date : days) {
            try {
                reserved.add(reserve(order, campsite, date));
            } catch (Exception e) {
                rollback(order, reserved);
            }
        }
    }

    private Reservation reserve(final Order order, final Campsite campsite, final LocalDate date) {
        final Reservation reservation = new Reservation();
        reservation.setCampsite(campsite);
        reservation.setDate(date);
        reservation.setOrder(order);
        reservations.store(reservation);
        return reservation;
    }

    private void rollback(final Order order, final List<Reservation> reserved) {
        reserved.forEach(reservations::delete);
        orders.delete(order);
    }

    @Override
    public Optional<Order> findOrderById(final String orderId) {
        final var order = orders.findById(orderId);
        order.ifPresent(it -> {
            customers.findById(it.getCustomer().getId()).ifPresent(it::setCustomer);
            it.setReservations(reservations.findByOrder(orderId));
        });
        return order;
    }

    @Override
    public List<Order> findOrdersByCustomerAfterDate(final String customerId, final LocalDate date) {
        final List<Order> customerOrders = this.orders.findByCustomer(customerId, date);
        customers.findById(customerId)
                .ifPresent(customer -> customerOrders.forEach(order -> order.setCustomer(customer)));
        return customerOrders;
    }
}
