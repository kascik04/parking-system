package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    
    List<ParkingSlot> findByFloor(Integer floor);
    List<ParkingSlot> findByIsOccupied(Boolean isOccupied);
    List<ParkingSlot> findByBlockId(Long blockId);
    long countByIsOccupied(Boolean isOccupied);
    List<ParkingSlot> findBySlotType(ParkingSlot.SlotType slotType);
}
