package com.example.parkingsystem.repository;

import com.example.parkingsystem.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {

    /**
     * Tìm camera theo lane ID
     */
    Optional<Camera> findByAssociatedLaneId(Long laneId);
    
    /**
     * Tìm tất cả camera theo status
     */
    List<Camera> findByStatus(String status);
    
    /**
     * Tìm camera đang hoạt động
     */
    List<Camera> findByStatusAndIsOcrEnabled(String status, boolean isOcrEnabled);
    
    /**
     * Tìm camera theo IP address
     */
    Optional<Camera> findByIpAddress(String ipAddress);
    
    /**
     * Tìm camera theo OCR engine
     */
    List<Camera> findByOcrEngine(String ocrEngine);
    
    /**
     * Query tùy chỉnh: Tìm camera active có OCR
     */
    @Query("SELECT c FROM Camera c WHERE c.status = 'ACTIVE' AND c.isOcrEnabled = true")
    List<Camera> findActiveOCRCameras();
    
    /**
     * Query tùy chỉnh: Đếm camera theo lane type
     */
    @Query("SELECT COUNT(c) FROM Camera c JOIN c.associatedLane l WHERE l.type = :laneType")
    Long countCamerasByLaneType(@Param("laneType") String laneType);
    
    /**
     * Tìm camera theo block ID (thông qua lane)
     */
    @Query("SELECT c FROM Camera c JOIN c.associatedLane l JOIN l.block b WHERE b.id = :blockId")
    List<Camera> findCamerasByBlockId(@Param("blockId") Long blockId);
    
    /**
     * Kiểm tra camera có đang hoạt động không
     */
    boolean existsByAssociatedLaneIdAndStatus(Long laneId, String status);

    /**
     * Tìm camera offline (không ping trong 5 phút)
     */
    @Query("SELECT c FROM Camera c WHERE c.lastPing < :cutoffTime")
    List<Camera> findOfflineCameras(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Cập nhật status cho nhiều camera
     */
    @Modifying
    @Query("UPDATE Camera c SET c.status = :status WHERE c.id IN :cameraIds")
    void updateCameraStatus(@Param("cameraIds") List<Long> cameraIds, @Param("status") Camera.CameraStatus status);
}
