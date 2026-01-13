package com.example.datacenter_backend_api.service;

import com.example.datacenter_backend_api.domain.dto.DeviceCreateRequest;
import com.example.datacenter_backend_api.domain.dto.DeviceDto;
import com.example.datacenter_backend_api.domain.dto.DeviceUpdateRequest;
import com.example.datacenter_backend_api.domain.mapper.DeviceMapper;
import com.example.datacenter_backend_api.exception.ConflictException;
import com.example.datacenter_backend_api.exception.NotFoundException;
import com.example.datacenter_backend_api.persistence.model.DeviceEntity;
import com.example.datacenter_backend_api.persistence.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceDto create(DeviceCreateRequest request) {
        if (deviceRepository.existsBySerialNumber(request.serialNumber())) {
            throw new ConflictException("Device with serialNumber already exists: " + request.serialNumber());
        }
        DeviceEntity saved = deviceRepository.save(DeviceMapper.fromCreateRequest(request));
        return DeviceMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<DeviceDto> getAll() {
        return deviceRepository.findAll().stream().map(DeviceMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public DeviceDto getById(Long id) {
        DeviceEntity entity = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found: " + id));
        return DeviceMapper.toDto(entity);
    }

    public DeviceDto update(Long id, DeviceUpdateRequest request) {
        DeviceEntity existing = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found: " + id));

        if (request.serialNumber() != null
                && !request.serialNumber().equals(existing.getSerialNumber())
                && deviceRepository.existsBySerialNumber(request.serialNumber())) {
            throw new ConflictException("Device with serialNumber already exists: " + request.serialNumber());
        }

        DeviceMapper.applyUpdate(existing, request);
        return DeviceMapper.toDto(deviceRepository.save(existing));
    }

    public void delete(Long id) {
        DeviceEntity existing = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found: " + id));
        deviceRepository.delete(existing);
    }
}
