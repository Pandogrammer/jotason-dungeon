package com.farguito.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.farguito.Response;
import com.farguito.personajes.Enemigo;
import com.farguito.personajes.Jugador;
import com.farguito.personajes.Personaje;
import com.farguito.personajes.Rata;
import com.farguito.terreno.Escalera;
import com.farguito.terreno.Pared;
import com.farguito.terreno.Piso;
import com.farguito.terreno.PorcionTerreno;

@Service
@Scope("session")
public class Mapa {

	@Autowired
	Jugador pj;
	
	int filas = 10;
	int columnas = 11;
	PorcionTerreno terreno[][];
	Random random = new Random();
	int nivel = 0;
	List<Enemigo> enemigos = new ArrayList<>();
			
	public Mapa() {
		random.setSeed(System.currentTimeMillis());
	}
	
	@PostConstruct
	private void init() {
		filas = filas+nivel*2;
		columnas = columnas+nivel*2;
		terreno = new PorcionTerreno[filas][columnas];
		initPiso();
		initParedes();
		initJugador();		
		initEnemigos();
		initEscalera();
		explorar();
	}
	
	public Map<String, String> mostrar() {
		Map<String, String> mapa = new LinkedHashMap<>();
		int i;
		int j;
		StringBuilder value;
		
		for(i = 0; i < filas; i++) {
			value = new StringBuilder(" ");
			
			for(j = 0; j < columnas; j++) {
				value.append(terreno[i][j].toString()+" ");
			}
			
			mapa.put(String.format("%3s", String.valueOf(i)).replace(' ', '0'), value.toString());
		}
		return mapa;
	}

	public Map<String, String> movimiento(){
		Map<String, String> retorno = new LinkedHashMap<>();
		retorno.put("arriba",   Partida.URL+"?accion=mover&direccion=arriba");
		retorno.put("abajo",    Partida.URL+"?accion=mover&direccion=abajo");
		retorno.put("izquierda",Partida.URL+"?accion=mover&direccion=izquierda");
		retorno.put("derecha",  Partida.URL+"?accion=mover&direccion=derecha");
		
		return retorno;
	}	
	
	
	
	private void initParedes() {
		int i;
		int j;
		
		for(i = 0; i < filas; i++) {
			for(j = 0; j < columnas; j++) {
				if (random.nextInt(10) > 8) 
					terreno[i][j] = new Pared();
			}
		}
	}
	
	private void initPiso() {
		int i;
		int j;
		
		for(i = 0; i < filas; i++) {
			for(j = 0; j < columnas; j++) {
				terreno[i][j] = new Piso();
			}
		}	
	}

	
	private void initJugador() {
		int i = random.nextInt(filas);
		int j = random.nextInt(columnas);
		terreno[i][j] = new Piso();
		pj.setX(i); pj.setY(j);
		terreno[i][j].setPersonaje(pj);
	}
	
	private void initEnemigos() {
		int cantidad = 3 + nivel * 2;
		int i = 0;
		boolean listo = false;
		while (!listo) {
			int x = random.nextInt(filas);
			int y = random.nextInt(columnas);
			if(terreno[x][y].isTraspasable() && !terreno[x][y].ocupado()) {
				Enemigo rata = new Rata();
				rata.setX(x); rata.setY(y);
				terreno[x][y].setPersonaje(rata);
				enemigos.add(rata);
				i++;
			}
			
			if(i == cantidad) listo = true;
		}
		
	}

	
	private void initEscalera() {
		boolean listo = false;
		while (!listo) {
			int x = random.nextInt(filas);
			int y = random.nextInt(columnas);
			if(terreno[x][y].isTraspasable() && !terreno[x][y].ocupado()) {
				terreno[x][y] = new Escalera();
				listo = true;
			}
		}
	}
	
	
	public String mover(String direccion) {
		String mensaje = null;
		int x = pj.getX(); 
		int y = pj.getY();
		boolean exito = false;
		try {
			//TODO: hacerlo mas lindo, claramente se puede abstraer esta chanchadita
			switch(direccion) {
				case "arriba" : exito = arriba(pj); break;
				case "abajo" : exito = abajo(pj); break;
				case "derecha" : exito = derecha(pj); break;
				case "izquierda" : exito = izquierda(pj); break;
				default : 
					mensaje = "que flasheaste capo?";
					break;
			}

			if(exito) {
				mensaje = "te moviste hacia "+direccion;				
				explorar();
				if(chequearEscalera()) {
					mensaje = "pasaste de nivel!";
					
				}
			} else {
				mensaje = "no se puede ir para "+direccion;
			}
			
		} catch (Exception e) {
			//out of boundssss yey
			mensaje = "no se puede ir para "+direccion;
		}

		return mensaje;
	}
	
	private boolean chequearEscalera() {
		if(terreno[pj.getX()][pj.getY()].getClass().equals(Escalera.class)) {
			pasarDeNivel();
			return true;
		}
		else return false;
	}
	
	public List<String> updateEnemigos() {
		boolean exito = false;
		List<String> mensajes = new ArrayList<>();
		for(Enemigo e : enemigos) {
			if(e.getObjetivo() == null) {
				moverRandom(e);
				chequearVision(e);
				if(e.getObjetivo() != null)
					mensajes.add(e.getNombre()+" empezó a seguirte!");
			} else {
				exito = intentarAtacar(e);
				if(exito)
					mensajes.add(e.getNombre()+" te atacó!");
				else
					intentarAcercarse(e);
			}			
		};
		return mensajes;
	}
	

	private boolean derecha(Personaje pj) {
		int x = pj.getX();
		int y = pj.getY();

		if(terreno[x][y+1].isTraspasable() && !terreno[x][y+1].ocupado()) {
			pj.setY(y+1);
			terreno[x][y].setPersonaje(null);
			terreno[x][y+1].setPersonaje(pj);
			return true;
		} else return false;
	}
	

	private boolean izquierda(Personaje pj) {
		int x = pj.getX();
		int y = pj.getY();

		if(terreno[x][y-1].isTraspasable() && !terreno[x][y-1].ocupado()) {
			pj.setY(y-1);
			terreno[x][y].setPersonaje(null);
			terreno[x][y-1].setPersonaje(pj);
			return true;
		} else return false;
	}
	
	
	private boolean abajo(Personaje pj) {
		int x = pj.getX();
		int y = pj.getY();
		
		if(terreno[x+1][y].isTraspasable() && !terreno[x+1][y].ocupado()) {
			pj.setX(x+1);
			terreno[x][y].setPersonaje(null);
			terreno[x+1][y].setPersonaje(pj);
			return true;
		} else return false;
	}
	
	private boolean arriba(Personaje pj){
		int x = pj.getX();
		int y = pj.getY();
		
		if(terreno[x-1][y].isTraspasable() && !terreno[x-1][y].ocupado()) {
			pj.setX(x-1);
			terreno[x][y].setPersonaje(null);
			terreno[x-1][y].setPersonaje(pj);
			return true;
		} else return false;
	}
	
	
	private void moverRandom(Enemigo e) {		
		boolean exito = false;
		while(!exito) {
			try {
				switch(random.nextInt(4)+1) {
					case 1 : exito = arriba(e); break;
					case 2 : exito = abajo(e); break;
					case 3 : exito = derecha(e); break;
					case 4 : exito = izquierda(e); break;
				}
			} catch (Exception ex) {}
		}
	}
	
	
	private void chequearVision(Enemigo e) {
		int vision = e.getVision();
		int x = e.getX();
		int y = e.getY();
		
		//vision del personaje
		for(int i = x-vision; i <= x+vision; i++) {
			for(int j = y-vision; j <= y+vision; j++) {
				try{
					//mas adelante se puede cambiar para que se ataquen entre si (?
					if(terreno[i][j].getPersonaje().getClass().equals(Jugador.class))
						e.setObjetivo(terreno[i][j].getPersonaje());
				} catch(Exception ex) {
					//aca nos hacemos los giles OUT OF BOUNDS y demas falopas
				}
			}			
		}
		
	}
	
	private boolean intentarAtacar(Enemigo e) {
		Personaje p = e.getObjetivo();
		int distanciaX = p.getX() - e.getX();
		int distanciaY = p.getY() - e.getY();
		if(    ((distanciaX == -1 || distanciaX == 1) && distanciaY == 0) 
			|| ((distanciaY == -1 || distanciaY == 1) && distanciaX == 0) ) {
			p.dañar(e.getDaño());
			return true;
		} else {
			return false;
		}
	}
	
	private void intentarAcercarse(Enemigo e) {
		Personaje p = e.getObjetivo();
		int distanciaX = p.getX() - e.getX();
		int distanciaY = p.getY() - e.getY();
		try {
			if(distanciaX <= -1) {
				if(!arriba(e))
					if(!derecha(e))
						izquierda(e);
			} else if(distanciaX >= 1) {
				if(!abajo(e))
					if(!derecha(e))
						izquierda(e);
			} else if(distanciaY <= -1) {
				if(!izquierda(e))
					if(!abajo(e))
						arriba(e);
			} else if(distanciaY >= 1) {
				if(!derecha(e))
					if(!abajo(e))
						arriba(e);
			}
		} catch (Exception ex) {}
			
	}
	
	
	
	private void pasarDeNivel() {
		this.nivel++;
		init();
	}
	
	private void explorar() {
		resetearMapa();
		
		int vision = pj.getVision();
		int x = pj.getX();
		int y = pj.getY();
		
		terreno[x][y].setExplorado(true);
		//vision del jugador
		for(int i = x-vision; i <= x+vision; i++) {
			for(int j = y-vision; j <= y+vision; j++) {
				try{
					terreno[i][j].setExplorado(true);
				} catch(Exception e) {
					//aca nos hacemos los giles OUT OF BOUNDS
				}
			}			
		}
		
		
	}
	
	private void resetearMapa(){
		for(int i = 0; i < filas; i++) 
			for(int j = 0; j < columnas; j++)
				terreno[i][j].setExplorado(false);
			
	}
	
	public String chequearVidas() {
		String mensaje = null;
		
		for(int i = 0; i < enemigos.size(); i++) {
			Enemigo e = enemigos.get(i);
			if(e.muerto()) {
				terreno[e.getX()][e.getY()].setPersonaje(null);
				enemigos.remove(e);
			}
		}
		
		if(pj.muerto()) {
			mensaje = "te moriste :c";
			nivel = 0;
			columnas = 11;
			filas = 10;
			pj.setVidaActual(pj.getVidaTotal());
			init();
		} 
		
		return mensaje;
	}

	public PorcionTerreno[][] getTerreno() {
		return terreno;
	}

	public int getNivel() {
		return nivel;
	}
	
	
	
		

}
