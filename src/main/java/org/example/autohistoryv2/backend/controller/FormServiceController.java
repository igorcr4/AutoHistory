package org.example.autohistoryv2.backend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.autohistoryv2.backend.alert.AlertNotifications;
import org.example.autohistoryv2.backend.exceptions.CarExceptions;
import org.example.autohistoryv2.backend.exceptions.ServiceHistoryExceptions;
import org.example.autohistoryv2.backend.models.ServiceHistory;
import org.example.autohistoryv2.backend.service.ServiceHistoryInterface;
import org.example.autohistoryv2.backend.validation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Controller
public class FormServiceController {

    @Autowired
    ServiceHistoryInterface serviceHistoryInterface;
    @Autowired
    InputValidation inputValidation;
    @Autowired
    AlertNotifications alertNotifications;
    @Autowired
    CarPageController carPageController;

    @FXML
    private TextField licensePlateField;
    @FXML
    private DatePicker serviceDatePicker;
    @FXML
    private TextField kmField;
    @FXML
    private TextField costField;
    @FXML
    private TextField serviceNameField;
    @FXML
    private TextArea descriptionField;

    // Obiect de tip ServiceHistory asupra căruia facem update
    private ServiceHistory currentHistory;

    @FXML
    public void initialize() {
        inputValidation.licensePlateValidation(licensePlateField);
        inputValidation.mileageValidation(kmField);
        inputValidation.costValidation(costField);
        setupDatePicker();
    }

    // Metodă folosită la deschiderea formularului pentru un istoric existent
    public void setServiceHistory(ServiceHistory history) {
        this.currentHistory = history;
        if (history != null && history.getCar() != null) {
            licensePlateField.setText(history.getCar().getLicensePlate());
        }
        if (history != null) {
            serviceDatePicker.setValue(history.getDate());
            kmField.setText(String.valueOf(history.getKm()));
            costField.setText(String.valueOf(history.getCost()));
            serviceNameField.setText(history.getServiceName());
            descriptionField.setText(history.getDescription());
        }
    }

    @FXML
    public void submitServiceForm(ActionEvent event) {
        try {
            LocalDate date = serviceDatePicker.getValue();
            Integer km = Integer.parseInt(kmField.getText());
            BigDecimal cost = new BigDecimal(costField.getText());
            String serviceName = serviceNameField.getText();
            String description = descriptionField.getText();

            // Dacă currentHistory != null înseamnă că vrem să facem update
            if (currentHistory != null && currentHistory.getId() != null) {
                currentHistory.setDate(date);
                currentHistory.setKm(km);
                currentHistory.setCost(cost);
                currentHistory.setServiceName(serviceName);
                currentHistory.setDescription(description);

                serviceHistoryInterface.updateInformation(currentHistory);

                carPageController.getLoadServiceHistory();
            } else {
                // Altfel, e un istoric nou
                ServiceHistory history = new ServiceHistory();
                history.setDate(date);
                history.setKm(km);
                history.setCost(cost);
                history.setServiceName(serviceName);
                history.setDescription(description);

                // Adăugăm noua intrare (folosim metoda deja existentă)
                serviceHistoryInterface.addNewServiceHistory(licensePlateField.getText(), history);
            }

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (ServiceHistoryExceptions.InformationValidityException
                 | CarExceptions.FindCarException
                 | NumberFormatException e) {
            alertNotifications.showErrorAlert(e.getMessage());
        }
    }

    private void setupDatePicker() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        serviceDatePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.trim().isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });
    }
}

