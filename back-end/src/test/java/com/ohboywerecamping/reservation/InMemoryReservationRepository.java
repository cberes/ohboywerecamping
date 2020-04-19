package com.ohboywerecamping.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ohboywerecamping.domain.Campsite;
import com.ohboywerecamping.domain.Customer;
import com.ohboywerecamping.domain.Reservation;
import com.ohboywerecamping.test.InMemoryRepository;

import static java.util.Collections.emptyList;

public class InMemoryReservationRepository
        extends InMemoryRepository<Reservation, Long> implements ReservationRepository {
    private InMemoryReservationRepository() {
        super(i -> (long) i, Reservation::setId, emptyList());
    }

    public InMemoryReservationRepository(final List<Campsite> campsites,
                                         final Customer customer,
                                         final LocalDate start) {
        super(i -> (long) i, Reservation::setId, reservations(campsites, customer, start));
    }

    private static List<Reservation> reservations(final List<Campsite> campsites,
                                                  final Customer customer,
                                                  final LocalDate start) {
        final List<Reservation> reservations = new LinkedList<>();
        for (int i = 0; i < campsites.size(); ++i) {
            final Campsite campsite = campsites.get(i);
            final List<Reservation> toAdd = i % 2 == 0 ? reservations1(campsite, customer, start)
                    : reservations2(campsite, customer, start);
            reservations.addAll(toAdd);
        }
        return reservations;
    }

    private static List<Reservation> reservations1(final Campsite campsite, final Customer customer,
                                                   final LocalDate start) {
        final List<Reservation> reservations = new ArrayList<>();
        reservations.add(build(campsite, customer, start.plusDays(10), start.plusDays(12)));
        reservations.add(build(campsite, customer, start.minusDays(12), start.minusDays(9)));
        reservations.add(build(campsite, customer, start.plusDays(13), start.plusDays(25)));
        reservations.add(build(campsite, customer, start.plusDays(7), start.plusDays(8)));
        reservations.add(build(campsite, customer, start.minusDays(4), start));
        reservations.add(build(campsite, customer, start.plusDays(3), start.plusDays(4)));
        reservations.add(build(campsite, customer, start.plusDays(8), start.plusDays(9)));
        reservations.add(build(campsite, customer, start, start.plusDays(2)));
        reservations.add(build(campsite, customer, start.minusDays(6), start.minusDays(5)));
        return reservations;
    }

    private static List<Reservation> reservations2(final Campsite campsite, final Customer customer,
                                                   final LocalDate start) {
        final List<Reservation> reservations = new ArrayList<>();
        reservations.add(build(campsite, customer, start.plusDays(7), start.plusDays(9)));
        reservations.add(build(campsite, customer, start.minusDays(9), start.minusDays(5)));
        reservations.add(build(campsite, customer, start.plusDays(9), start.plusDays(11)));
        reservations.add(build(campsite, customer, start.minusDays(14), start.minusDays(12)));
        return reservations;
    }

    private static Reservation build(final Campsite campsite, final Customer customer,
                                     final LocalDate start, final LocalDate end) {
        final Reservation reservation = new Reservation();
        reservation.setCampsite(campsite);
        reservation.setCustomer(customer);
        reservation.setStarting(start);
        reservation.setEnding(end);
        return reservation;
    }

    public static InMemoryReservationRepository empty() {
        return new InMemoryReservationRepository();
    }
}
