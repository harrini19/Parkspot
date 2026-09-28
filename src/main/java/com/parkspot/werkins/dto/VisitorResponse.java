package com.parkspot.werkins.dto;
import java.time.LocalDateTime;
public record VisitorResponse(Long id,String vehicleNumber,String visitorName,Long flatId,String flatNumber,
                              Long parkingSlotId,String slotNumber,LocalDateTime entryTime,LocalDateTime exitTime) {}
