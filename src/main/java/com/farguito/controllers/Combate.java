package com.farguito.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.farguito.personajes.Jugador;
import com.farguito.terreno.PorcionTerreno;

@Service
@Scope("session")
public class Combate {
    
	@Autowired
	Jugador pj;
	
	@Autowired
	Mapa mapa;
	
	public String atacar(String direccion) {
		String retorno = null;
		PorcionTerreno terreno[][] = mapa.getTerreno();
		int x = pj.getX(); 
		int y = pj.getY();
		boolean exito = false;
		try {
			//TODO: hacerlo mas lindo, claramente se puede abstraer esta chanchadita
			switch(direccion) {
				case "arriba" : {
					if(terreno[x-1][y].ocupado()) {
						terreno[x-1][y].getPersonaje().dañar(pj.getDaño());
						pj.dañar(terreno[x-1][y].getPersonaje().getDaño());
						exito = true;
					} break;
				}
				case "abajo" : {
					if(terreno[x+1][y].ocupado()) {
						terreno[x+1][y].getPersonaje().dañar(pj.getDaño());
						pj.dañar(terreno[x+1][y].getPersonaje().getDaño());
						exito = true;
					} break;
				}
				case "derecha" : {
					if(terreno[x][y+1].ocupado()) {
						terreno[x][y+1].getPersonaje().dañar(pj.getDaño());
						pj.dañar(terreno[x][y+1].getPersonaje().getDaño());
						exito = true;
					} break;
				}
				case "izquierda" : {
					if(terreno[x][y-1].ocupado()) {
						terreno[x][y-1].getPersonaje().dañar(pj.getDaño());
						pj.dañar(terreno[x][y-1].getPersonaje().getDaño());
						exito = true;
					} break;
				}
				default : 
					retorno = "que flasheaste capo?";
					break;
			}

			if(exito)
				retorno = "atacaste hacia "+direccion;
			
		} catch (Exception e) {
			e.printStackTrace();
			//out of boundssss yey
			retorno = "no se puede ir para "+direccion;
		}

		return retorno;
	}
	
	
	public Map<String, String> ataque(){
		Map<String, String> retorno = new LinkedHashMap<>();
		retorno.put("arriba",   Partida.URL+"?accion=atacar&direccion=arriba");
		retorno.put("abajo",    Partida.URL+"?accion=atacar&direccion=abajo");
		retorno.put("izquierda",Partida.URL+"?accion=atacar&direccion=izquierda");
		retorno.put("derecha",  Partida.URL+"?accion=atacar&direccion=derecha");
		
		return retorno;
	}	
}
