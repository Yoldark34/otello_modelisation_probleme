/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ema.arbre;

/**
 *
 * @author Admin
 */
public class Algo {

	public static Integer minMax(Node arbre) {
		int val;

		if (!arbre.isNoeudFeuille()) {
			int min = -1;
			int max = -1;
			boolean first = true;
			for (int i=0; i < arbre.getNbFils(); i++) {
				val = minMax(arbre.getFils(i));
				if (first || (arbre.getType() == Node.MAX && arbre.getHeuristique() - val > max)) { // Traitement Max
					max = arbre.getHeuristique() - val;
				}
				if (first || (arbre.getType() == Node.MIN && arbre.getHeuristique() + val < min)) { // Traitement Min
					min = arbre.getHeuristique() + val;
				}
				first = false;
			}
			if (arbre.getType() == Node.MIN) {
				val = min;
			} else {
				val = max;
			}
			
		}
		else {
			val = arbre.getHeuristique();
		}
		return val;
	}
	
	public static int alphaBeta(Node arbre) {
		return alphaBeta(arbre, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public static int alphaBeta(Node arbre, int alpha, int beta) {	
		/* alpha est toujours inférieur à beta */
		int val;
		if (arbre.isNoeudFeuille()) {
			// P est une feuille alors
			val = arbre.getHeuristique();
		} else {
			if (arbre.getType() == Node.MIN) {
				val = Integer.MAX_VALUE;
				
				for (int i = 0; i < arbre.getNbFils(); i++) {
					
					val = Math.min(val, alphaBeta(arbre.getFils(i), alpha, beta));
					if (alpha >= val) {
						/* coupure alpha */
						return val;
					}
					beta = Math.min(beta, val);
				}
			} else {
				val = Integer.MIN_VALUE;
				for (int i = 0; i < arbre.getNbFils(); i++) {
					
					val = Math.max(val, alphaBeta(arbre.getFils(i), alpha, beta));
					if (val >= beta) {
						/* coupure beta */
						return val;
					}
					alpha = Math.max(alpha, val);
				}
			}
		}
		return val;
	}
}
