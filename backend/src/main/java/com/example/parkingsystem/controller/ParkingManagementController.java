package com.example.parkingsystem.controller;

import com.example.parkingsystem.entity.VehicleLog;
import com.example.parkingsystem.service.ParkingManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ParkingManagementController {

    private final ParkingManagementService parkingService;

    /**
     * API lấy thống kê tổng quan
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getParkingStatistics() {
        try {
            ParkingManagementService.ParkingStatistics stats = parkingService.getParkingStatistics();
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalSlots", stats.getTotalSlots());
            response.put("occupiedSlots", stats.getOccupiedSlots());
            response.put("availableSlots", stats.getAvailableSlots());
            response.put("motorbikes", stats.getMotorbikes());
            response.put("cars", stats.getCars());
            response.put("vips", stats.getVips());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Không thể lấy thống kê: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * API xử lý xe vào
     */
    @PostMapping("/checkin")
    public ResponseEntity<Map<String, Object>> checkInVehicle(
            @RequestParam String licensePlate,
            @RequestParam(required = false) String cardNumber,
            @RequestParam String vehicleType,
            @RequestParam(required = false) String imageIn,
            @RequestParam Long laneInId) {
        
        try {
            VehicleLog result = parkingService.checkInVehicle(
                licensePlate, cardNumber, vehicleType, imageIn, laneInId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Xe vào thành công");
            response.put("vehicleLog", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * API xử lý xe ra
     */
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkOutVehicle(
            @RequestParam String licensePlate,
            @RequestParam(required = false) String imageOut,
            @RequestParam Long laneOutId) {
        
        try {
            VehicleLog result = parkingService.checkOutVehicle(licensePlate, imageOut, laneOutId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Xe ra thành công");
            response.put("vehicleLog", result);
            response.put("fee", result.getFee());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Test API connection
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testConnection() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Backend đang hoạt động");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
