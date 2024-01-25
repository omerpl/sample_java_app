package com.personal.interview.hostfully.bookingapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personal.interview.hostfully.bookingapp.dto.BookingDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table
public class Booking {
    @Id
    @GeneratedValue
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "UTC")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "UTC")
    private Date toDate;

    private boolean isBlock;

    @ManyToOne
    private Property property;

    @ManyToOne
    private Guest guest;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public BookingDto toDto() {
        return BookingDto.builder()
                .bookingId(this.id)
                .fromDate(this.fromDate)
                .toDate(this.toDate)
                .propertyId(this.property.getId())
                .guestName(this.guest.getName())
                .isBlocking(this.isBlock)
                .status(this.status)
                .build();
    }
}
