package com.example.proyecto_gio;

import java.util.List;

public class Pregunta {
    private String pregunta;
    private String respuestaSeleccionada;
    private List<String> respuestas;
    private int indiceCorrecto;

    private String message;
    private boolean visible;

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuestaSeleccionada() {
        return respuestaSeleccionada;
    }

    public List<String> getRespuestas() {
        return respuestas;
    }

    public String getMessage() {
        return message;
    }

    public int getIndiceCorrecto() {
        return indiceCorrecto;
    }

    public boolean isVisible() {
        return visible;
    }
}
