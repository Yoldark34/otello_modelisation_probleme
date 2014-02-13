package com.ema.othelloVE;

import java.util.ArrayList;
import java.util.Random;

import android.os.AsyncTask;
import android.util.Log;

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
		return (Coup)this.calculMinCoupAdversaire().get(0);
	}
	
	private ArrayList<Object> calculMinCoupAdversaire() {
		int nbCoups = plateau.getMouvementPossible(this.getCouleur()).size();
		int minCoups = -1;
		int indexMinCoups = -1;
		Coup coupTemp;
		int nbCoupsAdverse;
		byte couleurAdverse = Jeton.NOIR;
		
		if (this.getCouleur() == Jeton.NOIR) {
			couleurAdverse = Jeton.BLANC;
		}
		
		for (int i = 0; i < nbCoups; i++) {
			coupTemp = plateau.getMouvementPossible(this.getCouleur()).get(i);
			plateau.isCoupValide(coupTemp, true, true);
			nbCoupsAdverse = plateau.getMouvementPossible(couleurAdverse, true).size();
			if (nbCoupsAdverse < minCoups || minCoups == -1) {
				minCoups = nbCoupsAdverse;
				indexMinCoups = i;
			}
		}
		
		ArrayList<Object> result= new ArrayList<Object>();
		result.add(plateau.getMouvementPossible(this.getCouleur()).get(indexMinCoups));
		result.add(minCoups);
		
		return result;
	}

	private Coup calculCoupExpert() { // retourne le meilleur coup
										// obtenu par recherche Minmax sur arbre
										// de recherche développé à 2 ou 3
										// niveaux
										// A COMPLETER
		return (new Coup(4, 2, Jeton.NOIR));

	}

}