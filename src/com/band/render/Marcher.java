package com.band.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/* 
 * This class will manage the math 
 * for the dotBook for every Individual
 * marcher and will make all the calls to
 * the dotBook
 */

public class Marcher {
	
	public float oldX;
	public float oldY;
	public float currX;
	public float currY;
	public float moveX;
	public float moveY;
	public float newY;
	public float newX;
	public int currDot;
	public int BPM;
	public int setCount;
	public DotBook dotBook;
	public String iden;
	
	public Marcher( Context context ) {
		dotBook = new DotBook( context );
		currDot = 0;
	}
	
	public void init( String path, int increment ) {
		Log.d("debugMarcherList", "In the marcher init");
		dotBook.init( path, increment );
		Dot dot = dotBook.getDot( currDot );
		currX = dot.getX();
		currY = dot.getY();
		oldX = currX;
		oldY = currY;
		setCount = dot.getSetCount();
		BPM = dot.getBPM();
		Log.d("Marcher" , " BPM " + BPM );
		
		dot = dotBook.getDot( currDot + 1 );
		newX = dot.getX();
		newY = dot.getY();
		
		moveX = ( newX - currX ) / setCount;
		moveY = ( newY - currY ) / setCount;
		
		Log.d( "Dot", "Init dotX: " + oldX );
		Log.d( "Dot", "Init Set Count: " + setCount );
		Log.d( "Dot", "Init BPM: " + BPM );
		Log.d( "Dot", "Init dotY: " + oldY );
		Log.d("debugMarcherList", "Out of the dotbook and marcher init()");
	}
	
	/**
	@param beatsPassed is the change in time divided by how much time a beat would take up in a minute based on the BPM. Should be updates.
	*/
	
	public void update( float beatsPassed ) {
		currX += ( moveX * beatsPassed );
		currY += ( moveY * beatsPassed );
	}
	
	/**
	 * Function is called when the set x and y was reached and then the y and x move needs to be updated 
	 * It will then handle all the I/O for grabing the new X and Y from the dotbook
	 */
	public void fullUpdate( float beatsPassed ) {
		currDot++;
		oldX = newX;
		oldY = newY;
		Dot dot = dotBook.getDot( currDot );
		BPM = dot.getBPM();
		setCount = dot.getSetCount();
		newX = dot.getX();
		newY = dot.getY();
		moveX = ( newX - currX ) / setCount;
		moveY = ( newY - currY ) / setCount;
		currX += ( moveX * beatsPassed );
		currY += ( moveY * beatsPassed );
	}
	
	public State isLastDot() {
		if( currDot == dotBook.getSize() - 1 ) {
			return State.PAUSED;
		} else {
			return State.RUNNING;
		}
	}
	
	public State isFirstDot() {
		if( currDot == 0 ) {
			return State.PAUSED;
		} else {
			return State.RUNNING;
		}
	}
	
	public void draw( Canvas canvas, Paint paint ) {
		canvas.drawCircle( currX, currY, 4.0f, paint );
		Log.d( "render ", "CurrX: " + currX );
		Log.d( "render ", "CuurY: " + currY );
	}
	
	public int getBPM() {
		return BPM;
	}
	
	public int getSetCount() {
		return setCount;
	}
	
	public String getId() {
		iden = dotBook.getId();
		return iden;
	}
	
	public void setCurrDot( int dot ) {
		currDot = dot;
		updateCurrDot( dot );
	}
	
	public void updateCurrDot( int selectDot ) {
		Dot dot = dotBook.getDot( selectDot );
		BPM = dot.getBPM();
		setCount = dot.getSetCount();
		oldX = dot.getX();
		oldY = dot.getY();
		currX = dot.getX();
		currY = dot.getY();
		
		
		// Next dot loaded if user presses play
		dot = dotBook.getDot( selectDot + 1 );
		newX = dot.getX();
		newY = dot.getY();
		moveX = ( newX - oldX ) / setCount;
		moveY = ( newY - oldY ) / setCount;
		Log.d( "marcherDot", "Select CurrX: " + currX + "Select CurrY: " + currY );
		
		
	}
}