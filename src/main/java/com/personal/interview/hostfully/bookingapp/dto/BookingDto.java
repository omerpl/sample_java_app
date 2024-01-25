package com.personal.interview.hostfully.bookingapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personal.interview.hostfully.bookingapp.model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {
    private Integer bookingId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "UTC")
    private Date fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "UTC")
    private Date toDate;
    private Integer propertyId;
    private String guestName;
    private boolean isBlocking = false;
    private BookingStatus status = BookingStatus.NORMAL;
}
