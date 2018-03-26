package com.farguito.personajes;

public class Rata extends Enemigo {

	public Rata() {
		super();
		this.nombre = "rata";
		this.vision = 1;
		this.icono = "r";
		this.vidaTotal = 1;
		this.vidaActual = vidaTotal;
		this.daño = 1;
	}
	
}
