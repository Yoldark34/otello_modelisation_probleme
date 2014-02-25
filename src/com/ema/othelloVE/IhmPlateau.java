package com.ema.othelloVE;

import com.ema.othelloVE.Plateau;
import com.ema.othelloVE.Jeton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class IhmPlateau extends View {

	private static final int NB_LIG = 8;
	private static final int NB_COL = NB_LIG;
	private static final int NB_COORD = 4;

	private int tailleJeton;
	private int tailleGrille;
	private int largeurPlateau;
	private Plateau othellier;
	private ControleurJeu controleur;
	private Context context;


	public IhmPlateau(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
		context=aContext;
	}


	public void setControleur(ControleurJeu jeu) {
		controleur = jeu;
	}

	public void initPlateau(Plateau plateau) {
		othellier = plateau;
	}

	protected void onDraw(Canvas aCanvas) {
		int width = getWidth();
		int height = getHeight();

		Log.v(this.toString(), "onDraw " + width + height);

		// orientation tablette
		if (width > height) {
			// landscape
			largeurPlateau = height;
		} else {
			// portrait
			largeurPlateau = width;
		}

		// calcul taille Grille
		if ((largeurPlateau % NB_LIG) >= 0.5)
			tailleGrille = largeurPlateau / NB_LIG + 1;
		else
			tailleGrille = largeurPlateau / NB_LIG;

		// ajustement taille Jeton
		tailleJeton = tailleGrille / 2 - 2;

		Log.v(this.toString(), "onDraw " + tailleGrille);

		// affichage plateau
		affichePlateau(aCanvas);

		// affichage jetons
		try {
			
			afficheJetons(aCanvas);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

		
	protected void afficheJetons(Canvas aCanvas) { 
		Log.v(this.toString(), "drawButtons");
		int x, y; // coordonnées affichage
		
		if (othellier != null) {

			for (int i = 0; i < NB_COL; i++) {
				for (int j = 0; j < NB_LIG; j++) {
					byte jeton = othellier.getJeton(i, j);
					byte couleurJoueurEnCours = controleur.getCurrentPlayer().couleur;
					aCanvas.drawText(j+","+i, (i * tailleGrille) + tailleGrille / 2, (j * tailleGrille) + tailleGrille / 2, new Paint(Color.BLACK));
					if( (jeton != Jeton.VIDE )){
						x = (i * tailleGrille) + tailleGrille / 2;
						y = (j * tailleGrille) + tailleGrille / 2;
						afficheJeton(aCanvas, x, y, jeton, true);
					}
					/*else if (jeton == Jeton.VIDE && othellier.isCoupValide(i,j,couleurJoueurEnCours, false)) {
						afficheJeton(aCanvas, (i * tailleGrille) + tailleGrille / 2, (j * tailleGrille) + tailleGrille / 2, couleurJoueurEnCours, false);
					}*/
				}
			}
		}
		Log.v(this.toString(), "drawButtons <<");
	}

	protected void afficheJeton(Canvas aCanvas, int aX, int aY, byte aColor, boolean filled) {
		Log.v(this.toString(), "drawButton");
		Paint paint = new Paint();

		if (aColor == Jeton.NOIR) {
			paint.setColor(Color.BLACK);
		} else if (aColor == Jeton.BLANC) {
			paint.setColor(Color.WHITE);
		} else if (aColor == Jeton.VIDE) {
			 paint.setColor(Color.GREEN);
		 }
		if(!filled) {
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(2);
		} 
		aCanvas.drawCircle(aX, aY, tailleJeton, paint);
	}

	protected void affichePlateau(Canvas aCanvas) {
		// grille : 14 lignes, 4 coordonnées par ligne
		float grid[] = new float[(NB_COL - 1 + NB_LIG - 1) * NB_COORD];

		Log.v(this.toString(), "affichagePlateau");

		Paint green = new Paint();
		green.setColor(Color.GREEN);

		Rect r = new Rect(0, 0, largeurPlateau, largeurPlateau);
		aCanvas.drawRect(r, green);

		// white grid
		Paint white = new Paint();
		white.setColor(Color.WHITE);

		for (int i = 0; i < NB_LIG - 1; i++) {
			grid[NB_COORD * i] = 0;
			grid[NB_COORD * i + 1] = tailleGrille * (i + 1);
			grid[NB_COORD * i + 2] = largeurPlateau;
			grid[NB_COORD * i + 3] = tailleGrille * (i + 1);
		}

		int verticalStart = (NB_COL - 1) * NB_COORD;

		for (int i = 0; i < NB_COL - 1; i++) {
			//
			grid[NB_COORD * i + verticalStart] = tailleGrille * (i + 1);
			grid[NB_COORD * i + verticalStart + 1] = 0;
			grid[NB_COORD * i + verticalStart + 2] = tailleGrille * (i + 1);
			grid[NB_COORD * i + verticalStart + 3] = largeurPlateau;
		}

		aCanvas.drawLines(grid, white);
	}

	@Override
	public boolean onTouchEvent(MotionEvent aEvent) {

		if (aEvent.getAction() == MotionEvent.ACTION_DOWN) {
			Point place = positionSurPlateau(aEvent.getX(), aEvent.getY());

			if (place.x < NB_COL && place.y < NB_LIG) {
				MyEventMotion event = new MyEventMotion();
				event.x = place.x;
				event.y = place.y;
				controleur.publishEvent(event);
			}
		}
		return true;
	}

	protected Point positionSurPlateau(float aX, float aY) {
		Point place = new Point(NB_COL + 1, NB_LIG + 1);

		if (aX >= 0 && aX <= tailleGrille * NB_COL) {
			if (aY >= 0 && aY <= tailleGrille * NB_LIG) {
				place.x = (int) aX / tailleGrille;
				place.y = (int) aY / tailleGrille;
			}
		}
		return place;
	}


	public void notifyTourPassed() {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(getContext(),
				"Vous passez votre tour", duration);
		toast.show();		
	}

}