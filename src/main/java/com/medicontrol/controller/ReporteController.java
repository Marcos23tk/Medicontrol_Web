package com.medicontrol.controller;

import com.medicontrol.dto.ReporteResumen;
import com.medicontrol.service.ReporteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final ReporteService reporteService;
    public ReporteController(ReporteService reporteService) { this.reporteService = reporteService; }
    @GetMapping("/resumen")
    public ReporteResumen resumen() { return reporteService.resumen(); }
}
