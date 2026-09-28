package com.parkspot.werkins.controller;
import com.parkspot.werkins.dto.*; import com.parkspot.werkins.service.FlatService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/flats") public class FlatController {
 private final FlatService service; public FlatController(FlatService s){service=s;}
 @GetMapping public List<FlatResponse> all(){return service.all();} @GetMapping("/{id}") public FlatResponse get(@PathVariable Long id){return service.get(id);}
 @PostMapping public ResponseEntity<FlatResponse> create(@Valid @RequestBody FlatRequest r){return ResponseEntity.status(201).body(service.create(r));}
 @PutMapping("/{id}") public FlatResponse update(@PathVariable Long id,@Valid @RequestBody FlatRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
