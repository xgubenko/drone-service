package com.drones.service.impl;

import com.drones.entity.Drone;
import com.drones.entity.Medication;
import com.drones.repository.DroneRepository;
import com.drones.service.DroneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;

    @Transactional
    @Override
    public Drone registerDrone(final Drone drone) {
        log.debug("Registering drone: {}", drone);
        if (droneRepository.existsBySerialNumber(drone.getSerialNumber())) {
            log.error("Drone with serial number {} already exists", drone.getSerialNumber());
            throw new DuplicateKeyException("Serial number must be unique.");
        }
        return droneRepository.save(drone);
    }

    @Override
    public Drone getDroneInfo(String serialNumber) {
        log.debug("Get drone info: {}", serialNumber);
        return droneRepository.findBySerialNumber(serialNumber);
    }

    @Transactional
    @Override
    public Drone loadDroneWithMedicationItems(List<Medication> medicationList, String serialNumber) {
        log.debug("Loading drone {} with medications: {}", serialNumber, medicationList);
        Drone drone = droneRepository.findBySerialNumber(serialNumber);

        int weight = medicationList.stream()
                .reduce(0, (total, current) -> total + current.getWeight(), Integer::sum);

        if (weight > drone.getWeightLimit()) {
            log.error("Medications weight is too high: {}; drone will not be loaded", medicationList);
            throw new DataIntegrityViolationException(
                    String.format("Medications weight should be below drone weight limit: %s", drone.getWeightLimit()));
        }
        drone.setMedications(medicationList);
        return droneRepository.save(drone);
    }

    @Override
    public List<Medication> getDroneMedications(String serialNumber) {
        log.debug("Get medications for specific drone: {}", serialNumber);
        Drone drone = droneRepository.findBySerialNumber(serialNumber);
        return drone.getMedications();
    }

    @Override
    public List<Drone> getAvailableDrones() {
        log.debug("Get drones available for loading");
        return droneRepository.findAll().stream()
                .filter(drone -> drone.getMedications().size() == 0)
                .toList();
    }
}