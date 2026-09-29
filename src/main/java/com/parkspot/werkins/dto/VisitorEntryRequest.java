package com.parkspot.werkins.dto;
import jakarta.validation.constraints.*;
public record VisitorEntryRequest(
        @NotBlank(message = "Vehicle number is required")
        @Size(max = 20, message = "Vehicle number must not exceed 20 characters")
        @Pattern(
            regexp = "^[A-Za-z0-9][A-Za-z0-9 \\-]*$",
            message = "Enter a valid vehicle number (letters, digits, spaces, hyphens only; must start with a letter or digit)"
        )
        String vehicleNumber,
        String visitorName,
        @NotBlank @Pattern(regexp = "[A-Za-z0-9-]+") String flatNumber,
        @NotNull @Positive Long parkingSlotId,
        java.time.LocalDateTime entryTime) {}

