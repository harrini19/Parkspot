package com.parkspot.werkins.repository;
import com.parkspot.werkins.entity.*;
import org.springframework.data.jpa.repository.*;
import java.time.*;
import java.util.*;
public interface VisitorVehicleRepository extends JpaRepository<VisitorVehicle,Long> {
    boolean existsByParkingSlotIdAndExitTimeIsNull(Long slotId);
    boolean existsByVehicleNumberIgnoreCaseAndExitTimeIsNull(String vehicleNumber);
    List<VisitorVehicle> findByEntryTimeBetweenOrderByEntryTimeDesc(LocalDateTime from, LocalDateTime to);
    List<VisitorVehicle> findByExitTimeIsNullOrderByEntryTimeAsc();
}
