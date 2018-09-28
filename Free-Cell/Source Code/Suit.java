// Description: A card suit type.

package freecelll;

import java.awt.Color;

enum Suit 
{
    //================================================================ constants
    SPADES(Color.BLACK), HEARTS(Color.RED), CLUBS(Color.BLACK), DIAMONDS(Color.RED);  
    //==================================================================== field
    private final Color _color;    
    //============================================================== constructor
    Suit(Color color) 
    {
        _color = color;
    } 
    //================================================================= getColor
    public Color getColor() 
    {
        return _color;
    }
}