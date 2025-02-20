package org.example.autohistoryv2.backend.service;

import org.example.autohistoryv2.backend.dto.CarDTO;
import org.example.autohistoryv2.backend.models.Car;
import java.util.List;

public interface CarService {
    void saveCar(Car car);

    List<Car> getAllCars();

    void deleteCar(Long id);

    Car findById(Long carId);

    Car findByLicensePlate(String licensePlate);

    void updateInfo(Long carId, CarDTO updatedCar);
}
