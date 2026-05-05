package com.medicontrol.repository;

import com.medicontrol.model.EstadoDosis;
import com.medicontrol.model.RegistroDosis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroDosisRepository extends JpaRepository<RegistroDosis, Long> {
    List<RegistroDosis> findByFechaOrderByHoraProgramadaAsc(LocalDate fecha);
    Optional<RegistroDosis> findByMedicamentoIdAndFecha(Long medicamentoId, LocalDate fecha);
    long countByFechaAndEstado(LocalDate fecha, EstadoDosis estado);
    long countByFecha(LocalDate fecha);
    List<RegistroDosis> findTop20ByOrderByFechaDescHoraProgramadaDesc();
    void deleteByMedicamentoId(Long medicamentoId);
}