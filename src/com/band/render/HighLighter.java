package com.band.render;

import java.util.ArrayList;

import com.band.supporting.DotBookParser;
import com.band.supporting.DotBookSupport;

import android.content.Context;
import android.graphics.Color;

public class HighLighter {
	
	Context context;
	
	ArrayList<String> idList;
	ArrayList<String> sectionList;
	//ArrayList<HighLight> idList;
	//ArrayList<HighLight> sectionList;
	DotBookParser parser;
	ArrayList<DotBookSupport> dotBook;
	Color color;
	
	public HighLighter( Context cont ) {
		parser = new DotBookParser( context );
		parser.init( "masterDotBookFile.xml" );
		
		dotBook = parser.getDotBooks();
		for( int i = 0; i < dotBook.size(); i++ ) {
			idList.add( dotBook.get( i ).getId() );
			sectionList.add( dotBook.get( i ).getSection() );
		}
		
		
	}	
	
	public ArrayList<String> getIdList() {
		return idList;
	}
	
	public ArrayList<String> getSectionList() {
		return sectionList;
	}
	
	/*
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
	*/
}
