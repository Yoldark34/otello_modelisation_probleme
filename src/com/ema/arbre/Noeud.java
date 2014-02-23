/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ema.arbre;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Noeud <T> {
	private T info;
	boolean isMin;

	public boolean isMin() {
		return isMin;
	}

	public void setMin(boolean isMin) {
		this.isMin = isMin;
	}

	public T getInfo() {
		return info;
	}

	public int getHeuristique() {
		return heuristique;
	}

	public void setHeuristique(int heuristique) {
		this.heuristique = heuristique;
	}
	private Noeud<T> pere;
	private final ArrayList<Noeud<T>> fils  = new ArrayList<Noeud<T>>();
	private int heuristique;

	public Noeud(T info) {
		this.info = info;
	}
	
	public void Noeud(T item, Noeud<T> pere) {
		this.info = item;
		this.pere = pere;
	}

	public Noeud(T info, Noeud<T> pere) {
		this.info = info;
		this.pere = pere;
	}
	
	public void addFils(T item) {
		this.fils.add(new Noeud(item, this));
	}
		
	public boolean isRacine() {
		return null == pere;
	}
	
	public boolean isNoeudFeuille() {
		return fils.isEmpty();
	}
	
	public int getNbFils() {
		return fils.size();
	}

	public Noeud<T> getPere() {
		return pere;
	}
	
	public Noeud<T> getFils(int i) {
		return this.fils.get(i);
	}
	
	public void suppressNoeud() {
		if (this.getNbFils() > 0) {
			for (Noeud n : fils) {
				n.suppressNoeud();
				n = null;
			}
		}
	}
	public String toString() {
		return info.toString();
	}
}
