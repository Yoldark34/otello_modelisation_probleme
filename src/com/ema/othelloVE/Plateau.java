package com.ema.othelloVE;

import java.util.ArrayList;
import java.util.List;

import com.ema.othelloVE.Jeton;

public class Plateau {
	private static final int NUM_LIGNES = 8;
	/** othellier[ligne][colonne] */
	private byte[][] othellier;
	private byte[][] othellierSurcharge;

	public Plateau() {
		othellier = new byte[NUM_LIGNES][NUM_LIGNES];
		othellierSurcharge = new byte[NUM_LIGNES][NUM_LIGNES];
	}
	
	public void resetSurcharge() {
		synchronized (othellier) {
			for (int i = 0; i < NUM_LIGNES; i++) {
				for (int j = 0; j < NUM_LIGNES; j++) {
					othellierSurcharge[i][j] = new Byte(othellier[i][j]).byteValue();
				}
			}
		}
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

	/**
	 * Retourne si le coup est valide ou non
	 * 
	 * @param coup
	 * @return
	 */
	public boolean isCoupValide(Coup coup, boolean retourner) {
		return isCoupValide(coup, retourner, false);
	}
	
	public boolean isCoupValide(Coup coup, boolean retourner, boolean surcharge) {
		byte[][] othellierParcours = othellier;
		if (surcharge) {
			othellierParcours = othellierSurcharge;
			this.resetSurcharge();
		}
		boolean valide = false;
		if (othellierParcours[coup.getLigne()][coup.getColonne()] == Jeton.VIDE) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 || y != 0) {
						if (parcourirDroite(coup, x, y, retourner, surcharge)) {
							valide = true;
						}
					}
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
	private boolean parcourirDroite(Coup origine, int coeffX, int coeffY, boolean renversement, boolean surcharge) {
		byte[][] othellierParcours = othellier;
		if (surcharge) {
			othellierParcours = othellierSurcharge;
			this.resetSurcharge();
		}
		boolean parcours = true;
		boolean valide = false;
		Coup check = new Coup(origine.getLigne(), origine.getColonne(), origine.getCouleur());
		int distance =0;
		while(parcours) {
			distance ++;
			check  = new Coup(check.getLigne() + coeffX, check.getColonne() + coeffY, origine.getCouleur());
			// Test si le pion est dans le plateau
			parcours = check.getColonne() >= 0 && check.getColonne() < NUM_LIGNES && 
					check.getLigne() >= 0 && check.getLigne() < NUM_LIGNES;
			if (parcours) {
				// Il y a quelque chose sur la case
				if (othellierParcours[check.getLigne()][check.getColonne()] != Jeton.VIDE) {
					// Jeton de couleur identique de l'origine
					if (origine.getCouleur() == othellierParcours[check.getLigne()][check.getColonne()]) {
						// Test si la distance est plus grande que 1
						if (distance == 1) {
							parcours = false;
						}
						else {
							valide = true;
							if (renversement) {
								Coup checkRetournement = new Coup(origine.getLigne(), origine.getColonne(), origine.getCouleur());
								while (check.getLigne() != checkRetournement.getLigne() || check.getColonne() != checkRetournement.getColonne()) {
									checkRetournement  = new Coup(checkRetournement.getLigne() + coeffX, checkRetournement.getColonne() + coeffY, origine.getCouleur());
									othellierParcours[checkRetournement.getLigne()][checkRetournement.getColonne()] = origine.getCouleur();
								}
							}
						}
					}
				} else {
					parcours = false;
				}
			}
		}
		return valide;
	}

	/**
	 * Retourne les coups possibles pour un joueur donné
	 * @param joueurEnCours
	 * @return
	 */
	public List<Coup> getMouvementPossible(byte couleur) {
		return getMouvementPossible(couleur, false);
	}
	
	public List<Coup> getMouvementPossible(byte couleur, boolean surcharge) {
		List<Coup> toReturn = new ArrayList<Coup>();
		// Parcours de toute la grille
		for (int x=0;x<NUM_LIGNES;x++) {
			for (int y=0;y<NUM_LIGNES;y++) {
				Coup test = new Coup(x, y, couleur);
				// Si le coup est valide on l'ajoute
				if (isCoupValide(test, false)) {
					toReturn.add(test);
				}
			}
		}
		return toReturn;
	}

}
