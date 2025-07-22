package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.Lane;
import com.example.parkingsystem.entity.ParkingSlot;
import com.example.parkingsystem.entity.VehicleLog;
import com.example.parkingsystem.repository.LaneRepository;
import com.example.parkingsystem.repository.ParkingSlotRepository;
import com.example.parkingsystem.repository.VehicleLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor; // ✅ Thêm import này
import lombok.Getter; // ✅ Thêm import này
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingManagementService {

    private final VehicleLogRepository vehicleLogRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final LaneRepository laneRepository; // ⚠️ Thêm dependency

    /**
     * Xử lý xe vào bãi
     */
    @Transactional
    public VehicleLog checkInVehicle(String licensePlate, String cardNumber, String vehicleType,
                                     String imageIn, Long laneInId) { // ⚠️ Đổi thành Long

        // Kiểm tra xe đã có trong bãi chưa
        Optional<VehicleLog> existingVehicle = vehicleLogRepository
                .findByLicensePlateAndTimeOutIsNull(licensePlate);

        if (existingVehicle.isPresent()) {
            throw new RuntimeException("Xe đã có trong bãi!");
        }

        // Tìm slot trống phù hợp
        Optional<ParkingSlot> availableSlot = findAvailableSlotByType(vehicleType);

        if (availableSlot.isEmpty()) {
            throw new RuntimeException("Không có chỗ trống cho " + vehicleType);
        }

        ParkingSlot slot = availableSlot.get();
        slot.setIsOccupied(true); // ⚠️ Fix method name
        parkingSlotRepository.save(slot);

        // Tìm Lane theo ID
        Lane laneIn = laneRepository.findById(laneInId)
            .orElseThrow(() -> new RuntimeException("Lane không tồn tại"));

        // Lưu xe với slot được gán
        VehicleLog log = VehicleLog.builder()
                .licensePlate(licensePlate)
                .cardNumber(cardNumber)
                .vehicleType(VehicleLog.VehicleType.valueOf(vehicleType.toUpperCase()))
                .assignedSlot(slot)
                .timeIn(LocalDateTime.now())
                .imageIn(imageIn)
                .laneIn(laneIn)
                .status(VehicleLog.VehicleLogStatus.ACTIVE) // ⚠️ Thêm status
                .build();

        return vehicleLogRepository.save(log);
    }

    /**
     * Xử lý xe ra bãi
     */
    @Transactional
    public VehicleLog checkOutVehicle(String licensePlate, String imageOut, Long laneOutId) {
        // Tìm log xe vào chưa có thời gian ra
        Optional<VehicleLog> vehicleLogOpt = vehicleLogRepository
                .findByLicensePlateAndTimeOutIsNull(licensePlate);

        if (vehicleLogOpt.isEmpty()) {
            throw new RuntimeException("Không tìm thấy xe với biển số: " + licensePlate);
        }

        VehicleLog log = vehicleLogOpt.get();
        log.setTimeOut(LocalDateTime.now());
        log.setImageOut(imageOut);
        
        // Tìm Lane ra theo ID
        Lane laneOut = laneRepository.findById(laneOutId)
            .orElseThrow(() -> new RuntimeException("Lane ra không tồn tại"));
        log.setLaneOut(laneOut);

        // Tính phí đỗ xe
        long fee = calculateParkingFee(log.getTimeIn(), log.getTimeOut(), log.getVehicleType().toString());
        log.setFee(fee);
        log.setStatus(VehicleLog.VehicleLogStatus.COMPLETED);

        // Giải phóng slot
        releaseSlotForVehicle(log);

        return vehicleLogRepository.save(log);
    }

    /**
     * Tìm slot trống theo loại xe
     */
    private Optional<ParkingSlot> findAvailableSlotByType(String vehicleType) {
        List<ParkingSlot> availableSlots = parkingSlotRepository.findByIsOccupied(false);
        
        // Lọc theo loại xe nếu cần
        if ("VIP".equalsIgnoreCase(vehicleType)) {
            return availableSlots.stream()
                .filter(slot -> slot.getSlotType() == ParkingSlot.SlotType.VIP)
                .findFirst();
        }
        
        return availableSlots.stream().findFirst();
    }

    /**
     * Giải phóng slot khi xe ra
     */
    private void releaseSlotForVehicle(VehicleLog vehicleLog) {
        if (vehicleLog.getAssignedSlot() != null) {
            ParkingSlot slot = vehicleLog.getAssignedSlot();
            slot.setIsOccupied(false); // ⚠️ Fix method name
            parkingSlotRepository.save(slot);
        }
    }

    /**
     * Tính phí đỗ xe
     */
    private long calculateParkingFee(LocalDateTime timeIn, LocalDateTime timeOut, String vehicleType) {
        long hours = ChronoUnit.HOURS.between(timeIn, timeOut);
        if (hours == 0) hours = 1; // Tối thiểu 1 giờ

        switch (vehicleType.toLowerCase()) {
            case "motorbike":
                return hours * 5000; // 5k/giờ
            case "car":
                return hours * 15000; // 15k/giờ
            case "vip":
                return hours * 30000; // 30k/giờ
            default:
                return hours * 10000; // Mặc định 10k/giờ
        }
    }

    /**
     * Thống kê tổng quan
     */
    public ParkingStatistics getParkingStatistics() {
        long totalSlots = parkingSlotRepository.count();
        long occupiedSlots = parkingSlotRepository.countByIsOccupied(true);
        long availableSlots = totalSlots - occupiedSlots;

        // Thống kê theo loại xe
        long motorbikes = vehicleLogRepository.countByVehicleTypeAndTimeOutIsNull(VehicleLog.VehicleType.MOTORBIKE);
        long cars = vehicleLogRepository.countByVehicleTypeAndTimeOutIsNull(VehicleLog.VehicleType.CAR);
        long vips = vehicleLogRepository.countByVehicleTypeAndTimeOutIsNull(VehicleLog.VehicleType.VIP);

        return new ParkingStatistics(totalSlots, occupiedSlots, availableSlots,
                motorbikes, cars, vips);
    }

    // ✅ Fix: DTO class riêng biệt
    @Getter // ✅ Chỉ dùng @Getter thay vì @Data
    @AllArgsConstructor
    public static class ParkingStatistics {
        private final long totalSlots;
        private final long occupiedSlots;
        private final long availableSlots;
        private final long motorbikes;
        private final long cars;
        private final long vips;
    }
}
