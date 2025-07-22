package com.example.parkingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data // ⚠️ Quan trọng: Lombok sẽ tự tạo getter/setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parking_slots")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private ParkingBlock block;

    @Column(nullable = false)
    private Integer floor;

    @Builder.Default
    @Column(name = "is_occupied")
    private Boolean isOccupied = false; // ⚠️ Boolean thay vì boolean

    @Column(name = "slot_number", nullable = false, unique = true)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SlotType slotType = SlotType.STANDARD;

    @Column(name = "x_coordinate")
    private Integer xCoordinate;

    @Column(name = "y_coordinate")
    private Integer yCoordinate;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum SlotType {
        STANDARD("Tiêu chuẩn"),
        VIP("VIP"),
        DISABLED("Người khuyết tật"),
        ELECTRIC("Xe điện");

        private final String displayName;

        SlotType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
