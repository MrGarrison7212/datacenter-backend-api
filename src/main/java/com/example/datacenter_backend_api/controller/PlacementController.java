package com.example.datacenter_backend_api.controller;

import com.example.datacenter_backend_api.domain.dto.placement.BalancedPlacementRequest;
import com.example.datacenter_backend_api.domain.dto.placement.BalancedPlacementResultDto;
import com.example.datacenter_backend_api.service.PlacementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/placements")
public class PlacementController {

    private final PlacementService placementService;

    @PostMapping("/balanced")
    public BalancedPlacementResultDto calculate(@Valid @RequestBody BalancedPlacementRequest request){
        return placementService.calculateBalancedPlacement(request);
    }
}
