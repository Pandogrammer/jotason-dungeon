package com.farguito.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farguito.Response;
import com.farguito.personajes.Jugador;

@RestController
@Scope("session")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class Partida {
	
	//public static String URL = "http://25.87.195.123:8080";
	public static String URL = "https://jotason-dungeon.herokuapp.com";
    
	@Autowired
	Jugador pj;
	
	@Autowired
	Combate combate;
	
	@Autowired
	Mapa mapa;
	
	@GetMapping("info")
	public Response info() {
		Response response = new Response();
		response.put("saludo", "Hola ! Bienvenido a JOTASON DUNGEON :D !");
		Map<String, String> personajes = new LinkedHashMap<>();
			personajes.put("jugador", " @ : este sos vos! a las ratas las matas de una, pero cuando las atacas te muerden.");
			personajes.put("rata", " r : estos son malos, pegan por 1, guarda que son astutas.");
		Map<String, String> terreno = new LinkedHashMap<>();
			terreno.put("piso", " · : piso vacío, se puede caminar por aca");
			terreno.put("pared", " # : no se pueden traspasar");
			terreno.put("escalera", " ¬ : este es tu objetivo");
		    terreno.put("niebla", " ~ : todo lo que este fuera del rango de visión");

		Map<String, Map<String, String>> iconos = new LinkedHashMap<>();
			iconos.put("personajes", personajes);
			iconos.put("terreno", terreno);
		
		response.put("iconos",iconos);
		
		response.put("jugar", URL+" - te recomiendo abrirla en otra tab (asi podes leer esto)");
		
		return response;
		
	}
	
	@GetMapping
	public Response get(
			@RequestParam(value = "accion", required = false) String accion,
			@RequestParam(value = "direccion", required = false) String direccion){
		Response response = new Response();
		if(accion != null) {
			Response accionResponse;
			switch(accion) {
				case "mover" : {
					response.agregarMensaje(mapa.mover(direccion));
					} break;
				case "atacar" : {
					response.agregarMensaje(combate.atacar(direccion));
					} break;					
			}
			response.agregarMensaje(mapa.chequearVidas());
			response.agregarMensaje(mapa.updateEnemigos());
			response.agregarMensaje(mapa.chequearVidas());
		}
		response.put("nivel", mapa.getNivel()+1);
		response.put("vida", pj.getVidaActual());
		response.put("mapa", mapa.mostrar());
		Map<String, Map> acciones = new LinkedHashMap<>();
			acciones.put("mover" , mapa.movimiento());
			acciones.put("atacar", combate.ataque());
		response.put("acciones", acciones);
		return response;
	}
}
