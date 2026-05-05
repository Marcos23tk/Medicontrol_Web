package com.medicontrol.service;

import com.medicontrol.dto.ReporteResumen;
import com.medicontrol.model.EstadoDosis;
import com.medicontrol.model.EstadoMedicamento;
import com.medicontrol.repository.MedicamentoRepository;
import com.medicontrol.repository.PacienteRepository;
import com.medicontrol.repository.RegistroDosisRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ReporteService {
    private final PacienteRepository pacienteRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final RegistroDosisRepository registroDosisRepository;
    private final DosisService dosisService;

    public ReporteService(PacienteRepository pacienteRepository, MedicamentoRepository medicamentoRepository, RegistroDosisRepository registroDosisRepository, DosisService dosisService) {
        this.pacienteRepository = pacienteRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.registroDosisRepository = registroDosisRepository;
        this.dosisService = dosisService;
    }

    public ReporteResumen resumen() {
        dosisService.generarDosisDelDia();
        LocalDate hoy = LocalDate.now();
        ReporteResumen r = new ReporteResumen();
        r.pacientes = pacienteRepository.count();
        r.medicamentosActivos = medicamentoRepository.findByEstado(EstadoMedicamento.ACTIVO).size();
        r.dosisProgramadasHoy = registroDosisRepository.countByFecha(hoy);
        r.dosisTomadasHoy = registroDosisRepository.countByFechaAndEstado(hoy, EstadoDosis.TOMADO);
        r.dosisOmitidasHoy = registroDosisRepository.countByFechaAndEstado(hoy, EstadoDosis.OMITIDO);
        r.dosisPendientesHoy = registroDosisRepository.countByFechaAndEstado(hoy, EstadoDosis.PENDIENTE);
        r.cumplimientoHoy = r.dosisProgramadasHoy == 0 ? 0 : Math.round((float) r.dosisTomadasHoy * 100 / r.dosisProgramadasHoy);
        return r;
    }
}
