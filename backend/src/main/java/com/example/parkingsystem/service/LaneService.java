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
        if (repository.existsByType(lane.getType())) throw new RuntimeException("Lane type already exists");
        return repository.save(lane);
    }

    public Lane update(Long id, Lane updated) {
        Lane lane = repository.findById(id).orElseThrow();
        if (!lane.getType().equals("IN") && !lane.getType().equals("OUT")) {
            lane.setType(updated.getType());
        }
        lane.setDescription(updated.getDescription());
        return repository.save(lane);
    }

    public void delete(Long id) {
        Lane lane = repository.findById(id).orElseThrow();
        if (lane.getType().equals("IN") || lane.getType().equals("OUT")) {
            throw new RuntimeException("Cannot delete default lanes IN or OUT");
        }
        repository.deleteById(id);
    }
}