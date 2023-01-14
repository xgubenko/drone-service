package com.drones.service;

import com.drones.entity.Drone;
import com.drones.entity.Medication;

import java.util.List;

public interface DroneService {
    Drone registerDrone(Drone drone);

    Drone getDroneInfo(String serialNumber);

    Drone loadDroneWithMedicationItems(List<Medication> medicationList, String serialNumber);

    List<Medication> getDroneMedications(String serialNumber);

    List<Drone> getAvailableDrones();




//    ResponseBean loadDroneWithMedications(String serialNumber);
//
//    ResponseBean checkLoadedMedicationItems(String serialNumber);
//
//    ResponseBean checkDroneAvailability();
//
//    ResponseBean obtainDroneBatteryLevel(String serialNumber);
}