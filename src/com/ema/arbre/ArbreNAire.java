/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ema.arbre;

import com.ema.othelloVE.Plateau;


/**
 *
 * @author Admin
 */
public class ArbreNAire<T>{
	private Noeud<T> racine;
	private Noeud<T> vue;
	Plateau plateau;
	Byte couleur;
	
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
		return this.vue.isNoeudFeuille();
	}

	public int getNbFils() {
		//regarder nombre de coups etc etc
		
		return this.vue.getNbFils();
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

	public void setHeuristique(int heuristique) {
		this.vue.setHeuristique(heuristique);
	}
	
	public boolean isMin() {
		return this.vue.isMin();
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}
	
	public Plateau getPlateau() {
		return plateau;
	}

	public Byte getCouleur() {
		return couleur;
	}

	public void setCouleur(Byte couleur) {
		this.couleur = couleur;
	}
	
}
