package com.ema.othelloVE;

import com.ema.othelloVE.Jeton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;


	public class IhmScore extends RelativeLayout
	{
	    private TextView textTour;
	    private TextView textNoir;
	    private TextView textBlanc;
	    private static final String BLANK = "blank";
	    
	    private int nbBlancs;
	    private int nbNoirs;
	    private byte couleurEnCours;
	    
	    private String msgFin;

	    public IhmScore(Context aContext, AttributeSet aAttrs)
	    {
	    	
	    	
	        super(aContext, aAttrs);

	        textTour = new TextView(aContext);
	        textTour.setText(BLANK);
	        LayoutParams tourParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	        tourParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	        textTour.setLayoutParams(tourParams);

	        textNoir = new TextView(aContext);
	        textNoir.setText(BLANK);
	        LayoutParams noirParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	        noirParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        noirParams.addRule(RelativeLayout.CENTER_VERTICAL);
	        textNoir.setLayoutParams(noirParams);


	        textBlanc = new TextView(aContext);
	        textBlanc.setText(BLANK);
	        LayoutParams blancParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	        blancParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        blancParams.addRule(RelativeLayout.CENTER_VERTICAL);
	        textBlanc.setLayoutParams(blancParams);

	        addView(textNoir);
	        addView(textBlanc);
	        addView(textTour);
	        
	        msgFin="";
	    }

	    public void setScore(int blancs, int noirs, byte courant)
	    {
	        nbBlancs = blancs;
	        nbNoirs = noirs;
	        couleurEnCours=courant;
	    }
	    
	    
	    public void setScoreFin(String msg,int blancs, int noirs, byte courant)
	    { 	nbBlancs = blancs;
        	nbNoirs = noirs;
        	couleurEnCours=courant;
	        msgFin=msg;

	    }
	    
	    protected void dispatchDraw (Canvas canvas){

	    	textBlanc.setText("Blanc: " + nbBlancs);
	        textNoir.setText("Noir: " + nbNoirs );
	        if (msgFin=="")
	        	if (couleurEnCours==Jeton.NOIR) 
	        		textTour.setText("Tour du joueur : Noir");
	        	else 
	        		textTour.setText("Tour du joueur : Blanc");
	        else
	        {
	        	textTour.setText(msgFin);
	        	textTour.setTextSize(24);
	        	textTour.setTextColor(Color.RED);	        		        	
	        }
	        
	    	super.dispatchDraw(canvas);
	    }


}