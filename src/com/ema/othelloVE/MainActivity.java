package com.ema.othelloVE;

import com.ema.othelloVE.ControleurJeu;
import com.ema.othelloVE.IhmPlateau;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Menu items
	private static final int MENU_QUIT = 0;
	private static final int MENU_NEW_GAME_LEVEL1 = 1;
	private static final int MENU_NEW_GAME_LEVEL2 = 2;
	private static final int MENU_NEW_GAME_LEVEL3 = 3;

	private static final int MENU_IA_BLANC = 10;
	private static final int MENU_IA_NOIR = 11;

	// String for menu items
	private static final String QUIT = "Quit";
	private static final String NEW_GAME1 = "Nouveau Jeu - Débutant";
	private static final String NEW_GAME2 = "Nouveau Jeu - Moyen";
	private static final String NEW_GAME3 = "Nouveau Jeu - Expert";
	private static final String IA_BLANC = "IA est Blanc";
	private static final String IA_NOIR = "IA est Noir";

	private ControleurJeu controlJeu = null;
	private boolean isIANoir = false;
	private boolean isIA = false;
	private IhmPlateau ihm = null;
	private String msgFin = "";

	private final String TAG = MainActivity.class.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle aSavedInstanceState) {
		Log.i(TAG, "OnCreate");
		super.onCreate(aSavedInstanceState);
	}

	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	protected void onStart() {
		Log.i(TAG, "OnStart");
		super.onStart();

	}

	protected void onRestart() {
		Log.i(TAG, "OnRestart");
		super.onRestart();
	}

	protected void onStop() {
		Log.i(TAG, "OnStop");
		super.onStop();
	}

	protected void onPause() {
		Log.i(TAG, "OnPause");
		super.onPause();
	}

	protected void onResume() {
		Log.i(TAG, "OnResume");
		super.onResume();
	}

	protected void onSaveInstanceState(Bundle aOutState) {
		Log.i(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(aOutState);
	}

	protected void onRestoreInstanceState(Bundle aSavedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState");
		super.onRestoreInstanceState(aSavedInstanceState);
	}

	public boolean onCreateOptionsMenu(Menu aMenu) {
		aMenu.add(0, MENU_QUIT, 0, QUIT);
		aMenu.add(0, MENU_NEW_GAME_LEVEL1, 0, NEW_GAME1);
		aMenu.add(0, MENU_NEW_GAME_LEVEL2, 0, NEW_GAME2);
		aMenu.add(0, MENU_NEW_GAME_LEVEL3, 0, NEW_GAME3);
		aMenu.add(0, MENU_IA_BLANC, 0, IA_BLANC);
		aMenu.add(0, MENU_IA_NOIR, 0, IA_NOIR);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem aItem) {
		switch (aItem.getItemId()) {
		case MENU_NEW_GAME_LEVEL1:
			nouveauJeu(MENU_NEW_GAME_LEVEL1, isIANoir);
			break;
		case MENU_NEW_GAME_LEVEL2:
			nouveauJeu(MENU_NEW_GAME_LEVEL2, isIANoir);
			break;
		case MENU_NEW_GAME_LEVEL3:
			nouveauJeu(MENU_NEW_GAME_LEVEL3, isIANoir);
			break;
		case MENU_IA_BLANC:
			isIANoir = false;
			isIA = true;
			break;
		case MENU_IA_NOIR:
			isIANoir = true;
			isIA = true;
			break;
		case MENU_QUIT:
			finish();
			break;
		}
		return true;
	}

	private void nouveauJeu(int level, boolean isIANoir) {
		setContentView(R.layout.activity_main);
		controlJeu = new ControleurJeu(level, isIANoir, isIA);
		ihm = (IhmPlateau) findViewById(R.id.ihmPlateau1);
		controlJeu.setIhm(ihm);
		ihm.setControleur(controlJeu);
		IhmScore ihmScore = (IhmScore) findViewById(R.id.ihmScore1);
		controlJeu.setIhmScore(ihmScore);

		Context context = ihm.getContext();
		CharSequence msg = " La partie commence  ! ";
		if (isIA)
			if (isIANoir)
				msg = msg + "  IA est NOIR";
			else
				msg = msg + "  IA est BLANC";
		else
			msg = msg + " (Mode MANUEL) ";
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();

		Thread thread = new Thread(controlJeu);
		thread.start();
	}

}
