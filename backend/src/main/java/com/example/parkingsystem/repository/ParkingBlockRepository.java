package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.ParkingBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingBlockRepository extends JpaRepository<ParkingBlock, Long> {
}