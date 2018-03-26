package com.farguito.terreno;

import java.util.List;

import com.farguito.items.Item;
import com.farguito.personajes.Personaje;

public abstract class PorcionTerreno {
	
	protected boolean traspasable;
	protected boolean explorado = false;
	protected List<Item> items;
	protected Personaje personaje;
	protected String icono;

	public boolean isTraspasable() {
		return traspasable;
	}

	public void setTraspasable(boolean traspasable) {
		this.traspasable = traspasable;
	}

	public boolean isExplorado() {
		return explorado;
	}

	public void setExplorado(boolean explorado) {
		this.explorado = explorado;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Personaje getPersonaje() {
		return personaje;
	}

	public void setPersonaje(Personaje personaje) {
		this.personaje = personaje;
	}
	
	public boolean ocupado() {
		if(personaje != null)
			return true;
		else 
			return false;
	}

	public String toString() {
		if(!explorado) return "~";
		else if (personaje != null) return personaje.toString();
		else return icono;
	}
	
	
}
