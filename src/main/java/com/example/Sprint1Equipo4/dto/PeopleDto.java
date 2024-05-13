package com.example.Sprint1Equipo4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDto {

    private String dni;
    private String name;
    private String lastName;
    private String birthDate;
    private String email;

}
