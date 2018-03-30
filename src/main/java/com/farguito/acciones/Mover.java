package com.farguito.acciones;

import com.farguito.Respuesta;
import com.farguito.terreno.Terreno;
import com.farguito.utils.Activable;

public class Mover extends Accion {

	private Direcciones direccion;	
	Terreno[][] espacio;
	
	public Mover(Terreno[][] espacio, Direcciones direccion) {
		this.pre = 0;
		this.post = 100;
		this.espacio = espacio;
		this.direccion = direccion;
	}
	
	
	public Respuesta ejecutar() {
		Respuesta respuesta = new Respuesta();
		
		if(actor.puedeActuar()) {
			int x = actor.getX(); 
			int y = actor.getY();
			
			switch(direccion) {
				case ARRIBA :    x -= 1; break;
				case ABAJO :     x += 1; break;
				case IZQUIERDA : y -= 1 ; break;
				case DERECHA :   y += 1 ; break;
			}
	
			try {
				if(!espacio[x][y].isTraspasable())
					respuesta.agregarMensaje("no se puede pasar por ahí");
				else if (espacio[x][y].estaOcupado()) 
					respuesta.agregarMensaje("el espacio está ocupado");
				else {
					espacio[actor.getX()][actor.getY()].setPersonaje(null);
					actor.setX(x); actor.setY(y);
					espacio[x][y].setPersonaje(actor);
					respuesta.agregarMensaje("te moviste hacia "+direccion);
					actor.agregarCansancio(this.post);
					respuesta.exito();
	
					if(espacio[x][y] instanceof Activable) {
						respuesta.agregarMensaje( ( (Activable) espacio[x][y] ).activar() );
					}
				} 
				
			} catch (ArrayIndexOutOfBoundsException e) {
				respuesta.agregarMensaje("no se puede ir para "+direccion);
			}
		} else {
			respuesta.agregarMensaje("estás muy cansado para moverte");
		}

		return respuesta;
	}


	public Direcciones getDireccion() {
		return direccion;
	}


	public void setDireccion(Direcciones direccion) {
		this.direccion = direccion;
	}


	public Terreno[][] getEspacio() {
		return espacio;
	}


	public void setEspacio(Terreno[][] espacio) {
		this.espacio = espacio;
	}


	
	

}
