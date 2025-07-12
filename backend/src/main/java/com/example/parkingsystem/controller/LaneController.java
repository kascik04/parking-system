package com.example.parkingsystem.controller;

import com.example.parkingsystem.dto.ApiResponse;
import com.example.parkingsystem.entity.Lane;
import com.example.parkingsystem.service.LaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lanes")
@RequiredArgsConstructor
public class LaneController {

    private final LaneService service;

    @GetMapping
    public ResponseEntity<List<Lane>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lane> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Lane>> create(@RequestBody Lane lane) {
        Lane created = service.create(lane);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lane created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Lane>> update(@PathVariable Long id, @RequestBody Lane lane) {
        Lane updated = service.update(id, lane);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lane updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lane deleted successfully", null));
    }
}