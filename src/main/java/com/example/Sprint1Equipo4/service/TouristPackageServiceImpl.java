package com.example.Sprint1Equipo4.service;

import com.example.Sprint1Equipo4.dto.response.StatusDTO;
import com.example.Sprint1Equipo4.exception.ResourceNotFoundException;
import com.example.Sprint1Equipo4.model.FlightReservation;
import com.example.Sprint1Equipo4.model.HotelBooking;
import com.example.Sprint1Equipo4.model.TouristPackage;
import com.example.Sprint1Equipo4.repository.TouristPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TouristPackageServiceImpl implements TouristPackageService {

    @Autowired
    private TouristPackageRepository touristPackageRepository;

    @Autowired
    private FlightReservationRepository flightReservationRepository;

    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    public TouristPackage createPackage(TouristPackage paquete) {

        if (paquete.getBookingsOrReservations().size() != 2) {
            throw new IllegalArgumentException("El paquete debe contener exactamente dos reservas.");
        }

        return touristPackageRepository.save(paquete);
    }

    public TouristPackage updatePackage(Long packageNumber, TouristPackage packageDetails) {
        TouristPackage paquete = touristPackageRepository.findById(packageNumber)
                .orElseThrow(ResourceNotFoundException::new);

        paquete.setName(packageDetails.getName());
        paquete.setCreationDate(packageDetails.getCreationDate());
        paquete.setClientId(packageDetails.getClientId());
        paquete.setBookingsOrReservations(packageDetails.getBookingsOrReservations());

        if (paquete.getBookingsOrReservations().size() != 2) {
            throw new IllegalArgumentException("El paquete debe contener exactamente dos reservas.");
        }

        return touristPackageRepository.save(paquete);
    }

    public List<TouristPackage> getAllPackages() {
        return touristPackageRepository.findAll();
    }

    public void deletePackage(Long packageNumber) {
        TouristPackage paquete = touristPackageRepository.findById(packageNumber)
                .orElseThrow(ResourceNotFoundException::new);
        touristPackageRepository.delete(paquete);
    }
}


