package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.ParkingBlock;
import com.example.parkingsystem.repository.ParkingBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingBlockService {

    private final ParkingBlockRepository repository;

    public List<ParkingBlock> getAll() {
        return repository.findAll();
    }

    public Optional<ParkingBlock> getById(Long id) {
        return repository.findById(id);
    }

    public ParkingBlock create(ParkingBlock block) {
        
        return repository.save(block);
    }

    public ParkingBlock update(Long id, ParkingBlock updatedBlock) {
        ParkingBlock block = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Block ID " + id + " not found"));

        block.setName(updatedBlock.getName());
        block.setTotalSlots(updatedBlock.getTotalSlots());
        return repository.save(block);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Block ID " + id + " không tồn tại. Xoá thất bại.");
        }
        repository.deleteById(id);
    }
}
