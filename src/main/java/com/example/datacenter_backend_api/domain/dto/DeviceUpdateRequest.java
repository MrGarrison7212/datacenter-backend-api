package com.example.datacenter_backend_api.domain.dto;

import jakarta.validation.constraints.Min;

public record DeviceUpdateRequest(
        String name,
        String description,
        String serialNumber,
        @Min(1) Integer units,
        @Min(1) Integer powerConsumptionW
) {}
