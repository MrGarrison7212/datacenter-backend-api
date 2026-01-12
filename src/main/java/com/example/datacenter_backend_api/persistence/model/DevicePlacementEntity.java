package com.example.datacenter_backend_api.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "device_placement", indexes = {
        @Index(name = "placement_by_device_idx", columnList = "device_id", unique = true),
        @Index(name = "placement_by_rack_idx", columnList = "rack_id")
})
public class DevicePlacementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id", nullable = false)
    private RackEntity rack;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private DeviceEntity device;

    @Column(name = "start_unit", nullable = false)
    private int startUnit;
}