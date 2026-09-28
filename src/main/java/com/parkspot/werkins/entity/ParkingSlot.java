package com.parkspot.werkins.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "parking_slots", uniqueConstraints = @UniqueConstraint(name = "uk_slot_number", columnNames = "slot_number"))
public class ParkingSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(name="slot_number", nullable=false, unique=true)
    private String slotNumber;
    @Column(nullable=false) private boolean active = true;
    @OneToMany(mappedBy = "parkingSlot", fetch = FetchType.LAZY)
    private List<VisitorVehicle> visitorVehicles = new ArrayList<>();
    public ParkingSlot() {}
    public ParkingSlot(String slotNumber){this.slotNumber=slotNumber;}
    public Long getId(){return id;} public String getSlotNumber(){return slotNumber;} public void setSlotNumber(String v){slotNumber=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
