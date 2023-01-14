package com.drones.repository;

import com.drones.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DroneRepository extends JpaRepository<Drone, UUID> {

    Drone findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);
}
