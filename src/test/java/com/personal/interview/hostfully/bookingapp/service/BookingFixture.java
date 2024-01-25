package com.personal.interview.hostfully.bookingapp.service;

import com.personal.interview.hostfully.bookingapp.dto.BookingDto;
import com.personal.interview.hostfully.bookingapp.model.Booking;
import com.personal.interview.hostfully.bookingapp.model.BookingStatus;
import com.personal.interview.hostfully.bookingapp.model.Guest;
import com.personal.interview.hostfully.bookingapp.model.Property;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class BookingFixture {
    public static LocalDate localFromDate = LocalDate.of(2050, 8, 19);
    public static LocalDate localToDate = LocalDate.of(2050, 8, 22);
    public static Property aProperty = new Property(1, "property 1");
    public static Guest aGuest = new Guest(1, "Guest 1");
    public static Guest anotherGuest = new Guest(1, "Guest 2");
    public static Booking aBooking = new Booking(
            1,
            Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            false,
            aProperty,
            aGuest,
            BookingStatus.NORMAL
    );
    public static Booking aCancelledBooking = new Booking(
            1,
            Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            false,
            aProperty,
            aGuest,
            BookingStatus.CANCELLED
    );
    public static Booking anotherBooking = new Booking(
            1,
            Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            false,
            aProperty,
            anotherGuest,
            BookingStatus.NORMAL
    );
    public static BookingDto aBookingDto = new BookingDto(
            1,
            Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            1, aGuest.getName(), false, BookingStatus.NORMAL
    );
    public static BookingDto aCancelledBookingDto = new BookingDto(
            1,
            Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            1, aGuest.getName(), false, BookingStatus.CANCELLED
    );
    public static BookingDto anotherBookingDto = new BookingDto(
            1,
            Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
            1, anotherGuest.getName(), false, BookingStatus.NORMAL
    );
}
