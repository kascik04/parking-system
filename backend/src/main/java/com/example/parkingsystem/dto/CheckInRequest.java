package com.example.parkingsystem.dto;

import lombok.Data;

@Data
public class CheckInRequest {
    private String licensePlate;
    private String cardNumber;
    private String vehicleType; // "Xe máy", "Ô tô", "VIP"
    private String imageIn;     // Base64 string hoặc URL
    private String laneIn;      // Lane vào
}
