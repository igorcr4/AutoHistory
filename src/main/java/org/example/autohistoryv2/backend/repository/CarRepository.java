package org.example.autohistoryv2.backend.repository;

import org.example.autohistoryv2.backend.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByLicensePlate(String licensePlate);

    @Query("SELECT l FROM Car l WHERE l.licensePlate = :licensePlate")
    Optional<Car> findByLicensePlate(@Param("licensePlate") String licensePlate);
}
