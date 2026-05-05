package com.medicontrol.controller;

import com.medicontrol.dto.LoginRequest;
import com.medicontrol.dto.LoginResponse;
import com.medicontrol.model.Rol;
import com.medicontrol.model.Usuario;
import com.medicontrol.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    public AuthController(UsuarioRepository usuarioRepository) { this.usuarioRepository = usuarioRepository; }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return usuarioRepository.findByCorreoAndPassword(request.correo, request.password)
                .map(u -> ResponseEntity.ok(new LoginResponse(true, "Acceso correcto", u.getId(), u.getNombres(), u.getRol().name())))
                .orElse(ResponseEntity.status(401).body(new LoginResponse(false, "Credenciales incorrectas", null, null, null)));
    }

    @PostMapping("/seed-admin")
    public Usuario seedAdmin() {
        if (!usuarioRepository.existsByCorreo("admin@medicontrol.com")) {
            Usuario u = new Usuario();
            u.setNombres("Administrador Medicontrol");
            u.setCorreo("admin@medicontrol.com");
            u.setPassword("123456");
            u.setRol(Rol.ADMIN);
            return usuarioRepository.save(u);
        }
        return usuarioRepository.findByCorreoAndPassword("admin@medicontrol.com", "123456").orElse(null);
    }
}
