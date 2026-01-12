package com.example.datacenter_backend_api.domain.dto;


import jakarta.validation.constraints.Min;

public record PlaceDeviceRequest(
        @Min(1) int startUnit
) {}
