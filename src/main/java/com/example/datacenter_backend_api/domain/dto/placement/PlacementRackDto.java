package com.example.datacenter_backend_api.domain.dto.placement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PlacementRackDto(
        @NotBlank String name,
        String description,
        @NotBlank String serialNumber,
        @Min(1) int totalUnits,
        @Min(1) int maxPowerW
) {}
