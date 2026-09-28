package com.parkspot.werkins.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="visitor_vehicles", indexes = {
        @Index(name="idx_visit_entry", columnList="entry_time"),
        @Index(name="idx_visit_slot_exit", columnList="parking_slot_id,exit_time")
})
public class VisitorVehicle {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String vehicleNumber;
    @Column(nullable=false) private String visitorName;
    @Column(nullable=false) private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="flat_id", nullable=false) private Flat flat;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="parking_slot_id", nullable=false) private ParkingSlot parkingSlot;
    public VisitorVehicle(){}
    public Long getId(){return id;} public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){vehicleNumber=v;}
    public String getVisitorName(){return visitorName;} public void setVisitorName(String v){visitorName=v;}
    public LocalDateTime getEntryTime(){return entryTime;} public void setEntryTime(LocalDateTime v){entryTime=v;}
    public LocalDateTime getExitTime(){return exitTime;} public void setExitTime(LocalDateTime v){exitTime=v;}
    public Flat getFlat(){return flat;} public void setFlat(Flat v){flat=v;}
    public ParkingSlot getParkingSlot(){return parkingSlot;} public void setParkingSlot(ParkingSlot v){parkingSlot=v;}
}
