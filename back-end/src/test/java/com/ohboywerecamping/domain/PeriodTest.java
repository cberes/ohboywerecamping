package com.ohboywerecamping.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class PeriodTest {
    @Test
    void test() {
        final Period period = new Period();
        assertNull(period.getStart());
    }
}
