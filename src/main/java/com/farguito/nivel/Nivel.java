package com.farguito.nivel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.farguito.online.Mundo;
import com.farguito.personajes.Jugador;
import com.farguito.personajes.Monstruo;
import com.farguito.personajes.Rata;
import com.farguito.terreno.Escalera;
import com.farguito.terreno.Pared;
import com.farguito.terreno.Suelo;
import com.farguito.terreno.Terreno;

public class Nivel {

	Random random = new Random(System.currentTimeMillis());
	
	int filas = 7;
	int columnas = filas+filas/2;
	int piso = 0;
	
	Terreno terreno[][];
	List<Monstruo> monstruos = new ArrayList<>();
	
	public Nivel(int piso) {
		this.piso = piso;
		filas = filas+piso*2;
		columnas = columnas+piso*2;
		terreno = new Terreno[filas][columnas];
		initPiso();
		initParedes();
		initEnemigos();
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
	
	
	private void initEnemigos() {
		int cantidad = 3 + piso * 2;
		int i = 0;
		boolean listo = false;
		while (!listo) {
			int x = random.nextInt(filas);
			int y = random.nextInt(columnas);
			if(terreno[x][y].isTraspasable() && !terreno[x][y].estaOcupado()) {
				Monstruo rata = new Rata();
				rata.setX(x); rata.setY(y); rata.setZ(piso);
				terreno[x][y].setPersonaje(rata);
				monstruos.add(rata);
				i++;
			}
			
			if(i == cantidad) listo = true;
		}
		
	}

	private void initPiso() {
		int i;
		int j;
		
		for(i = 0; i < filas; i++) {
			for(j = 0; j < columnas; j++) {
				terreno[i][j] = new Suelo(i, j);
			}
		}	
	}
	
	private void initParedes() {
		int i;
		int j;
		
		for(i = 0; i < filas; i++) {
			for(j = 0; j < columnas; j++) {
				if (random.nextInt(10) > 8) 
					terreno[i][j] = new Pared(i, j);
			}
		}
	}

	public Escalera generarEscalera(Nivel origen, Nivel destino) {
		Escalera escalera = null;
		boolean listo = false;
		while (!listo) {
			int x = random.nextInt(filas);
			int y = random.nextInt(columnas);
			if( terreno[x][y] instanceof Suelo 
			&& !terreno[x][y].estaOcupado()) {
				escalera = new Escalera(x, y);
				escalera.setOrigen(origen);
				escalera.setDestino(destino);
				terreno[x][y] = escalera;
				
				listo = true;
			}
		}
		return escalera;
	}


	public void generarEscalera(int x, int y, Nivel destino) {
		Escalera escalera = new Escalera(x, y);
		escalera.setOrigen(this);
		escalera.setDestino(destino);
		terreno[x][y] = escalera;			
	}

	

	public void invocarJugador(Jugador jugador) {
		boolean listo = false;
		while (!listo) {
			int x = random.nextInt(filas);
			int y = random.nextInt(columnas);
			if(terreno[x][y].isTraspasable() && !terreno[x][y].estaOcupado()) {
				jugador.setX(x); jugador.setY(y); jugador.setZ(piso);
				terreno[x][y].setPersonaje(jugador);
				listo = true;
			}
		}
	}
	

	public Terreno[][] getTerreno() {
		return terreno;
	}


	public int getFilas() {
		return filas;
	}


	public int getColumnas() {
		return columnas;
	}


	public List<Monstruo> getMonstruos() {
		return monstruos;
	}



	public int getPiso() {
		return piso;
	}


	
	
	

	
}
