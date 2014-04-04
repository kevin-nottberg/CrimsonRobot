package com.band.render;

import android.graphics.Color;

public class HighLight {
	
	String id;
	String section;
	Color color;
	
	public HighLight() {
		
	}
	
	public void setColor( Color col ) {
		color = col;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setId( String iden ) {
		id = iden;
	}
	
	public String getId() {
		return id;
	}
	
	public void setSection( String sec ) {
		section = sec;
	}
	
	public String getSection() {
		return section;
	}
}
