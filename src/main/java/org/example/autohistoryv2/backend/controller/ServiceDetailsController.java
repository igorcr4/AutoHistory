package org.example.autohistoryv2.backend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.autohistoryv2.backend.models.ServiceHistory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@Component
public class ServiceDetailsController {

    @FXML
    private Label serviceLicensePlate, serviceKm, serviceCost, serviceName;

    @FXML
    private TextArea serviceDetails;

    public void populate(ServiceHistory history) {
        if (history != null) {
            if (history.getCar() != null && history.getCar().getLicensePlate() != null) {
                serviceLicensePlate.setText( history.getCar().getLicensePlate());
            }
            serviceKm.setText("Km: " + history.getKm());
            serviceCost.setText("Cost: " + history.getCost());
            serviceName.setText("Service: " + history.getServiceName());
            serviceDetails.setText(history.getDescription());
        }
    }
}