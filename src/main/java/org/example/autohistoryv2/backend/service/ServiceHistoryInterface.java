package org.example.autohistoryv2.backend.service;
import org.example.autohistoryv2.backend.dto.ServiceHistoryDTO;
import org.example.autohistoryv2.backend.models.ServiceHistory;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface ServiceHistoryInterface {
    void addNewServiceHistory(String licensePlate, ServiceHistory history);

    List<ServiceHistory> getHistory(Long carId);

    void updateInformation(ServiceHistory updatedHistory);

    Page<ServiceHistoryDTO> findServiceHistorySinceDate(LocalDate date, Long carId, int page, int size);

    void deleteHistory(Long historyId);
}