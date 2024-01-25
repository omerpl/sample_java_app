package com.personal.interview.hostfully.bookingapp;

import com.personal.interview.hostfully.bookingapp.exception.BookingNotAvailableException;
import com.personal.interview.hostfully.bookingapp.exception.InvalidBookingDatesException;
import com.personal.interview.hostfully.bookingapp.exception.InvalidBookingUpdateException;
import com.personal.interview.hostfully.bookingapp.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookingNotAvailableException.class)
    public ResponseEntity<String> HandleBookingNotAvailableException(BookingNotAvailableException e) {
        return new ResponseEntity<>("Booking is not successful: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBookingUpdateException.class)
    public ResponseEntity<String> handleInvalidBookingUpdateException(InvalidBookingUpdateException e) {
        return new ResponseEntity<>("Booking could not be updated: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> HandleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>("Required resource not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBookingDatesException.class)
    public ResponseEntity<String> handleInvalidBookingDatesException(InvalidBookingDatesException e) {
        return new ResponseEntity<>("Specified dates are not valid: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
