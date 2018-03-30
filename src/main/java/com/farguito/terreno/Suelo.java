package com.farguito.terreno;

public class Suelo extends Terreno {
	
	public Suelo(int x, int y) {
		super(x, y);
		this.icono = "·";
		this.traspasable = true;
	}

	
}
