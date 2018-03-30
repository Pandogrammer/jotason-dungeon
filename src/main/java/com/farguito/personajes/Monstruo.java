package com.farguito.personajes;

public abstract class Monstruo extends Personaje {

	protected Personaje objetivo;

	public Personaje getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Personaje objetivo) {
		this.objetivo = objetivo;
	}


	
}
