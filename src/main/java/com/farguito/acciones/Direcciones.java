package com.farguito.acciones;

public enum Direcciones {
	ARRIBA("arriba"),
	ABAJO("abajo"),
	IZQUIERDA("izquierda"),
	DERECHA("derecha");	
	

    private final String descripcion;

    private Direcciones(String value) {
        descripcion = value;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}

