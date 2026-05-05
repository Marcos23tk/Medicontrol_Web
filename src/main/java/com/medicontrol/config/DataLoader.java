package com.medicontrol.config;

import com.medicontrol.model.*;
import com.medicontrol.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initData(UsuarioRepository usuarios, PacienteRepository pacientes, MedicamentoRepository medicamentos) {
        return args -> {
            if (!usuarios.existsByCorreo("admin@medicontrol.com")) {
                Usuario u = new Usuario();
                u.setNombres("Administrador Medicontrol");
                u.setCorreo("admin@medicontrol.com");
                u.setPassword("123456");
                u.setRol(Rol.ADMIN);
                usuarios.save(u);
            }
            if (pacientes.count() == 0) {
                Paciente p = new Paciente();
                p.setDni("70000001"); p.setNombres("María Elena"); p.setApellidos("Quispe Rojas");
                p.setEdad(67); p.setTelefono("999888777"); p.setDireccion("Ayacucho");
                p.setContactoEmergencia("Hijo: 988777666"); p.setObservaciones("Paciente con tratamiento diario");
                pacientes.save(p);

                Medicamento m1 = new Medicamento();
                m1.setPaciente(p); m1.setNombre("Losartán"); m1.setDosis("50 mg"); m1.setPresentacion("Tableta");
                m1.setFrecuencia("Diario"); m1.setHoraToma(LocalTime.of(7,0));
                m1.setFechaInicio(LocalDate.now().minusDays(5)); m1.setFechaFin(LocalDate.now().plusDays(30));
                m1.setIndicaciones("Tomar después del desayuno"); m1.setEstado(EstadoMedicamento.ACTIVO);
                medicamentos.save(m1);

                Medicamento m2 = new Medicamento();
                m2.setPaciente(p); m2.setNombre("Metformina"); m2.setDosis("850 mg"); m2.setPresentacion("Tableta");
                m2.setFrecuencia("Cada 12 horas"); m2.setHoraToma(LocalTime.of(20,0));
                m2.setFechaInicio(LocalDate.now().minusDays(3)); m2.setFechaFin(LocalDate.now().plusDays(20));
                m2.setIndicaciones("Tomar con alimentos"); m2.setEstado(EstadoMedicamento.ACTIVO);
                medicamentos.save(m2);
            }
        };
    }
}
