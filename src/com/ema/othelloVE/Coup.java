package com.ema.othelloVE;

public class Coup {

	private int lig;
	private int col;
	private byte couleur;

	public Coup(int l, int c, byte codeCouleur) {
		lig = l;
		col = c;
		couleur = codeCouleur;
	}

	public int getLigne() {
		return (lig);
	}

	public int getColonne() {
		return (col);
	}

	public byte getCouleur() {
		return (couleur);
	}

}
