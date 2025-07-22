package com.example.parkingsystem.controller;

import com.example.parkingsystem.entity.VehicleLog;
import com.example.parkingsystem.service.VehicleLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/vehicle-logs")
@RequiredArgsConstructor
public class VehicleLogController {

    private final VehicleLogService service;

    @GetMapping
    public ResponseEntity<List<VehicleLog>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<VehicleLog> create(@RequestBody VehicleLog log) {
        return ResponseEntity.ok(service.create(log));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleLog> update(@PathVariable Long id, @RequestBody VehicleLog log) {
        return ResponseEntity.ok(service.update(id, log));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
