package com.drones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.drones.controller.dto.DroneDto;
import com.drones.controller.dto.MedicationDto;
import com.drones.enums.Model;
import com.drones.enums.State;
import com.drones.repository.DroneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DroneControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DroneRepository droneRepository;

    private DroneDto drone;

    @BeforeEach
    void setUpDrone() {
        drone = new DroneDto();
        drone.setSerialNumber("serial_number");
        drone.setState(State.DELIVERED);
        drone.setModel(Model.Cruiserweight);
        drone.setBatteryCapacity(99);
        drone.setWeightLimit(499);
    }

    @AfterEach
    void clearDrones() {
        droneRepository.deleteAll();
    }

    @Test
    void registerDroneSuccess() throws Exception {
        mockMvc.perform(
                        post("/api/v1/drone")
                                .content(objectMapper.writeValueAsString(drone))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"));
    }

    @Test
    void registerDroneDuplicateKeyException() throws Exception {
        mockMvc.perform(
                        post("/api/v1/drone")
                                .content(objectMapper.writeValueAsString(drone))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"));

        mockMvc.perform(
                        post("/api/v1/drone")
                                .content(objectMapper.writeValueAsString(drone))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Serial number must be unique."));
    }

    @Test
    void loadDroneWithMedicationItemsSuccess() throws Exception {
        mockMvc.perform(
                        post("/api/v1/drone")
                                .content(objectMapper.writeValueAsString(drone))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"));

        List<MedicationDto> medicationDtos = List.of(MedicationDto.builder().name("medication1").weight(100).code("CODE").build());

        mockMvc.perform(
                        post("/api/v1/drone/medication")
                                .param("serialNumber", "serial_number")
                                .content(objectMapper.writeValueAsString(medicationDtos))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"))
                .andExpect(jsonPath("$.medications[0].name").value("medication1"));
    }

    @Test
    void getDroneMedicationsSuccess() throws Exception {
        mockMvc.perform(
                        post("/api/v1/drone")
                                .content(objectMapper.writeValueAsString(drone))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"));

        List<MedicationDto> medicationDtos = List.of(
                MedicationDto.builder().name("medication1").weight(100).code("CODE").build(),
                MedicationDto.builder().name("medication2").weight(100).code("CODE").build());

        mockMvc.perform(
                        post("/api/v1/drone/medication")
                                .param("serialNumber", "serial_number")
                                .content(objectMapper.writeValueAsString(medicationDtos))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"))
                .andExpect(jsonPath("$.medications[0].name").value("medication1"))
                .andExpect(jsonPath("$.medications[1].name").value("medication2"));

        mockMvc.perform(
                        get("/api/v1/drone/medication")
                                .param("serialNumber", "serial_number")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("medication1"))
                .andExpect(jsonPath("$[1].name").value("medication2"));
    }

    @Test
    void getAvailableDronesSuccess() throws Exception {
        mockMvc.perform(
                        get("/api/v1/drone")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        mockMvc.perform(
                        post("/api/v1/drone")
                                .content(objectMapper.writeValueAsString(drone))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"));

        mockMvc.perform(
                        get("/api/v1/drone")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber").value("serial_number"));
    }

    @Test
    void getDroneBatteryCapacitySuccess() throws Exception {
        mockMvc.perform(
                        post("/api/v1/drone")
                                .content(objectMapper.writeValueAsString(drone))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial_number"));

        mockMvc.perform(
                        get("/api/v1/drone/battery")
                                .param("serialNumber", "serial_number")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value("99"));
    }
}