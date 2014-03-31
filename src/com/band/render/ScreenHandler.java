package com.band.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.widget.Button;

/** 
 * Class that will handle multiple screen sizes
 *  for the dot renderer. This is will need to be revised
 *  to be fast.
 *  
 *  
 *  @param HORFIELDX = 8 The constant ratio of a football field 
 *  @param HORFIELDY = 15 The constant ration of a football field
 */

public class ScreenHandler {
	
	// Screen Handler
	float HEIGHT;
	int WIDTH;
	int STEP;
	int HORPIXMARG;
	int VERTPIXMARG;
	int HORFIELDX;
	int HORFIELDY;
	float VERTFIELDHASH;
	int VERTFIELDY;
	float deadSpaceHeight;
	ArrayList<Button> buttonList;
	AssetManager assets;
	Bitmap pause;
	
	// PlayerManager 
	State state;
	Context context;
	float BPM;
	float beatCoeificient;
	MarcherList marcherList;
	int marcherNum;
	Display display;
	
	
	public ScreenHandler( MarcherList mList, Context cont ) {
		marcherList = mList;
		context = cont;
		buttonList = new ArrayList<Button>();
		createBitMap();
		/*
		 * I will then evenly space the UI buttons within this dead space
		 * PAUSE, SET CHOOSER / SETTER, PLAY, UI control 
		*/
	}
	
	public void drawButtons( Canvas canvas ) {
		// Create and design the button UI
	}
	
	public void update( float beatsPassed ) {
		marcherList.update( beatsPassed );
	}
	
	public void present( Canvas canvas, Paint paint ) {
		marcherList.draw( canvas, paint );
		paint.setColor( Color.BLACK );
		canvas.drawRect( 0 , HEIGHT - deadSpaceHeight, WIDTH, HEIGHT, paint );
		canvas.drawBitmap(pause, WIDTH - 75, HEIGHT, paint );
		for( int i = 0; i <= 19; i++ ) {
			paint.setColor( Color.WHITE );
			int xDraw = ( HORPIXMARG * i );
			// Yard line draw
			canvas.drawRect( xDraw - 1, 0, xDraw + 1, HEIGHT - deadSpaceHeight, paint );
			// Hash line draw
			canvas.drawRect( xDraw - 4, (VERTPIXMARG * 1) - 2, xDraw + 4, (VERTPIXMARG * 1) + 2, paint);
			canvas.drawRect( xDraw - 4, (VERTPIXMARG * 2) - 2, xDraw + 4, (VERTPIXMARG * 2) + 2, paint);
		}
	}
	
	public boolean isLastDot() {
		if( marcherList.isLastDot() == true ){
			setState(State.PAUSED);
			return true;
		} else {
			return false;
		}
	}
	
	public void init() {
		marcherList.init( marcherNum );
	}
	
	public void initHndler( Canvas canvas ) {
		WIDTH = canvas.getWidth(); 
		HEIGHT = canvas.getHeight();
		HORFIELDY = 15;
		HORFIELDX = 8;
		VERTFIELDY = 160;
		VERTFIELDHASH = 53.4f;
		deadSpaceHeight = ( HEIGHT - (( HORFIELDX * WIDTH ) / HORFIELDY ));
		 
		HORPIXMARG = ( WIDTH / 19 ) + 1;
		VERTPIXMARG = (int) (( (HEIGHT - deadSpaceHeight) * VERTFIELDHASH ) / VERTFIELDY);
		STEP = ( HORPIXMARG / 5 );
	}
	
	public void setState( State st ) {
		state = st;
	}
	
	public State getState() {
		return state;
	}
	
	public float getBeatsPassed( float deltaTime ) {
		return deltaTime / .375f;
	}
	
	public int getHorizontalPixMarg() {
		return HORPIXMARG;
	}
	
	public int getVerticalPixMarg() {
		return VERTPIXMARG;
	}
	
	public float getDeadSpace() {
		return deadSpaceHeight;
	}
	
	public int getStep() {
		return STEP;
	}
	
	public int getMarcherNum() {
		return marcherNum;
	}
	
	public void setMarcherNum( int marcher ) {
		marcherNum = marcher;
	}
	
	public void createBitMap()
	{
		try {
			//Log.d( "bitmapCalss", "Bitmap try called");
			assets = context.getAssets();
			InputStream inputStream = assets.open( "pause.jpg" );
			pause = BitmapFactory.decodeStream( inputStream );
			inputStream.close();
		} catch (IOException e) {
			Log.d( "bitmapClass", "Bitmap IO prob" );
			// Spacer commet
		} finally {
		}
	}
}
