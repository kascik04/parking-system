package com.example.parkingsystem.controller;

import com.example.parkingsystem.dto.ApiResponse;
import com.example.parkingsystem.entity.ParkingSlot;
import com.example.parkingsystem.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService service;

    @GetMapping
    public ResponseEntity<List<ParkingSlot>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSlot> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParkingSlot>> create(@RequestBody ParkingSlot slot) {
        ParkingSlot created = service.create(slot);
        return ResponseEntity.ok(new ApiResponse<>(true, "Slot created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingSlot>> update(@PathVariable Long id, @RequestBody ParkingSlot slot) {
        ParkingSlot updated = service.update(id, slot);
        return ResponseEntity.ok(new ApiResponse<>(true, "Slot updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Slot deleted successfully", null));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(false, "Không có dữ liệu, không thành công", null));
        }
    }

    // 🔍 Get all slots on a specific floor
    @GetMapping("/floor/{floor}")
    public ResponseEntity<ApiResponse<List<ParkingSlot>>> getByFloor(@PathVariable int floor) {
        List<ParkingSlot> slots = service.getByFloor(floor);
        return ResponseEntity.ok(new ApiResponse<>(true, "Slots on floor " + floor, slots));
    }

    // 🔍 Get occupied or available slots
    @GetMapping("/occupied")
    public ResponseEntity<ApiResponse<List<ParkingSlot>>> getByOccupied(@RequestParam boolean status) {
        List<ParkingSlot> slots = service.getByOccupied(status);
        String message = status ? "Occupied slots" : "Available slots";
        return ResponseEntity.ok(new ApiResponse<>(true, message, slots));
    }
}
