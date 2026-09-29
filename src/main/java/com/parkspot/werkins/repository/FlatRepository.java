package com.parkspot.werkins.repository;
import com.parkspot.werkins.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface FlatRepository extends JpaRepository<Flat,Long> {
	boolean existsByFlatNumberIgnoreCase(String flatNumber);
	Optional<Flat> findByFlatNumberIgnoreCase(String flatNumber);
}
