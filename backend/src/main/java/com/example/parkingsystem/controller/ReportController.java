package com.example.parkingsystem.controller;

import com.example.parkingsystem.dto.ApiResponse;
import com.example.parkingsystem.entity.VehicleLog;
import com.example.parkingsystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<ReportService.DailyReport>> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        LocalDate reportDate = date != null ? date : LocalDate.now();
        ReportService.DailyReport report = reportService.getDailyReport(reportDate);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Báo cáo ngày " + reportDate, report));
    }

    @GetMapping("/current-vehicles")
    public ResponseEntity<ApiResponse<List<VehicleLog>>> getCurrentVehicles() {
        List<VehicleLog> vehicles = reportService.getCurrentVehiclesInParking();
        return ResponseEntity.ok(new ApiResponse<>(true, "Danh sách xe trong bãi", vehicles));
    }

    @GetMapping("/vehicle-history/{licensePlate}")
    public ResponseEntity<ApiResponse<List<VehicleLog>>> getVehicleHistory(@PathVariable String licensePlate) {
        List<VehicleLog> history = reportService.getVehicleHistory(licensePlate);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lịch sử xe " + licensePlate, history));
    }
}
