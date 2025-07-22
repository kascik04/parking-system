package com.example.parkingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parking_blocks")
public class ParkingBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;

    @Column(length = 500)
    private String description; // Thêm field này

    @Builder.Default
    private Integer availableSlots = 0;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        if (availableSlots == null) {
            availableSlots = totalSlots;
        }
    }
}