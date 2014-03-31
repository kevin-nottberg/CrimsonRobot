package com.band.supporting;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

import com.band.render.RawDot;
import com.band.render.ScreenHandler;

public class RawDotBook {
	// Constants
	ScreenHandler hndler;
	static final String MARCHERLIST = "marcherlist";
	static final String MARCHER = "marcher";
	static final String BPM = "bpm";
	static final String DOT = "dot";
	static final String SETCOUNT = "setcount";
	static final String SIDE = "side";
	static final String HORIZONTAL = "horiz";
	static final String HORIZONTALDIRECTION = "horizdir";
	static final String HORIZONTALSTEP = "horizstep";
	static final String VERTICAL = "vert";
	static final String VERTICALDIRECTION = "vertdir";
	static final String VERTICALSTEP = "vertstep";
	XmlWriter xWriter;	
	// Other variables and arrays
	Context context;
	
	public RawDotBook( Context cont, ScreenHandler screenHandler ) {
		context = cont;
		hndler = screenHandler;
		xWriter = new XmlWriter();
		xWriter.startNewDoc();
	}
	
	public void init( String path ) {
		XmlParser parser = new XmlParser( context );
		Document doc = parser.getDomElement( path );
		Log.d("debugMarcherList", "Got parser and created got the doc");
		//Log.d("debugMarcherList", ""+doc.getTextContent());
		//NodeList id = doc.getElementsByTagName(ID);
		//Element el = ( Element ) id.item( 0 );
		//iden = parser.getElementValue(el);
		
		NodeList marcherList = doc.getElementsByTagName( MARCHER );
		hndler.setMarcherNum( marcherList.getLength() );
		System.out.println( "marcherlength" + marcherList.getLength() );
		for( int j = 0; j < marcherList.getLength(); j++ ) {
			NodeList dl = marcherList.item(j).getChildNodes();
			ArrayList<RawDot> dotBook = new ArrayList<RawDot>();
			System.out.println( "Iterator: " + j );
			for( int i = 0; i < dl.getLength(); i++ ) {
				if (dl.item(i).getNodeType() == Node.ELEMENT_NODE) {

					Element e = (Element) dl.item(i);
					RawDot dots = new RawDot();
					dots.setBpm(Integer.parseInt(parser.getValue(e, BPM)));
					dots.setCount(Integer.parseInt(parser.getValue(e, SETCOUNT)));
					dots.setSide(Integer.parseInt(parser.getValue(e, SIDE)));
					Log.d("driveTest", "Got here 1");
					dots.setHorizontal(Integer.parseInt(parser.getValue(e, HORIZONTAL)));
					Log.d("driveTest", "Got here 2");
					dots.setHorizontalDirection(parser.getValue(e, HORIZONTALDIRECTION));
					Log.d("driveTest",  "Got here 3");
					dots.setHorizontalStep(Integer.parseInt(parser.getValue(e, HORIZONTALSTEP)));
					dots.setVertical(parser.getValue(e, VERTICAL));
					Log.d("driveTest",  "Got here 4");
					dots.setVerticalDir(parser.getValue(e, VERTICALDIRECTION));
					Log.d("driveTest",  "Got here 5");
					dots.setVerticalStep(Integer.parseInt(parser.getValue(e, VERTICALSTEP)));
					Log.d("driveTest",  "Got here 6");
					
					dotBook.add(dots);
		//			Log.d("debugMarcherList", ""+dotBook.size());
		//			Log.d( "DotBook", "BPM: " + i + " " + dot.getBPM() );
		//			Log.d( "DotBook", "SetCount: " + i + " " + dot.getSetCount() );
				}

			}
			convertAndWrite( dotBook );
		}
		xWriter.finishAndWriteDoc( context, "masterDotBookFile.xml" );
		Log.d("debugMarcherList", "Done with the init"); 
	}
	
	public void convertAndWrite( ArrayList<RawDot> dotBook ) {
		int FIELD = 100;
		int SCREEN = 5;
		int HORPIXMARG = hndler.getHorizontalPixMarg();
		int STEP = hndler.getHorizontalPixMarg() / 8;
		int VERTPIXMARG = hndler.getVerticalPixMarg();
		xWriter.addMarcher();
		for( int i = 0; i < dotBook.size(); i++ ) {
			int x = 0;
			int y = 0;
			int setcount = 0;
			int bpm = 0;
			
			// Horizontal pixel if-block
			if( dotBook.get( i ).getSide() == 1 ) {
				int margCoeficient = ( dotBook.get( i ).getHorizontal() / SCREEN );
				int horizontal = dotBook.get(i).getHorizontal();
				Log.d( "render", "Horizontal side 1: " + horizontal);
				Log.d( "render", "Marg Coeficient Side 1: " + margCoeficient );
				x = HORPIXMARG * margCoeficient;
				Log.d( "render", "Side 1 X: " + x );
				if( dotBook.get( i ).getHorizontalDirection().equals("in") ) {
					x = x + ( dotBook.get(i).getHorizontalStep() * STEP );
				}
				if( dotBook.get( i ).getHorizontalDirection().equals("out") ) {
					x = x - ( dotBook.get(i).getHorizontalStep() * STEP );
				}
			}
			if( dotBook.get( i ).getSide() == 2 ) {
				//int margCoeficient = ( dotBook.get( i ).getHorizontal() * SCREEN ) / 2;
				//Log.d( "render", "Marg Coeficient Side 2: " + margCoeficient );
				int margCoeficient = 0;
				if( dotBook.get( i ).getHorizontal() == 45 ){
					margCoeficient = 11;
				}
				if( dotBook.get( i ).getHorizontal() == 40 ){
					margCoeficient = 12;
				}
				if( dotBook.get( i ).getHorizontal() == 35 ){
					margCoeficient = 13;
				}
				if( dotBook.get( i ).getHorizontal() == 30 ){
					margCoeficient = 14;
				}
				if( dotBook.get( i ).getHorizontal() == 25 ){
					margCoeficient = 15;
				}
				if( dotBook.get( i ).getHorizontal() == 20 ){
					margCoeficient = 16;
				}
				if( dotBook.get( i ).getHorizontal() == 15 ){
					margCoeficient = 17;
				}
				if( dotBook.get( i ).getHorizontal() == 10 ){
					margCoeficient = 18;
				}
				if( dotBook.get( i ).getHorizontal() == 5 ){
					margCoeficient = 19;
				}
				if( dotBook.get( i ).getHorizontal() == 0 ) {
					margCoeficient = 20;
				}
				x = HORPIXMARG * margCoeficient;
				if( dotBook.get( i ).getHorizontalDirection().equals("in") ) {
					x = x - ( dotBook.get(i).getHorizontalStep() * STEP );
				}
				if( dotBook.get( i ).getHorizontalDirection().equals("out") ) {
					x = x + ( dotBook.get(i).getHorizontalStep() * STEP );
				}
			}
			
			Log.d( "render", "X: " + x );
			
			Log.d("render", "Vertical: " + dotBook.get( i ).getVertical());
			Log.d("render", "Vertical" + dotBook.get( i ).getVerticalStep());
			// Vertical pixel if-block
			if ( dotBook.get( i ).getVertical().equals( "front" )) {
				y = (int) ((VERTPIXMARG * 2) - hndler.getDeadSpace()) + 10;
			}
			if ( dotBook.get( i ).getVertical().equals( "back" )) {
				y = (int) (VERTPIXMARG - hndler.getDeadSpace()) + 10;
			}
			Log.d( "render", "Y: " + y );
			
			setcount = dotBook.get( i ).getSetCount();
			bpm = dotBook.get( i ).getBPM();
			xWriter.addDot( bpm, setcount, x, y );
		}
	}
}
