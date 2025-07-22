package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.VehicleLog;
import com.example.parkingsystem.repository.VehicleLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final VehicleLogRepository vehicleLogRepository;

    /**
     * Báo cáo doanh thu theo ngày
     */
    public DailyReport getDailyReport(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<VehicleLog> logs = vehicleLogRepository.findAll()
                .stream()
                .filter(log -> log.getTimeOut() != null &&
                              log.getTimeOut().isAfter(startOfDay) &&
                              log.getTimeOut().isBefore(endOfDay))
                .toList();

        long totalRevenue = logs.stream().mapToLong(VehicleLog::getFee).sum();
        long totalVehicles = logs.size();
        
        long motorcycles = logs.stream().filter(log -> "Xe máy".equals(log.getVehicleType())).count();
        long cars = logs.stream().filter(log -> "Ô tô".equals(log.getVehicleType())).count();
        long vips = logs.stream().filter(log -> "VIP".equals(log.getVehicleType())).count();

        return new DailyReport(date, totalRevenue, totalVehicles, motorcycles, cars, vips);
    }

    /**
     * Lấy danh sách xe hiện tại trong bãi
     */
    public List<VehicleLog> getCurrentVehiclesInParking() {
        return vehicleLogRepository.findByTimeOutIsNull();
    }

    /**
     * Lịch sử xe theo biển số
     */
    public List<VehicleLog> getVehicleHistory(String licensePlate) {
        return vehicleLogRepository.findByLicensePlateOrderByTimeInDesc(licensePlate);
    }

    // DTO for daily report
    public static class DailyReport {
        public final LocalDate date;
        public final long totalRevenue;
        public final long totalVehicles;
        public final long motorcycles;
        public final long cars;
        public final long vips;

        public DailyReport(LocalDate date, long totalRevenue, long totalVehicles,
                          long motorcycles, long cars, long vips) {
            this.date = date;
            this.totalRevenue = totalRevenue;
            this.totalVehicles = totalVehicles;
            this.motorcycles = motorcycles;
            this.cars = cars;
            this.vips = vips;
        }
    }
}
