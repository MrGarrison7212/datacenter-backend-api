package com.example.datacenter_backend_api.persistence.repository;

import com.example.datacenter_backend_api.persistence.model.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RackRepository extends JpaRepository<RackEntity, Long> {

    Optional<RackEntity> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);
}
