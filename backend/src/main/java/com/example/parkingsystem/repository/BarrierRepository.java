package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.Barrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BarrierRepository extends JpaRepository<Barrier, Long> {
    Optional<Barrier> findByLaneId(Long laneId);
    Optional<Barrier> findByHardwareId(String hardwareId);
}
