package com.example.datacenter_backend_api.controller;

import com.example.datacenter_backend_api.domain.dto.PlaceDeviceRequest;
import com.example.datacenter_backend_api.domain.dto.RackCreateRequest;
import com.example.datacenter_backend_api.domain.dto.RackDto;
import com.example.datacenter_backend_api.domain.dto.RackUpdateRequest;
import com.example.datacenter_backend_api.service.RackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/racks")
public class RackController {

    private final RackService rackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RackDto create(@Valid @RequestBody RackCreateRequest request) {
        return rackService.create(request);
    }

    @GetMapping
    public List<RackDto> getAll() {
        return rackService.getAll();
    }

    @GetMapping("/{id}")
    public RackDto getById(@PathVariable Long id) {
        return rackService.getById(id);
    }

    @PatchMapping("/{id}")
    public RackDto update(@PathVariable Long id, @Valid @RequestBody RackUpdateRequest request) {
        return rackService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rackService.delete(id);
    }

    @PostMapping("/{rackId}/devices/{deviceId}/place")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void placeDevice(
        @PathVariable Long rackId,
        @PathVariable Long deviceId,
        @Valid @RequestBody PlaceDeviceRequest request
    ) {
        rackService.placeDevice(rackId, deviceId, request.startUnit());
    }
}
