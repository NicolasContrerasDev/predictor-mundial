package com.example.predictor_mundial.repository;

import com.example.predictor_mundial.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {
    
    List<Partido> findByEquipoLocalAndEquipoVisita(String equipoLocal, String equipoVisita);

}