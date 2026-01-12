package com.example.datacenter_backend_api.domain.mapper;

import com.example.datacenter_backend_api.domain.dto.DeviceCreateRequest;
import com.example.datacenter_backend_api.domain.dto.DeviceDto;
import com.example.datacenter_backend_api.domain.dto.DeviceUpdateRequest;
import com.example.datacenter_backend_api.persistence.model.DeviceEntity;

public final class DeviceMapper {

    private DeviceMapper() {}

    public static DeviceDto toDto(DeviceEntity e) {
        if (e == null) return null;

        return new DeviceDto(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getSerialNumber(),
                e.getUnits(),
                e.getPowerConsumptionW()
        );
    }

    public static DeviceEntity fromCreateRequest(DeviceCreateRequest req) {
        if (req == null) return null;

        DeviceEntity e = new DeviceEntity();
        e.setName(req.name());
        e.setDescription(req.description());
        e.setSerialNumber(req.serialNumber());
        e.setUnits(req.units());
        e.setPowerConsumptionW(req.powerConsumptionW());
        return e;
    }

    public static void applyUpdate(DeviceEntity existing, DeviceUpdateRequest req) {
        if (existing == null || req == null) return;

        if (req.name() != null) existing.setName(req.name());
        if (req.description() != null) existing.setDescription(req.description());
        if (req.serialNumber() != null) existing.setSerialNumber(req.serialNumber());
        if (req.units() != null) existing.setUnits(req.units());
        if (req.powerConsumptionW() != null) existing.setPowerConsumptionW(req.powerConsumptionW());
    }
}
