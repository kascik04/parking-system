package com.example.parkingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Table(name = "barriers")
public class Barrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "lane_id")
    private Lane lane;

    private String status; // OPEN, CLOSED, MAINTENANCE
    private String controlType; // AUTO, MANUAL, REMOTE
    private String hardwareId; // ID thiết bị vật lý

    private LocalDateTime lastOpened;
    private LocalDateTime lastClosed;

    @Builder.Default
    @Column(name = "is_auto_mode")
    private Boolean isAutoMode = true; // ✅ Fix: Đổi thành Boolean và đổi tên field
}
