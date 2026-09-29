package com.parkspot.werkins.dto;
import jakarta.validation.constraints.*;
public record VisitorEntryRequest(@NotBlank @Size(max=20) String vehicleNumber, String visitorName,
                                  @NotBlank @Pattern(regexp="[A-Za-z0-9-]+") String flatNumber,
                                  @NotNull @Positive Long parkingSlotId,
                                  java.time.LocalDateTime entryTime) {}
