/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ema.arbre;

import com.ema.othelloVE.Jeton;
import com.ema.othelloVE.Plateau;


/**
 *
 * @author Admin
 */
public class ArbreNAire<T>{
	/*private Noeud<T> racine;
	private Noeud<T> vue;
	int profondeur;
	
	public int getProfondeur() {
		return profondeur;
	}

	public void setProfondeur(int profondeur) {
		this.profondeur = profondeur;
	}

	private byte getCouleurAdverse() {
		byte couleurAdverse = Jeton.NOIR;
		
		if (this.getCouleur() == Jeton.NOIR) {
			couleurAdverse = Jeton.BLANC;
		}
		return couleurAdverse;
	}
	
	public ArbreNAire() {
	}

	public ArbreNAire(T racine) {
		this.racine = new Noeud(racine);
		this.vue = this.racine;
	}

	public void initRacine(T item) {
		this.racine = new Noeud(item);
		this.vue = this.racine;
	}

	public void addFils(T item) {
		this.vue.addFils(item);
	}

	public T getItem() {
		return this.vue.getInfo();
	}

	public boolean isRacine() {
		return this.vue.isRacine();
	}

	public boolean isNoeudFeuille() {
		return this.profondeur == 0;
	}

	public int getNbFils() {
		return this.plateau.getMouvementPossible(this.getCouleurAdverse()).size();
	}
	
	public void createFils(T item) {
		byte[][] surchargeSav = this.vue.plateau.cloneSurcharge();
		this.vue.addFils(item);
		this.goToFils(item);
		this.plateau.getMouvementPossible(this.getCouleurAdverse()).size();
		
		plateau.setSurcharge(surchargeSav);
	}

	public void goToRacine() {
		this.vue = this.racine;
	}

	public void goToFils(int i) {
		this.vue = vue.getFils(i);
	}

	public void goToPere() {
		this.vue = vue.getPere();
	}

	public void suppressNoeud() {
		this.vue.suppressNoeud();
	}

	@Override 
	public String toString() {
		return this.vue.toString();
	}
	
	public int getHeuristique() {
		
		
		
		
		return this.vue.getHeuristique();
	}
	
	public boolean getType() {
		return this.vue.type;
	}

	public void setType(boolean isMin) {
		this.vue.type = isMin;
	}


	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}
	
	public Plateau getPlateau() {
		return this.plateau;
	}

	public Byte getCouleur() {
		return this.vue.couleur;
	}

	public void setCouleur(Byte couleur) {
		this.vue.couleur = couleur;
	}*/
	
}
