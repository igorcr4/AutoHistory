package org.example.autohistoryv2.backend.repository;

import org.example.autohistoryv2.backend.models.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByCarId(Long carId);
}
