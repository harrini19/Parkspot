package com.parkspot.werkins.service;
import com.parkspot.werkins.dto.*; import com.parkspot.werkins.entity.ParkingSlot; import com.parkspot.werkins.exception.*; import com.parkspot.werkins.repository.*; import org.springframework.stereotype.Service; import java.util.*;
@Service public class ParkingSlotService {
 private final ParkingSlotRepository repo; private final VisitorVehicleRepository visits; public ParkingSlotService(ParkingSlotRepository r,VisitorVehicleRepository v){repo=r;visits=v;}
 public List<ParkingSlotResponse> all(){return repo.findAll().stream().map(this::out).toList();} public ParkingSlot getEntity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Parking slot not found: "+id));}
 public ParkingSlotResponse get(Long id){return out(getEntity(id));}
 public ParkingSlotResponse create(ParkingSlotRequest r){if(repo.existsBySlotNumberIgnoreCase(r.slotNumber()))throw new BusinessRuleException("Slot number already exists");ParkingSlot s=new ParkingSlot(r.slotNumber().trim());if(r.active()!=null)s.setActive(r.active());return out(repo.save(s));}
 public ParkingSlotResponse update(Long id,ParkingSlotRequest r){ParkingSlot s=getEntity(id);if(!s.getSlotNumber().equalsIgnoreCase(r.slotNumber())&&repo.existsBySlotNumberIgnoreCase(r.slotNumber()))throw new BusinessRuleException("Slot number already exists");s.setSlotNumber(r.slotNumber().trim());if(r.active()!=null)s.setActive(r.active());return out(repo.save(s));}
 public void delete(Long id){ParkingSlot s=getEntity(id);if(visits.existsByParkingSlotIdAndExitTimeIsNull(id))throw new BusinessRuleException("Cannot delete an occupied slot");repo.delete(s);}
 public List<ParkingSlotResponse> occupied(){return visits.findByExitTimeIsNullOrderByEntryTimeAsc().stream().map(v->out(v.getParkingSlot())).distinct().toList();}
 private ParkingSlotResponse out(ParkingSlot s){return new ParkingSlotResponse(s.getId(),s.getSlotNumber(),s.isActive(),visits.existsByParkingSlotIdAndExitTimeIsNull(s.getId()));}
}
