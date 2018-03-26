package com.farguito.personajes;

public abstract class Personaje {
	
	int vision;	
	int x;
	int y;
	String icono;
	int vidaTotal;
	int vidaActual;
	int daño;
	
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
	
	public void dañar(int daño) {
		vidaActual = vidaActual - daño;
	}
	public int getDaño() {
		return daño;
	}
	public void setDaño(int daño) {
		this.daño = daño;
	}
	
	public boolean muerto() {
		return vidaActual <= 0;
	}
	
	
}
