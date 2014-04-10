package com.band.activities;

import java.util.ArrayList;

import com.band.gen.R;
import com.band.supporting.DotBookParser;
import com.band.supporting.PerDotBook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PersonalDotBook extends BaseAct {
	
	DotBookParser parser;
	ArrayList<PerDotBook> dotBooks;
	ArrayList<String> idList;
	ArrayList<String> bpmList;
	ArrayList<String> setCountList;
	ArrayList<String> sideToSide;
	ArrayList<String> frontToBack;
	String selectedID;
	int currDot;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		parser = new DotBookParser( this );
		currDot = 0;
		sideToSide = new ArrayList<String>();
		frontToBack = new ArrayList<String>();
		
		getDotBook();
		setContentView(R.layout.personal_layout);
		//View setting 
		Spinner spinner = (Spinner) findViewById( R.id.spinner3 );
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, idList );
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		// Apply the adapter to the spinner
		spinner.setAdapter( adapter );
	}
	
	// Gets the raw dotBook and loads it into list
	public void getDotBook() {
		parser.initRaw( "rawMasterDotBook.xml" );
		dotBooks = parser.getRawDotBooks();
		idList = new ArrayList<String>();
		setIdList();
	}
	
	public void setIdList() {
		for( int i = 0; i < dotBooks.size(); i++ ) {
			idList.add( dotBooks.get( i ).getId() );
		}
	}
	
	/* 
	 * Create the strings that will be put
	 * in the side to side and front to back
	 * string list
	 */
	public void createStrings() {
		PerDotBook dotBook = new PerDotBook();
		for( int i = 0; i < dotBooks.size(); i++ ) {
			if( dotBooks.get( i ).getId() == selectedID ) {
				dotBook = dotBooks.get( i );
				break;
			}
		}
		
		for( int i = 0; i < dotBook.getSize(); i++ ) {
			String bpm = Integer.toString( dotBook.getBpm( i ) );
			String setCount = Integer.toString( dotBook.getSetCount( i ) );
			
			bpmList.add( bpm );
			setCountList.add( setCount );
			
			String side = Integer.toString( dotBook.getSide( i ) );
			
			String horiz = Integer.toString( dotBook.getHorizontal( i ) );
			String horizDir = dotBook.getHorizontalDirection( i );
			String horizStep = Integer.toString( dotBook.getHorizontalStep( i ) );
			
			String sideSide = horizStep + " Steps " + horizDir + " the " + horiz + " on side " + side ;
			
			sideToSide.add( sideSide );
			
			String vert = dotBook.getVertical( i );
			String vertDir = dotBook.getVerticalDirection( i );
			if( vertDir == "back" ) {
				vertDir = " behind ";
			}
			if( vertDir == "front" ) {
				vertDir = " in front of ";
			}
			String vertStep = Integer.toString( dotBook.getVerticalStep( i ) );
			
			String frontBack = vertStep + " Steps " + vertDir + " the " + vert + " hash.";  
			
			frontToBack.add( frontBack );
		}
	}
	
	// Allows you to add notes page of dotBook
	public void addNote() {
		
	}
	
	public void deleteNote() {
		
	}
	
}
