package com.band.supporting;

import java.util.ArrayList;

import com.band.render.RawDot;

public class PerDotBook {
	
	ArrayList<RawDot> dotBook;
	String id;
	String section;
	
	public void DotBook() {
		dotBook = new ArrayList<RawDot>();
	}
	
	public void addDot( RawDot dot ) {
		dotBook.add( dot );
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
	
}
