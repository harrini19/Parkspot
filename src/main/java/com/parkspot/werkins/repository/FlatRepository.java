package com.parkspot.werkins.repository;
import com.parkspot.werkins.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FlatRepository extends JpaRepository<Flat,Long> { boolean existsByFlatNumberIgnoreCase(String flatNumber); }
