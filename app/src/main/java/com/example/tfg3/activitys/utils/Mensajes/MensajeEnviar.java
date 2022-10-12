package com.example.tfg3.activitys.utils.Mensajes;

import com.example.tfg3.activitys.utils.Mensajes.Mensaje;

import java.util.Map;

public class MensajeEnviar extends Mensaje {
    private Map hora;

    public MensajeEnviar() {
    }

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String nombre, String mensaje, String type_mensaje, Map hora) {
        super(nombre, mensaje, type_mensaje);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
