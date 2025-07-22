package com.example.parkingsystem.controller;

import com.example.parkingsystem.dto.ApiResponse;
import com.example.parkingsystem.entity.ParkingBlock;
import com.example.parkingsystem.service.ParkingBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class ParkingBlockController {

    private final ParkingBlockService service;

    @GetMapping
    public ResponseEntity<List<ParkingBlock>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingBlock> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParkingBlock>> create(@RequestBody ParkingBlock block) {
        ParkingBlock created = service.create(block);
        return ResponseEntity.ok(new ApiResponse<>(true, "Block created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingBlock>> update(@PathVariable Long id, @RequestBody ParkingBlock block) {
        ParkingBlock updated = service.update(id, block);
        return ResponseEntity.ok(new ApiResponse<>(true, "Block updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Block deleted successfully", null));
    }
}