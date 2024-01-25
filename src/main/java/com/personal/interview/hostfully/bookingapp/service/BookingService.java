package com.personal.interview.hostfully.bookingapp.service;

import com.personal.interview.hostfully.bookingapp.dto.BookingDto;
import com.personal.interview.hostfully.bookingapp.exception.BookingNotAvailableException;
import com.personal.interview.hostfully.bookingapp.exception.InvalidBookingDatesException;
import com.personal.interview.hostfully.bookingapp.exception.InvalidBookingUpdateException;
import com.personal.interview.hostfully.bookingapp.exception.ResourceNotFoundException;
import com.personal.interview.hostfully.bookingapp.model.Booking;
import com.personal.interview.hostfully.bookingapp.model.BookingStatus;
import com.personal.interview.hostfully.bookingapp.model.Guest;
import com.personal.interview.hostfully.bookingapp.model.Property;
import com.personal.interview.hostfully.bookingapp.repository.BookingRepository;
import com.personal.interview.hostfully.bookingapp.repository.GuestRepository;
import com.personal.interview.hostfully.bookingapp.repository.PropertyRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final PropertyRepository propertyRepository;

    public BookingService(BookingRepository bookingRepository, GuestRepository guestRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.propertyRepository = propertyRepository;
    }

    @SneakyThrows
    public BookingDto getBooking(int id) {
        return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No bookings found")).toDto();
    }

    @SneakyThrows
    public BookingDto createBooking(BookingDto bookingDto) {
        validateDtoDates(bookingDto);
        List<Integer> existingBookings = bookingRepository.getBookingIdsInDatesAndProperty(bookingDto.getToDate(), bookingDto.getFromDate(), bookingDto.getPropertyId());
        if (!existingBookings.isEmpty()) {
            throw new BookingNotAvailableException("Specified dates are not available to book");
        }
        Guest guest = getGuestByName(bookingDto);
        Property property = getPropertyById(bookingDto);

        return bookingRepository.save(toBookingModel(bookingDto, guest, property)).toDto();
    }

    @SneakyThrows
    public BookingDto updateBooking(int id, BookingDto bookingDto) {
        validateDtoDates(bookingDto);
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No booking with id :" + id));
        if (!booking.getStatus().equals(bookingDto.getStatus())) {
            throw new InvalidBookingUpdateException("For status updates please use related apis");
        }
        Guest guest = getGuestByName(bookingDto);
        Property property = getPropertyById(bookingDto);
        List<Integer> existingBookings = bookingRepository.getBookingIdsInDatesAndProperty(bookingDto.getToDate(), bookingDto.getFromDate(), bookingDto.getPropertyId());
        if (existingBookings.isEmpty() || (existingBookings.size() == 1 && Objects.equals(existingBookings.get(0), booking.getId()))) {
            booking.setFromDate(bookingDto.getFromDate());
            booking.setToDate(bookingDto.getToDate());
            booking.setProperty(property);
            booking.setGuest(guest);
            booking.setBlock(bookingDto.isBlocking());
            return bookingRepository.save(booking).toDto();
        } else {
            throw new InvalidBookingUpdateException("Cant rebook booking because it was booked by another");
        }
    }

    @SneakyThrows
    public BookingDto cancelBooking(int id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No booking with id :" + id));
        if (!booking.getStatus().equals(BookingStatus.CANCELLED)) {
            booking.setStatus(BookingStatus.CANCELLED);
            return bookingRepository.save(booking).toDto();
        }
        return booking.toDto();
    }

    @SneakyThrows
    public BookingDto rebookCencelledBooking(int id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No booking with id :" + id));
        if(booking.getStatus().equals(BookingStatus.NORMAL)){
            throw new InvalidBookingUpdateException("Cant rebook booking because it was not cancelled");
        }
        List<Integer> existingBookings = bookingRepository.getBookingIdsInDatesAndProperty(booking.getToDate(), booking.getFromDate(), booking.getProperty().getId());
        if (existingBookings.isEmpty() || (existingBookings.size() == 1 && Objects.equals(existingBookings.get(0), booking.getId()))) {
            booking.setStatus(BookingStatus.NORMAL);
            return bookingRepository.save(booking).toDto();
        } else {
            throw new InvalidBookingUpdateException("Cant rebook booking because it was booked by another");
        }
    }

    public void deleteBooking(int id) {
        bookingRepository.deleteById(id);
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(Booking::toDto).collect(Collectors.toList());
    }

    private Property getPropertyById(BookingDto bookingDto) throws ResourceNotFoundException {
        return propertyRepository.findById(bookingDto.getPropertyId()).orElseThrow(() -> new ResourceNotFoundException("No property with id " + bookingDto.getPropertyId()));
    }

    private Guest getGuestByName(BookingDto bookingDto) {
        return guestRepository.findByName(bookingDto.getGuestName())
                .orElseGet(()->guestRepository.save(Guest.builder().name(bookingDto.getGuestName()).build()));
    }

    private Booking toBookingModel(BookingDto bookingDto, Guest guest, Property property) {
        return Booking.builder()
                .fromDate(bookingDto.getFromDate())
                .toDate(bookingDto.getToDate())
                .isBlock(bookingDto.isBlocking())
                .property(property)
                .guest(guest)
                .status(BookingStatus.NORMAL)
                .build();
    }

    private void validateDtoDates(BookingDto dto) throws InvalidBookingDatesException {
        LocalDate now = LocalDate.now();
        LocalDate localFromDate = LocalDate.from(dto.getFromDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
        LocalDate localToDate = LocalDate.from(dto.getToDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());

        if (now.isAfter(localFromDate) ||
                localFromDate.isAfter(localToDate) ||
                localFromDate.isEqual(localToDate)) {
            throw new InvalidBookingDatesException("Booking dates are not valid.");
        }
    }
}
