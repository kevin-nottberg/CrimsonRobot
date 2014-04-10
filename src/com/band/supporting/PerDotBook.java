package com.band.supporting;

import java.util.ArrayList;

import com.band.render.RawDot;

public class PerDotBook {
	
	ArrayList<RawDot> dotBook;
	String id;
	String section;
	
	public PerDotBook() {
		dotBook = new ArrayList<RawDot>();
	}
	
	public void init() {
		dotBook = new ArrayList<RawDot>();
	}
	
	public void addDot( RawDot dot ) {
		dotBook.add( dot );
	}
	
	public int getBpm( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getBPM();
	}
	
	public int getSetCount( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getSetCount();
	}
	
	public int getSide( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getSide();
	}
	
	public int getHorizontal( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getHorizontal();
	}
	
	public String getHorizontalDirection( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getHorizontalDirection();
	}
	
	public int getHorizontalStep( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getHorizontalStep();
	}
	
	public String getVertical( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getHorizontalDirection();
	}
	
	public String getVerticalDirection( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getVerticalDirection();
	}
	
	public int getVerticalStep( int currDot ) {
		RawDot dot = new RawDot();
		dot = dotBook.get( currDot );
		return dot.getVerticalStep();
	}
	
	public void setId( String marcherId ) {
		id = marcherId;
	}
	
	public String getId() {
		return id;
	}
	
	public void setSection( String sectionId ) {
		section = sectionId;
	}
	
	public String getSection() {
		return section;
	}
	
	public int getSize() {
		return dotBook.size();
	}
	
}
