package com.example.parkingsystem.entity;

public enum SlotType {
    STANDARD("Tiêu chuẩn"),
    VIP("VIP"),
    DISABLED("Người khuyết tật"),
    ELECTRIC("Xe điện"),
    LARGE("Xe lớn");
    
    private final String displayName;
    
    SlotType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
