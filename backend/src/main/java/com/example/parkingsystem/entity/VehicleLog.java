package com.example.parkingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehicle_logs")
public class VehicleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "card_number")
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "assigned_slot_id")
    private ParkingSlot assignedSlot;

    @Column(name = "time_in")
    private LocalDateTime timeIn;

    @Column(name = "time_out")
    private LocalDateTime timeOut;

    @Column(columnDefinition = "TEXT")
    private String imageIn;

    @Column(columnDefinition = "TEXT") 
    private String imageOut;

    @ManyToOne
    @JoinColumn(name = "lane_in_id")
    private Lane laneIn;

    @ManyToOne
    @JoinColumn(name = "lane_out_id")
    private Lane laneOut;

    private Long fee;

    @Column(name = "ocr_confidence_in")
    private Float ocrConfidenceIn;

    @Column(name = "ocr_confidence_out")
    private Float ocrConfidenceOut;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VehicleLogStatus status = VehicleLogStatus.ACTIVE;

    public enum VehicleType {
        MOTORBIKE("Xe máy"),
        CAR("Ô tô"),
        VIP("VIP"),
        ELECTRIC("Xe điện");

        private final String displayName;

        VehicleType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum VehicleLogStatus {
        ACTIVE("Đang trong bãi"),
        COMPLETED("Đã ra"),
        CANCELLED("Hủy"),
        EXPIRED("Hết hạn");

        private final String displayName;

        VehicleLogStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
