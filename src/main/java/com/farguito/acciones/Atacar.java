package com.farguito.acciones;

import java.awt.Point;

import com.farguito.Respuesta;
import com.farguito.personajes.Jugador;
import com.farguito.personajes.Personaje;

public class Atacar extends Accion{
	
	int rango;
	Personaje objetivo;
	
	public Atacar(int rango, Personaje objetivo) {
		super();
		this.pre = 25;
		this.post = 150;
		this.rango = rango;
		this.objetivo = objetivo;
	}

	public Respuesta ejecutar() {
		Respuesta respuesta = new Respuesta();
		
		if(actor.puedeActuar()) {
			Point origen = new Point(actor.getX(), actor.getY());
			Point destino = new Point(objetivo.getX(), objetivo.getY());
			if(origen.distance(destino) <= rango) {
				int daño = actor.getAtaque();
				objetivo.recibirDaño(daño);
				if (objetivo instanceof Jugador){
					((Jugador) objetivo).agregarMensaje(actor.getNombre()+" te atacó ("+daño+")");
				}
				respuesta.agregarMensaje("atacaste a "+objetivo.getNombre()+" ("+daño+")");
				actor.agregarCansancio(this.post);
				respuesta.exito();
				
				if(objetivo.estaMuerto()) {
					if (objetivo instanceof Jugador){
						((Jugador) objetivo).agregarMensaje(actor.getNombre()+" te mató :c");
					}

					respuesta.agregarMensaje("mataste a "+objetivo.getNombre());
				}					
			} else {
				respuesta.agregarMensaje("el objetivo esta muy lejos");
			}
		}
		
		return respuesta;
					
	}

}
