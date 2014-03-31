package com.band.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.band.gen.R;
import com.band.render.MarcherList;
import com.band.render.RenderAct;
import com.band.render.RenderRewrite;
import com.band.render.ScreenHandler;
import com.band.render.State;
import com.band.supporting.RawDotBook;

/* 
 * This is the main class that will handle the screen
 * that links the user to all the different features of 
 * the Dot Book, Dot Book Renderer, Tuner, Metrophnome
 * Don't know if this will be self drawn or use the android
 * APIs to create the display yet.
 */

public class RenderActivity extends BaseAct implements OnTouchListener {

	MarcherList mList;
	RenderRewrite render;
	Button updateButton;
	Button dotBookParse;
	Button startButton;
	Display display;
	Context context;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		Log.d("debug", "In onCreate");
		
		context = this;
		
		mList = new MarcherList( this );
				
		final ScreenHandler handlr = new ScreenHandler( mList, this );
		handlr.initHndler( new Canvas() );
		
		Log.d("onCreate", "Done with marching list");
		render = new RenderRewrite( this, mList, handlr );
		Log.d("debug", "Finished making the DotBook render object");
		setContentView(R.layout.render_layout);
		// setContentView( render );
		updateButton = (Button) findViewById(R.id.button1);
		updateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent( getBaseContext(), DriveUpdate.class );
				startActivity(intent);
				Toast.makeText(getBaseContext(), "Updating", 10000);
			}
		});
		
		dotBookParse = (Button) findViewById( R.id.button2 );
		dotBookParse.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RawDotBook rawDotBook = new RawDotBook( context, handlr );
				rawDotBook.init( "masterDotBook.xml" );
				handlr.setState(State.RUNNING);
			}
		});
		startButton = (Button) findViewById( R.id.button3 );
		startButton.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handlr.init();
				setContentView(render);
				render.resume();
			}
		});
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//render.resume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		render.pause();
	}
	
	@Override
	public boolean onTouch( View v, MotionEvent event) {
		//int halfHeight = view.getHeight() / 2;
		int maxCount = event.getPointerCount();
		int action = MotionEventCompat.getActionMasked( event );
		Log.d("KevinApp", "OnTouch: " + String.valueOf( action ));
		for( int i = 0; i < maxCount; i++ )
		{
			//int mActivePointerId = event.getPointerId(i);
			// int pointerIndex = event.findPointerIndex(mActivePointerId);
			//int index = MotionEventCompat.getActionIndex(event);
			switch( action )
			{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_MOVE:
				Log.d( "KevinApp", "OnTouch" );
				
				//If on what button was pressed
				//if ( MotionEventCompat.getY( event, i) < halfHeight )
				{
					//curPointPosXtwo = (int) MotionEventCompat.getX( event, i );
					// curPointPosYtwo = (int) MotionEventCompat.getY(event, i);
					//view.invalidate();
				}
				//if ( MotionEventCompat.getY( event, i) > halfHeight )
				{
					//curPointPosXone = (int) MotionEventCompat.getX( event, i );
					// curPointPosYone = (int) MotionEventCompat.getY(event, i);
				}
			break;
			}
		}	
		return true;
	}
}
