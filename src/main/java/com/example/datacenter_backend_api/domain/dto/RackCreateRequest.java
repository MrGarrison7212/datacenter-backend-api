package com.example.datacenter_backend_api.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RackCreateRequest(
        @NotBlank String name,
        String description,
        @NotBlank String serialNumber,
        @Min(1) int totalUnits,
        @Min(1) int maxPowerW
) {}