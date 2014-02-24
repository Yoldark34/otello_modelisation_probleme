package com.ema.othelloVE;

import java.util.ArrayList;

import android.util.Log;

public class ControleurJeu implements Runnable {

	private Plateau plateau;
	private IhmPlateau ihmPlateau;
	private IhmScore ihmScore;

	private int niveauIA;
	private boolean isIANoir;
	private boolean isIA;

	private Joueur joueur1;
	private Joueur joueur2;
	private Joueur joueurEnCours;

	private final String TAG = ControleurJeu.class.getSimpleName();
	private boolean fin = false;

	public ControleurJeu(int level) {
		niveauIA = level;
		plateau = new Plateau();
		joueur1 = new JoueurIA(Jeton.NOIR, plateau, niveauIA, this);
		joueur2 = new JoueurIA(Jeton.BLANC, plateau, niveauIA, this);
		joueurEnCours = joueur1;
	}

	public ControleurJeu(int level, boolean IANoir, boolean IA) {
		// initialisation du plateau
		// nitialisationdes joueurs en fonction de la sélection de l'interface
		// utilisateur
		niveauIA = level;
		isIANoir = IANoir;
		isIA = IA;
		plateau = new Plateau();

		if (!isIA) {
			joueur1 = new Joueur(Jeton.NOIR, plateau, false);
			joueur2 = new Joueur(Jeton.BLANC, plateau, false);
			joueurEnCours = joueur1;
		} else {
			if (isIANoir) {
				joueur1 = new JoueurIA(Jeton.NOIR, plateau, niveauIA, this);
				joueur2 = new Joueur(Jeton.BLANC, plateau, false);
				joueurEnCours = joueur1;

			} else {
				joueur2 = new JoueurIA(Jeton.BLANC, plateau, niveauIA, this);
				joueur1 = new Joueur(Jeton.NOIR, plateau, false);
				joueurEnCours = joueur1;
			}
		}

	}

	public void setIhm(IhmPlateau ihm) {
		// initialisation du lien entre le contrôleur et l'ihm plateau
		ihmPlateau = ihm;
	}

	public void setIhmScore(IhmScore score) {
		// initialisation fdu lien entre le contrôleur et l'ihm score
		ihmScore = score;
	}

	public void start() {
		// initialisation des interfaces et affichage
		plateau.initPlateau();
		ihmPlateau.initPlateau(plateau);
		ihmScore.setScore(plateau.nombreJetons(Jeton.BLANC),
				plateau.nombreJetons(Jeton.NOIR), Jeton.NOIR);
		updateUI();
	}

	public void changeIACouleur(boolean IANoir) {
		// modification de la couleur de l'automate par demande de l'interface
		isIANoir = IANoir;
	}

	public void changeNiveauIA(int level) {
		// modification du niveau de l'automate par demande de l'interface
		niveauIA = level;
	}

	private ArrayList<MyEvent> events = new ArrayList<MyEvent>();
	private boolean iaReflechi = false;

	@Override
	public void run() {
		// thread du contrôleur
		// attente d' événements fournis par l'interface graphique (EventMotion)
		// si mode de jeu manuel
		// ou par l'automate (EventCoupIA) et par l'interface graphique
		// (EventMotion) alternativement si mode de jeu semi-automatique

		Log.v(TAG, "start");
		Coup p;
		plateau.initPlateau();
		ihmPlateau.initPlateau(plateau);
		ihmScore.setScore(plateau.nombreJetons(Jeton.BLANC),
				plateau.nombreJetons(Jeton.NOIR), Jeton.NOIR);
		updateUI();

		if (joueurEnCours.isIA()) {
			synchronized (events) {
				iaReflechi = true;
			}
			((JoueurIA) joueurEnCours).calculCoup();

		}
		while (!fin) {
			MyEvent event = null;
			synchronized (events) {
				try {
					if (events.isEmpty()) {
						events.wait();
					}
					event = events.remove(0);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			synchronized (events) {
				if (event instanceof MyEventMotion) {
					MyEventMotion myEventMotion = (MyEventMotion) event;
					if (plateau.isCoupValide(new Coup(myEventMotion.x,
							myEventMotion.y, joueurEnCours.getCouleur()), true)) {
						Log.i("Coup", "Valide");

						if (!iaReflechi) {
							// A COMPLETER
							// mettre à jour le plateau par retournement des
							// pions
							// exemple : mise à jour du plateau par pion joué
							// par
							// l'humain :
							plateau.setPlateau(myEventMotion.x,
									myEventMotion.y, joueurEnCours.getCouleur());
							// faire le changement du joueur courant
							changeJoueurEnCours();

							// mise à jour de l'état de l'IA
							iaReflechi = false;

							// mise à jour de l'affichage
							updateUI();
						} else {
							Log.i("Coup", "Invalide");
						}
					}
				} else if (event instanceof MyEventCoupIA) {
					MyEventCoupIA myEventunCoup = (MyEventCoupIA) event;
					if (plateau.isCoupValide(
							new Coup(myEventunCoup.coup.getLigne(),
									myEventunCoup.coup.getColonne(),
									joueurEnCours.getCouleur()), true)) {
						Log.i("Coup", "Valide");

						iaReflechi = false;

						// A COMPLETER
						// vérifier si coup valide
						// mettre à jour le plateau par retournement des pions
						// exemple : mise à jour du plateau par pion joué par
						// l'automate :
						plateau.setPlateau(myEventunCoup.coup.getLigne(),
								myEventunCoup.coup.getColonne(),
								joueurEnCours.getCouleur());
						// faire le changement du joueur courant
						changeJoueurEnCours();
						// mise à jour de l'affichage
						updateUI();
					} else {
						Log.i("Coup", "Invalide");
					}
				} else {
					throw new java.lang.Error();
				}

				// verification si joueur en cours peut jouer
				// A COMPLETER
				// si joueur en cours est de type IA : mettre à jour iaReflechi
				// et lancer la demande de calcul du coup:
				if (joueurEnCours.isIA && !plateau.isFull()) {
					iaReflechi = true;
					((JoueurIA) joueurEnCours).calculCoup();

				}
				//
			}

			// positionner booleen fin quand aucun des deux joueurs ne peut plus
			// jouer.
			// A COMPLETER
			// fin = .... ;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// fin de partie : mise à jour de l'interface score
		updateMessageFin();
	}

	protected void updateUI() {
		// mise à jour des interfaces et affichage
		ihmScore.setScore(plateau.nombreJetons(Jeton.BLANC),
				plateau.nombreJetons(Jeton.NOIR), joueurEnCours.getCouleur());

		ihmPlateau.postInvalidate();
		ihmScore.postInvalidate();
	}

	private void changeJoueurEnCours()
	// modification du joueur en cours
	{
		if (joueurEnCours == joueur1) {
			joueurEnCours = joueur2;
		} else {
			joueurEnCours = joueur1;
		}
		if (plateau.isFull()
				|| (plateau.getMouvementPossible(joueur1.couleur).isEmpty() 
						&& plateau.getMouvementPossible(joueur2.couleur).isEmpty())) {
			fin = true;
		}
		// Change de joueur si le nouveau joueur ne peux rien faire
		else if (plateau.getMouvementPossible(joueurEnCours.couleur).isEmpty()) {
			changeJoueurEnCours();
		}

	}

	public void publishEvent(MyEvent event) {
		// abonnement aux événements émis par le joueur IA
		synchronized (events) {
			events.add(event);
			events.notifyAll();
		}
	}

	private void updateMessageFin() { // mise à jour interface score et
										// affichage
		String gagnant = "", msg;
		Boolean egalite = false;
		if (plateau.nombreJetons(Jeton.BLANC) > plateau
				.nombreJetons(Jeton.NOIR))
			gagnant = "BLANC";
		else if (plateau.nombreJetons(Jeton.BLANC) < plateau
				.nombreJetons(Jeton.NOIR))
			gagnant = "NOIR";
		else
			egalite = true;
		if (egalite)
			msg = "FIN DE LA PARTIE :  EGALITE ENTRE LES JOUEURS !";
		else
			msg = "FIN DE LA PARTIE : " + gagnant + " a gagné !";

		ihmScore.setScoreFin(msg, plateau.nombreJetons(Jeton.BLANC),
				plateau.nombreJetons(Jeton.NOIR), joueurEnCours.getCouleur());
		ihmScore.postInvalidate();

	}

	public Joueur getCurrentPlayer() {
		return joueurEnCours;
	}

}