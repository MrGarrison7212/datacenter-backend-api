package com.example.datacenter_backend_api.domain.mapper;

import com.example.datacenter_backend_api.domain.dto.RackCreateRequest;
import com.example.datacenter_backend_api.domain.dto.RackDto;
import com.example.datacenter_backend_api.domain.dto.RackUpdateRequest;
import com.example.datacenter_backend_api.persistence.model.RackEntity;

public final class RackMapper {

    private RackMapper() {}

    public static RackDto toDto(RackEntity e, int currentPowerW) {
        if (e == null) return null;

        return new RackDto(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getSerialNumber(),
                e.getTotalUnits(),
                e.getMaxPowerW(),
                currentPowerW
        );
    }

    public static RackEntity fromCreateRequest(RackCreateRequest req) {
        if (req == null) return null;

        RackEntity e = new RackEntity();
        e.setName(req.name());
        e.setDescription(req.description());
        e.setSerialNumber(req.serialNumber());
        e.setTotalUnits(req.totalUnits());
        e.setMaxPowerW(req.maxPowerW());
        return e;
    }

    public static void applyUpdate(RackEntity existing, RackUpdateRequest req) {
        if (existing == null || req == null) return;

        if (req.name() != null) existing.setName(req.name());
        if (req.description() != null) existing.setDescription(req.description());
        if (req.serialNumber() != null) existing.setSerialNumber(req.serialNumber());
        if (req.totalUnits() != null) existing.setTotalUnits(req.totalUnits());
        if (req.maxPowerW() != null) existing.setMaxPowerW(req.maxPowerW());
    }
}
