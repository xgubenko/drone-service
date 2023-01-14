package com.drones.controller.dto;

import com.drones.enums.Model;
import com.drones.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DroneDto {
    private String serialNumber;
    private Model model;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private State state;
    private List<MedicationDto> medications = new ArrayList<>();
}
