package com.example.Sprint1Equipo4.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightCode;
    private String origin;
    private String destination;
    private String seatType;
    private Integer pricePerPerson;
    @JsonFormat(pattern ="dd/MM/yyyy")
    private LocalDate dateFrom;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateTo;
}
