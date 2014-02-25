package com.ema.othelloVE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.AsyncTask;

import com.ema.arbre.Node;
import com.ema.arbre.Algo;

public class JoueurIA extends Joueur implements JoueurIAAction {

	private int force;
	private Coup p;

	
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
		return this.calculMinCoupAdversaire(1, true);
	}
	
	private Coup calculMinCoupAdversaire(int profondeur, boolean minMax) {
		List<Coup> coupsPossible = plateau.getMouvementPossible(this.getCouleur());
		Coup coupTemp;
		int maxi = Integer.MIN_VALUE;
		int maxIndex = -1;
		
		for (int i = 0; i < coupsPossible.size(); i++) {
			coupTemp = coupsPossible.get(i);
			
			Node node = new Node(plateau, coupTemp, this.getCouleur(), Node.MAX, profondeur, null);
		
			int heuristique = 0;
			
			if (minMax) {
				heuristique = Algo.minMax(node);
			} else {
				heuristique = Algo.alphaBeta(node);
			}
			
			if (heuristique > maxi) {
				maxIndex = i;
				maxi = heuristique;
			}

		}
		
		return coupsPossible.get(maxIndex);
	}

	private Coup calculCoupExpert() {
		return this.calculMinCoupAdversaire(3, false);

	}

}
