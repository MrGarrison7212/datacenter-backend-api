package com.example.datacenter_backend_api.persistence.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rack", indexes = {
        @Index(name = "rack_by_serial_idx", columnList = "serial_number", unique = true)
})
public class RackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "serial_number", nullable = false, length = 120)
    private String serialNumber;

    @Column(name = "total_units", nullable = false)
    private int totalUnits;

    @Column(name = "max_power_w", nullable = false)
    private int maxPowerW;
}