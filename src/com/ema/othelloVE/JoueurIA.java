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

	private Coup calculCoupDebutant() { // retourne un coup possible choisi
										// aléatoirement
										// A COMPLETER
		Log.i("TAB", "message");
		return (new Coup(4, 2, Jeton.NOIR));

	}

	private Coup calculCoupMoyen() { // retourne le coup qui maximise les
										// retournements
										// sur arbre de recherche développé à 1
										// niveau
										// A COMPLETER

		return (new Coup(4, 2, Jeton.NOIR));

	}

	private Coup calculCoupExpert() { // retourne le meilleur coup
										// obtenu par recherche Minmax sur arbre
										// de recherche développé à 2 ou 3
										// niveaux
										// A COMPLETER
		return (new Coup(4, 2, Jeton.NOIR));

	}

}