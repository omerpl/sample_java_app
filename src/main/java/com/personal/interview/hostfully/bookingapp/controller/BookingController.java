package com.personal.interview.hostfully.bookingapp.controller;

import com.personal.interview.hostfully.bookingapp.dto.BookingDto;
import com.personal.interview.hostfully.bookingapp.service.BookingService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {

    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Integer id) {
        return service.getBooking(id);
    }

    @GetMapping("")
    public List<BookingDto> getAllBookings() {
        return service.getAllBookings();
    }

    @PostMapping("")
    @SneakyThrows
    public BookingDto createBooking(@RequestBody BookingDto bookingDto) {
        return service.createBooking(bookingDto);
    }

    @PutMapping("/{id}")
    @SneakyThrows
    public BookingDto updateBooking(@PathVariable("id") final Integer id,
                                    @RequestBody BookingDto dto) {
        return service.updateBooking(id, dto);
    }

    @DeleteMapping("/{id}")
    @SneakyThrows
    public void deleteBooking(@PathVariable("id") final Integer id) {
        service.deleteBooking(id);
    }

    @PutMapping("/{id}/cancel")
    @SneakyThrows
    public BookingDto cancelBooking(@PathVariable("id") final Integer id) {
        return service.cancelBooking(id);
    }

    @PutMapping("/{id}/rebook")
    @SneakyThrows
    public BookingDto rebookCencelledBooking(@PathVariable("id") final Integer id) {
        return service.rebookCencelledBooking(id);
    }
}
