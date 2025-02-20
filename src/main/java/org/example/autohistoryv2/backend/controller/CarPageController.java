package org.example.autohistoryv2.backend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.autohistoryv2.backend.alert.AlertNotifications;
import org.example.autohistoryv2.backend.alert.ConfirmationNotification;
import org.example.autohistoryv2.backend.dto.CarDTO;
import org.example.autohistoryv2.backend.exceptions.CarExceptions;
import org.example.autohistoryv2.backend.models.Car;
import org.example.autohistoryv2.backend.models.Notes;
import org.example.autohistoryv2.backend.models.ServiceHistory;
import org.example.autohistoryv2.backend.service.CarService;
import org.example.autohistoryv2.backend.service.NotesService;
import org.example.autohistoryv2.backend.service.ServiceHistoryInterface;
import org.example.autohistoryv2.backend.validation.InputValidation;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Controller
public class CarPageController {

    @Autowired
    ServiceHistoryInterface serviceHistoryInterface;
    @Autowired
    HomePageController homePageController;
    @Autowired
    InputValidation inputValidation;
    @Autowired
    CarService carService;
    @Autowired
    AlertNotifications alertNotifications;
    @Autowired
    NotesService notesService;
    @Autowired
    ConfigurableApplicationContext applicationContext;
    @Autowired
    ConfirmationNotification confirmationNotification;

    @FXML
    private Label lbCarName, lbLicensePlate, lbYear, lbEngineCapacity;

    @FXML
    private TextField tfCarName, tfLicensePlate, tfYear, tfEngineCapacity;

    @FXML
    private ListView<ServiceHistory> serviceHistoryList;

    @FXML
    private ListView<Notes> notesTextArea;

    @FXML
    private Button btnToggle;

    @FXML
    private Button saveNote;

    @FXML
    private Button deleteHistoryButton;


    @FXML
    private Pane serviceDetailsPane;

    private ServiceDetailsController serviceDetailsController;
    private boolean isEditing = false;
    private Car car;

    private final ObservableList<Notes> noteObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        showServiceDetails();

        inputValidation.licensePlateValidation(tfLicensePlate);
        inputValidation.carYearValidation(tfYear);
        inputValidation.engineCapacityValidation(tfEngineCapacity);

        tfCarName.setVisible(false);
        tfLicensePlate.setVisible(false);
        tfYear.setVisible(false);
        tfEngineCapacity.setVisible(false);

        btnToggle.setText("Editează");

        deleteHistoryButton.setOnAction(e -> deleteServiceHistory());
    }

    public void setCar(Car car) {
        this.car = car;
        lbCarName.setText(car.getName());
        lbLicensePlate.setText(car.getLicensePlate());
        lbYear.setText(String.valueOf(car.getManufacturingYear()));
        lbEngineCapacity.setText(String.valueOf(car.getEngineCapacity()));

        loadServiceHistory(car.getId());
        loadNotes(car.getId());
        setNoteText();
    }

    public void deleteServiceHistory() {
        ServiceHistory selectedHistory = serviceHistoryList.getSelectionModel().getSelectedItem();
        if (selectedHistory != null) {
            boolean confirmed = confirmationNotification.showConfirmationDialog(
                    "Confirmare ștergere",
                    "Ești sigur că vrei să ștergi acest istoric de service?",
                    "Data service: " + selectedHistory.getDate()
            );
            if (confirmed) {
                serviceHistoryInterface.deleteHistory(selectedHistory.getId());
                getLoadServiceHistory();
            }
        }
    }

    @FXML
    private void onToggleClicked() {
        if (!isEditing) {
            isEditing = true;
            tfCarName.setVisible(true);
            tfLicensePlate.setVisible(true);
            tfYear.setVisible(true);
            tfEngineCapacity.setVisible(true);
            btnToggle.setText("Confirmă");
        } else {
            try {
                CarDTO updatedInfo = getCarDTO();
                carService.updateInfo(car.getId(), updatedInfo);

                if (updatedInfo.getName() != null) {
                    lbCarName.setText(updatedInfo.getName());
                }
                if (updatedInfo.getLicensePlate() != null) {
                    lbLicensePlate.setText(updatedInfo.getLicensePlate());
                }
                if (updatedInfo.getManufacturingYear() != null) {
                    lbYear.setText(String.valueOf(updatedInfo.getManufacturingYear()));
                }
                if (updatedInfo.getEngineCapacity() != null) {
                    lbEngineCapacity.setText(String.valueOf(updatedInfo.getEngineCapacity()));
                }

                tfCarName.setVisible(false);
                tfLicensePlate.setVisible(false);
                tfYear.setVisible(false);
                tfEngineCapacity.setVisible(false);
                btnToggle.setText("Editează");
                isEditing = false;
            } catch (CarExceptions.FindCarException | CarExceptions.InformationValidityException e) {
                alertNotifications.showErrorAlert(e.getMessage());
            }
            homePageController.getLoadCarsMethod();
        }
    }

    private CarDTO getCarDTO() {
        CarDTO updatedInfo = new CarDTO();
        if (tfCarName.getText() != null && !tfCarName.getText().trim().isEmpty()) {
            updatedInfo.setName(tfCarName.getText());
        }
        if (tfLicensePlate.getText() != null && !tfLicensePlate.getText().trim().isEmpty()) {
            updatedInfo.setLicensePlate(tfLicensePlate.getText());
        }
        if (tfYear.getText() != null && !tfYear.getText().trim().isEmpty()) {
            updatedInfo.setManufacturingYear(Integer.parseInt(tfYear.getText()));
        }
        if (tfEngineCapacity.getText() != null && !tfEngineCapacity.getText().trim().isEmpty()) {
            updatedInfo.setEngineCapacity(Double.parseDouble(tfEngineCapacity.getText()));
        }
        return updatedInfo;
    }

    private void loadServiceHistory(Long carId) {
        List<ServiceHistory> serviceHistories = serviceHistoryInterface.getHistory(carId);
        ObservableList<ServiceHistory> observableService = FXCollections.observableArrayList(serviceHistories);
        FilteredList<ServiceHistory> filteredHistory = new FilteredList<>(observableService, history -> true);
        serviceHistoryList.setItems(filteredHistory);

        serviceHistoryList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ServiceHistory history, boolean empty) {
                super.updateItem(history, empty);
                if (empty || history == null) {
                    setText(null);
                    setOnMouseEntered(null);
                    setOnMouseExited(null);
                    setOnMouseClicked(null);
                    if (serviceDetailsPane.isVisible()) {
                        serviceDetailsPane.setVisible(false);
                    }
                } else {
                    setText(String.valueOf(history.getDate()));
                    setOnMouseEntered(event -> {
                        serviceDetailsController.populate(history);
                        serviceDetailsPane.setVisible(true);
                    });
                    setOnMouseExited(event -> {
                        serviceDetailsPane.setVisible(false);
                    });
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            openServiceEditForm(history);
                        }
                    });
                }
            }
        });
    }

    private void openServiceEditForm(ServiceHistory history) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/FormService.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            FormServiceController formController = loader.getController();
            formController.setServiceHistory(history);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNotes(Long carId) {
        List<Notes> notes = notesService.getNotesByCarId(carId);
        noteObservableList.setAll(notes);
        notesTextArea.setItems(noteObservableList);
        notesTextArea.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Notes obsNotes, boolean empty) {
                super.updateItem(obsNotes, empty);
                if (empty || obsNotes == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(obsNotes.getDate()));
                }
            }
        });
    }

    private void showServiceDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/ServiceDetails.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent detailsRoot = loader.load();
            serviceDetailsController = loader.getController();
            serviceDetailsPane.getChildren().add(detailsRoot);
            serviceDetailsPane.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getLoadServiceHistory() {
        if (car != null) {
            loadServiceHistory(car.getId());
        }
    }

    private void openAddNoteForm(Notes noteToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/NoteForm.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            NotesController formController = loader.getController();
            formController.setCarPageController(this);
            formController.setCarId(car.getId());
            if (noteToEdit != null) {
                formController.setNote(noteToEdit);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateNoteList(Notes savedNote) {
        if (savedNote.getId() != null) {
            Optional<Notes> existing = noteObservableList.stream()
                    .filter(n -> n.getId().equals(savedNote.getId()))
                    .findFirst();
            existing.ifPresent(noteObservableList::remove);
            noteObservableList.add(savedNote);
            notesTextArea.refresh();
        }

        List<Notes> notes = notesService.getNotesByCarId(car.getId());
        noteObservableList.setAll(notes);
        notesTextArea.refresh();
    }

    public void setNoteText() {
        notesTextArea.setItems(noteObservableList);
        notesTextArea.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Notes note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setText(null);
                } else {
                    String dateStr = (note.getDate() != null) ? note.getDate().toString() : "Fără dată";
                    String shortDesc = (note.getDescription() != null)
                            ? note.getDescription().substring(0, Math.min(10, note.getDescription().length())) + "..."
                            : "";
                    setText(dateStr + " - " + shortDesc);
                }
            }
        });

        notesTextArea.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Notes selectedNote = notesTextArea.getSelectionModel().getSelectedItem();
                if (selectedNote != null) {
                    openAddNoteForm(selectedNote);
                }
            }
        });

        saveNote.setOnAction(e -> openAddNoteForm(null));
    }
}






