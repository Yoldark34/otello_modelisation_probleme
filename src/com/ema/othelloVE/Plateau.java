package com.ema.othelloVE;

import java.util.List;

import com.ema.othelloVE.Jeton;

public class Plateau {
	private static final int NUM_LIGNES = 8;
	/** othellier[ligne][colonne] */
	private byte[][] othellier;

	public Plateau() {
		othellier = new byte[NUM_LIGNES][NUM_LIGNES];
	}

	public void initPlateau() {
		synchronized (othellier) {
			for (int i = 0; i < NUM_LIGNES; i++) {
				for (int j = 0; j < NUM_LIGNES; j++)
					othellier[i][j] = Jeton.VIDE;
			}

			othellier[3][3] = Jeton.NOIR;
			othellier[4][3] = Jeton.BLANC;
			othellier[3][4] = Jeton.BLANC;
			othellier[4][4] = Jeton.NOIR;
		}
	}

	public void setPlateau(int i, int j, byte couleur) {
		this.othellier[i][j] = couleur;
	}

	public int getNbLignes() {
		return NUM_LIGNES;
	}

	public byte getJeton(int x, int y) { // retourne la couleur du jeton présent
											// sur le plateau aux coordonnées
											// x,y
		return othellier[x][y];
	}

	public int nombreJetons(int couleur) { // retourne le nombre de jetons, de
											// la couleur donnée, présents sur
											// le plateau
		int nb = 0;
		synchronized (othellier) {
			for (int i = 0; i < NUM_LIGNES; i++) {
				for (int j = 0; j < NUM_LIGNES; j++) {
					if (othellier[i][j] == couleur) {
						nb++;
					}
				}
			}
		}
		return nb;
	}

	// A COMPLETER

}
