package com.medicontrol.dto;

public class LoginResponse {
    public boolean ok;
    public String mensaje;
    public Long usuarioId;
    public String nombres;
    public String rol;

    public LoginResponse(boolean ok, String mensaje, Long usuarioId, String nombres, String rol) {
        this.ok = ok; this.mensaje = mensaje; this.usuarioId = usuarioId; this.nombres = nombres; this.rol = rol;
    }
}
