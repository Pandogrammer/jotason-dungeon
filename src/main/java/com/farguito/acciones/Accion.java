package com.farguito.acciones;

import com.farguito.Respuesta;
import com.farguito.personajes.Personaje;

public abstract class Accion {
	
	Personaje actor; 
	int pre;
	int post;
	
	public abstract Respuesta ejecutar();
	
	public Personaje getActor() {
		return actor;
	}
	public void setActor(Personaje actor) {
		this.actor = actor;
	}

	
	
	
}
