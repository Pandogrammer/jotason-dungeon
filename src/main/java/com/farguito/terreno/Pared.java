package com.farguito.terreno;

public class Pared extends Terreno {

	public Pared(int x, int y) {
		super(x, y);
		this.icono = "#";
		this.traspasable = false;
	}

}
