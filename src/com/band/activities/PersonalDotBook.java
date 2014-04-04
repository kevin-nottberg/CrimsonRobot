package com.band.activities;

import java.util.ArrayList;

import com.band.supporting.DotBookParser;
import com.band.supporting.PerDotBook;

import android.os.Bundle;

public class PersonalDotBook extends BaseAct {
	
	DotBookParser parser;
	ArrayList<PerDotBook> dotBook;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		
		 parser = new DotBookParser( this );
	}
	
	// Gets the raw dotBook and loads it into list
	public void getDotBook() {
		parser.initRaw( "rawMasterDotBook.xml" );
		dotBook = parser.getRawDotBooks();
	}
	
	// Changes what id you retreve
	public void changeDotBook() {
		
	}
	
	// Allows you to add notes page of dotBook
	public void addNote() {
		
	}
	
	public void deleteNote() {
		
	}
	
}
