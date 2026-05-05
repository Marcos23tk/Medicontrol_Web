package com.medicontrol.repository;

import com.medicontrol.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDniContainingIgnoreCase(String nombres, String apellidos, String dni);
}
