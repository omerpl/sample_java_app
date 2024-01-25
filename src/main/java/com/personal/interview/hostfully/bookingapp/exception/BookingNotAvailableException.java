package com.personal.interview.hostfully.bookingapp.exception;

public class BookingNotAvailableException extends Exception{
    public BookingNotAvailableException(String errorMessage) {
        super(errorMessage);
    }
}
