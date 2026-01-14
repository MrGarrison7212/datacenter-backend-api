package com.example.datacenter_backend_api.service;

import com.example.datacenter_backend_api.domain.dto.RackCreateRequest;
import com.example.datacenter_backend_api.domain.dto.RackDto;
import com.example.datacenter_backend_api.domain.dto.RackUpdateRequest;
import com.example.datacenter_backend_api.domain.mapper.RackMapper;
import com.example.datacenter_backend_api.exception.BusinessValidationException;
import com.example.datacenter_backend_api.exception.ConflictException;
import com.example.datacenter_backend_api.exception.NotFoundException;
import com.example.datacenter_backend_api.persistence.model.DeviceEntity;
import com.example.datacenter_backend_api.persistence.model.DevicePlacementEntity;
import com.example.datacenter_backend_api.persistence.model.RackEntity;
import com.example.datacenter_backend_api.persistence.repository.DevicePlacementRepository;
import com.example.datacenter_backend_api.persistence.repository.DeviceRepository;
import com.example.datacenter_backend_api.persistence.repository.RackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RackService {

    private final RackRepository rackRepository;
    private final DevicePlacementRepository devicePlacementRepository;
    private final DeviceRepository deviceRepository;

    public RackDto create(RackCreateRequest request) {
        if (rackRepository.existsBySerialNumber(request.serialNumber())) {
            throw new ConflictException("Rack with serialNumber already exists: " + request.serialNumber());
        }
        RackEntity saved = rackRepository.save(RackMapper.fromCreateRequest(request));
        return RackMapper.toDto(saved, 0);
    }

    @Transactional(readOnly = true)
    public List<RackDto> getAll() {
        return rackRepository.findAll().stream()
                .map(r -> RackMapper.toDto(r, currentPowerW(r.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public RackDto getById(Long id) {
        RackEntity rack = rackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rack not found: " + id));
        return RackMapper.toDto(rack, currentPowerW(id));
    }

    public RackDto update(Long id, RackUpdateRequest request) {
        RackEntity existing = rackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rack not found: " + id));

        if (request.serialNumber() != null
                && !request.serialNumber().equals(existing.getSerialNumber())
                && rackRepository.existsBySerialNumber(request.serialNumber())) {
            throw new ConflictException("Rack with serialNumber already exists: " + request.serialNumber());
        }

        RackMapper.applyUpdate(existing, request);
        RackEntity saved = rackRepository.save(existing);
        return RackMapper.toDto(saved, currentPowerW(id));
    }

    public void delete(Long id) {
        RackEntity existing = rackRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rack not found: " + id));
        rackRepository.delete(existing);
    }

    private int currentPowerW(Long rackId) {
        return devicePlacementRepository.findByRackId(rackId).stream()
                .mapToInt(p -> p.getDevice().getPowerConsumptionW())
                .sum();
    }

    public void placeDevice(Long rackId, Long deviceId, int startUnit)  {
        RackEntity rack = rackRepository.findById(rackId)
                .orElseThrow(() -> new NotFoundException("Rack not found: " + rackId));

        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("Device not found: " + deviceId));

        if(devicePlacementRepository.existsByDeviceId(deviceId)){
            throw new ConflictException("Device is already  placed: " + deviceId);
        }

        int endUnit = startUnit + device.getUnits() - 1;
        if(startUnit < 1 || endUnit > rack.getTotalUnits()){
            throw new BusinessValidationException(
                    "Device does not fit into rack units, startUnit = " + startUnit + ", endUnit = " +
                            endUnit + ", rackTotalUnits = " + rack.getTotalUnits());
        }

        var placements = devicePlacementRepository.findByRackId(rackId);
        for(DevicePlacementEntity p : placements){
            int existingStart = p.getStartUnit();
            int existingEnd = existingStart + p.getDevice().getUnits() - 1;

            boolean overlaps = startUnit <= existingEnd && endUnit >= existingStart;
            if(overlaps) {
                throw new BusinessValidationException(
                        "Units overlap with existing ones");
            }
        }

        int currentPower = placements.stream()
                .mapToInt(p -> p.getDevice().getPowerConsumptionW())
                .sum();

        int newTotalPower = currentPower + device.getPowerConsumptionW();
        if(newTotalPower > rack.getMaxPowerW()){
            throw new BusinessValidationException(
                    "Rack power limit exceeded");
        }

        DevicePlacementEntity placement = new DevicePlacementEntity();
        placement.setRack(rack);
        placement.setDevice(device);
        placement.setStartUnit(startUnit);
        devicePlacementRepository.save(placement);
    }
}
