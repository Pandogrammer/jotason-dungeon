package com.farguito.personajes;

public abstract class Enemigo extends Personaje {

	protected Personaje objetivo;
	protected String nombre;

	public Personaje getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Personaje objetivo) {
		this.objetivo = objetivo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
}
