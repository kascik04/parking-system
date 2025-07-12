package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.ParkingSlot;
import com.example.parkingsystem.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingSlotService {

    private final ParkingSlotRepository repository;

    public List<ParkingSlot> getAll() {
        return repository.findAll();
    }

    public Optional<ParkingSlot> getById(Long id) {
        return repository.findById(id);
    }

    public ParkingSlot create(ParkingSlot slot) {
        return repository.save(slot);
    }

    public ParkingSlot update(Long id, ParkingSlot updatedSlot) {
        ParkingSlot slot = repository.findById(id).orElseThrow();
        slot.setBlock(updatedSlot.getBlock());
        slot.setFloor(updatedSlot.getFloor());
        slot.setOccupied(updatedSlot.isOccupied());
        return repository.save(slot);
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ParkingSlot> getByFloor(int floor) {
        return repository.findByFloor(floor);
    }

    public List<ParkingSlot> getByOccupied(boolean status) {
        return repository.findByIsOccupied(status);
    }
}
