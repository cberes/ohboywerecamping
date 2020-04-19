package com.ohboywerecamping.availability;

import com.ohboywerecamping.domain.Availability;

import java.time.LocalDate;
import java.util.function.Function;

final class AvailabilityFunction implements Function<LocalDate, Availability> {
    private final LocalDate maxDate;
    private final Availability positiveStatus;

    AvailabilityFunction(final LocalDate maxDate, final Availability positiveStatus) {
        this.maxDate = maxDate;
        this.positiveStatus = positiveStatus;
    }

    @Override
    public Availability apply(final LocalDate date) {
        return date.isBefore(maxDate) ? positiveStatus : Availability.AVAILABLE;
    }
}
