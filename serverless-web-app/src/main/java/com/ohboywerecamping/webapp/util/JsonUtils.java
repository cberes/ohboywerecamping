package com.ohboywerecamping.webapp.util;

import java.io.IOException;

import com.ohboywerecamping.webapp.Main;

public final class JsonUtils {
    private JsonUtils() {
        throw new UnsupportedOperationException("cannot instantiate " + getClass());
    }

    public static String toJson(final Object obj) {
        try {
            return Main.jackson().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("error creating JSON", e);
        }
    }
}
