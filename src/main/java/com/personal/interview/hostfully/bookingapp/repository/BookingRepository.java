package com.personal.interview.hostfully.bookingapp.repository;

import com.personal.interview.hostfully.bookingapp.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {
    Optional<Booking> findById(Integer id);
    List<Booking> findAll();
    @Query(value = "SELECT id FROM Booking  " +
            "WHERE property_id = :propertyId "+
            "AND status != 'CANCELLED'" +
            "AND ((from_date > :toDate AND to_date < :fromDate) OR (to_date > :fromDate AND from_date < :toDate)) ",
            nativeQuery = true
    )
    List<Integer> getBookingIdsInDatesAndProperty(Date toDate, Date fromDate, Integer propertyId);
}
