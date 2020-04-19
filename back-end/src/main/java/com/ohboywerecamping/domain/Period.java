package com.ohboywerecamping.domain;

import java.time.LocalDate;

public class Period {
    private LocalDate start;
    private LocalDate end;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(final LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(final LocalDate end) {
        this.end = end;
    }
}
