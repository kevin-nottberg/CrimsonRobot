package com.band.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.band.gen.R;
import com.band.render.HighLighter;
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
	ScreenHandler handlr;
	int startInc;
	HighLighter highLighter;
	ArrayList<String> idList;
	ArrayList<String> sectionList;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		Log.d("debug", "In onCreate");
		
		context = this;
		
		startInc = 0;
		
		mList = new MarcherList( this );
				
		handlr = new ScreenHandler( mList, this );
		
		//highLighter = new HighLighter( context );
		
		Log.d("onCreate", "Done with marching list");
		render = new RenderRewrite( this, mList, handlr );
		
		render.setOnTouchListener( this );
		Log.d("debug", "Finished making the DotBook render object");
		setContentView( R.layout.render_layout );
		// setContentView( render );
		updateButton = (Button) findViewById(R.id.button1);
		updateButton.setOnClickListener( updateHandlr );
		
		startButton = (Button) findViewById( R.id.button3 );
		startButton.setOnClickListener( startHandlr );
		
		//idList = highLighter.getIdList();
		//sectionList = highLighter.getSectionList();
		
		/*
		ArrayAdapter<String> idAdapter = new ArrayAdapter<String>( this, R.layout.relative_render_layout, idList );
		Spinner idList = (Spinner) findViewById(R.id.spinner1);
		idList.setAdapter( idAdapter );
		*/

	}
	
	public void startTouch() {
		render.setOnTouchListener( this );
	}
	
	View.OnClickListener updateHandlr = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// Starts Activity
			Intent intent = new Intent( getBaseContext(), DriveUpdate.class );
			startActivity(intent);
			
			//Handles the view
			setContentView( R.layout.render_layout );
			updateButton = (Button) findViewById(R.id.button1);
			updateButton.setOnClickListener( updateHandlr );
			
			startButton = (Button) findViewById( R.id.button3 );
			startButton.setOnClickListener( startHandlr );
			Toast.makeText(getBaseContext(), "Updating", Toast.LENGTH_SHORT).show();
		}
	};
	
	View.OnClickListener startHandlr = new View.OnClickListener() {
		
		@Override
		public void onClick( View v ) {
			// TODO Auto-generated method stub
			handlr.setState( State.INITRUN );
			Log.d("render", "Trying to start the render()");
			setContentView( render );
			render.resume();
		}
	};
	
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
	public boolean onTouch( View v, MotionEvent event ) {
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
				if ( MotionEventCompat.getY( event, i) <= 80 ) {
				
					// Play, pause button
					if ( MotionEventCompat.getX(event, i) >= 1280 - 75 && MotionEventCompat.getX(event, i) <= 1280 ) {
						if( handlr.getState() == State.PAUSED ) {
							handlr.setState( State.RUNNING );
						} else if( handlr.getState() == State.RUNNING ) {
							handlr.setState( State.PAUSED );
						}
					}
					
					// Dot selection buttons
					if ( MotionEventCompat.getX(event, i) >= 1280 - 175 && MotionEventCompat.getX(event, i) <= 1280 - 175 + 75 ) {
						handlr.setState( State.PAUSED );
						if( !handlr.isLastDot() ) {
							Log.d( "ButtonDebug", "Curr Dot: " + handlr.getCurrDot() );
							Log.d( "ButtonDebug", "Curr Dot + 1: " + (handlr.getCurrDot() + 1));
							handlr.setCurrDot( handlr.getCurrDot() + 1 );
							try {
								Thread.sleep( 150 );
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if( handlr.isLastDot() ) {
							Toast.makeText( getBaseContext(), "Is the last dot. Can't go any further!", Toast.LENGTH_SHORT ).show();
						}
					}
					
					if ( MotionEventCompat.getX(event, i) >= 1280 - 350 && MotionEventCompat.getX(event, i) <= 1280 - 350 + 75 ) {
						handlr.setState( State.PAUSED );
						if( !handlr.isFirstDot() ) {
							Log.d( "ButtonDebug", "Curr Dot: " + handlr.getCurrDot() );
							Log.d( "ButtonDebug", "Curr Dot - 1: " + ( handlr.getCurrDot() - 1 ) );
							try {
								Thread.sleep( 150 );
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							handlr.setCurrDot( handlr.getCurrDot() - 1 );
						} else if( handlr.isFirstDot() ) {
							Toast.makeText( getBaseContext(), "Is the first dot. Can't go back!", Toast.LENGTH_SHORT ).show();
						}
					}
					
					
					
					// Back button
					if ( MotionEventCompat.getX(event, i) <= 80 ) {
						render.pause();
						setContentView(R.layout.render_layout);
						updateButton = (Button) findViewById(R.id.button1);
						updateButton.setOnClickListener( updateHandlr );
						
						startButton = (Button) findViewById( R.id.button3 );
						startButton.setOnClickListener( startHandlr );
					}
					
				}
			/*	
			case MotionEvent.ACTION_UP:
				if ( MotionEventCompat.getY( event, i) <= 80 ) {
					
					// Play, pause button
					if ( MotionEventCompat.getX(event, i) >= 1280 - 75 && MotionEventCompat.getX(event, i) <= 1280 ) {
						if( handlr.getState() == State.PAUSED ) {
							handlr.setState( State.RUNNING );
						} else if( handlr.getState() == State.RUNNING ) {
							handlr.setState( State.PAUSED );
						}
					}
					
					// Dot selection buttons
					if ( MotionEventCompat.getX(event, i) >= 1280 - 175 && MotionEventCompat.getX(event, i) <= 1280 - 175 + 75 ) {
						handlr.setState( State.PAUSED );
						handlr.setCurrDot( handlr.getCurrDot() + 1 );
					}
					
					if ( MotionEventCompat.getX(event, i) >= 1280 - 350 && MotionEventCompat.getX(event, i) <= 1280 - 350 + 75 ) {
						handlr.setState( State.PAUSED );
						handlr.setCurrDot( handlr.getCurrDot() - 1 );
					}
					
					
					
					// Back button
					if ( MotionEventCompat.getX(event, i) <= 80 ) {
						render.pause();
						setContentView(R.layout.render_layout);
						updateButton = (Button) findViewById(R.id.button1);
						updateButton.setOnClickListener( updateHandlr );
						
						startButton = (Button) findViewById( R.id.button3 );
						startButton.setOnClickListener( startHandlr );
					}
					
				}
			*/	
			break;
			}
		}	
		return true;
	}
}
