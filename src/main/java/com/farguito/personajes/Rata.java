package com.farguito.personajes;

import java.awt.Point;
import java.util.Random;

import com.farguito.acciones.Accion;
import com.farguito.acciones.Atacar;
import com.farguito.acciones.Direcciones;
import com.farguito.acciones.Mover;
import com.farguito.nivel.Nivel;

public class Rata extends Monstruo {

	Random random = new Random();
	public Rata() {
		super();
		random.setSeed(System.currentTimeMillis());
		this.nombre = "rata";
		this.vision = 1;
		this.icono = "r";
		this.vidaTotal = 1;
		this.vidaActual = vidaTotal;
		this.recuperacion = 3f;

		this.ataque = 1;
		this.rango = 1;
	}

	@Override
	public void actualizar(Nivel nivel) {
		if(!puedeActuar())
			descansar();	
		else {
			if(objetivo == null) {
				chequearVision(nivel);
				if(objetivo != null) {
					if(objetivo instanceof Jugador)
						((Jugador) objetivo).agregarMensaje(nombre+" empezó a seguirte!");
				} else {
					moverRandom(nivel);
					chequearVision(nivel);
				}
			} else {
				if(!objetivo.estaMuerto()) {
					if(!intentarAtacar())
						intentarAcercarse(nivel);
				} else objetivo = null;
			}	
		}
	}
	

	private void moverRandom(Nivel nivel) {	
		Accion accion = null;
		try {
			switch(random.nextInt(4)+1) {
				case 1 : accion = new Mover(nivel.getTerreno(), Direcciones.ARRIBA); break;
				case 2 : accion = new Mover(nivel.getTerreno(), Direcciones.ABAJO); break;
				case 3 : accion = new Mover(nivel.getTerreno(), Direcciones.DERECHA); break;
				case 4 : accion = new Mover(nivel.getTerreno(), Direcciones.IZQUIERDA); break;
			}
			accion.setActor(this);
			accion.ejecutar();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void chequearVision(Nivel nivel) {
		for(int i = x-vision; i <= x+rango; i++) {
			for(int j = y-rango; j <= y+rango; j++) {
				Point origen = new Point(x, y);
				Point destino = new Point(i, j);
				if( origen.distance(destino) > 0  //asi no me autotargetea
				&&  origen.distance(destino) <= rango) { 
					try{
						//mas adelante se puede cambiar para que se ataquen entre si (?
						if(nivel.getTerreno()[i][j].getPersonaje() instanceof Jugador) {
							objetivo = nivel.getTerreno()[i][j].getPersonaje();
						}
					} catch(Exception ex) {
						//aca nos hacemos los giles OUT OF BOUNDS y demas falopas
					}
				}	
			}
		}
		
	}
	
	

	private boolean intentarAtacar() {
		Accion accion = new Atacar(rango, objetivo);
		accion.setActor(this);
		return accion.ejecutar().containsKey("exito");
	}
	
	private void intentarAcercarse(Nivel nivel) {
		int distanciaX = objetivo.getX() - x;
		int distanciaY = objetivo.getY() - y;
		Mover mover;
		boolean exito = false;
			try {
				mover = new Mover(nivel.getTerreno(), Direcciones.ARRIBA);
				mover.setActor(this);
				if(distanciaX <= -1) {
					if(!mover.ejecutar().containsKey("exito")) {						
						mover.setDireccion(Direcciones.DERECHA);
						if(!mover.ejecutar().containsKey("exito")) {
							mover.setDireccion(Direcciones.IZQUIERDA);
							exito = mover.ejecutar().containsKey("exito");
						}
					}
				} else if(distanciaX >= 1) {				
					mover.setDireccion(Direcciones.ABAJO);
					if(!mover.ejecutar().containsKey("exito")) {						
						mover.setDireccion(Direcciones.DERECHA);
						if(!mover.ejecutar().containsKey("exito")) {
							mover.setDireccion(Direcciones.IZQUIERDA);
							exito = mover.ejecutar().containsKey("exito");
						}
					}
				} else if(distanciaY <= -1) {			
					mover.setDireccion(Direcciones.IZQUIERDA);
					if(!mover.ejecutar().containsKey("exito")) {						
						mover.setDireccion(Direcciones.ABAJO);
						if(!mover.ejecutar().containsKey("exito")) {
							mover.setDireccion(Direcciones.ARRIBA);
							exito = mover.ejecutar().containsKey("exito");
						}
					}
				} else if(distanciaY >= 1) {			
					mover.setDireccion(Direcciones.DERECHA);
					if(!mover.ejecutar().containsKey("exito")) {						
						mover.setDireccion(Direcciones.ABAJO);
						if(!mover.ejecutar().containsKey("exito")) {
							mover.setDireccion(Direcciones.ARRIBA);
							exito = mover.ejecutar().containsKey("exito");
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
	}
}
