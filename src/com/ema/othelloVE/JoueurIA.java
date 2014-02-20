package com.ema.othelloVE;

import java.util.ArrayList;
import java.util.Random;

import android.os.AsyncTask;

import com.ema.arbre.ArbreNAire;
import com.ema.arbre.main;

public class JoueurIA extends Joueur implements JoueurIAAction {

	private int force;
	private Coup p;
	private int[][] tabPonderation ;
	
	private static final int DEBUTANT = 1;
	private static final int MOYEN = 2;
	private static final int EXPERT = 3;
	private ControleurJeu controlJeu;

	private final String TAG = JoueurIA.class.getSimpleName();

	public JoueurIA(byte couleur, Plateau plateau, int niveau,
			ControleurJeu control) {
		super(couleur, plateau, true);
		force = niveau;
		controlJeu = control;
		fillTabPonderation();
	}

	private void fillTabPonderation() {
		tabPonderation = new int[plateau.getNbLignes()][plateau.getNbLignes()];
		int[] patternCorner = {7, 3, 6, 6, 6, 6, 3, 7};
		int[] patternClassicLine = {6, 3, 4, 4, 4, 4, 3, 6};	
		int[] patternNearCorner = {3, 3, 3, 3, 3, 3, 3, 3};
		for (int i=0;i<plateau.getNbLignes(); i++) { // Line
			for (int j=0;j<plateau.getNbLignes(); j++) { // Column
				if(i==0 || i==plateau.getNbLignes()-1) { // Corner
					tabPonderation[i][j] = patternCorner[j];
				} else if(i==1 || i==plateau.getNbLignes()-2) { // Just before or after the corner
					tabPonderation[i][j] = patternNearCorner[j];
				} else { // A simple line
					tabPonderation[i][j] = patternClassicLine[j];
				}
			}
		}		
	}

	private class JoueurExpert extends AsyncTask<Void, Void, Coup> { // classe
																		// permettant
																		// de
																		// lancer
																		// une
																		// tâche
																		// en
																		// fond
																		// (pour
																		// les
																		// calculs
																		// longs)
		protected void onPreExecute() {
		}

		protected Coup doInBackground(Void... aVoid) {
			p = calculCoupExpert();
			return p;
		}

		protected void onPostExecute(Coup coupExpert) {
			p = coupExpert;
			sendResult(p);

		}
	}

	private void sendResult(Coup coup) { // retour du coup calculé au contrôleur
		MyEventCoupIA event = new MyEventCoupIA();
		event.coup = coup;
		controlJeu.publishEvent(event);
	}

	// A COMPLETER
	public void calculCoup() { // calcul du coup à jouer en fonction de la force
								// du joueurIA
		p = null;
		switch (force) {
		case DEBUTANT:
			p = calculCoupDebutant(); // fonction courte qui peut s'exécuter
										// suite à la demande du contrôleur
			sendResult(p);
			break;
		case MOYEN:
			p = calculCoupMoyen(); // fonction courte qui peut s'exécuter suite
									// à la demande du contrôleur
			sendResult(p);
			break;
		case EXPERT:
			new JoueurExpert().execute(); // fonction chronophage : à lancer en
											// tâche de fond
			break;

		}

	}

	/**
	 * retourne un coup possible choisi aléatoirement
	 * 
	 * @return CoupDebutant
	 */
	private Coup calculCoupDebutant() {
		Random r = new Random();
		// Coup compris entre 0 et taille de la liste de coup possibles
		int coupAleatoire = 0 + r.nextInt(plateau.getMouvementPossible(this.getCouleur())
				.size() - 0);

		return (plateau.getMouvementPossible(this.getCouleur()).get(coupAleatoire));

	}

	private Coup calculCoupMoyen() {
		return (Coup)this.calculMinCoupAdversaire(2).get(0);
	}
	
	private byte getCouleurAdverse(byte couleur) {
		byte couleurAdverse = Jeton.NOIR;
		
		if (couleur == Jeton.NOIR) {
			couleurAdverse = Jeton.BLANC;
		}
		return couleurAdverse;
	}
	
	private ArrayList<Object> calculMinCoupAdversaire(int profondeur) {
		plateau.resetSurcharge();
		byte[][] surchargeSav = plateau.cloneSurcharge();
		int nbCoups = plateau.getMouvementPossible(this.getCouleur()).size();
		int minRetournement = -1;
		Coup coupTemp;
		int nbRetournement;
		
		ArbreNAire<Integer> arbre = new ArbreNAire<Integer>(1);
		
		
		for (int i = 0; i < nbCoups; i++) {
			plateau.setSurcharge(surchargeSav);
			arbre.addFils(arbre.getItem() * 10 + i);
			arbre.goToFils(i);
			//le coup de l'IA
			coupTemp = plateau.getMouvementPossible(this.getCouleur(), true).get(i);
		
			//nombre de retournement en jouant ce coup
			nbRetournement = plateau.getRetournementPossibleEnRetournant(coupTemp);
			
			//on place le jeton sur la surcharge
			plateau.setSurchargePlateau(coupTemp.getLigne(), coupTemp.getColonne(), this.getCouleur());
			
			arbre.setHeuristique(nbRetournement*tabPonderation[coupTemp.getLigne()][coupTemp.getColonne()]);
			
			this.createChildTree(plateau, arbre, getCouleurAdverse(this.getCouleur()), profondeur-1);
			
			arbre.goToPere();
		}
		arbre.goToRacine();
		main.depthSearch(arbre);
		ArrayList<Integer> resultMinMax = main.minMax(arbre, profondeur);
		
		ArrayList<Object> result= new ArrayList<Object>();
		result.add(plateau.getMouvementPossible(this.getCouleur()).get(resultMinMax.get(0)));
		result.add(minRetournement);
		
		return result;
	}
	
	private ArbreNAire<Integer> createChildTree(Plateau plateau, ArbreNAire<Integer> arbre, byte couleur, int profondeur) {
		byte[][] surchargeSav = plateau.cloneSurcharge();
		int nbCoups = plateau.getMouvementPossible(couleur, true).size();
		Coup coupTemp;
		int nbRetournement;
		
		for (int i = 0; i < nbCoups; i++) {
			plateau.setSurcharge(surchargeSav);
			arbre.addFils(arbre.getItem() * 10 + i);
			arbre.goToFils(i);
			//le coup de l'IA
			coupTemp = plateau.getMouvementPossible(couleur, true).get(i);
		
			//nombre de retournement en jouant ce coup
			nbRetournement = plateau.getRetournementPossibleEnRetournant(coupTemp);
			
			//on place le jeton sur la surcharge
			plateau.setSurchargePlateau(coupTemp.getLigne(), coupTemp.getColonne(), couleur);
			
			arbre.setHeuristique(nbRetournement);
			
			if (profondeur > 1) {
				this.createChildTree(plateau, arbre, getCouleurAdverse(couleur), profondeur-1);
			}
			
			arbre.goToPere();
		}
		
		return arbre;
	}

	private Coup calculCoupExpert() {
		return (Coup)this.calculMinCoupAdversaire(4).get(0);

	}

}