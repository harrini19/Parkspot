package com.parkspot.werkins.service;
import com.parkspot.werkins.dto.*; import com.parkspot.werkins.entity.Flat; import com.parkspot.werkins.exception.*; import com.parkspot.werkins.repository.FlatRepository; import org.springframework.stereotype.Service; import java.util.*;
@Service public class FlatService {
 private final FlatRepository repo; public FlatService(FlatRepository r){repo=r;}
 public List<FlatResponse> all(){return repo.findAll().stream().map(this::out).toList();}
 public FlatResponse get(Long id){return out(repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Flat not found: "+id)));}
 public FlatResponse create(FlatRequest r){if(repo.existsByFlatNumberIgnoreCase(r.flatNumber()))throw new BusinessRuleException("Flat number already exists"); return out(repo.save(new Flat(r.flatNumber().trim(),r.ownerName().trim())));}
 public FlatResponse update(Long id,FlatRequest r){Flat f=repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Flat not found: "+id)); if(!f.getFlatNumber().equalsIgnoreCase(r.flatNumber())&&repo.existsByFlatNumberIgnoreCase(r.flatNumber()))throw new BusinessRuleException("Flat number already exists"); f.setFlatNumber(r.flatNumber().trim());f.setOwnerName(r.ownerName().trim());return out(repo.save(f));}
 public void delete(Long id){if(!repo.existsById(id))throw new ResourceNotFoundException("Flat not found: "+id);repo.deleteById(id);}
 private FlatResponse out(Flat f){return new FlatResponse(f.getId(),f.getFlatNumber(),f.getOwnerName());}
}
