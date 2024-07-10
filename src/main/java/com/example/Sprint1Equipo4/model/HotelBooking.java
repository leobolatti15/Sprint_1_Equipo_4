package com.example.Sprint1Equipo4.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    private String destination;

    private String hotelCode;

    private Integer peopleAmount;

    private String roomType;

    private List<People> people;

    private PaymentMethod paymentMethod;


}


