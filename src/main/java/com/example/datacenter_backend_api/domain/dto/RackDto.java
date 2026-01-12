package com.example.datacenter_backend_api.domain.dto;

public record RackDto(
        Long id,
        String name,
        String description,
        String serialNumber,
        int totalUnits,
        int maxPowerW,
        int currentPowerW
) {}
