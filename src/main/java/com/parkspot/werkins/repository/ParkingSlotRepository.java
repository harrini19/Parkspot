package com.parkspot.werkins.repository;
import com.parkspot.werkins.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot,Long> { boolean existsBySlotNumberIgnoreCase(String slotNumber); }
