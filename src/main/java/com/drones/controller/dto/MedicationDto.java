package com.drones.controller.dto;

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
public class MedicationDto {
    String name;
    Integer weight;
    String code;
    Byte[] image;
    List<DroneDto> drones = new ArrayList<>();
}
