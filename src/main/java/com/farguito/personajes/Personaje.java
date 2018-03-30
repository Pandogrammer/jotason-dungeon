package com.farguito.personajes;

import com.farguito.nivel.Nivel;

public abstract class Personaje {
	
	int vision;	
	int x;
	int y;
	int z;
	String icono;
	int vidaTotal;
	int vidaActual;
	String nombre;
	
	int cansancio;
	float recuperacion = 0.25f;
	
	int ataque;
	int rango;
	
	public int getVision() {
		return vision;
	}
	public void setVision(int vision) {
		this.vision = vision;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public String getIcono() {
		return icono;
	}
	public void setIcono(String icono) {
		this.icono = icono;
	}

	public int getVidaTotal() {
		return vidaTotal;
	}
	public void setVidaTotal(int vidaTotal) {
		this.vidaTotal = vidaTotal;
	}
	public int getVidaActual() {
		return vidaActual;
	}
	public void setVidaActual(int vidaActual) {
		this.vidaActual = vidaActual;
	}
	public String toString() {
		return icono;
	}
	
	public void recibirDaño(int daño) {
		vidaActual -= daño;
	}
	public int getAtaque() {
		return ataque;
	}
	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}
	public int getCansancio() {
		return cansancio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
	
	public boolean estaMuerto() {
		return vidaActual <= 0;
	}
	
	public boolean puedeActuar() {
		return cansancio <= 0;
	}
	
	public void agregarCansancio(int cansancio) {
		this.cansancio += cansancio;
	}
	

	protected void descansar() {
		cansancio -= recuperacion;
	}
	
	
	public abstract void actualizar(Nivel nivel);
	
	
	public int getRango() {
		return rango;
	}
	public void setRango(int rango) {
		this.rango = rango;
	}
	
	
}
