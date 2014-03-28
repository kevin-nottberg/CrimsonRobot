package com.band.render;

import android.graphics.Color;

public class HighLighter {
	
	String marcherID;
	String sectionID;
	Color color;
	
	public HighLighter() {
		
	}
	
	public void setMarcherID( String marcher ) {
		marcherID = marcher;
	}
	
	public String getMarcherID() {
		return marcherID;
	}
	
	public void setSectionID( String section ) {
		sectionID = section;
	}
	
	public String getSectionID() {
		return sectionID;
	}
	
	public void setColor( Color col ) {
		color = col;
	}
	
	public Color getColor() {
		return color;
	}
}
