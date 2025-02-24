package org.example.autohistoryv2.backend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.autohistoryv2.backend.alert.ConfirmationNotification;
import org.example.autohistoryv2.backend.models.Notes;
import org.example.autohistoryv2.backend.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@Controller
public class NotesController {
        @Autowired
        NotesService notesService;
        @Autowired
        ConfirmationNotification confirmationNotification;

        @FXML
        TextArea descriptionArea;

        @FXML
        Button deleteNoteButton;

        @Setter
        private CarPageController carPageController;
        @Setter
        private Long carId;
        private Notes currentNote;

    @FXML
    public void initialize() {

        deleteNoteButton.setOnAction(e -> deleteNote());
    }

    public void setNote(Notes note) {
            this.currentNote = note;
            if (note != null) {
                descriptionArea.setText(note.getDescription());
            }
        }

    private void deleteNote() {
        if (currentNote != null && currentNote.getId() != null) {
            boolean confirmed = confirmationNotification.showConfirmationDialog(
                    "Confirmare ștergere",
                    "Ești sigur că vrei să ștergi notița?",
                    "Data notiță: " + currentNote.getDate()
            );
            if (confirmed) {
                notesService.deleteNote(currentNote.getId());
                carPageController.updateNoteList(currentNote);

            }
        }
    }

    @FXML
    public void saveNote(ActionEvent event) {
        String desc = descriptionArea.getText();

        Notes savedNote;

        if (currentNote == null || currentNote.getId() == null ||
                !notesService.existsById(currentNote.getId())) {
            currentNote = new Notes();
            currentNote.setDescription(desc);
            savedNote = notesService.addNote(currentNote, carId);
        } else {
            currentNote.setDescription(desc);
            savedNote = notesService.updateNote(currentNote);
        }

        carPageController.updateNoteList(savedNote);

        currentNote = null;

        // Închidem fereastra
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
