package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.ParkingBlock;
import com.example.parkingsystem.entity.ParkingSlot;
import com.example.parkingsystem.repository.ParkingBlockRepository;
import com.example.parkingsystem.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingBlockService {

    private final ParkingBlockRepository repository;
    private final ParkingSlotRepository slotRepository;

    public List<ParkingBlock> getAll() {
        return repository.findAll();
    }

    public Optional<ParkingBlock> getById(Long id) {
        return repository.findById(id);
    }

    public ParkingBlock create(ParkingBlock block) {
        ParkingBlock savedBlock = repository.save(block);

        // Tự động tạo slot tương ứng
        for (int i = 1; i <= block.getTotalSlots(); i++) {
            int floor = (i - 1) / 10 + 1; // 10 slots per floor
            int slotNum = ((i - 1) % 10) + 1;
            
            ParkingSlot slot = ParkingSlot.builder()
                .block(savedBlock)
                .floor(floor)
                .slotNumber(savedBlock.getName().charAt(6) + floor + "-" + String.format("%02d", slotNum))
                .slotType(i <= 3 ? ParkingSlot.SlotType.VIP : ParkingSlot.SlotType.STANDARD)
                .xCoordinate(slotNum * 50)
                .yCoordinate(floor * 60)
                .isOccupied(false) // ⚠️ Sử dụng Boolean
                .isActive(true)
                .build();
            
            slotRepository.save(slot);
        }

        return savedBlock;
    }

    public ParkingBlock update(Long id, ParkingBlock updatedBlock) {
        ParkingBlock block = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Block ID " + id + " not found"));

        block.setName(updatedBlock.getName());
        block.setTotalSlots(updatedBlock.getTotalSlots());
        block.setDescription(updatedBlock.getDescription()); // ⚠️ Thêm field này
        return repository.save(block);
    }

    public void delete(Long id) {
        ParkingBlock block = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Block ID " + id + " không tồn tại."));

        // Nếu block còn slot thì không cho xoá
        List<ParkingSlot> slots = slotRepository.findByBlockId(block.getId()); // ⚠️ Fix method name
        if (!slots.isEmpty()) {
            throw new IllegalStateException("Không thể xoá block khi vẫn còn slot bên trong.");
        }

        repository.delete(block);
    }
}
