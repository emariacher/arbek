// Description: Represents a single Card
//         Issues:
//         * Fragile: This loads each card image from a file, and has
//           the file-naming conventions for the card built into it
//           To change to another set of card images, it's necessary to change the code        
//             * It gets the images
//             * It keeps track of it's x,y coordinates
//             * It can draw itself
package freecelll;

import java.awt.*;
import javax.swing.*;
import java.io.*;
///////////////////////////////////////////////////////////////////// Class Card
public class Card 
{ 
    //================================================================ constants
    public  static final int CARD_WIDTH;  // Initialized in static initializer.
    public  static final int CARD_HEIGHT; // Initialized in static initializer..
    //doing the static final to make it more constant...faster compiling, "thread safe" in a way
    private static final String    IMAGE_PATH = "/cardimages/";
    private static final ImageIcon BACK_IMAGE;  // Image of back of a card
    
    private static final Class <?> CLASS = Card.class;	 //using reflection to get the 
    private static final String PACKAGE_NAME;			//Class object for Card
    private static final ClassLoader CLSLDR;		   //loads the java class file into the JVM
    //======================================================= static initializer
    static // just a static method that does some of the initialization, the static ones
    {
        //... Get current classloader, and get the image resources
        //    This is broken down into small steps for debugging
        PACKAGE_NAME = CLASS.getPackage().getName(); //identifying the class..
        CLSLDR = CLASS.getClassLoader(); //returns the class loader for the class
        String urlPath = PACKAGE_NAME + IMAGE_PATH + "b.gif";
        //using the .net.URL as a resource locator via the classloader
        java.net.URL imageURL = CLSLDR.getResource(urlPath);
        BACK_IMAGE = new ImageIcon(imageURL);
        
        //... These constants are assumed to work for all cards.
        CARD_WIDTH  = BACK_IMAGE.getIconWidth();
        CARD_HEIGHT = BACK_IMAGE.getIconHeight();
    } 
    //======================================================= instance variables
    private Face    _face;
    private Suit    _suit;
    private ImageIcon _faceImage;
    private int     _x;
    private int     _y;
    private boolean _faceUp  = true; 
    //============================================================== constructor
    public Card(Face face, Suit suit) {
        //... Set the face and suit values.
        _face = face;
        _suit = suit;
        
        //... Assume card is at 0,0
        _x = 0;
        _y = 0;
        
        //... By default the cards are face up.
        _faceUp = false;
        
        //... Read in the image associated with the card.
        //    Each card is stored in a GIF file where the name has two chars,
        //    ex, 3h.gif for the three of hearts.
        
        //... Create the file name from the face and suit. order through ordinal
        char faceChar = "a23456789tjqk".charAt(_face.ordinal());
        char suitChar = "shcd".charAt(_suit.ordinal());
        String cardFilename = "" + faceChar + suitChar + ".gif";
        
        //  the class loader is be used...  returns a URL of the card file.
        //... Get current classloader, and get the image resources.
        String path = PACKAGE_NAME + IMAGE_PATH + cardFilename;
        java.net.URL imageURL = CLSLDR.getResource(path); 
        
        //... Load the image from the URL.
        _faceImage = new ImageIcon(imageURL);
    }   
    //================================================================== getFace
    //  Returns face value of card.
    public Face getFace() 
    {
        return _face;
    }
    //================================================================== getSuit
    public Suit getSuit() 
    {
        return _suit;
    }    
    //============================================================== setPosition
    public void setPosition(int x, int y) 
    {
        _x = x;
        _y = y;
    } 
    //===================================================================== draw
    // Draws either face or back.
    public void draw(Graphics g) 
    {
        if (_faceUp) 
        {
            _faceImage.paintIcon(null, g, _x, _y);
        } 	else 
        	{
            	BACK_IMAGE.paintIcon(null, g, _x, _y);
        	}
    }  
    //================================================================= isInside
    // Given a point, it tells whether this is inside card image.
    // Used to determine if mouse was pressed in card.
    public boolean isInside(int x, int y) 
    {
        return (x >= _x && x < _x + CARD_WIDTH) && (y >= _y && y < _y + CARD_HEIGHT);
    }  
    //=============================================================== getX, getY
    public int getX() {return _x;}
    public int getY() {return _y;}
    
    //=============================================================== getX, getY
    public void setX(int x) {_x = x;}
    public void setY(int y) {_y = y;}
    
    //================================================================= toString
    @Override public String toString()
    {
        return "" + _face + " of " + _suit;
    }
    //=============================================================== turnFaceUp
    public void turnFaceUp() {_faceUp = true;}
    
    //============================================================= turnFaceDown
    public void turnFaceDown() {_faceUp = false;}
}