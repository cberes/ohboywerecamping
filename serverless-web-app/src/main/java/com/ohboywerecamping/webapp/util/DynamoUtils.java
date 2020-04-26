package com.ohboywerecamping.webapp.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import static java.util.stream.Collectors.toList;

public final class DynamoUtils {
    private DynamoUtils() {
        throw new UnsupportedOperationException("cannot instantiate " + getClass());
    }

    public static AttributeValue bool(final boolean b) {
        return AttributeValue.builder().bool(b).build();
    }

    public static AttributeValue s(final String s) {
        return AttributeValue.builder().s(s).build();
    }

    public static AttributeValue s(final LocalDate d) {
        return AttributeValue.builder().s(d.toString()).build();
    }

    public static AttributeValue s(final ZonedDateTime t) {
        return AttributeValue.builder().s(t.toString()).build();
    }

    public static AttributeValue l(final Collection<String> values) {
        return AttributeValue.builder().l(values.stream()
                .map(value -> s(value))
                .collect(toList())).build();
    }
}
