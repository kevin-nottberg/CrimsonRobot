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
	
	// Other variables and arrays
	Context context;
	ArrayList<RawDot> dotBook = new ArrayList<RawDot>();
	
	public RawDotBook( Context cont, ScreenHandler screenHandler ) {
		context = cont;
		hndler = screenHandler;
	}
	
	public void init( String path ) {
		XmlParser parser = new XmlParser( context );
		Document doc = parser.getDomElement( path );
		Log.d("debugMarcherList", "Got parser and created got the doc");
		Log.d("debugMarcherList", ""+doc.getTextContent());
		//NodeList id = doc.getElementsByTagName(ID);
		//Element el = ( Element ) id.item( 0 );
		//iden = parser.getElementValue(el);
		NodeList marcherList = doc.getElementsByTagName( MARCHER );

		System.out.println( "marcherlength" + marcherList.getLength() );
		for( int j = 0; j < marcherList.getLength(); j++ ) {
			NodeList dl = marcherList.item(j).getChildNodes();
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
			}
		Log.d("debugMarcherList", "Done with the init");
		convertAndWrite( path ); 
	}
	
	public void convertAndWrite( String path ) {
		XmlWriter xWriter = new XmlWriter();
		xWriter.startNewDoc();
		int FIELD = 100;
		int SCREEN = 20;
		int HORPIXMARG = hndler.getHorizontalPixMarg();
		int VERTPIXMARG = hndler.getVerticalPixMarg();
		xWriter.addMarcher();
		for( int i = 0; i < dotBook.size(); i++ ) {
			int x = 0;
			int y = 0;
			int setcount = 0;
			int bpm = 0;
			
			// Horizontal pixel if-block
			if( dotBook.get( i ).getSide() == 1 ) {
				int margCoeficient = ( (dotBook.get( i ).getHorizontal() * SCREEN) / FIELD );
				x = HORPIXMARG * margCoeficient;
			}
			if( dotBook.get( i ).getSide() == 2 ) {
				int margCoeficient = ( (dotBook.get( i ).getHorizontal() * SCREEN) / FIELD ) * 2;
				x = HORPIXMARG * margCoeficient;
			}
			
			Log.d("debugMarcherList", "Before null pointer");
			Log.d("debugMarcherList", "Vertical: " + dotBook.get(i).getVertical());
			Log.d("debugMarcherList", "Vertical" + dotBook.get(i).getVerticalStep());
			// Vertical pixel if-block
			if ( dotBook.get( i ).getVertical().equals( "front" )) {
				y = VERTPIXMARG * 2;
			}
			if ( dotBook.get( i ).getVertical().equals( "back" )) {
				y = VERTPIXMARG;
			}
			
			setcount = dotBook.get( i ).getSetCount();
			bpm = dotBook.get( i ).getBPM();
			xWriter.addDot( bpm, setcount, x, y );
		}
		xWriter.finishAndWriteDoc( context, path );
	}
}
