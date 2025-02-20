package org.example.autohistoryv2.backend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.autohistoryv2.backend.alert.AlertNotifications;
import org.example.autohistoryv2.backend.exceptions.CarExceptions;
import org.example.autohistoryv2.backend.models.Car;
import org.example.autohistoryv2.backend.service.CarService;
import org.example.autohistoryv2.backend.validation.InputValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@Controller
public class FormCarController {
    @Autowired
    CarService carService;
    @Autowired
    HomePageController homePageController;
    @Autowired
    InputValidation inputValidation;
    @Autowired
    AlertNotifications alertNotifications;

    @FXML
    private TextField carNameField;

    @FXML
    private TextField licensePlateField;

    @FXML
    private TextField carYearField;

    @FXML
    private TextField engineCapacityField;

    @FXML
    public void initialize() {
        inputValidation.licensePlateValidation(licensePlateField);
        inputValidation.carYearValidation(carYearField);
        inputValidation.engineCapacityValidation(engineCapacityField);
    }

    @FXML
    private void submitCarForm(ActionEvent event) {
        try {
            Car car = new Car();

            car.setName(carNameField.getText());
            car.setLicensePlate(licensePlateField.getText());

            Integer year = Integer.parseInt(carYearField.getText());
            car.setManufacturingYear(year);

            Double engineCapacity = Double.parseDouble(engineCapacityField.getText());
            car.setEngineCapacity(engineCapacity);

            carService.saveCar(car);
            homePageController.getLoadCarsMethod();

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (CarExceptions.InformationValidityException | CarExceptions.FindCarException e) {
            alertNotifications.showErrorAlert(e.getMessage());
        }
    }
}
