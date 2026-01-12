package com.example.datacenter_backend_api.domain.dto;


import jakarta.validation.constraints.Min;

public record RackUpdateRequest(
        String name,
        String description,
        String serialNumber,
        @Min(1) Integer totalUnits,
        @Min(1) Integer maxPowerW
) {}