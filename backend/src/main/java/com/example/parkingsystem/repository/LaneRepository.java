package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LaneRepository extends JpaRepository<Lane, Long> {
    
    List<Lane> findByType(Lane.LaneType type);
    List<Lane> findByIsActive(Boolean isActive);
    List<Lane> findByBlockId(Long blockId);
    
    // ⚠️ Fix: Sử dụng enum thay vì String
    boolean existsByType(Lane.LaneType type);
    
    // ⚠️ Thêm các method hữu ích
    List<Lane> findByNameContainingIgnoreCase(String name);
    long countByType(Lane.LaneType type);
}