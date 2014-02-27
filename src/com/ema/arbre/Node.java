package com.ema.arbre;

import java.util.List;

import com.ema.othelloVE.Coup;
import com.ema.othelloVE.Jeton;
import com.ema.othelloVE.Plateau;

public class Node {
	public final static boolean MIN = false;
	public final static boolean MAX = true;
	Plateau plateau;
	List<Coup> coups;
	Byte couleur;
	int profondeur;
	int heuristique;
	Node pere = null;
	boolean type;
	
	
	public Node() {
	}

	public Node(Plateau plateau, Coup coup, byte couleur, boolean type, int profondeur, Node pere) {
		this.plateau = new Plateau(plateau);

		if (coup != null) {
			this.plateau.getRetournementPossibleEnRetournant(coup.getColonne(), coup.getColonne(), coup.getCouleur());
			this.plateau.setPlateau(coup.getLigne(), coup.getColonne(), couleur);
		}
	
		this.couleur = couleur;
		this.profondeur = profondeur;
		this.coups = this.plateau.getMouvementPossible(this.getCouleurAdverse());
		this.type = type;
		this.pere = pere;
	}
	
	public int getNbFils() {
		if (this.coups.size() == 0) {
			this.coups.add(null);
		}
		
		return this.coups.size();
	}
	
	public Node getFils(int i) {
		int profondeurFils = this.profondeur;
		if (this.getType() == Node.MAX) {
			profondeurFils = this.profondeur - 1;
		}
		

		Coup coupTemp = this.coups.get(i);
		
		return new Node(this.plateau, coupTemp, this.getCouleurAdverse(), !this.type, profondeurFils, this);
	}
	
	public Plateau getPlateau() {
		return plateau;
	}
	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}
	public List<Coup> getCoups() {
		return coups;
	}
	public void setCoups(List<Coup> coups) {
		this.coups = coups;
	}
	public Byte getCouleur() {
		return couleur;
	}
	public void setCouleur(Byte couleur) {
		this.couleur = couleur;
	}
	public int getProfondeur() {
		return profondeur;
	}
	public void setProfondeur(int profondeur) {
		this.profondeur = profondeur;
	}
	public int getHeuristique() {
		List<Integer> pions = plateau.comptePionsAvecPonderation();
		
		int result = pions.get(0)-pions.get(1);
		
		if (this.couleur == Jeton.BLANC) {
			result = pions.get(1)-pions.get(0);
		}
		
		
		return result;
	}
	
	private byte getCouleurAdverse() {
		byte couleurAdverse = Jeton.NOIR;
		
		if (this.getCouleur() == Jeton.NOIR) {
			couleurAdverse = Jeton.BLANC;
		}
		return couleurAdverse;
	}
	
	public boolean getType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public boolean isNoeudFeuille() {
		return this.profondeur == 0;
	}
}
