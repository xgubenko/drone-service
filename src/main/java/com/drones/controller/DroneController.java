package com.drones.controller;

import com.drones.controller.dto.BatteryLevel;
import com.drones.controller.dto.DroneDto;
import com.drones.controller.dto.MedicationDto;
import com.drones.entity.Drone;
import com.drones.entity.Medication;
import com.drones.mapper.DroneMapper;
import com.drones.mapper.MedicationMapper;
import com.drones.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drone")
public class DroneController {
    private final DroneService droneService;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;

    @PostMapping
    public ResponseEntity<DroneDto> registerDrone(@RequestBody DroneDto requestDto) {
        Drone newDrone = droneService.registerDrone(droneMapper.fromDto(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(droneMapper.toDto(newDrone));
    }

    @PostMapping("/medication")
    public ResponseEntity<DroneDto> loadDroneWithMedicationItems(@RequestBody List<MedicationDto> dtoList,
                                                                 @RequestParam String serialNumber) {
        Drone drone = droneService.loadDroneWithMedicationItems(medicationMapper.fromDtoList(dtoList), serialNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(droneMapper.toDto(drone));
    }

    @GetMapping("/medication")
    public ResponseEntity<List<MedicationDto>> getDroneMedications(@RequestParam String serialNumber) {
        List<Medication> medications = droneService.getDroneMedications(serialNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(medicationMapper.toDtoList(medications));
    }

    @GetMapping
    public ResponseEntity<List<DroneDto>> getAvailableDrones() {
        List<Drone> drones = droneService.getAvailableDrones();
        return ResponseEntity.status(HttpStatus.OK)
                .body(droneMapper.toDtoList(drones));
    }

    @GetMapping("/battery")
    public ResponseEntity<BatteryLevel> getDroneBatteryCapacity(@RequestParam String serialNumber) {
        Drone drone = droneService.getDroneInfo(serialNumber);
        BatteryLevel level = new BatteryLevel(drone.getBatteryCapacity());
        return ResponseEntity.status(HttpStatus.OK)
                .body(level);
    }
}