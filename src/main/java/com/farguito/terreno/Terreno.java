package com.farguito.terreno;

import com.farguito.items.Item;
import com.farguito.personajes.Personaje;

public abstract class Terreno {

	int x;
	int y;
	protected String icono;
	protected boolean traspasable;
	protected Item item;
	protected Personaje personaje;
	
	public Terreno(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isTraspasable() {
		return traspasable;
	}

	public void setTraspasable(boolean traspasable) {
		this.traspasable = traspasable;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Personaje getPersonaje() {
		return personaje;
	}

	public void setPersonaje(Personaje personaje) {
		this.personaje = personaje;
	}
	
	
	public boolean estaOcupado() {
		if(personaje != null)
			return true;
		else 
			return false;
	}

	public String toString() {
		if (personaje != null) return personaje.toString();
		else if (item != null) return item.toString();
		else return icono;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	
	
}
