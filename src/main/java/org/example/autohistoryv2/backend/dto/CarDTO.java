package org.example.autohistoryv2.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.autohistoryv2.backend.models.ServiceHistory;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private Long id;
    private String name;
    private String licensePlate;
    private Integer manufacturingYear;
    private Double engineCapacity;

    private List<ServiceHistory> history = new ArrayList<>();
}
