package com.example.parkingsystem.config;

import com.example.parkingsystem.entity.*;
import com.example.parkingsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ParkingBlockRepository blockRepository;
    private final ParkingSlotRepository slotRepository;
    private final LaneRepository laneRepository;

    @Override
    public void run(String... args) throws Exception {
        if (blockRepository.count() == 0) {
            System.out.println("🔄 Loading sample data...");
            loadSampleData();
        }
    }

    private void loadSampleData() {
        // Tạo blocks
        ParkingBlock blockA = createAndSaveBlock("Block A", 50, "Khu vực chính - xe máy và ô tô");
        ParkingBlock blockB = createAndSaveBlock("Block B", 30, "Khu vực phụ - chỉ xe máy");
        ParkingBlock blockC = createAndSaveBlock("Block C", 20, "Khu VIP - xe sang");

        // Tạo lanes
        createAndSaveLane("Lane Vào 1", Lane.LaneType.IN, "Lane chính cho xe vào", blockA);
        createAndSaveLane("Lane Ra 1", Lane.LaneType.OUT, "Lane chính cho xe ra", blockA);

        // Tạo slots
        createSlotsForBlock(blockA, "A", 20);
        createSlotsForBlock(blockB, "B", 15);  
        createSlotsForBlock(blockC, "C", 10);

        System.out.println("✅ Sample data loaded!");
        System.out.println("📊 Blocks: " + blockRepository.count());
        System.out.println("📊 Slots: " + slotRepository.count());
        System.out.println("📊 Lanes: " + laneRepository.count());
    }

    private ParkingBlock createAndSaveBlock(String name, int totalSlots, String description) {
        ParkingBlock block = ParkingBlock.builder()
            .name(name)
            .totalSlots(totalSlots)
            .description(description)
            .availableSlots(totalSlots)
            .isActive(true)
            .build();
        return blockRepository.save(block);
    }

    private Lane createAndSaveLane(String name, Lane.LaneType type, String description, ParkingBlock block) {
        Lane lane = Lane.builder()
            .name(name)
            .type(type)
            .description(description)
            .block(block)
            .isActive(true)
            .build();
        return laneRepository.save(lane);
    }

    private void createSlotsForBlock(ParkingBlock block, String code, int count) {
        for (int i = 1; i <= count; i++) {
            int floor = (i - 1) / 10 + 1;
            int slotNum = ((i - 1) % 10) + 1;

            ParkingSlot slot = ParkingSlot.builder()
                .block(block)
                .floor(floor)
                .slotNumber(code + floor + "-" + String.format("%02d", slotNum))
                .slotType(code.equals("C") ? ParkingSlot.SlotType.VIP : 
                         (i <= 3 ? ParkingSlot.SlotType.VIP : ParkingSlot.SlotType.STANDARD))
                .xCoordinate(slotNum * 50)
                .yCoordinate(floor * 60)
                .isOccupied(false)
                .isActive(true)
                .build();

            slotRepository.save(slot);
        }
    }
}
