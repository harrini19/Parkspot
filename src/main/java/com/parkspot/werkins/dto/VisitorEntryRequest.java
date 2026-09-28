package com.parkspot.werkins.dto;
import jakarta.validation.constraints.*;
public record VisitorEntryRequest(@NotBlank @Size(max=20) String vehicleNumber, String visitorName,
                                  @NotNull @Positive Long flatId, @NotNull @Positive Long parkingSlotId,
                                  java.time.LocalDateTime entryTime) {}
