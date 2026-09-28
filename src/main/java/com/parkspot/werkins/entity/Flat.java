package com.parkspot.werkins.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "flats", uniqueConstraints = @UniqueConstraint(name = "uk_flat_number", columnNames = "flat_number"))
public class Flat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(name = "flat_number", nullable = false, unique = true)
    private String flatNumber;
    @NotBlank @Column(nullable = false)
    private String ownerName;
    @OneToMany(mappedBy = "flat", fetch = FetchType.LAZY)
    private List<VisitorVehicle> visitorVehicles = new ArrayList<>();
    public Flat() {}
    public Flat(String flatNumber, String ownerName) { this.flatNumber=flatNumber; this.ownerName=ownerName; }
    public Long getId(){return id;} public String getFlatNumber(){return flatNumber;} public void setFlatNumber(String v){flatNumber=v;}
    public String getOwnerName(){return ownerName;} public void setOwnerName(String v){ownerName=v;}
}
