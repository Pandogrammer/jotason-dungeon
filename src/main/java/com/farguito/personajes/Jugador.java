package com.farguito.personajes;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class Jugador extends Personaje {
	
	public Jugador() {
		super();
	}
	
	@PostConstruct
	private void init() {
		this.vision = 2;
		this.icono = "@";
		this.vidaTotal = 5;
		this.vidaActual = vidaTotal;
		this.daño = 1;
	}
	
}
