package com.personal.interview.hostfully.bookingapp.service;

import com.personal.interview.hostfully.bookingapp.dto.BookingDto;
import com.personal.interview.hostfully.bookingapp.exception.BookingNotAvailableException;
import com.personal.interview.hostfully.bookingapp.exception.InvalidBookingDatesException;
import com.personal.interview.hostfully.bookingapp.exception.InvalidBookingUpdateException;
import com.personal.interview.hostfully.bookingapp.exception.ResourceNotFoundException;
import com.personal.interview.hostfully.bookingapp.model.Booking;
import com.personal.interview.hostfully.bookingapp.model.BookingStatus;
import com.personal.interview.hostfully.bookingapp.repository.BookingRepository;
import com.personal.interview.hostfully.bookingapp.repository.GuestRepository;
import com.personal.interview.hostfully.bookingapp.repository.PropertyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.personal.interview.hostfully.bookingapp.service.BookingFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    static BookingRepository bookingRepository;

    @Mock
    static GuestRepository guestRepository;

    @Mock
    static PropertyRepository propertyRepository;
    static BookingService service;


    @BeforeEach
    void setUp() {
        service = new BookingService(bookingRepository, guestRepository, propertyRepository);
    }

    @Test
    public void should_throw_InvalidBookingDatesException_if_fromDate_is_after_than_toDate() {
        LocalDate localFromDate = LocalDate.of(2040, 8, 19);
        LocalDate localToDate = LocalDate.of(2040, 8, 18);

        BookingDto dto = BookingDto.builder()
                .fromDate(Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .toDate(Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .propertyId(1)
                .build();

        Assertions.assertThrows(InvalidBookingDatesException.class, () -> service.createBooking(dto));
    }

    @Test
    public void should_throw_InvalidBookingDatesException_if_dates_at_past() {
        LocalDate localFromDate = LocalDate.of(2010, 8, 19);
        LocalDate localToDate = LocalDate.of(2010, 8, 20);

        BookingDto dto = BookingDto.builder()
                .fromDate(Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .toDate(Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .propertyId(1)
                .build();

        Assertions.assertThrows(InvalidBookingDatesException.class, () -> service.createBooking(dto));
    }

    @Test
    public void should_throw_InvalidBookingDatesException_if_fromDate_is_equal_to_toDate() {
        LocalDate localFromDate = LocalDate.of(2050, 8, 19);
        LocalDate localToDate = LocalDate.of(2050, 8, 19);

        BookingDto dto = BookingDto.builder()
                .fromDate(Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .toDate(Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .propertyId(1)
                .build();

        Assertions.assertThrows(InvalidBookingDatesException.class, () -> service.createBooking(dto));
    }

    @Test
    public void should_throw_BookingNotAvailableException_if_dates_are_not_applicable() {
        LocalDate localFromDate = LocalDate.of(2050, 8, 19);
        LocalDate localToDate = LocalDate.of(2050, 8, 22);

        BookingDto dto = BookingDto.builder()
                .fromDate(Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .toDate(Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .propertyId(1)
                .build();

        when(bookingRepository.getBookingIdsInDatesAndProperty(any(), any(), any()))
                .thenReturn(List.of(1, 2, 3));

        Assertions.assertThrows(BookingNotAvailableException.class, () -> service.createBooking(dto));
    }

    @Test
    public void should_throw_ResourceNotFoundException_if_property_does_not_exist() {
        BookingDto dto = BookingDto.builder()
                .fromDate(Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .toDate(Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .propertyId(1234)
                .guestName(aGuest.getName())
                .build();

        when(bookingRepository.getBookingIdsInDatesAndProperty(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(guestRepository.findByName(aGuest.getName())).thenReturn(Optional.of(aGuest));
        when(propertyRepository.findById(1234)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.createBooking(dto));
    }

    @Test
    public void should_create_booking() {

        BookingDto dto = BookingDto.builder()
                .fromDate(Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .toDate(Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .propertyId(aProperty.getId())
                .guestName(aGuest.getName())
                .build();

        when(bookingRepository.getBookingIdsInDatesAndProperty(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(guestRepository.findByName(aGuest.getName())).thenReturn(Optional.of(aGuest));
        when(propertyRepository.findById(1)).thenReturn(Optional.of(aProperty));
        when(bookingRepository.save(any())).thenReturn(aBooking);

        BookingDto actual = service.createBooking(dto);

        verify(bookingRepository, times(1)).save(any());
        assertEquals(aBookingDto, actual);
    }

    @Test
    public void should_throw_InvalidBookingUpdateException_if_api_called_with_different_state() {
        BookingDto dto = BookingDto.builder()
                .fromDate(Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .toDate(Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()))
                .propertyId(aProperty.getId())
                .guestName(aGuest.getName())
                .status(BookingStatus.CANCELLED)
                .build();

        when(bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of(aBooking));


        Assertions.assertThrows(InvalidBookingUpdateException.class, () -> service.updateBooking(aBooking.getId(), dto));
    }


    @Test
    public void should_throw_InvalidBookingUpdateException_if_api_called_with_overlapping_dates() {
        BookingDto dto = aBooking.toDto();

        when(bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of(aBooking));
        when(guestRepository.findByName(aGuest.getName())).thenReturn(Optional.of(aGuest));
        when(propertyRepository.findById(1)).thenReturn(Optional.of(aProperty));
        when(bookingRepository.getBookingIdsInDatesAndProperty(any(), any(), any()))
                .thenReturn(List.of(1, 2, 3));
        Assertions.assertThrows(InvalidBookingUpdateException.class, () -> service.updateBooking(aBooking.getId(), dto));
    }

    @Test
    public void should_update_booking() {
        BookingDto anotherDto = new BookingDto(
                1,
                Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                1, anotherGuest.getName(), false, BookingStatus.NORMAL
        );
        when(bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of(new Booking(
                1,
                Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                false,
                aProperty,
                aGuest,
                BookingStatus.NORMAL
        )));
        when(guestRepository.findByName(anotherGuest.getName())).thenReturn(Optional.of(anotherGuest));
        when(propertyRepository.findById(1)).thenReturn(Optional.of(aProperty));
        when(bookingRepository.getBookingIdsInDatesAndProperty(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any())).thenReturn(anotherBooking);

        BookingDto actual = service.updateBooking(aBooking.getId(), anotherDto);
        verify(bookingRepository, times(1)).save(any());

        assertEquals(anotherBookingDto, actual);
    }

    @Test
    public void should_throw_ResourceNotFoundException_if_no_booking_to_cancel(){
        when( bookingRepository.findById(aCancelledBooking.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.cancelBooking(aBooking.getId()));
    }
    @Test
    public void should_not_call_save_because_booking_was_already_cancelled(){
        when( bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of(aCancelledBooking));

        BookingDto actual = service.cancelBooking(aCancelledBooking.getId());

        assertEquals(aCancelledBookingDto, actual);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    public void should_cancel_a_booking(){
        when( bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of( new Booking(
                1,
                Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                false,
                aProperty,
                aGuest,
                BookingStatus.NORMAL
        )));
        when(bookingRepository.save(any())).thenReturn(aCancelledBooking);

        BookingDto actual = service.cancelBooking(aBooking.getId());
        assertEquals(aCancelledBookingDto, actual);
    }


    @Test
    public void should_throw_ResourceNotFoundException_if_no_booking_to_rebook(){
        when( bookingRepository.findById(aCancelledBooking.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.rebookCencelledBooking(aBooking.getId()));
    }
    @Test
    public void should_throw_InvalidBookingUpdateException_because_booking_was_not_cancelled(){
        when( bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of(aBooking));
        Assertions.assertThrows(InvalidBookingUpdateException.class, () -> service.rebookCencelledBooking(aBooking.getId()));
    }

    @Test
    public void should_throw_InvalidBookingUpdateException_if_booking_dates_are_not_available(){
        when( bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of(aCancelledBooking));
        when(bookingRepository.getBookingIdsInDatesAndProperty(any(), any(), any()))
                .thenReturn(List.of(1, 2, 3));
        Assertions.assertThrows(InvalidBookingUpdateException.class, () -> service.rebookCencelledBooking(aBooking.getId()));
    }
    @Test
    public void should_rebook_a_booking(){
        when( bookingRepository.findById(aBooking.getId())).thenReturn(Optional.of(new Booking(
                1,
                Date.from(localFromDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                Date.from(localToDate.atStartOfDay(ZoneId.of("UTC")).toInstant()),
                false,
                aProperty,
                aGuest,
                BookingStatus.CANCELLED
        )));
        when(bookingRepository.getBookingIdsInDatesAndProperty(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any())).thenReturn((aBooking));

        service.rebookCencelledBooking(aCancelledBooking.getId());

        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    public void should_call_delete(){
        service.deleteBooking(aBooking.getId());
        verify(bookingRepository,times(1)).deleteById(aBooking.getId());
    }


    @Test
    public void should_call_findAll(){
        service.getAllBookings();
        verify(bookingRepository,times(1)).findAll();
    }
}
