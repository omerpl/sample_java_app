package com.personal.interview.hostfully.bookingapp.repository;

import com.personal.interview.hostfully.bookingapp.model.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends CrudRepository<Guest, Integer> {
    Optional<Guest> findByName(String name);
}
