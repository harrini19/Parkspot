package com.parkspot.werkins.dto;
import jakarta.validation.constraints.NotBlank;
public record ParkingSlotRequest(@NotBlank String slotNumber, Boolean active) {}
