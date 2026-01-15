package com.example.datacenter_backend_api.service;

import com.example.datacenter_backend_api.domain.dto.placement.BalancedPlacementRequest;
import com.example.datacenter_backend_api.domain.dto.placement.PlacementDeviceDto;
import com.example.datacenter_backend_api.domain.dto.placement.PlacementRackDto;
import com.example.datacenter_backend_api.exception.PlacementException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlacementServiceTest {

    private final PlacementService service = new PlacementService();

    @Test
    void should_balance_reasonably() {
        var racks = List.of(
                new PlacementRackDto("TestRack1", null, "R-1", 10, 1000),
                new PlacementRackDto("TestRack2", null, "R-2", 10, 1000)
        );

        var devices = List.of(
                new PlacementDeviceDto("TestD1", null, "D-1", 2, 400),
                new PlacementDeviceDto("TestD2", null, "D-2", 2, 300),
                new PlacementDeviceDto("TestD3", null, "D-3", 2, 200),
                new PlacementDeviceDto("TestD4", null, "D-4", 2, 100)
        );

        var result = service.calculateBalancedPlacement(new BalancedPlacementRequest(devices, racks));

        assertEquals(2, result.racks().size());

        double u1 = result.racks().get(0).utilization();
        double u2 = result.racks().get(1).utilization();

        assertTrue(Math.abs(u1 - u2) <= 0.4);
    }

    @Test
    void should_throw_if_device_cannot_fit_anywhere(){
        var racks = List.of(new PlacementRackDto("TestRack1", null, "R-1", 1, 100));
        var devices = List.of(new PlacementDeviceDto("TestDevice1-big", null, "D-1", 2, 500));

        assertThrows(PlacementException.class, () ->
                service.calculateBalancedPlacement(new BalancedPlacementRequest(devices, racks))
        );
    }

}
