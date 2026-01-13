package com.example.datacenter_backend_api.controller;

import com.example.datacenter_backend_api.domain.dto.DeviceCreateRequest;
import com.example.datacenter_backend_api.domain.dto.DeviceDto;
import com.example.datacenter_backend_api.domain.dto.DeviceUpdateRequest;
import com.example.datacenter_backend_api.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceDto create(@Valid @RequestBody DeviceCreateRequest request) {
        return deviceService.create(request);
    }

    @GetMapping
    public List<DeviceDto> getAll() {
        return deviceService.getAll();
    }

    @GetMapping("/{id}")
    public DeviceDto getById(@PathVariable Long id) {
        return deviceService.getById(id);
    }

    @PatchMapping("/{id}")
    public DeviceDto update(@PathVariable Long id, @Valid @RequestBody DeviceUpdateRequest request) {
        return deviceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deviceService.delete(id);
    }
}
