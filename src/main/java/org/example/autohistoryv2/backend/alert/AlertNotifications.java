package org.example.autohistoryv2.backend.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AlertNotifications {

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setHeight(250);
        alert.setWidth(700);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/FormCar.css")).toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        alert.showAndWait();
    }
}
