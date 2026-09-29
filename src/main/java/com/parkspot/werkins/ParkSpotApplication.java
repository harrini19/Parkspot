package com.parkspot.werkins;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.parkspot.werkins.entity.Flat;
import com.parkspot.werkins.entity.ParkingSlot;
import com.parkspot.werkins.repository.FlatRepository;
import com.parkspot.werkins.repository.ParkingSlotRepository;

import java.util.List;

@SpringBootApplication
public class ParkSpotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkSpotApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedFlats(FlatRepository flats) {
        return args -> {
            List<Flat> sampleFlats = List.of(
                new Flat("A-101", "Anita Sharma"),
                new Flat("A-102", "Rohan Mehta"),
                new Flat("A-103", "Priya Nair"),
                new Flat("A-104", "Vikram Rao"),
                new Flat("A-105", "Kavya Iyer"),
                new Flat("B-101", "Arjun Kapoor"),
                new Flat("B-102", "Neha Joshi"),
                new Flat("B-103", "Sanjay Menon"),
                new Flat("B-104", "Divya Shah"),
                new Flat("B-105", "Manish Verma"),
                new Flat("C-101", "Ishita Das"),
                new Flat("C-102", "Rahul Bhat"),
                new Flat("C-103", "Sneha Kulkarni"),
                new Flat("C-104", "Aditya Singh"),
                new Flat("C-105", "Pooja Nambiar"),
                new Flat("D-101", "Nitin Malhotra"),
                new Flat("D-102", "Simran Kaur"),
                new Flat("D-103", "Amit Chawla"),
                new Flat("D-104", "Lata Deshmukh"),
                new Flat("D-105", "Varun Pillai")
            );

            for (Flat flat : sampleFlats) {
                if (!flats.existsByFlatNumberIgnoreCase(flat.getFlatNumber())) {
                    flats.save(flat);
                }
            }
        };
    }

    @Bean
    public org.springframework.boot.CommandLineRunner seedParkingSlots(ParkingSlotRepository slots) {
        return args -> List.of(
                "01", "02", "03", "04", "05", "06", "07", "08",
                "09", "10", "11", "12", "13", "14", "15"
        ).forEach(slotNumber -> {
            if (!slots.existsBySlotNumberIgnoreCase(slotNumber)) {
                slots.save(new ParkingSlot(slotNumber));
            }
        });
    @Bean
    public org.springframework.web.servlet.config.annotation.WebMvcConfigurer corsConfigurer() {
        return new org.springframework.web.servlet.config.annotation.WebMvcConfigurer() {
            @Override
            public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
                registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("*").allowedHeaders("*");
            }
        };
    }

}
