package org.example.autohistoryv2.backend.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.autohistoryv2.backend.alert.ConfirmationNotification;
import org.example.autohistoryv2.backend.models.Car;
import org.example.autohistoryv2.backend.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;


@Component
@AllArgsConstructor
@NoArgsConstructor
@Controller
public class HomePageController {
    @Autowired
    CarService carService;
    @Autowired
    ConfigurableApplicationContext applicationContext;
    @Autowired
    ConfirmationNotification confirmationNotification;

    @FXML
    private ListView<Car> carList;

    @FXML
    private Button deleteCarButton;

    @FXML
    private TextField searchField;

    private FilteredList<Car> filteredCars;

    @FXML
    public void initialize() {
        loadCars();
        setupLiveSearch();
        setupDoubleClick();
        deleteCarButton.setOnAction(e -> deleteCar());
    }

    private void deleteCar() {
        Car selectedCar = carList.getSelectionModel().getSelectedItem();
        if (selectedCar != null) {
            boolean confirmed = confirmationNotification.showConfirmationDialog(
                    "Confirmare ștergere",
                    "Ești sigur că vrei să ștergi această mașină?",
                    "Mașina: " + selectedCar.getLicensePlate()
            );
            if (confirmed) {
                carService.deleteCar(selectedCar.getId());
                getLoadCarsMethod(); // reîncărcare listă
            }
        }
    }

    private void loadCars() {
        List<Car> cars = carService.getAllCars();
        ObservableList<Car> observableCars = FXCollections.observableArrayList(cars);

        filteredCars = new FilteredList<>(observableCars, car -> true);
        carList.setItems(filteredCars);

        carList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);
                if (empty || car == null) {
                    setText(null);
                } else {
                    setText(car.getLicensePlate());
                }
            }
        });
    }

    private void setupLiveSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCars.setPredicate(car -> {

                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String filter = newValue.toLowerCase();

                return car.getLicensePlate().toLowerCase().startsWith(filter);
            });
        });
    }

    private void setupDoubleClick() {
        carList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Car selectedCar = carList.getSelectionModel().getSelectedItem();
                if (selectedCar != null) {
                    try {
                        openCarDetails(selectedCar);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void openCarDetails(Car selectedCar) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/CarPage.fxml"));
        loader.setControllerFactory(applicationContext::getBean);

        Parent root = loader.load();

        CarPageController controller = loader.getController();
        controller.setCar(selectedCar);

        Stage formStage = new Stage();
        formStage.setScene(new Scene(root));
        formStage.setMaximized(true);
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.show();
    }

    private void openNewWindow(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();

        Stage formStage = new Stage();
        formStage.setScene(new Scene(root));
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.show();
    }

    @FXML
    public void openCarForm() throws IOException {
        openNewWindow("/frontend/FormCar.fxml");
    }

    @FXML
    public void openServiceForm() throws IOException {
        openNewWindow("/frontend/FormService.fxml");
    }

    // Metodă publică ce poate fi apelată de alte controllere pentru a reîncărca lista
    public void getLoadCarsMethod() {
        loadCars();
    }

}


