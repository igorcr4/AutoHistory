package org.example.autohistoryv2.backend.validation;

import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public interface InputValidation {
    void licensePlateValidation(TextField licensePlateField);
    void carYearValidation(TextField carYearField);
    void engineCapacityValidation(TextField engineCapacityField);

    void mileageValidation(TextField mileageField);
    void costValidation(TextField costField);
}
