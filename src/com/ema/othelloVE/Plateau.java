package com.ema.othelloVE;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.ema.othelloVE.Jeton;

public class Plateau {
	private static final int NUM_LIGNES = 8;
	/** othellier[ligne][colonne] */
	private byte[][] othellier;
	private static int[] patern1 = { 7, 1, 5, 5, 5, 5, 1, 7 };
	private static int[] patern2 = { 1, 1, 2, 2, 2, 2, 1, 1 };
	private static int[] patern3 = { 5, 1, 6, 6, 6, 6, 1, 5 };
	private static int[][] tabPonderation = { patern1, patern2, patern3, patern3,
		patern3, patern3, patern2, patern1 };

	public static int getPonderation(int ligne, int colonne) {
		return Plateau.tabPonderation[ligne][colonne];
	}

	public Plateau() {
		othellier = new byte[NUM_LIGNES][NUM_LIGNES];
	}

	public Plateau(Plateau plateau) {
		othellier = new byte[NUM_LIGNES][NUM_LIGNES];

		for (int i = 0; i < NUM_LIGNES; i++) {
			for (int j = 0; j < NUM_LIGNES; j++) {
				othellier[i][j] = new Byte(plateau.othellier[i][j]);
			}
		}
	}

	public void setPlateau(byte[][] othellier) {
		for (int i = 0; i < NUM_LIGNES; i++) {
			for (int j = 0; j < NUM_LIGNES; j++) {
				this.othellier[i][j] = new Byte(othellier[i][j]);
			}
		}
	}

	public void initPlateau() {
		synchronized (othellier) {
			for (int i = 0; i < NUM_LIGNES; i++) {
				for (int j = 0; j < NUM_LIGNES; j++)
					othellier[i][j] = Jeton.VIDE;
			}

			setPlateau(3, 3, Jeton.NOIR);
			setPlateau(4, 3, Jeton.BLANC);
			setPlateau(3, 4, Jeton.BLANC);
			setPlateau(4, 4, Jeton.NOIR);
		}
	}

	public void setPlateau(int i, int j, byte couleur) {
		this.othellier[i][j] = couleur;
	}

	public static int getNbLignes() {
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

	/**
	 * Retourne si le coup est valide ou non
	 * 
	 * @param coup
	 * @return
	 */
	public boolean isCoupValide(int ligne, int colonne, byte couleur, boolean retourner) {
		boolean valide = false;
		boolean stop = false;
		int x = -1;
		int y = -1;
		int ligneRetournement = -1;
		int colonneRetournement = -1;
		if (othellier[ligne][colonne] == Jeton.VIDE) {
			while (x <= 1 && y <= 1 && !stop) {
				if (x != 0 || y != 0) {
					Coup check = parcourirDroite(ligne, colonne, couleur, x, y);
					if (check != null) {
						valide = true;
						if (retourner) {
							ligneRetournement = ligne;
							colonneRetournement = colonne;
							while (check.getLigne() != ligneRetournement || check.getColonne() != colonneRetournement) {
								ligneRetournement += x;
								colonneRetournement += y;
								othellier[ligneRetournement][colonneRetournement] = couleur;
							}
						} else {
							stop = true;
						}
					}
				}
				x++;
				if (x > 1 && y < 1) {
					y++;
					x = -1; 
				}
			}
			
		}
		return valide;
	}

	/**
	 * Retourne true si la droite autorise qu'on pose un pion
	 * 
	 * @param coeffX
	 *            Coefficient directeur de la droite en X
	 * @param coeffY
	 *            Coefficient directeur de la droite en Y
	 * @param renversement
	 *            true si on veux aussi retourner
	 * @return
	 */
	private Coup parcourirDroite(int ligne, int colonne, byte couleur, int coeffX, int coeffY) {
		boolean parcours = true;
		Coup coupResult = null;
		Coup check = new Coup(ligne, colonne, couleur);
		int distance = 0;
		while (parcours) {
			distance++;
			check = new Coup(check.getLigne() + coeffX, check.getColonne() + coeffY, couleur);
			// Test si le pion est dans le plateau
			parcours = check.getColonne() >= 0 && check.getColonne() < NUM_LIGNES && check.getLigne() >= 0 && check.getLigne() < NUM_LIGNES;
			if (parcours) {
				// Il y a quelque chose sur la case
				if (othellier[check.getLigne()][check.getColonne()] != Jeton.VIDE) {
					// Jeton de couleur identique de l'origine
					if (couleur == othellier[check.getLigne()][check.getColonne()]) {
						// Test si la distance est plus grande que 1
						if (distance == 1) {
							parcours = false;
						} else {
							coupResult = check;
							// stop parcours
							parcours = false;
						}
					}
				} else {
					parcours = false;
				}
			}
		}
		return coupResult;
	}

	/**
	 * Retourne les coups possibles pour un joueur donné
	 * 
	 * @param joueurEnCours
	 * @return
	 */
	public List<Coup> getMouvementPossible(byte couleur) {
		List<Coup> toReturn = new ArrayList<Coup>();
		// Parcours de toute la grille
		for (int x = 0; x < NUM_LIGNES; x++) {
			for (int y = 0; y < NUM_LIGNES; y++) {
				// Si le coup est valide on l'ajoute
				if (isCoupValide(x, y, couleur, false)) {
					toReturn.add(new Coup(x, y, couleur));
				}
			}
		}
		return toReturn;
	}

	public int getRetournementPossibleEnRetournant(int ligne, int colonne, byte couleur) {
		int nombreRetournement = 0;
		int ligneRetournement = -1;
		int colonneRetournement = -1;
		if (othellier[ligne][colonne] == Jeton.VIDE) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 || y != 0) {
						Coup check = parcourirDroite(ligne, colonne, couleur, x, y);
						if (check != null) {
							ligneRetournement = ligne;
							colonneRetournement = colonne;
							while (check.getLigne() != ligneRetournement || check.getColonne() != colonneRetournement) {
								ligneRetournement += x;
								colonneRetournement += y;
								othellier[ligneRetournement][colonneRetournement] = couleur;
								// this.setPlateau(checkRetournement.getLigne(),
								// checkRetournement.getColonne(),
								// origine.getCouleur());
								nombreRetournement++;
							}
							// enlever 1 car le dernier jeton est un jeton de
							// notre couleur.
							nombreRetournement--;
						}
					}
				}
			}
		}

		return nombreRetournement;
	}

	public boolean isFull() {
		// Parcours de toute la grille
		for (int x = 0; x < NUM_LIGNES; x++) {
			for (int y = 0; y < NUM_LIGNES; y++) {
				if (othellier[x][y] == Jeton.VIDE) {
					return false;
				}
			}
		}
		return true;
	}
}
