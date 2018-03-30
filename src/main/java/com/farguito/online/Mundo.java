package com.farguito.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;

import com.farguito.JotasonDungeonApplication;
import com.farguito.Respuesta;
import com.farguito.nivel.Nivel;
import com.farguito.personajes.Jugador;
import com.farguito.personajes.Monstruo;
import com.farguito.personajes.Personaje;
import com.farguito.terreno.Escalera;

@RestController
@ApplicationScope
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class Mundo extends Thread {
	
	int updatesPorSegundo = 100; //100
	boolean prendido = false;
	int cantidadNiveles = 9;
	
	private Map<Integer, Jugador> jugadores = new HashMap<>();
	private List<Nivel> niveles = new ArrayList<>();

	public Map<Integer, Jugador> getJugadores() {
		return jugadores;
	}

	public void agregarNuevoJugador(Jugador pj) {
		Integer id = jugadores.size();
		pj.setId(id);
		niveles.get(0).invocarJugador(pj);
		jugadores.put(id, pj);
	}
	
	@PostConstruct
	private void iniciar() {
		prendido = true;
		for(int i = 0; i < cantidadNiveles; i++) {
			Nivel nivel = new Nivel(i);
			niveles.add(nivel);
		}
		
		for(int i = 0; i < cantidadNiveles; i++) {
			if(i < cantidadNiveles-1) {
				Escalera escalera = niveles.get(i).generarEscalera(niveles.get(i), niveles.get(i+1));
				niveles.get(i+1).generarEscalera(escalera.getX(), escalera.getY(), niveles.get(i));
			}
		}
		this.start();
	}
	
	
	public void run() {
		try {
			while(prendido){
				actualizar();
				Thread.sleep(updatesPorSegundo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void actualizar() {
		try {
			List<Jugador> pjsMuertos = new ArrayList<>();
			jugadores.values().forEach(pj -> {
				if(pj.estaMuerto()) {
					pjsMuertos.add(pj);
				} else {
					pj.actualizar(getNivel(pj.getZ()));
				}
			});
			for(Jugador pj : pjsMuertos) {
				eliminar(pj);
				jugadores.remove(pj.getId());
			}

			List<Monstruo> monstruosMuertos = new ArrayList<>();
			for(Nivel n : niveles) {
				for(Monstruo m : n.getMonstruos()) {
					if(m.estaMuerto())
						monstruosMuertos.add(m);
					else
						m.actualizar(getNivel(m.getZ()));
				//de momento hacerlo asi, pero despues hay que cambiar esta pobreza
				//le paso el nivel para que tenga todo lo necesario para manejarse
				}				
			}
			for(Monstruo m : monstruosMuertos) {
				eliminar(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void eliminar(Personaje pj) {
		//pj.droppear();
		getNivel(pj.getZ()).getTerreno()[pj.getX()][pj.getY()].setPersonaje(null);
		pj = null;
	}

	public Nivel getNivel(int z) {
		return niveles.get(z);
	}
	
	
	
	
	
	

	@GetMapping
	public Respuesta info() {
		Respuesta respuesta = new Respuesta();
		respuesta.put("saludo", "Hola ! Bienvenido a JOTASON DUNGEON :D !");
		Map<String, String> personajes = new LinkedHashMap<>();
			personajes.put("jugador", " @ : este sos vos (u otros jugadores)");
			personajes.put("rata", " r : estos son malos, pegan por 1, guarda que son astutas.");
		Map<String, String> terreno = new LinkedHashMap<>();
			terreno.put("piso", " · : piso vacío, se puede caminar por aca");
			terreno.put("pared", " # : no se pueden traspasar");
			terreno.put("escalera", " ¬ : este es tu objetivo");
		    terreno.put("niebla", " ~ : todo lo que esté fuera del rango de visión");

		Map<String, Map<String, String>> iconos = new LinkedHashMap<>();
			iconos.put("personajes", personajes);
			iconos.put("terreno", terreno);
		
		respuesta.put("iconos",iconos);

		respuesta.agregar("link", JotasonDungeonApplication.URL+"/partida/iniciar?nombre=TU_NOMBRE");
		respuesta.put("recomendacion", "te recomiendo abrirla en otra tab (asi podes leer esto)");
		
		return respuesta;
		
	}
	
	@GetMapping("online")
	public Respuesta jugadoresOnline() {
		Respuesta respuesta = new Respuesta();
		Respuesta online = new Respuesta();
		for(int i = 0; i < jugadores.size(); i++) {
			online.agregar(String.valueOf(i), jugadores.get(i).getNombre());
		}
		respuesta.agregar("online", online);
		return respuesta;
		
	}

}
