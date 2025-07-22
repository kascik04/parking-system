package com.example.parkingsystem.dto;

import lombok.Data;

@Data
public class CheckOutRequest {
    private String licensePlate;
    private String imageOut;    // Base64 string hoặc URL
    private String laneOut;     // Lane ra
}
