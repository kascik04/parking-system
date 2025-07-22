package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.Lane;
import com.example.parkingsystem.repository.LaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaneService {

    private final LaneRepository repository;

    public List<Lane> getAll() {
        return repository.findAll();
    }

    public Optional<Lane> getById(Long id) {
        return repository.findById(id);
    }

    public Lane create(Lane lane) {
        // ⚠️ Fix: Sử dụng enum thay vì toString()
        if (repository.existsByType(lane.getType())) {
            throw new RuntimeException("Lane type already exists");
        }
        return repository.save(lane);
    }

    public Lane update(Long id, Lane updated) {
        Lane lane = repository.findById(id).orElseThrow();
        // ⚠️ Fix: So sánh enum thay vì String
        if (lane.getType() != Lane.LaneType.IN && lane.getType() != Lane.LaneType.OUT) {
            lane.setType(updated.getType());
        }
        lane.setName(updated.getName()); // ⚠️ Thêm update name
        lane.setDescription(updated.getDescription());
        lane.setBlock(updated.getBlock()); // ⚠️ Thêm update block
        lane.setIsActive(updated.getIsActive()); // ⚠️ Thêm update active status
        return repository.save(lane);
    }

    public void delete(Long id) {
        Lane lane = repository.findById(id).orElseThrow();
        // ⚠️ Fix: So sánh enum thay vì String
        if (lane.getType() == Lane.LaneType.IN || lane.getType() == Lane.LaneType.OUT) {
            throw new RuntimeException("Cannot delete default lanes IN or OUT");
        }
        repository.deleteById(id);
    }

    // ⚠️ Thêm các method hữu ích
    public List<Lane> getByType(Lane.LaneType type) {
        return repository.findByType(type);
    }

    public List<Lane> getActiveOnes() {
        return repository.findByIsActive(true);
    }

    public List<Lane> getByBlockId(Long blockId) {
        return repository.findByBlockId(blockId);
    }
}