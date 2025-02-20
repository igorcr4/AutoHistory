package org.example.autohistoryv2.backend.validation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public interface ServiceValidation {
    void validatePastDate(LocalDate date);
    void validateKm(Integer km);
}
