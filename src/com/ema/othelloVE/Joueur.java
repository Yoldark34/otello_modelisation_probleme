package com.ema.othelloVE;

import com.ema.othelloVE.Plateau;

public class Joueur {

	protected byte couleur;
	protected Plateau plateau;
	protected boolean isIA;

	public Joueur(byte couleurJeton, Plateau plateauOthello, boolean ia) {
		plateau = plateauOthello;
		couleur = couleurJeton;
		isIA = ia;
	}

	public byte getCouleur() {
		return couleur;
	}

	public boolean isIA() {
		return isIA;
	}

}
