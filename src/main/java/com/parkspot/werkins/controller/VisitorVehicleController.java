package com.parkspot.werkins.controller;
import com.parkspot.werkins.dto.*; import com.parkspot.werkins.service.VisitorVehicleService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.*;
@RestController @RequestMapping("/api/visitors")
public class VisitorVehicleController {
 private final VisitorVehicleService service; public VisitorVehicleController(VisitorVehicleService s){service=s;}
 @PostMapping public ResponseEntity<VisitorResponse> entry(@Valid @RequestBody VisitorEntryRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.entry(r));}
 @PatchMapping("/{id}/exit") public VisitorResponse exit(@PathVariable Long id,@Valid @RequestBody(required=false) ExitRequest r){return service.exit(id,r);}
 @GetMapping("/daily-log") public List<VisitorResponse> daily(@RequestParam(required=false) LocalDate date){return service.daily(date);}
 @GetMapping("/occupied") public List<VisitorResponse> occupied(){return service.occupied();}
}
