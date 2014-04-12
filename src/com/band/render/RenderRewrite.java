package com.band.render;

import com.band.supporting.RawDotBook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RenderRewrite extends SurfaceView implements Runnable {
	
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean playing = false;
	Paint paint;
	MarcherList marcherList;
	ScreenHandler screenHandler;
	Context context;
	RawDotBook rawDotBook;
	float timeCounter;
	//PlayerManager playerManager;
	
	
	public RenderRewrite( Context cont, MarcherList list, ScreenHandler handlr ) {
		super( cont );
		context = cont;
		paint = new Paint();
		paint.setColor( Color.WHITE );
		holder = getHolder();
		screenHandler = handlr;
		//screenHandler.setState( State.READY );
		Log.d("render", "Finished with constructor");
		rawDotBook = new RawDotBook( context, screenHandler );
		timeCounter = 0;
	}
	
	@Override
	public void run() {
		long startTime = System.nanoTime();
		while( playing ) {
			
			if ( screenHandler.state == State.INITRUN ) {
				if(!holder.getSurface().isValid())
					continue; 
				Canvas canvas = holder.lockCanvas();
				
				screenHandler.initHndler( canvas );
				rawDotBook.init( "rawMasterDotBook.xml" );
				screenHandler.init();
				
				holder.unlockCanvasAndPost( canvas );
				
				screenHandler.setState( State.PAUSED );
			}
			
			// Executes when the state is running and needs to be updated and drawn
			if( screenHandler.state == State.RUNNING ) {
				// Time block 
				float deltaTime = ( System.nanoTime() - startTime ) / 1000000000.0f;
				startTime = System.nanoTime();
				float beatsPassed = screenHandler.getBeatsPassed( deltaTime );
				timeCounter += deltaTime;
				Log.d( "renderRe", "DeltaTime: " + deltaTime );
				Log.d( "renderRe", "BeatsPassed: " + beatsPassed );
				if( !screenHandler.isLastDot() ) {
					screenHandler.update( beatsPassed );
				}
				//Draw Block
				if(!holder.getSurface().isValid())   
					continue;
				Canvas canvas = holder.lockCanvas();
				canvas.drawRGB( 148, 214, 107 );
				paint.setColor( Color.BLUE );
				screenHandler.present( canvas, paint );
				// Draw buttons
				holder.unlockCanvasAndPost( canvas );
				}
			
			// Executes when the user paused the player not android and the view still needs drawing
			if( screenHandler.state == State.PAUSED ) {
				if( !holder.getSurface().isValid() )
					continue;
				Canvas canvas = holder.lockCanvas();
				canvas.drawRGB( 148, 214, 107 );
				paint.setColor( Color.WHITE );
				screenHandler.present( canvas, paint );
				
				holder.unlockCanvasAndPost( canvas );
				startTime = System.nanoTime();
			}
			try {
				Thread.sleep( 380 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}	


	
	public void resume() {
		Log.d("render", "Called resume");
		//marcherList.resume();
		//playerManager.setState( State.READY );
		playing = true;
		renderThread = new Thread( this );
		renderThread.start();
		Log.d("render", "Finishd resume");
	}
	
	public void pause() {
		Log.d("render", "Called pause");
		playing = false;
		screenHandler.setState( State.PAUSED );
		while( true ) {
			try {
				if( renderThread != null ) {
					renderThread.join();
				}	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("render", "Finishd pause");
			return;
		}
	}
}
