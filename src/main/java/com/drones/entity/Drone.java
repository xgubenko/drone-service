package com.drones.entity;


import com.drones.enums.Model;
import com.drones.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
A **Drone** has:
- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).
 */
@Entity(name = "drone")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Drone {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Size(min = 1, max = 100)
    @Column(unique = true)
    private String serialNumber;

    @NonNull
    private Model model;

    @Range(min = 1, max = 500)
    @NonNull
    private Integer weightLimit;

    @Range(min = 1, max = 100)
    @NonNull
    private Integer batteryCapacity;

    @NonNull
    private State state;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "drone_medication",
            joinColumns = @JoinColumn(name = "drone_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private List<Medication> medications = new ArrayList<>();
}