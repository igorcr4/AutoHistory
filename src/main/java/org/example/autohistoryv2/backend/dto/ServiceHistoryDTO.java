package org.example.autohistoryv2.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceHistoryDTO {
    private Long id;
    private LocalDate date;
    private Integer km;
    private BigDecimal cost;
    private String serviceName;
    private String description;
    private Long carId;
}
