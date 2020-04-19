package com.ohboywerecamping.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessTest {
    @Test
    void test() {
        assertEquals(0, Access.UNKNOWN.ordinal());
    }
}
