package com.parkspot.werkins.dto;
import jakarta.validation.constraints.NotBlank;
public record FlatRequest(@NotBlank String flatNumber, @NotBlank String ownerName) {}
