package org.example.autohistoryv2.backend.service.impl;

import org.example.autohistoryv2.backend.dto.CarDTO;
import org.example.autohistoryv2.backend.exceptions.CarExceptions;
import org.example.autohistoryv2.backend.models.Car;
import org.example.autohistoryv2.backend.repository.CarRepository;
import org.example.autohistoryv2.backend.service.CarService;
import org.example.autohistoryv2.backend.validation.CarValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    CarRepository repository;
    @Autowired
    CarValidation validation;


    public void saveCar(Car car) {
        String name = car.getName();
        String licensePlate = car.getLicensePlate();
        Integer year = car.getManufacturingYear();
        Double engineCapacity = car.getEngineCapacity();

        validation.validateName(name);

        validation.checkLicensePlateValidityAndExistence(licensePlate);

        validation.validateYear(year);

        validation.validateEngineCapacity(engineCapacity);

        repository.save(car);
    }

    public Car findByLicensePlate(String licensePlate) {

        validation.validateLicensePlate(licensePlate);

        return repository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new CarExceptions.FindCarException("Masina cu numarul de inmatriculare " + licensePlate + " nu exista!"));
    }

    public Car findById(Long carId) {
        validation.validateId(carId);

        return repository.findById(carId)
                .orElseThrow(() -> new CarExceptions.FindCarException("Masina cu id " + carId + " nu exista!"));
    }

    public List<Car> getAllCars() {
        return repository.findAll();
    }

    @Transactional
    public void deleteCar(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void updateInfo(Long carId, CarDTO updatedInfo) {

        Car car = repository.findById(carId)
                .orElseThrow(() -> new CarExceptions.FindCarException("Masina nu exista!"));

        if(updatedInfo.getName() != null) {
            validation.validateName(updatedInfo.getName());
            car.setName(updatedInfo.getName());
        }

        if(updatedInfo.getLicensePlate() != null) {
            validation.checkLicensePlateValidityAndExistence(updatedInfo.getLicensePlate());
            car.setLicensePlate(updatedInfo.getLicensePlate());
        }

        if(updatedInfo.getManufacturingYear() != null) {
            validation.validateYear(updatedInfo.getManufacturingYear());
            car.setManufacturingYear(updatedInfo.getManufacturingYear());
        }

        if(updatedInfo.getEngineCapacity() != null) {
            validation.validateEngineCapacity(updatedInfo.getEngineCapacity());
            car.setEngineCapacity(updatedInfo.getEngineCapacity());
        }

        repository.save(car);
    }

}