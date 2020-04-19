package com.ohboywerecamping.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationTest {
    @Test
    void testNights() {
        final Reservation reservation = new Reservation();
        reservation.setStarting(LocalDate.of(2018, 4, 1));
        reservation.setEnding(LocalDate.of(2018, 4, 3));
        assertEquals(reservation.getNights(), 2);
    }
}
