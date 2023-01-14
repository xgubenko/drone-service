package com.drones.mapper;

import com.drones.controller.dto.MedicationDto;
import com.drones.entity.Medication;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    Medication fromDto(MedicationDto dto);

    List<MedicationDto> toDtoList(List<Medication> dto);

    List<Medication> fromDtoList(List<MedicationDto> dto);

    MedicationDto toDto(Medication medication);
}
