package com.band.activities;

import java.util.ArrayList;

import com.band.gen.R;
import com.band.supporting.DotBookParser;
import com.band.supporting.PerDotBook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalDotBook extends BaseAct {
	
	DotBookParser parser;
	ArrayList<PerDotBook> dotBooks;
	ArrayList<String> idList;
	ArrayList<String> bpmList;
	ArrayList<String> setCountList;
	ArrayList<String> sideToSide;
	ArrayList<String> frontToBack;
	String selectedID;
	Button nextButton;
	Button backButton;
	Button addNote;
	TextView frontToBackText;
	TextView sideToSideText;
	int currDot;
	PerDotBook dotBookPub;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		dotBookPub = new PerDotBook();
		parser = new DotBookParser( this );
		currDot = 0;
		
		bpmList = new ArrayList<String>();
		setCountList = new ArrayList<String>();
		
		sideToSide = new ArrayList<String>();
		frontToBack = new ArrayList<String>();
		
		getDotBook();
		selectedID = idList.get( 0 );
		createStrings();
		setContentView(R.layout.personal_layout);
		//View setting 
		frontToBackText = (TextView)findViewById( R.id.textView2 );
		sideToSideText = (TextView)findViewById( R.id.textView1 );
		
		Spinner spinner = (Spinner) findViewById( R.id.spinner3 );
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, idList );
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		// Apply the adapter to the spinner
		spinner.setAdapter( adapter );
		
		backButton = (Button)findViewById( R.id.button4 );
		backButton.setOnClickListener( backDot );
		
		nextButton = (Button)findViewById( R.id.button5 );
		nextButton.setOnClickListener( nextDot );
		
		sideToSideText.setText( sideToSide.get( currDot ) );
		frontToBackText.setText( frontToBack.get( currDot ) );
		
		spinner.setOnItemSelectedListener( new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d( "perDotBook", "In on selected" );
				Log.d( "perDotBook", "Id before: " + selectedID );
				selectedID = idList.get( position );
				Log.d( "perDotBook", "Id changed: " +selectedID );
				createStrings();
				updateDots();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		/*
		addNote = (Button)findViewById( R.id.button6 );
		addNot.setOnClickListener( noteHandler );
		*/
	}
	
	View.OnClickListener nextDot = new View.OnClickListener() {
			
			@Override
			public void onClick( View v ) {
				if( currDot == dotBookPub.getSize() - 1 ) {
					Toast.makeText( getBaseContext(), "Last Dot", Toast.LENGTH_SHORT );
					return;
				}
				if( currDot < dotBookPub.getSize() ) {
					Log.d( "perDotBook", "dotBookSize: " + dotBookPub.getSize() );
					currDot++;
					updateDots();
				}
			}	
	};
	
	View.OnClickListener backDot = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( currDot > 0 ) {
					currDot--;
					updateDots();
				}
			}
	};
	
	/*
	View.OnClickListener noteHandler = new View.OnClickListener() {
			
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
	*/
	
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
		
		bpmList.clear();
		setCountList.clear();
		sideToSide.clear();
		frontToBack.clear();
		
		Log.d( "perDotBook", "create string dotBook size: " + dotBook.getSize() );
		for( int i = 0; i < dotBooks.size(); i++ ) {
			if( dotBooks.get( i ).getId() == selectedID ) {
				dotBook = dotBooks.get( i );
				break;
			}
		}
		
		for( int i = 0; i < dotBook.getSize(); i++ ) {
			String bpm = Integer.toString( dotBook.getBpm( i ) );
			Log.d( "perDotBook", "Bpm: " + bpm );
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
			Log.d( "perDotBook", "vertdir right after: " + vertDir );
			if( vertDir.equals( "front" ) ) {
				vertDir = " in front of ";
				Log.d( "perDotBook", "Verdir in front if statement block" );
			}
			if( vertDir.equals( "back" ) ) {
				vertDir = " behind ";
			}
			String vertStep = Integer.toString( dotBook.getVerticalStep( i ) );
			
			String frontBack = vertStep + " Steps" + vertDir + "the " + vert + " hash.";  
			
			frontToBack.add( frontBack );
			dotBookPub = dotBook;
		}
	}
	
	public void updateDots() {
		sideToSideText.setText( sideToSide.get( currDot ) );
		frontToBackText.setText( frontToBack.get( currDot ) );
		Log.d( "perDotBook", "CurrDot: " + currDot );
	}
	
	// Allows you to add notes page of dotBook
	public void addNote() {
		
	}
	
	public void deleteNote() {
		
	}
	
}	
