package com.example.datacenter_backend_api.domain.dto.placement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BalancedPlacementRequest(
        @NotEmpty @Valid List<PlacementDeviceDto> devices,
        @NotEmpty @Valid List<PlacementRackDto> racks
) {}
