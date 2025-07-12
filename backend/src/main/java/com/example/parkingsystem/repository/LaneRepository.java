package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LaneRepository extends JpaRepository<Lane, Long> {
    Optional<Lane> findByType(String type);
    boolean existsByType(String type);
}