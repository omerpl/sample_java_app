package com.personal.interview.hostfully.bookingapp.exception;

public class InvalidBookingDatesException extends Exception{
    public InvalidBookingDatesException(String errorMessage) {
        super(errorMessage);
    }
}
