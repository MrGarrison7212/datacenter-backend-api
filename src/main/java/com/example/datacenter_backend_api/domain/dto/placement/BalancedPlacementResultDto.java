package com.example.datacenter_backend_api.domain.dto.placement;

import java.util.List;

public record BalancedPlacementResultDto(
   List<RackPlacementDto> racks
) {}
