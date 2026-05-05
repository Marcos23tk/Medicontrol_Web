package com.medicontrol.controller;

import com.medicontrol.dto.MedicamentoRequest;
import com.medicontrol.model.*;
import com.medicontrol.repository.MedicamentoRepository;
import com.medicontrol.repository.PacienteRepository;
import com.medicontrol.repository.RegistroDosisRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {
    private final MedicamentoRepository medicamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final RegistroDosisRepository registroDosisRepository;

    public MedicamentoController(MedicamentoRepository medicamentoRepository, PacienteRepository pacienteRepository, RegistroDosisRepository registroDosisRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.registroDosisRepository = registroDosisRepository;
    }

    @GetMapping
    public List<Medicamento> listar(@RequestParam(required = false) Long pacienteId) {
        if (pacienteId != null) return medicamentoRepository.findByPacienteId(pacienteId);
        return medicamentoRepository.findAll();
    }

    @PostMapping
    public Medicamento guardar(@RequestBody MedicamentoRequest req) {
        Medicamento m = new Medicamento();
        aplicarDatos(m, req);
        return medicamentoRepository.save(m);
    }

    @PutMapping("/{id}")
    public Medicamento actualizar(@PathVariable Long id, @RequestBody MedicamentoRequest req) {
        Medicamento m = medicamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
        aplicarDatos(m, req);
        return medicamentoRepository.save(m);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        registroDosisRepository.deleteByMedicamentoId(id);
        medicamentoRepository.deleteById(id);
    }

    private void aplicarDatos(Medicamento m, MedicamentoRequest req) {
        Paciente p = pacienteRepository.findById(req.pacienteId).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        m.setPaciente(p); m.setNombre(req.nombre); m.setDosis(req.dosis); m.setPresentacion(req.presentacion);
        m.setFrecuencia(req.frecuencia); m.setHoraToma(LocalTime.parse(req.horaToma));
        m.setFechaInicio(req.fechaInicio == null || req.fechaInicio.isBlank() ? LocalDate.now() : LocalDate.parse(req.fechaInicio));
        m.setFechaFin(req.fechaFin == null || req.fechaFin.isBlank() ? null : LocalDate.parse(req.fechaFin));
        m.setIndicaciones(req.indicaciones);
        m.setEstado(req.estado == null || req.estado.isBlank() ? EstadoMedicamento.ACTIVO : EstadoMedicamento.valueOf(req.estado));
    }
}