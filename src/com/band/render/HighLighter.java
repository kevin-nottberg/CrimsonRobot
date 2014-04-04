package com.band.render;

import java.util.ArrayList;

import com.band.supporting.DotBookParser;

import android.content.Context;
import android.graphics.Color;

public class HighLighter {
	
	Context context;
	
	ArrayList<HighLight> idList;
	ArrayList<HighLight> sectionList;
	DotBookParser parser;
	Color color;
	
	public HighLighter( Context cont, MarcherList marcherList ) {
		parser = new DotBookParser( context );
		parser.init( "masterDotBookFile.xml" );
	}	
	
	public ArrayList<HighLight> getIdList() {
		return idList;
	}
	
	public ArrayList<HighLight> getSectionList() {
		return sectionList;
	}
	
	public void setHighLightById( String id, Color color ) {
		HighLight highLight = new HighLight();
		highLight.setColor( color );
		highLight.setId( id );
		idList.add( highLight );
	}
	
	public void setHighLightBySection( String section, Color color ) {
		HighLight highLight = new HighLight();
		highLight.setColor( color );
		highLight.setSection( section );
		sectionList.add( highLight );
	}
}
