package com.ohboywerecamping.domain;

import java.math.BigDecimal;

public class Payment {
    private long id;

    private PaymentProcessor processor;

    private String transactionId;

    private BigDecimal amount;

    private BigDecimal tax;

    private BigDecimal fee;

    private Reservation reservation;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public PaymentProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(final PaymentProcessor processor) {
        this.processor = processor;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(final BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(final BigDecimal fee) {
        this.fee = fee;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(final Reservation reservation) {
        this.reservation = reservation;
    }
}
