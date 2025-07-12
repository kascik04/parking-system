package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByFloor(int floor);
    List<ParkingSlot> findByIsOccupied(boolean isOccupied);
    
   
    List<ParkingSlot> findByBlockId(Long blockId);
    List<ParkingSlot> findByBlockIdAndIsOccupied(Long blockId, boolean isOccupied);
    List<ParkingSlot> findByBlockName(String blockName);
}
