package com.farguito.personajes;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.farguito.Respuesta;
import com.farguito.nivel.Nivel;
import com.farguito.online.Partida;

@Component
@Scope("session")
public class Jugador extends Personaje {
	
	private Partida partida;
	
	private Integer id;
	
	public Jugador() {
		super();
	}
	
	@PostConstruct
	public void init() {
		this.vision = 3;
		this.icono = "@";
		this.vidaTotal = 5;
		this.vidaActual = vidaTotal;
		this.ataque = 1;
		this.recuperacion = 10;
		this.rango = 1;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public void actualizar(Nivel nivel) {
		if(!puedeActuar())
			descansar();	
	}

	//pasamanos del mal
	public void agregarMensaje(String mensaje) {
		partida.agregarMensaje(mensaje);
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public Respuesta mostrar() {
		Respuesta respuesta = new Respuesta();
		respuesta.agregar("nombre", nombre);
		respuesta.agregar("vida", vidaActual+"/"+vidaTotal);

			Respuesta combate = new Respuesta();
			combate.agregar("daño", ataque);
			combate.agregar("rango", rango);
		respuesta.agregar("ataque", combate);
				
		respuesta.agregar("vision", vision);
		
		return respuesta;			
	}


	
}
