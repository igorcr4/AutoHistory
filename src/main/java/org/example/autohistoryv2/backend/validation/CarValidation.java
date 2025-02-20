package org.example.autohistoryv2.backend.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public interface CarValidation {
    void validateId(Long id);

    void validateFutureDate(LocalDate date);

    void validateName(String name);

    void validateYear(Integer year);

    void validateLicensePlate(String licensePlate);

    void checkLicensePlateValidityAndExistence(String licensePlate);

    void validateEngineCapacity(Double cylindricalCapacity);
}