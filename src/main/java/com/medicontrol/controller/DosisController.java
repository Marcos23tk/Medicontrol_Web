package com.medicontrol.controller;

import com.medicontrol.model.EstadoDosis;
import com.medicontrol.model.RegistroDosis;
import com.medicontrol.repository.RegistroDosisRepository;
import com.medicontrol.service.DosisService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dosis")
public class DosisController {
    private final DosisService dosisService;
    private final RegistroDosisRepository registroDosisRepository;
    public DosisController(DosisService dosisService, RegistroDosisRepository registroDosisRepository) {
        this.dosisService = dosisService; this.registroDosisRepository = registroDosisRepository;
    }
    @GetMapping("/hoy")
    public List<RegistroDosis> hoy() { return dosisService.listarHoy(); }
    @GetMapping("/historial")
    public List<RegistroDosis> historial() { return registroDosisRepository.findTop20ByOrderByFechaDescHoraProgramadaDesc(); }
    @PostMapping("/{id}/tomado")
    public RegistroDosis tomado(@PathVariable Long id) { return dosisService.cambiarEstado(id, EstadoDosis.TOMADO); }
    @PostMapping("/{id}/omitido")
    public RegistroDosis omitido(@PathVariable Long id) { return dosisService.cambiarEstado(id, EstadoDosis.OMITIDO); }
    @PostMapping("/{id}/pendiente")
    public RegistroDosis pendiente(@PathVariable Long id) { return dosisService.cambiarEstado(id, EstadoDosis.PENDIENTE); }
}
