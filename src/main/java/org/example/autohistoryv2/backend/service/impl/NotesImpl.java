package org.example.autohistoryv2.backend.service.impl;

import org.example.autohistoryv2.backend.exceptions.CarExceptions;
import org.example.autohistoryv2.backend.models.Car;
import org.example.autohistoryv2.backend.models.Notes;
import org.example.autohistoryv2.backend.repository.CarRepository;
import org.example.autohistoryv2.backend.repository.NotesRepository;
import org.example.autohistoryv2.backend.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class NotesImpl implements NotesService {
    @Autowired
    NotesRepository notesRepository;
    @Autowired
    CarRepository carRepository;

    @Transactional
    public Notes addNote(Notes notes, Long carId) {
        Car car = carRepository.findById(carId).orElseThrow();

        notes.setDate(LocalDate.now());

        notes.setCar(car);
        car.getNotes().add(notes);

        notesRepository.save(notes);
        carRepository.save(car);
        return notes;
    }

    public List<Notes> getNotesByCarId(Long carId) {
        return notesRepository.findByCarId(carId);
    }

    public Optional<Notes> getNoteById(Long noteId) {
        return notesRepository.findById(noteId);
    }

    @Transactional
    public Notes updateNote(Notes updatedNote) {

        Notes existing = notesRepository.findById(updatedNote.getId())
                .orElseThrow(() -> new RuntimeException("Nu exista notita cu id " + updatedNote.getId()));


        if(updatedNote.getDate()!= null) {
            existing.setDate(updatedNote.getDate());
        }
        if(updatedNote.getDescription()!= null){
            existing.setDescription(updatedNote.getDescription());
        }

        return notesRepository.save(existing);
    }

    @Transactional
    public void deleteNote(Long noteId) {
        notesRepository.deleteById(noteId);
    }

    public boolean existsById(Long noteId) {
        return notesRepository.existsById(noteId);
    }

}


