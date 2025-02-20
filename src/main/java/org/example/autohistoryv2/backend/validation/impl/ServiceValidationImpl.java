package org.example.autohistoryv2.backend.validation.impl;

import org.example.autohistoryv2.backend.exceptions.ServiceHistoryExceptions;
import org.example.autohistoryv2.backend.validation.ServiceValidation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ServiceValidationImpl implements ServiceValidation {

    public void validatePastDate(LocalDate date) {
        if (date == null) {
            throw new ServiceHistoryExceptions.InformationValidityException("Introduceți o dată!");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new ServiceHistoryExceptions.InformationValidityException("Data trebuie să fie in prezent sau în trecut!");
        }
    }

    public void validateKm(Integer km) {
        if(km > 1000000) {
            throw new ServiceHistoryExceptions.InformationValidityException("Valoarea introdusă la kilometraj este prea mare!");
        }
    }
}
