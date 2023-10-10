package com.example.proyecto_gio;

public class Respuesta {
    private int id;
    private boolean correcto;

    public Respuesta(int id, boolean correcto) {
        this.id = id;
        this.correcto = correcto;
    }

    public int getId() {
        return id;
    }

    public boolean isCorrecto() {
        return correcto;
    }
}
