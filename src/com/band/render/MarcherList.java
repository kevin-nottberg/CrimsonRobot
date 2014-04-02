package com.band.render;

import java.io.IOException;
import java.util.ArrayList;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class MarcherList {
	
	public ArrayList<Marcher> marcherList;
	int beatCounter;
	int setCount;
	Context context;
	int currDot;
	
	public MarcherList( Context cont ) {
		marcherList = new ArrayList<Marcher>();
		beatCounter = 0;
		context = cont;
		currDot = 0;
	}
	
	public void init( int size ) {
		Log.d("debugMarcherList", "Started with the init");
		for( int i = 0; i < size; i++ ) {
			Marcher marcher = new Marcher(context);
			marcherList.add(marcher);
		}
		for( int i = 0; i < marcherList.size(); i++ ) {
			marcherList.get(i).init( "masterDotBookFile.xml", i );
		}
		Log.d("debugMarcherList", "Ended with the init");
	}
	
	public void draw( Canvas canvas, Paint paint ){
		for( int i = 0; i < marcherList.size(); i++ ) {
			// Create
			Log.d( "render", "Marcher: " + i );
			marcherList.get( i ).draw( canvas, paint );
		}
		Log.d( "Dot", "MarcherList size: "+ marcherList.size() );
	}
	
	/**
	 * @param beatsPassed is the change in time divided by how much time a beat would take up in a minute based on the BPM. Should be updates.
	 */
	
	public void update( float beatsPassed ) {
		// Loop through the the list and call update function and passed the beatsPassed
		Log.d( "Update", "Updating" );
		Log.d( "Update", "Dot: " + reachedDot() );
		if( reachedDot() ) {
			Log.d( "Update", "Call full update" );
			fullUpdate( beatsPassed );
		} else {
			Log.d( "Update", "Calling normal update" );
			for( int i = 0; i < marcherList.size(); i++ ) {
				marcherList.get( i ).update( beatsPassed );
			}
		}
		
		//setCount = getSetCount();
		beatCounter += beatsPassed;
		Log.d( "Dot", "beatCounter: " + beatCounter );
	}

	
	public boolean reachedDot() {
		Log.d( "Update", "In reached dot" );
		if( beatCounter >= setCount ) {
			return true;
		} else {
			return false;
		}
	}
	
	public void fullUpdate( float beatsPassed ) {
		Log.d( "FullUpdate", "Reached full update" );
		currDot++;
		for ( int i = 0; i < marcherList.size(); i++ ) {
			Log.d( "FullUpdate", "MarcherList Size:" + marcherList.size() );
			Log.d( "render", "Marcher: " + i );
			marcherList.get( i ).fullUpdate( beatsPassed );
		}
		beatCounter = 0;
		Log.d( "FullUpdate", "Out of full update" );
	}
	
	public int getBPM() {
		// Return the BPM listed
		return marcherList.get( 0 ).getBPM();
	}
	
	public int getSetCount() {
		// I/O To retrieve the set count and pass it to the Renderer 
		return marcherList.get( 0 ).getSetCount();
	}
	
	public boolean isLastDot() {
		if( marcherList.get( 0 ).isLastDot() == State.PAUSED ) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setCurrDot( int dot ) {
		currDot = dot;
		Log.d("marcherDot", "Set dot");
		for( int i = 0; i < marcherList.size(); i++ ) {
			marcherList.get( i ).setCurrDot( dot );
		}
	}
	
	public int getCurrDot() {
		return currDot;
	}
}
