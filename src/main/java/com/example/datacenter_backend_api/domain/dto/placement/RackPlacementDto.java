package com.example.datacenter_backend_api.domain.dto.placement;

import java.util.List;

public record RackPlacementDto(
        String rackSerialNumber,
        int totalUnits,
        int maxPowerW,
        int usedUnits,
        int usedPowerW,
        double utilization,

        List<PlacementDeviceDto> devices
) {}
