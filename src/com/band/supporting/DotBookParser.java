package com.band.supporting;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.band.render.Dot;
import com.band.render.DotBook;
import com.band.render.RawDot;

import android.content.Context;

public class DotBookParser {
	
	ArrayList<PerDotBook> raw_dotBook_List;
	ArrayList<DotBookSupport> norm_dotBook;
	
	Context context;
	
	static final String MARCHER = "marcher";
	static final String SECTION = "section";
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
	static final String ID = "id";
	
	static final String X = "x";
	static final String Y = "y";
	
	public DotBookParser( Context cont ) {
		context = cont;
		raw_dotBook_List = new ArrayList<PerDotBook>();
		norm_dotBook = new ArrayList<DotBookSupport>();
	}
	
	public void initRaw( String path ) {
		XmlParser parser = new XmlParser( context );
		Document doc = parser.getDomElement( path );
		
		NodeList marchers = doc.getElementsByTagName( MARCHER );
		for( int i = 0; i < marchers.getLength(); i++ ) {
			Element e = (Element) marchers.item( i );
			PerDotBook dotBook = new PerDotBook();
			raw_dotBook_List.add( dotBook );
			dotBook.setId( e.getAttribute( ID ) );
			dotBook.setSection( e.getAttribute( SECTION ) );
			NodeList dl = e.getChildNodes();
			for( int j = 0; j < marchers.getLength(); j++ ){
				RawDot dot = new RawDot();
				Element el = (Element) dl.item( j );
				dot.setBpm( Integer.parseInt( parser.getValue( el, BPM ) ) ); 
				dot.setCount( Integer.parseInt( parser.getValue( el, SETCOUNT ) ) );
				dot.setHorizontal( Integer.parseInt( parser.getValue( el, HORIZONTAL ) ) );
				dot.setHorizontalDirection( parser.getValue( el, HORIZONTALDIRECTION ));
				dot.setHorizontalStep( Integer.parseInt( parser.getValue( el, HORIZONTALSTEP ) ) );
				dot.setVertical( parser.getValue( el, VERTICAL ) );
				dot.setVerticalDir( parser.getValue( el, VERTICALDIRECTION) );
				dot.setVerticalStep( Integer.parseInt( parser.getValue( el, VERTICALSTEP ) ) );
				dotBook.addDot( dot );
			}
		}
	}
	
	public void init( String path ) {
		XmlParser parser = new XmlParser( context );
		Document doc = parser.getDomElement( path );
		NodeList marchers = doc.getElementsByTagName( MARCHER );
		for( int i = 0; i < marchers.getLength(); i++ ) {
			Element e = (Element) marchers.item( i );
			DotBookSupport dotBook = new DotBookSupport();
			norm_dotBook.add( dotBook );
			dotBook.setId( e.getAttribute( ID ) );
			dotBook.setSection( e.getAttribute( SECTION ) );
			NodeList dl = e.getChildNodes();
			for( int j = 0; j < marchers.getLength(); j++ ) {
				Dot dot = new Dot();
				Element el = (Element) dl.item( j );
				dot.setBpm( Integer.parseInt( parser.getValue( el, BPM ) ) );
				dot.setCount( Integer.parseInt( parser.getValue( el, SETCOUNT ) ) );
				dot.setX( Integer.parseInt( parser.getValue( el, X ) ) );
				dot.setY( Integer.parseInt( parser.getValue( el, Y ) ) );
			}
		}
	}
	
	public ArrayList<PerDotBook> getRawDotBooks() { 
		return raw_dotBook_List;
	}
	
	public ArrayList<DotBookSupport> getDotBooks() {
		return norm_dotBook;
	}
}
