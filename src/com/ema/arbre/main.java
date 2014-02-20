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
public class main {

	/**
	 * @param args the command line arguments
	 */
	/*public static void main(String[] args) {
		ArbreNAire<Integer> arbre = createArbre(3,2);
		depthSearch(arbre);
		arbre.goToRacine();
		minMax(arbre, 2);
	}*/
	
	private static ArbreNAire<Integer> createArbre(int largeur, int profondeur) {
		ArbreNAire<Integer> arbre = new ArbreNAire<Integer>(1);
		createArbre(arbre, largeur, profondeur);
		arbre.goToRacine();
		return arbre;
	}
	
	private static void createArbre(ArbreNAire<Integer> arbre, int largeur, int profondeur) {
		if (profondeur > 0) {
			for (int i = 0; i < largeur; i++) {
				int number = arbre.getItem() * 10 + i;
				arbre.addFils(arbre.getItem() * 10 + i);
				
				arbre.goToFils(i);
				arbre.setHeuristique(number);
				createArbre(arbre, largeur, profondeur - 1);
				arbre.goToPere();
			}
		}
	}
	
	public static void depthSearch(ArbreNAire<Integer> arbre) {
		if (!arbre.isRacine()) {
			System.out.println(" - ");
		}
		for (int i = 0; i < arbre.getNbFils(); i++) {
			arbre.goToFils(i);
			System.out.println(arbre.getItem() + " ( " + arbre.getHeuristique() + " ) ");
			depthSearch(arbre);
			arbre.goToPere();
		}
	}
	
	public static ArrayList<Integer> minMax(ArbreNAire<Integer> arbre, int profondeur) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>();
		toReturn.add(0);
		boolean minMax=true; // max = true
		int max = -1;
		int maxHeuristiqueItem = -1;
		for (int i=0;i<arbre.getNbFils(); i++) {
			arbre.goToFils(i);
			ArrayList<Integer> result = minMax(arbre, profondeur-1, !minMax);
			if (max == -1 || max < arbre.getHeuristique() - result.get(1)) {
				max = arbre.getHeuristique() - result.get(1);
				maxHeuristiqueItem = i;
				toReturn.set(0, i);
			}
			arbre.goToPere();
		}
		toReturn.add(max);
		arbre.goToFils(maxHeuristiqueItem);
		return toReturn;
	}
	
	private static ArrayList<Integer> minMax(ArbreNAire<Integer> arbre, int profondeur, boolean minMax) {
		ArrayList<Integer> toReturn = new ArrayList<Integer>(); // 0 : index / 1 : heuristique
		toReturn.add(0);
		if (profondeur > 0 && arbre.getNbFils() > 0) {
			int min = -1;
			int max = -1;
			for (int i=0; i < arbre.getNbFils(); i++) {
				arbre.goToFils(i);
				ArrayList<Integer> result = minMax(arbre, profondeur-1, !minMax);
				if (max == -1 || (minMax == true && arbre.getHeuristique() - result.get(1) > max)) { // Traitement Max
					max = arbre.getHeuristique() - result.get(1);
					toReturn.set(0, i);
				}
				if (min == -1 || (minMax == false && arbre.getHeuristique() + result.get(1) < min)) { // Traitement Min
					min = arbre.getHeuristique() + result.get(1);
					toReturn.set(0, i);
				}
				
				arbre.goToPere();
			}
			if (minMax == false) {
				toReturn.add(min);
			} else {
				toReturn.add(max);
			}
			
		}
		else {
			toReturn.add(arbre.getHeuristique()	);
		}
		return toReturn;
	}
	
	public int alphaBeta(ArbreNAire<Integer> arbre) {
		return alphaBeta(arbre, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public int alphaBeta(ArbreNAire<Integer> arbre, int alpha, int beta) {
		/* alpha est toujours inf�rieur � beta */
		int val;
		if (arbre.isNoeudFeuille()) {
			// P est une feuille alors
			val = arbre.getHeuristique();
		} else {
			if (arbre.isMin()) {
				val = Integer.MAX_VALUE;
				for (int i = 0; i < arbre.getNbFils(); i++) {
					arbre.goToFils(i);
					
					val = Math.min(val, alphaBeta(arbre, alpha, beta));
					if (alpha >= val) {
						/* coupure alpha */
						return val;
					}
					beta = Math.min(beta, val);
					arbre.goToPere();
				}
			} else {
				val = Integer.MIN_VALUE;
				for (int i = 0; i < arbre.getNbFils(); i++) {
					arbre.goToFils(i);
					val = Math.max(val, alphaBeta(arbre, alpha, beta));
					if (val >= beta) {
						/* coupure beta */
						return val;
					}
					alpha = Math.max(alpha, val);
					arbre.goToPere();
				}
			}
		}
		return val;
	}
}
