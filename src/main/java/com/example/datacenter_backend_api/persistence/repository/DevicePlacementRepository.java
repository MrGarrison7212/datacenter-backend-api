package com.example.datacenter_backend_api.persistence.repository;

import com.example.datacenter_backend_api.persistence.model.DevicePlacementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevicePlacementRepository extends JpaRepository<DevicePlacementEntity, Long> {

    List<DevicePlacementEntity> findByRackId(Long rackId);

    boolean existsByDeviceId(Long deviceId);

    Optional<DevicePlacementEntity> findByDeviceId(Long deviceId);

}
