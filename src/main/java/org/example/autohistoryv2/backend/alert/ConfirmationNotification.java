package org.example.autohistoryv2.backend.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConfirmationNotification {

    public boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType yesButton = new ButtonType("DA", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("NU", ButtonBar.ButtonData.CANCEL_CLOSE);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/css/Alert.css").toExternalForm()
        );

        dialogPane.getStyleClass().add("custom-alert");

        dialogPane.setPrefWidth(400);
        dialogPane.setPrefHeight(250);

        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.setResizable(false);

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

}
