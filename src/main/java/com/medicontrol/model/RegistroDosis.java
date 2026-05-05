package com.medicontrol.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "registro_dosis")
public class RegistroDosis {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;
    private LocalDate fecha;
    private LocalTime horaProgramada;
    @Enumerated(EnumType.STRING)
    private EstadoDosis estado = EstadoDosis.PENDIENTE;
    private LocalDateTime marcadoEn;
    private String observacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraProgramada() { return horaProgramada; }
    public void setHoraProgramada(LocalTime horaProgramada) { this.horaProgramada = horaProgramada; }
    public EstadoDosis getEstado() { return estado; }
    public void setEstado(EstadoDosis estado) { this.estado = estado; }
    public LocalDateTime getMarcadoEn() { return marcadoEn; }
    public void setMarcadoEn(LocalDateTime marcadoEn) { this.marcadoEn = marcadoEn; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
