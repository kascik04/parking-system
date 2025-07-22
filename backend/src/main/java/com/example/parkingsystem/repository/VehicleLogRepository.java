package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.VehicleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleLogRepository extends JpaRepository<VehicleLog, Long> {

    // ✅ Các method finder chuẩn
    List<VehicleLog> findByTimeOutIsNull();
    Optional<VehicleLog> findByLicensePlateAndTimeOutIsNull(String licensePlate);
    List<VehicleLog> findByLicensePlateOrderByTimeInDesc(String licensePlate);
    long countByVehicleTypeAndTimeOutIsNull(VehicleLog.VehicleType vehicleType);
    List<VehicleLog> findByStatus(VehicleLog.VehicleLogStatus status);

    // ✅ Sử dụng @Query cho logic phức tạp
    @Query("SELECT COALESCE(SUM(v.fee), 0) FROM VehicleLog v WHERE DATE(v.timeOut) = CURRENT_DATE")
    Long getTotalFeeToday();

    @Query("SELECT COUNT(v) FROM VehicleLog v WHERE DATE(v.timeIn) = CURRENT_DATE")
    Long getVehicleCountToday();

    // ✅ Thêm các query hữu ích khác
    @Query("SELECT COUNT(v) FROM VehicleLog v WHERE v.timeOut IS NULL")
    Long getCurrentVehicleCount();

    @Query("SELECT v FROM VehicleLog v WHERE DATE(v.timeIn) = CURRENT_DATE ORDER BY v.timeIn DESC")
    List<VehicleLog> getTodayVehicleLogs();
}
