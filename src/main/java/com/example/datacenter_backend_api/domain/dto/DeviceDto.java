package com.example.datacenter_backend_api.domain.dto;

public record DeviceDto(
        Long id,
        String name,
        String description,
        String serialNumber,
        int units,
        int powerConsumptionW
) {}