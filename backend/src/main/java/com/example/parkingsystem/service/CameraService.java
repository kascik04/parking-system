package com.example.parkingsystem.service;

import com.example.parkingsystem.entity.Camera;
import com.example.parkingsystem.repository.CameraRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CameraService {
    
    private final CameraRepository cameraRepository;
    
    /**
     * Chụp ảnh từ camera tại lane
     */
    public String captureImage(Long laneId) {
        Camera camera = cameraRepository.findByAssociatedLaneId(laneId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy camera tại lane " + laneId));
            
        if (camera.getStatus() != Camera.CameraStatus.ACTIVE) {
            throw new RuntimeException("Camera không hoạt động: " + camera.getStatus().getDisplayName());
        }
        
        // TODO: Integrate với camera hardware
        // return captureFromRTSP(camera.getRtspUrl());
        
        return "mock_image_base64"; // Mock data
    }
    
    /**
     * OCR nhận diện biển số
     */
    public OCRResult performOCR(String imageData, Camera.OCREngine ocrEngine) {
        // TODO: Integrate với OCR engine
        switch (ocrEngine) {
            case TESSERACT:
                return performTesseractOCR(imageData);
            case EASYOCR:
                return performEasyOCR(imageData);
            case PADDLEOCR:
                return performPaddleOCR(imageData);
            case CLOUD_VISION:
                return performCloudVisionOCR(imageData);
            default:
                throw new RuntimeException("OCR Engine không được hỗ trợ: " + ocrEngine);
        }
    }
    
    /**
     * Ping camera để kiểm tra kết nối
     */
    public boolean pingCamera(Long cameraId) {
        Camera camera = cameraRepository.findById(cameraId)
            .orElseThrow(() -> new RuntimeException("Camera không tồn tại"));
            
        // TODO: Implement ping logic
        boolean isOnline = true; // Mock
        
        if (isOnline) {
            camera.setLastPing(java.time.LocalDateTime.now());
            cameraRepository.save(camera);
        }
        
        return isOnline;
    }
    
    public List<Camera> getAllCameras() {
        return cameraRepository.findAll();
    }

    public Camera getCameraById(Long id) {
        return cameraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy camera với id: " + id));
    }
    
    // Private methods for different OCR engines
    private OCRResult performTesseractOCR(String imageData) {
        // TODO: Tesseract implementation
        return createMockOCRResult("30A-12345", 0.95f);
    }
    
    private OCRResult performEasyOCR(String imageData) {
        // TODO: EasyOCR implementation  
        return createMockOCRResult("30A-12345", 0.92f);
    }
    
    private OCRResult performPaddleOCR(String imageData) {
        // TODO: PaddleOCR implementation
        return createMockOCRResult("30A-12345", 0.88f);
    }
    
    private OCRResult performCloudVisionOCR(String imageData) {
        // TODO: Google Cloud Vision implementation
        return createMockOCRResult("30A-12345", 0.98f);
    }
    
    private OCRResult createMockOCRResult(String licensePlate, float confidence) {
        return OCRResult.builder()
            .licensePlate(licensePlate)
            .confidence(confidence)
            .boundingBox("100,50,300,120")
            .rawText(licensePlate + " detected")
            .build();
    }
    
    @Data
    @Builder
    public static class OCRResult {
        private String licensePlate;
        private Float confidence;
        private String boundingBox; // x1,y1,x2,y2
        private String rawText;
        private Camera.OCREngine usedEngine;
        private java.time.LocalDateTime processedAt;
    }
}
