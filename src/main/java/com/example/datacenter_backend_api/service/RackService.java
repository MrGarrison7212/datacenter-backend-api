package com.example.datacenter_backend_api.service;

import com.example.datacenter_backend_api.domain.dto.RackCreateRequest;
import com.example.datacenter_backend_api.domain.dto.RackDto;
import com.example.datacenter_backend_api.domain.dto.RackUpdateRequest;
import com.example.datacenter_backend_api.domain.mapper.RackMapper;
import com.example.datacenter_backend_api.exception.ConflictException;
import com.example.datacenter_backend_api.exception.NotFoundException;
import com.example.datacenter_backend_api.persistence.model.RackEntity;
import com.example.datacenter_backend_api.persistence.repository.DevicePlacementRepository;
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
}
