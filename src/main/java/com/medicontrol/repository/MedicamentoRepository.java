package com.medicontrol.repository;

import com.medicontrol.model.EstadoMedicamento;
import com.medicontrol.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    List<Medicamento> findByEstado(EstadoMedicamento estado);
    List<Medicamento> findByPacienteId(Long pacienteId);
}
