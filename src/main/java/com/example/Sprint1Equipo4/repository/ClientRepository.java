package com.example.Sprint1Equipo4.repository;

import com.example.Sprint1Equipo4.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
   @Query("SELECT c FROM Client c " +
         "JOIN c.hotelBooking hb " +
         "WHERE YEAR(hb.reservedDate) = :year " +
         "GROUP BY c.id " +
         "ORDER BY COUNT(hb.id) DESC")
   List<Client> findBookingQuantityByYear(@Param("year") int year);

   Client findByUserName(String userName);

//   @Query("SELECT c FROM Client c " +
//         "JOIN c.flightReservations fr WHERE YEAR(fr.reservedDate) = :year GROUP BY c ORDER BY COUNT(fr) DESC LIMIT 3 ")
//   List<Client> findClientsByBookingQuantity(@Param("year") int year);
}
