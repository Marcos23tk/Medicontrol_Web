package com.medicontrol.controller;

import com.medicontrol.model.Paciente;
import com.medicontrol.repository.PacienteRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    private final PacienteRepository repository;
    public PacienteController(PacienteRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Paciente> listar(@RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) return repository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDniContainingIgnoreCase(q, q, q);
        return repository.findAll();
    }

    @PostMapping
    public Paciente guardar(@RequestBody Paciente paciente) { return repository.save(paciente); }

    @PutMapping("/{id}")
    public Paciente actualizar(@PathVariable Long id, @RequestBody Paciente datos) {
        Paciente p = repository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        p.setDni(datos.getDni()); p.setNombres(datos.getNombres()); p.setApellidos(datos.getApellidos());
        p.setEdad(datos.getEdad()); p.setTelefono(datos.getTelefono()); p.setDireccion(datos.getDireccion());
        p.setContactoEmergencia(datos.getContactoEmergencia()); p.setObservaciones(datos.getObservaciones());
        return repository.save(p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { repository.deleteById(id); }
}
