package com.personal.interview.hostfully.bookingapp.repository;

import com.personal.interview.hostfully.bookingapp.model.Property;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Integer> {
    Optional<Property> findById(Integer id);
}
