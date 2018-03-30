package com.farguito.terreno;

import com.farguito.Respuesta;
import com.farguito.nivel.Nivel;
import com.farguito.personajes.Personaje;
import com.farguito.utils.Activable;

public class Escalera extends Terreno implements Activable {
	
	Nivel origen;
	Nivel destino;
	
	public Escalera(int x, int y) {
		super(x, y);
		this.icono = "¬";
		this.traspasable = true;
	}

	@Override
	public Respuesta activar() {
		Respuesta respuesta = new Respuesta();
		Personaje pj = origen.getTerreno()[x][y].getPersonaje();
		origen.getTerreno()[x][y].setPersonaje(null);
		destino.getTerreno()[x][y].setPersonaje(pj);
		pj.setZ(destino.getPiso());
		
		if(origen.getPiso() < destino.getPiso())
			respuesta.agregarMensaje("subiste la escalera");
		else 
			respuesta.agregarMensaje("bajaste la escalera");
		
		return respuesta;
	}

	public Nivel getOrigen() {
		return origen;
	}

	public void setOrigen(Nivel origen) {
		this.origen = origen;
	}

	public Nivel getDestino() {
		return destino;
	}

	public void setDestino(Nivel destino) {
		this.destino = destino;
	}
	
	

}
