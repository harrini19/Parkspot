package com.parkspot.werkins.controller;
import com.parkspot.werkins.dto.*; import com.parkspot.werkins.service.ParkingSlotService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/parking-slots") public class ParkingSlotController {
 private final ParkingSlotService service; public ParkingSlotController(ParkingSlotService s){service=s;}
 @GetMapping public List<ParkingSlotResponse> all(){return service.all();} @GetMapping("/{id}") public ParkingSlotResponse get(@PathVariable Long id){return service.get(id);}
 @GetMapping("/occupied") public List<ParkingSlotResponse> occupied(){return service.occupied();}
 @PostMapping public ResponseEntity<ParkingSlotResponse> create(@Valid @RequestBody ParkingSlotRequest r){return ResponseEntity.status(201).body(service.create(r));}
 @PutMapping("/{id}") public ParkingSlotResponse update(@PathVariable Long id,@Valid @RequestBody ParkingSlotRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
