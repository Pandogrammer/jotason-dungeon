package com.farguito.online;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.farguito.JotasonDungeonApplication;
import com.farguito.nivel.Nivel;
import com.farguito.personajes.Jugador;
import com.farguito.terreno.Terreno;

@Service
@Scope("session")
public class Mapa {

	@Autowired
	Mundo mundo;
	@Autowired
	Jugador jugador;
	
	private String noVisible = "~";
	Random random = new Random();
			
	public Mapa() {
		random.setSeed(System.currentTimeMillis());
	}



	public Map<String, String> mostrar() {
		Map<String, String> mapa = new LinkedHashMap<>();
		Nivel nivel = mundo.getNivel(jugador.getZ());
		Terreno[][] espacio = nivel.getTerreno(); 		
		
		StringBuilder value;
		
		for(int i = 0; i < nivel.getFilas(); i++) {
			value = new StringBuilder(" ");
			
			for(int j = 0; j < nivel.getColumnas(); j++) {
				//esto no va a ser performante, la opcion pubera era mejor, pero buen.
				//ademas de que no sirve para manejar la vision cuando hay cosas que la tapan (paredes)
				Point origen = new Point(jugador.getX(), jugador.getY());
				Point destino = new Point(i, j);
				if(origen.distance(destino) <= jugador.getVision()) {
					Terreno terr = espacio[i][j];
					value.append(espacio[i][j].toString()+" ");
				} else
					value.append(noVisible+" ");
			}
			
			mapa.put(String.format("%3s", String.valueOf(i)).replace(' ', '0'), value.toString());
		}
		return mapa;
	}
	
	

}
