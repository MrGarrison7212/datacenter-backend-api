package com.example.datacenter_backend_api.persistence.repository;

import com.example.datacenter_backend_api.persistence.model.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    Optional<DeviceEntity> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);
}
