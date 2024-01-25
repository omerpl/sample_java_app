package com.personal.interview.hostfully.bookingapp.model;

public enum BookingStatus {
    NORMAL("NORMAL"),
    CANCELLED("CANCELLED");

    private final String value;

    BookingStatus(final String val) {
        this.value = val;
    }

    @Override
    public String toString() {
        return value;
    }
}
