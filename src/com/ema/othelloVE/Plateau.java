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
		synchronized (othellierSurcharge) {
			for (int i = 0; i < NUM_LIGNES; i++) {
				for (int j = 0; j < NUM_LIGNES; j++) {
					othellierSurcharge[i][j] = new Byte(othellier[i][j]);
				}
			}
		}
	}
	
	public byte[][] cloneSurcharge() {
		byte[][] result = new byte[NUM_LIGNES][NUM_LIGNES];
		for (int i = 0; i < NUM_LIGNES; i++) {
			for (int j = 0; j < NUM_LIGNES; j++) {
				result[i][j] = new Byte(othellierSurcharge[i][j]);
			}
		}
		
		return result;
	}
	
	public byte[][] getSurcharge() {
		return this.othellierSurcharge;
	}
	
	public void setSurcharge(byte[][] surcharge) {
		for (int i = 0; i < NUM_LIGNES; i++) {
			for (int j = 0; j < NUM_LIGNES; j++) {
				othellierSurcharge[i][j] = new Byte(surcharge[i][j]);
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
	
	public void setSurchargePlateau(int i, int j, byte couleur) {
		this.othellierSurcharge[i][j] = couleur;
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
	public boolean isCoupValide(Coup origine, boolean retourner) {
		return isCoupValide(origine, retourner, false);
	}
	public boolean isCoupValide(Coup origine, boolean retourner, boolean surcharge) {
		byte[][] othellierParcours = othellier;
		if (surcharge) {
			othellierParcours = othellierSurcharge;
		}
		boolean valide = false;
		if (othellierParcours[origine.getLigne()][origine.getColonne()] == Jeton.VIDE) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 || y != 0) {
						Coup check = parcourirDroite(origine, x, y, surcharge);
						if ( check != null) {
							valide = true;
							if (retourner) {
								Coup checkRetournement = new Coup(origine.getLigne(), origine.getColonne(), origine.getCouleur());
								while (check.getLigne() != checkRetournement.getLigne() || check.getColonne() != checkRetournement.getColonne()) {
									checkRetournement  = new Coup(checkRetournement.getLigne() + x, checkRetournement.getColonne() + y, origine.getCouleur());
									othellierParcours[checkRetournement.getLigne()][checkRetournement.getColonne()] = origine.getCouleur();
								}
							}
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
	private Coup parcourirDroite(Coup origine, int coeffX, int coeffY, boolean surcharge) {
		byte[][] othellierParcours = othellier;
		if (surcharge) {
			othellierParcours = othellierSurcharge;
		}
		boolean parcours = true;
		Coup coupResult = null;
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
						} else {
							coupResult = check;
							//stop parcours
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
				if (isCoupValide(test, false, surcharge)) {
					toReturn.add(test);
				}
			}
		}
		return toReturn;
	}
	
	public int getRetournementPossibleEnRetournant(Coup origine) {
		
		int nombreRetournement = 0;
		if (othellierSurcharge[origine.getLigne()][origine.getColonne()] == Jeton.VIDE) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 || y != 0) {
						Coup check = parcourirDroite(origine, x, y, true);
						if ( check != null) {
							Coup checkRetournement = new Coup(origine.getLigne(), origine.getColonne(), origine.getCouleur());
							while (check.getLigne() != checkRetournement.getLigne() || check.getColonne() != checkRetournement.getColonne()) {
								checkRetournement  = new Coup(checkRetournement.getLigne() + x, checkRetournement.getColonne() + y, origine.getCouleur());
								othellierSurcharge[checkRetournement.getLigne()][checkRetournement.getColonne()] = origine.getCouleur();
								//this.setPlateau(checkRetournement.getLigne(), checkRetournement.getColonne(), origine.getCouleur());
								nombreRetournement++;
							}
							//enlever 1 car le dernier jeton est un jeton de notre couleur.
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
		for (int x=0;x<NUM_LIGNES;x++) {
			for (int y=0;y<NUM_LIGNES;y++) {
				if (othellier[x][y] == Jeton.VIDE) {
					return false;
				}
			}
		}
		return true;
	}
}
