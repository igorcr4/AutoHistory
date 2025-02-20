package org.example.autohistoryv2.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "notes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notes{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @JsonBackReference
    private Car car;

    private LocalDate date;
    private String description;
}
