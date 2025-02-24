package org.example.autohistoryv2.backend.service;

import org.example.autohistoryv2.backend.models.Notes;

import java.util.List;
import java.util.Optional;

public interface NotesService {

    List<Notes> getNotesByCarId(Long carId);

    Notes addNote(Notes notes, Long carId);

    Optional<Notes> getNoteById(Long noteId);

    Notes updateNote(Notes updatedNote);

    void deleteNote(Long noteId);

    boolean existsById(Long noteId);
}
