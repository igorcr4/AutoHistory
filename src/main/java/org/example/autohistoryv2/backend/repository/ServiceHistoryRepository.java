package org.example.autohistoryv2.backend.repository;

import org.example.autohistoryv2.backend.dto.ServiceHistoryDTO;
import org.example.autohistoryv2.backend.models.ServiceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, Long> {
    List<ServiceHistory> findByCarLicensePlate(String licensePlate);

    List<ServiceHistory> findByCarId(Long carId);


    @Query("SELECT new org.example.autohistoryv2.backend.dto.ServiceHistoryDTO(h.id, h.date, h.km, h.cost, h.serviceName, h.description, h.car.id) "
            + "FROM ServiceHistory h WHERE h.date >= :date AND h.car.id = :id")
    Page<ServiceHistoryDTO> findServiceHistoryFromDateToNow(@Param("date") LocalDate date, @Param("id") Long carId, Pageable pageable);


}