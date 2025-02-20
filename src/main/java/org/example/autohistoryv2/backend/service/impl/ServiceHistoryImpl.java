package org.example.autohistoryv2.backend.service.impl;

import org.example.autohistoryv2.backend.dto.ServiceHistoryDTO;
import org.example.autohistoryv2.backend.exceptions.CarExceptions;
import org.example.autohistoryv2.backend.models.Car;
import org.example.autohistoryv2.backend.models.ServiceHistory;
import org.example.autohistoryv2.backend.repository.CarRepository;
import org.example.autohistoryv2.backend.repository.ServiceHistoryRepository;
import org.example.autohistoryv2.backend.service.ServiceHistoryInterface;
import org.example.autohistoryv2.backend.validation.CarValidation;
import org.example.autohistoryv2.backend.validation.ServiceValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class ServiceHistoryImpl implements ServiceHistoryInterface {
    @Autowired
    ServiceHistoryRepository historyRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    CarValidation carValidation;
    @Autowired
    ServiceValidation serviceValidation;

    @Transactional
    public void addNewServiceHistory(String licensePlate, ServiceHistory history) {
        carValidation.validateLicensePlate(licensePlate);

        Car car = carRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new CarExceptions.FindCarException("Masina cu numărul " + licensePlate + " nu există!"));

        serviceValidation.validatePastDate(history.getDate());

        serviceValidation.validateKm(history.getKm());

        history.setCar(car);
        car.getHistory().add(history);

        carRepository.save(car);
    }

    public List<ServiceHistory> getHistory(Long carId) {
        carValidation.validateId(carId);

        return historyRepository.findByCarId(carId);
    }

    @Transactional
    public void updateInformation(ServiceHistory updatedHistory) {

        if (updatedHistory.getDate() != null) {
            serviceValidation.validatePastDate(updatedHistory.getDate());
        }
        if (updatedHistory.getKm() != null) {
            serviceValidation.validateKm(updatedHistory.getKm());
        }

        ServiceHistory history = historyRepository.findById(updatedHistory.getId())
                .orElseThrow(() -> new CarExceptions.FindCarException("Istoric cu id " + updatedHistory.getId() + " nu exista!"));

        if (updatedHistory.getDate() != null) {
            history.setDate(updatedHistory.getDate());
        }
        if (updatedHistory.getKm() != null) {
            history.setKm(updatedHistory.getKm());
        }
        if (updatedHistory.getCost() != null) {
            history.setCost(updatedHistory.getCost());
        }
        if (updatedHistory.getServiceName() != null) {
            history.setServiceName(updatedHistory.getServiceName());
        }
        if (updatedHistory.getDescription() != null) {
            history.setDescription(updatedHistory.getDescription());
        }

        historyRepository.save(history);
    }


    public Page<ServiceHistoryDTO> findServiceHistorySinceDate(LocalDate date, Long carId, int page, int size) {
        serviceValidation.validatePastDate(date);
        carValidation.validateId(carId);

        Pageable pageable = PageRequest.of(page, size);
        return historyRepository.findServiceHistoryFromDateToNow(date, carId, pageable);
    }

    @Transactional
    public void deleteHistory(Long historyId) {
        historyRepository.deleteById(historyId);
    }


}
