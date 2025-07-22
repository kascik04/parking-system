package com.example.parkingsystem.controller;

import com.example.parkingsystem.dto.ApiResponse;
import com.example.parkingsystem.entity.Camera;
import com.example.parkingsystem.service.CameraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cameras")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CameraController {

    private final CameraService cameraService;

    /**
     * Lấy tất cả camera
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Camera>>> getAllCameras() {
        try {
            List<Camera> cameras = cameraService.getAllCameras();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách camera thành công", cameras));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Lỗi: " + e.getMessage(), null));
        }
    }

    /**
     * Lấy camera theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Camera>> getCameraById(@PathVariable Long id) {
        try {
            Camera camera = cameraService.getCameraById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy camera thành công", camera));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Lỗi: " + e.getMessage(), null));
        }
    }

    /**
     * Chụp ảnh từ camera
     */
    @PostMapping("/capture/{laneId}")
    public ResponseEntity<ApiResponse<String>> captureImage(@PathVariable Long laneId) {
        try {
            String imageData = cameraService.captureImage(laneId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Chụp ảnh thành công", imageData));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Lỗi: " + e.getMessage(), null));
        }
    }

    /**
     * OCR biển số xe
     */
    @PostMapping("/ocr")
    public ResponseEntity<ApiResponse<CameraService.OCRResult>> performOCR(
            @RequestBody OCRRequest request) {
        try {
            CameraService.OCRResult result = cameraService.performOCR(
                request.getImageData(), 
                Camera.OCREngine.valueOf(request.getOcrEngine())
            );
            return ResponseEntity.ok(new ApiResponse<>(true, "OCR thành công", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Lỗi OCR: " + e.getMessage(), null));
        }
    }

    /**
     * Ping camera
     */
    @PostMapping("/ping/{cameraId}")
    public ResponseEntity<ApiResponse<Boolean>> pingCamera(@PathVariable Long cameraId) {
        try {
            boolean isOnline = cameraService.pingCamera(cameraId);
            return ResponseEntity.ok(new ApiResponse<>(true, 
                isOnline ? "Camera online" : "Camera offline", isOnline));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, "Lỗi ping: " + e.getMessage(), null));
        }
    }

    // DTO classes
    public static class OCRRequest {
        private String imageData;
        private String ocrEngine;
        private Long laneId;
        
        // Getters and setters
        public String getImageData() { return imageData; }
        public void setImageData(String imageData) { this.imageData = imageData; }
        public String getOcrEngine() { return ocrEngine; }
        public void setOcrEngine(String ocrEngine) { this.ocrEngine = ocrEngine; }
        public Long getLaneId() { return laneId; }
        public void setLaneId(Long laneId) { this.laneId = laneId; }
    }
}
