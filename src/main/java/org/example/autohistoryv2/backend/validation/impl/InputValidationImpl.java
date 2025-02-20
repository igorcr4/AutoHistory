package org.example.autohistoryv2.backend.validation.impl;

import javafx.scene.control.TextField;
import org.example.autohistoryv2.backend.validation.InputValidation;
import org.springframework.stereotype.Component;

@Component
public class InputValidationImpl implements InputValidation {

        public void licensePlateValidation(TextField licensePlateField) {
            licensePlateField.textProperty().addListener((observable, oldValue, newValue) -> {

                newValue = newValue.toUpperCase().replaceAll("[^A-Z0-9]", "");
                if (newValue.length() <= 3) {
                    newValue = newValue.replaceAll("[^A-Z]", "");
                }

                if (newValue.length() > 3) {
                    String firstPart = newValue.substring(0, 3).replaceAll("[^A-Z]", "");
                    newValue = firstPart + " " + newValue.substring(3);
                    String secondPart = newValue.substring(firstPart.length() + 1)
                            .replaceAll("\\D", "");
                    if (secondPart.length() > 3) {
                        secondPart = secondPart.substring(0, 3);
                    }
                    newValue = firstPart + " " + secondPart;
                }

                licensePlateField.setText(newValue);
            });
        }

        public void carYearValidation(TextField carYearField) {
            carYearField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    carYearField.setText(newValue.replaceAll("\\D", ""));
                }
                if (newValue.length() > 4) {
                    carYearField.setText(newValue.substring(0, 4));
                }
            });
        }

        public void engineCapacityValidation(TextField engineCapacityField) {
            engineCapacityField.textProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue.isEmpty()) {
                    return;
                }

                if (newValue.length() > 3) {
                    engineCapacityField.setText(oldValue);
                    return;
                }

                if (newValue.length() == 1) {
                    if (!newValue.matches("\\d")) {
                        engineCapacityField.setText(oldValue);
                    }
                    return;
                }

                if (newValue.length() == 2) {
                    if (!newValue.matches("\\d\\.")) {
                        engineCapacityField.setText(oldValue);
                    }
                    return;
                }

                if (!newValue.matches("\\d\\.\\d")) {
                    engineCapacityField.setText(oldValue);
                }
            });
        }

    public void mileageValidation(TextField mileageField) {
        mileageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                mileageField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
      }

    public void costValidation(TextField costField) {
        costField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                costField.setText(oldValue);
            }
        });
    }
}



