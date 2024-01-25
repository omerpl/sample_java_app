package com.personal.interview.hostfully.bookingapp.exception;

public class InvalidBookingUpdateException extends Exception{
    public InvalidBookingUpdateException(String errorMessage) {
        super(errorMessage);
    }
}
