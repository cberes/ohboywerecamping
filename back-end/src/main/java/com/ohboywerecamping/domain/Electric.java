package com.ohboywerecamping.domain;

public enum Electric {
    NONE, UNKNOWN_AMP, THIRTY_AMP, FIFTY_AMP;

    public boolean isElectricPresent() {
        return this != NONE;
    }
}
