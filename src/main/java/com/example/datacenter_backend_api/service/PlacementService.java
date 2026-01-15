package com.example.datacenter_backend_api.service;

import com.example.datacenter_backend_api.domain.dto.placement.*;
import com.example.datacenter_backend_api.exception.PlacementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlacementService {

    @RequiredArgsConstructor
    private static final class RackState {
        private final PlacementRackDto rack;
        private int usedUnits = 0;
        private int usedPowerW = 0;
        private final List<PlacementDeviceDto> devices = new ArrayList<>();

        double utilization(){
            return usedPowerW/ (double) rack.maxPowerW();
        }

        boolean canFit(PlacementDeviceDto d){
            return (usedUnits + d.units() <= rack.totalUnits())
                    && (usedPowerW + d.powerConsumptionW() <= rack.maxPowerW());
        }
        void place(PlacementDeviceDto d){
            usedUnits += d.units();
            usedPowerW += d.powerConsumptionW();
            devices.add(d);
        }
    }

    public BalancedPlacementResultDto calculateBalancedPlacement(BalancedPlacementRequest request){
        List<PlacementDeviceDto> devices = new ArrayList<>(request.devices());
        devices.sort(Comparator.comparingInt(PlacementDeviceDto::powerConsumptionW).reversed());

        List<RackState> states = request.racks().stream()
                .map(RackState::new)
                .toList();

        for(PlacementDeviceDto d : devices){
            RackState best = states.stream()
                    .filter(s -> s.canFit(d))
                    .min(Comparator.comparingDouble(RackState::utilization))
                    .orElseThrow(() -> new PlacementException("Device cannot fit into any rack : serialNumber" + d.serialNumber()
                    ));
            best.place(d);
        }

        List<RackPlacementDto> resultRacks = states.stream()
                .map(s -> new RackPlacementDto(
                        s.rack.serialNumber(),
                        s.rack.totalUnits(),
                        s.rack.maxPowerW(),
                        s.usedUnits,
                        s.usedPowerW,
                        s.utilization(),
                        List.copyOf(s.devices)
                ))
                .toList();

        return new BalancedPlacementResultDto(resultRacks);
    }
}
