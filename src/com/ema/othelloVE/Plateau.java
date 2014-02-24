package com.ema.othelloVE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.ema.othelloVE.Jeton;

public class Plateau {
	private static final int NUM_LIGNES = 8;
	/** othellier[ligne][colonne] */
	private byte[][] othellier;
	private List<Coup> coupsPossible = new ArrayList<Coup>(); //coups possibles
	private static int[][] tabPonderation = {{7, 3, 6, 6, 6, 6, 3, 7}, {6, 3, 4, 4, 4, 4, 3, 6}, {3, 3, 3, 3, 3, 3, 3, 3}, {3, 3, 3, 3, 3, 3, 3, 3}, 
											{3, 3, 3, 3, 3, 3, 3, 3}, {3, 3, 3, 3, 3, 3, 3, 3}, {6, 3, 4, 4, 4, 4, 3, 6}, {7, 3, 6, 6, 6, 6, 3, 7}} ;
	
	
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
		manageCoupsPossible(new Coup(i, j, couleur));
		this.othellier[i][j] = couleur;
	}

	private void manageCoupsPossible(Coup coup) {
		// Parcours de la liste de coup initialis� avec une couleur VIDE
		Log.v("manageCoupsPossibles", "Gestion de " + coup.getLigne() + "," + coup.getColonne());
		for (Coup c : getCaseProche(coup)) {
			if (!existeDansCoupPossible(c, false)) {
				Log.v("manageCoupsPossibles", "Ajout de " + c.getLigne() + "," + c.getColonne());
				coupsPossible.add(c);
				
			}
		}
		// Si le coup �tait dans la liste, on le sort
		existeDansCoupPossible(new Coup(coup.getLigne(), coup.getColonne(), Jeton.VIDE), true);
	}

	private boolean existeDansCoupPossible(Coup coup, boolean suppress) {
		for (Coup c : coupsPossible) {
			if (c.getLigne() == coup.getLigne() && c.getColonne() == coup.getColonne()) {
				if (suppress) {
					coupsPossible.remove(c);
				}
				return true;
			}
		}
		return false;
	}

	public static int getNbLignes() {
		return NUM_LIGNES;
	}

	public byte getJeton(int x, int y) { // retourne la couleur du jeton pr�sent
											// sur le plateau aux coordonn�es
											// x,y
		return othellier[x][y];
	}

	public int nombreJetons(int couleur) { // retourne le nombre de jetons, de
											// la couleur donn�e, pr�sents sur
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
		boolean valide = false;
		if (othellier[origine.getLigne()][origine.getColonne()] == Jeton.VIDE) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 || y != 0) {
						Coup check = parcourirDroite(origine, x, y);
						if (check != null) {
							valide = true;
							if (retourner) {
								Coup checkRetournement = new Coup(
										origine.getLigne(),
										origine.getColonne(),
										origine.getCouleur());
								while (check.getLigne() != checkRetournement
										.getLigne()
										|| check.getColonne() != checkRetournement
												.getColonne()) {
									checkRetournement = new Coup(
											checkRetournement.getLigne() + x,
											checkRetournement.getColonne() + y,
											origine.getCouleur());
									othellier[checkRetournement
											.getLigne()][checkRetournement
											.getColonne()] = origine
											.getCouleur();
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
	private Coup parcourirDroite(Coup origine, int coeffX, int coeffY) {
		boolean parcours = true;
		Coup coupResult = null;
		Coup check = new Coup(origine.getLigne(), origine.getColonne(),
				origine.getCouleur());
		int distance = 0;
		while (parcours) {
			distance++;
			check = new Coup(check.getLigne() + coeffX, check.getColonne()
					+ coeffY, origine.getCouleur());
			// Test si le pion est dans le plateau
			parcours = check.getColonne() >= 0
					&& check.getColonne() < NUM_LIGNES && check.getLigne() >= 0
					&& check.getLigne() < NUM_LIGNES;
			if (parcours) {
				// Il y a quelque chose sur la case
				if (othellier[check.getLigne()][check.getColonne()] != Jeton.VIDE) {
					// Jeton de couleur identique de l'origine
					if (origine.getCouleur() == othellier[check
							.getLigne()][check.getColonne()]) {
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

	private List<Coup> getCaseProche(Coup origine) {
		byte[][] othellierParcours = othellier;
		List<Coup> toReturn = new ArrayList<Coup>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x != 0 || y != 0) {
					Coup check = new Coup(origine.getLigne() + x, origine.getColonne() + y, origine.getCouleur());
					if (check.getColonne() >= 0	&& check.getColonne() < NUM_LIGNES 
							&& check.getLigne() >= 0 && check.getLigne() < NUM_LIGNES) {
						// La case est vide
						if (othellierParcours[check.getLigne()][check.getColonne()] == Jeton.VIDE) {
							toReturn.add(new Coup(check.getLigne(), check.getColonne(), Jeton.VIDE));
						}
					}
				}
			}
		}
		return toReturn;
	}

	/**
	 * Retourne les coups possibles pour un joueur donn�
	 * 
	 * @param joueurEnCours
	 * @return
	 */
	public List<Coup> getMouvementPossible(byte couleur) {
		List<Coup> toReturn = new ArrayList<Coup>();
		for (Coup c : coupsPossible) {
			if (isCoupValide(new Coup(c.getLigne(), c.getColonne(), couleur), false)) {
				toReturn.add(new Coup(c.getLigne(), c.getColonne(), couleur));
			}
		}
		return toReturn;
	}

	public int getRetournementPossibleEnRetournant(Coup origine) {
		int nombreRetournement = 0;
		if (othellier[origine.getLigne()][origine.getColonne()] == Jeton.VIDE) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 || y != 0) {
						Coup check = parcourirDroite(origine, x, y);
						if (check != null) {
							Coup checkRetournement = new Coup(
									origine.getLigne(), origine.getColonne(),
									origine.getCouleur());
							while (check.getLigne() != checkRetournement
									.getLigne()
									|| check.getColonne() != checkRetournement
											.getColonne()) {
								checkRetournement = new Coup(
										checkRetournement.getLigne() + x,
										checkRetournement.getColonne() + y,
										origine.getCouleur());
								othellier[checkRetournement.getLigne()][checkRetournement
										.getColonne()] = origine.getCouleur();
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
