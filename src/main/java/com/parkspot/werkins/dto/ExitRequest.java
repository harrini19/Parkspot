package com.parkspot.werkins.dto;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
public record ExitRequest(@PastOrPresent LocalDateTime exitTime) {}
