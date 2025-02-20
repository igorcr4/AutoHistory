package org.example.autohistoryv2.backend.validation.impl;

import org.example.autohistoryv2.backend.exceptions.CarExceptions;
import org.example.autohistoryv2.backend.repository.CarRepository;
import org.example.autohistoryv2.backend.validation.CarValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CarValidationImpl implements CarValidation {
    @Autowired
    CarRepository repository;

    String licensePlateRegex = "^[A-Z]{3}\\s\\d{3}$";


    public void validateId(Long id) {
        if(id == null || id <= 0) {
            throw new CarExceptions.InformationValidityException("Id " + id + " este invalid!");
        }

    }

    public void validateFutureDate(LocalDate date) {
        if (date == null) {
            throw new CarExceptions.InformationUpdateException("Data introdusa nu poate fi nula!");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new CarExceptions.InformationUpdateException("Data " + date + " trebuie sa fie in viitor!");
        }
    }

    public void validateName(String name) {
        if(name == null || !name.matches("^[a-zA-Z\\s-]+$")) {
            throw new CarExceptions.InformationValidityException("Numele " + name + " este invalid!");
        }
    }

    public void validateYear(Integer year) {
        if(year == null || (year < 2000 || year > LocalDate.now().getYear())) {
            throw new CarExceptions.InformationValidityException("Anul " + year + " este invalid!");
        }
    }

    public void validateLicensePlate(String licensePlate) {
        if(licensePlate == null || !licensePlate.matches(licensePlateRegex)) {
            throw new CarExceptions.FindCarException("Numar de inmatriculare " + licensePlate + " invalid!");
        }
    }

    public void checkLicensePlateValidityAndExistence(String licensePlate) {
        if(licensePlate == null || repository.existsByLicensePlate(licensePlate) || !licensePlate.matches(licensePlateRegex)) {
            throw new CarExceptions.FindCarException("Numar de inmatriculare " + licensePlate + " este invalid sau deja exista!");
        }
    }

    public void validateEngineCapacity(Double cylindricalCapacity) {
        if (cylindricalCapacity ==null || cylindricalCapacity < 0.5 || cylindricalCapacity > 3.0) {
            throw new CarExceptions.InformationValidityException("Capacitatea cilindrica trebuie sa fie intre 0.5 și 3 litri.");
        }
    }
}

