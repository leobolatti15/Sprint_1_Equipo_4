package com.example.Sprint1Equipo4.config;

import com.example.Sprint1Equipo4.dto.ErrorDto;
import com.example.Sprint1Equipo4.exception.HotelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
   @ExceptionHandler(HotelNotFoundException.class)
   public ResponseEntity<ErrorDto> handleHotelNonExistException(HotelNotFoundException ex) {
      ErrorDto error = new ErrorDto("El destino o fecha seleccionado no coincide con los de nuestra lista de hoteles", 404);
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
   }
}