package com.example.Sprint1Equipo4.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    private String userName;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    private String destination;

    private String hotelCode;

    private Integer peopleAmount;

    private String roomType;

    @OneToMany(mappedBy = "hotelBooking")
    private List<People> people;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "hotelBooking")
    private Hotel hotel;


    public void setUserName(String userName) {
    }
}


