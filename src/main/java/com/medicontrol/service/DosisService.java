package com.medicontrol.service;

import com.medicontrol.model.*;
import com.medicontrol.repository.MedicamentoRepository;
import com.medicontrol.repository.RegistroDosisRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DosisService {
    private final MedicamentoRepository medicamentoRepository;
    private final RegistroDosisRepository registroDosisRepository;

    public DosisService(MedicamentoRepository medicamentoRepository, RegistroDosisRepository registroDosisRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.registroDosisRepository = registroDosisRepository;
    }

    public void generarDosisDelDia() {
        LocalDate hoy = LocalDate.now();
        List<Medicamento> activos = medicamentoRepository.findByEstado(EstadoMedicamento.ACTIVO);
        for (Medicamento m : activos) {
            boolean enRango = (m.getFechaInicio() == null || !hoy.isBefore(m.getFechaInicio()))
                    && (m.getFechaFin() == null || !hoy.isAfter(m.getFechaFin()));
            if (enRango && registroDosisRepository.findByMedicamentoIdAndFecha(m.getId(), hoy).isEmpty()) {
                RegistroDosis r = new RegistroDosis();
                r.setMedicamento(m);
                r.setFecha(hoy);
                r.setHoraProgramada(m.getHoraToma());
                r.setEstado(EstadoDosis.PENDIENTE);
                registroDosisRepository.save(r);
            }
        }
    }

    public List<RegistroDosis> listarHoy() {
        generarDosisDelDia();
        return registroDosisRepository.findByFechaOrderByHoraProgramadaAsc(LocalDate.now());
    }

    public RegistroDosis cambiarEstado(Long id, EstadoDosis estado) {
        RegistroDosis r = registroDosisRepository.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        r.setEstado(estado);
        r.setMarcadoEn(LocalDateTime.now());
        return registroDosisRepository.save(r);
    }
}
