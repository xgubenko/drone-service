package com.drones.mapper;

import com.drones.controller.dto.DroneDto;
import com.drones.entity.Drone;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DroneMapper {

    Drone fromDto(DroneDto dto);

    List<Drone> fromDtoList(List<DroneDto> dto);

    List<DroneDto> toDtoList(List<Drone> dto);

    DroneDto toDto(Drone drone);
}