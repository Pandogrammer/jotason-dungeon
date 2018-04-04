package com.farguito.online;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.farguito.JotasonDungeonApplication;
import com.farguito.Respuesta;
import com.farguito.acciones.Accion;
import com.farguito.acciones.Atacar;
import com.farguito.acciones.Direcciones;
import com.farguito.acciones.Mover;
import com.farguito.personajes.Jugador;
import com.farguito.personajes.Personaje;
import com.farguito.terreno.Terreno;

@RestController
@Scope("session")
@RequestMapping("partida")
public class Partida {
	
	static String URL_PARTIDA = JotasonDungeonApplication.URL+"/partida";
	
	@Autowired
	Mundo mundo;
	
	@Autowired
	Jugador pj;
	
	@Autowired
	Mapa mapa;
	
	Respuesta mensajes = new Respuesta();
	Map<String, Personaje> objetivosPosibles = new HashMap<>();
	
	List<Respuesta> menuPrincipal = new ArrayList<>(); 
	Map<String, Respuesta> menuesPosibles = new LinkedHashMap<>();

	private boolean jugando = false;
		
	@GetMapping("iniciar")
	public Respuesta init(@RequestParam(value = "nombre", required = false) String nombre) {
		Respuesta respuesta = new Respuesta();	
		if(pj.estaMuerto()) {
			jugando = false;
			pj.init();			
		}
		
		if(jugando) {
			respuesta.agregarMensaje("ya estas jugando");
			respuesta.agregar("actualizar", URL_PARTIDA);
		} else if (nombre == null || nombre.equalsIgnoreCase("TU_NOMBRE")) {
			respuesta.agregarMensaje("escribí tu nombre en la URL de la siguiente forma:");
			respuesta.agregar("link", URL_PARTIDA+"/iniciar?nombre=TU_NOMBRE");
		} else {
			pj.setPartida(this);
			mundo.agregarNuevoJugador(pj);
			pj.setNombre(nombre);
			jugando = true;
			initMenu();
			menuPrincipal.addAll(menuesPosibles.values());
			respuesta = menuPrincipal();
			respuesta.agregarMensaje("Bienvenido, "+nombre+", al mundo de JOTASON!");			
		}
		return respuesta;
	}

	
	@GetMapping
	public Respuesta menuPrincipal() {	
		Respuesta respuesta = new Respuesta();
		
		if(jugando) {
			objetivosPosibles.clear();
			/* fixear esto
			menuPrincipal.stream().forEach(menu -> {
				respuesta.agregar(menu);
			});
			*/
			respuesta.agregar("piso", pj.getZ());
			respuesta.agregar("vida", pj.mostrar().get("vida"));
			respuesta.agregar("mapa", mapa.mostrar());
			respuesta.agregar("actualizar", URL_PARTIDA);
			if(pj.estaMuerto())
				respuesta.agregar("reiniciar", URL_PARTIDA+"/iniciar?nombre="+pj.getNombre());
			respuesta.agregar("mover", movimiento());
			respuesta.agregar("atacar", ataque());
			
			respuesta.agregarMensaje(mensajes);
			
			mensajes.clear();
		} else {
			respuesta.agregarMensaje("no estás jugando, andá a");
			respuesta.agregar("link", URL_PARTIDA+"/iniciar?nombre=TU_NOMBRE");
		}
		
		return respuesta;
	}
	
	@GetMapping("personaje")
	public Respuesta personaje() {
		Respuesta respuesta = new Respuesta();
		respuesta.agregar("informacion", pj.mostrar());
		return  respuesta;
	}
	
	
	private void initMenu() {
		menuesPosibles.put("piso", new Respuesta("piso", pj.getZ()));
		menuesPosibles.put("mapa", new Respuesta("mapa", mapa.mostrar()));
		menuesPosibles.put("actualizar", new Respuesta("actualizar", URL_PARTIDA));
		menuesPosibles.put("mover", new Respuesta("mover", movimiento()));
		menuesPosibles.put("atacar", new Respuesta("atacar", ataque()));
		menuesPosibles.put("personaje", new Respuesta("personaje", pj.mostrar()));
		menuesPosibles.put("mensajes", new Respuesta("mensajes", mensajes));
	}
	
	@GetMapping("config")
	public Respuesta configuracion(
			@RequestParam(value = "menu", required = false) String menu,
			@RequestParam(value = "lugar", required = false) String lugar,
			@RequestParam(value = "borrar", required = false) String borrar) {

		Respuesta respuesta = new Respuesta();
		
		if(borrar != null) {
			menuPrincipal.clear();
			respuesta.agregar("accion", "borraste el orden actual");
		}


		if(menu != null && lugar != null) {
			try {
				int posicion = Integer.valueOf(lugar);
				if(menuesPosibles.containsKey(menu)) {
					if(!menuPrincipal.isEmpty())
						menuPrincipal.add(posicion, menuesPosibles.get(menu));
					else
						menuPrincipal.add(menuesPosibles.get(menu));
				} else {
					respuesta.agregar("error", "el menu que ingresaste no existe");		
				}
			} catch (Exception e) {
				respuesta.agregar("error", "escribiste algo mal en la URL (el numero?)");				
			}
		} 
		
		
		Respuesta orden = new Respuesta();
		for(int i = 0; i < menuPrincipal.size(); i++) {
			orden.agregar(String.valueOf(i), menuPrincipal.get(i).keySet().iterator().next());
		}
		respuesta.agregar("orden", orden);
		
		
		Respuesta menues = new Respuesta();
		int i = 0;
		for(String m : menuesPosibles.keySet()) {
			menues.agregar(String.valueOf(i), m);
			i++;
		}
		respuesta.agregar("menues", menues);
		

		respuesta.agregarMensaje("podes modificar el orden de lo que aparece en la pantalla principal");
		respuesta.agregarMensaje(URL_PARTIDA+"/config?menu=NOMBRE&lugar=NUMERO");	
		respuesta.agregarMensaje("tambien podes eliminar el orden actual ingresando a");
		respuesta.agregarMensaje(URL_PARTIDA+"/config?borrar=si");
		
		return respuesta;
		
	}
	

	@GetMapping("mover")
	public Respuesta mover(	@RequestParam(value = "direccion", required = false)
							String direccion ) {
		Accion accion;
		switch(direccion) {
			case "arriba" : accion = new Mover(mundo.getNivel(pj.getZ()).getTerreno(), Direcciones.ARRIBA); break;
			case "abajo" : accion = new Mover(mundo.getNivel(pj.getZ()).getTerreno(), Direcciones.ABAJO); break;
			case "derecha" : accion = new Mover(mundo.getNivel(pj.getZ()).getTerreno(), Direcciones.DERECHA); break;
			case "izquierda" : accion = new Mover(mundo.getNivel(pj.getZ()).getTerreno(), Direcciones.IZQUIERDA); break;
			default : mensajes.agregarMensaje("direccion incorrecta"); return menuPrincipal();
		}
		accion.setActor(pj);
		mensajes.agregarMensaje(accion.ejecutar());
		
		return menuPrincipal();
	}

	@GetMapping("atacar")
	public Respuesta atacar( @RequestParam(value = "objetivo", required = false)
							String objetivo ) throws InterruptedException {
		Accion accion;
		if(objetivosPosibles.containsKey(objetivo)) {
			accion = new Atacar(pj.getRango(), objetivosPosibles.get(objetivo));
			accion.setActor(pj);
			mensajes.agregarMensaje(accion.ejecutar());			
		} else {
			mensajes.agregarMensaje("el objetivo es incorrecto");
		}
		return menuPrincipal();
	}
		

	private Map<String, String> movimiento(){
		if(!pj.estaMuerto()) {
			Map<String, String> retorno = new LinkedHashMap<>();
			retorno.put("arriba",   URL_PARTIDA+"/mover?direccion=arriba");
			retorno.put("izquierda",URL_PARTIDA+"/mover?direccion=izquierda");
			retorno.put("derecha",  URL_PARTIDA+"/mover?direccion=derecha");
			retorno.put("abajo",    URL_PARTIDA+"/mover?direccion=abajo");
			
			return retorno;
		} else return null;
	}	
	
	private Map<String, String> ataque(){
		if(!pj.estaMuerto()) {
			Map<String, String> retorno = new LinkedHashMap<>();
			Terreno[][] terreno = mundo.getNivel(pj.getZ()).getTerreno();
			
			for(int i = pj.getX()-pj.getRango(); i <= pj.getX()+pj.getRango(); i++) {
				for(int j = pj.getY()-pj.getRango(); j <= pj.getY()+pj.getRango(); j++) {
					Point origen = new Point(pj.getX(), pj.getY());
					Point destino = new Point(i, j);
					if( origen.distance(destino) > 0  //asi no me autotargetea
					&&  origen.distance(destino) <= pj.getRango()) { 
						try{
							if(terreno[i][j].estaOcupado()) {
								String nombreCodigo = terreno[i][j].getPersonaje().getNombre();
								if(objetivosPosibles.containsKey(nombreCodigo)) {
									nombreCodigo = nombreCodigo+"_1";
									while(objetivosPosibles.containsKey(nombreCodigo)) {
										nombreCodigo = nombreCodigo+"_"+(Integer.parseInt(
												nombreCodigo.substring(
														nombreCodigo.indexOf("_")+1
														)
												)+1);
									}
								}
								objetivosPosibles.put(nombreCodigo
										    , terreno[i][j].getPersonaje());
								
								retorno.put(String.valueOf(nombreCodigo), 
										URL_PARTIDA+"/atacar?objetivo="+nombreCodigo);
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							//aguante el poxyran
						}
					}
				}
			}
			if(!retorno.isEmpty())
				return retorno;
			else return null;
		} else return null;
	}	
	
	public void agregarMensaje(String mensaje) {
		mensajes.agregarMensaje(mensaje);
	}
}
