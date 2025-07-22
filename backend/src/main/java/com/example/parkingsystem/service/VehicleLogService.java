package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.VehicleLog;
import com.example.parkingsystem.repository.VehicleLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleLogService {

    private final VehicleLogRepository repository;

    public List<VehicleLog> getAll() {
        return repository.findAll();
    }

    public Page<VehicleLog> getAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<VehicleLog> getById(Long id) {
        return repository.findById(id);
    }

    public VehicleLog create(VehicleLog log) {
        return repository.save(log);
    }

    public VehicleLog update(Long id, VehicleLog log) {
        log.setId(id);
        return repository.save(log);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Business logic methods
    public List<VehicleLog> getCurrentVehiclesInParking() {
        return repository.findByTimeOutIsNull();
    }

    public Optional<VehicleLog> findVehicleInParking(String licensePlate) {
        return repository.findByLicensePlateAndTimeOutIsNull(licensePlate);
    }

    public List<VehicleLog> getVehicleHistory(String licensePlate) {
        return repository.findByLicensePlateOrderByTimeInDesc(licensePlate);
    }

    // Statistics methods
    public Long getTotalRevenueToday() {
        return repository.getTotalFeeToday();
    }

    public Long getVehicleCountToday() {
        return repository.getVehicleCountToday();
    }
}
