package com.example.parkingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cameras")
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "rtsp_url")
    private String rtspUrl;

    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "lane_id", nullable = false)
    private Lane associatedLane;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CameraStatus status = CameraStatus.INACTIVE;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_ocr_enabled")
    private Boolean isOcrEnabled = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OCREngine ocrEngine = OCREngine.TESSERACT;

    private Integer resolution;
    private Integer fps;

    @Column(name = "last_ping")
    private LocalDateTime lastPing;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum CameraStatus {
        ACTIVE("Hoạt động"),
        INACTIVE("Không hoạt động"),
        ERROR("Lỗi"),
        MAINTENANCE("Bảo trì");

        private final String displayName;

        CameraStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum OCREngine {
        TESSERACT("Tesseract"),
        EASYOCR("EasyOCR"),
        PADDLEOCR("PaddleOCR"),
        CLOUD_VISION("Google Cloud Vision");

        private final String displayName;

        OCREngine(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
