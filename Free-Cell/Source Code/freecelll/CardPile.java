// Description: A pile of cards (can be used for a hand, deck, discard pile...)
//               Subclasses: Deck (a CardPile of 52 Cards)
package freecelll;

import java.util.*;

public class CardPile implements Iterable<Card> 
{
    //======================================================= instance variables
    private ArrayList<Card> _cards = new ArrayList<Card>(); // All the cards.
    
    //========================================================== pushIgnoreRules
    public void pushIgnoreRules(Card newCard) 
    {
        _cards.add(newCard);
    }
    //========================================================== popIgnoreRules
    public Card popIgnoreRules() 
    {
        int lastIndex = size()-1;
        Card crd = _cards.get(lastIndex);
        _cards.remove(lastIndex);
        return crd;
    }   
    //===================================================================== push
    public boolean push(Card newCard) {
        if (rulesAllowAddingThisCard(newCard)) {
            _cards.add(newCard);
            return true;
        } else {
            return false;
        }
    }   
    //================================================= rulesAllowAddingThisCard
    //... Subclasses can override this to enforce their rules for adding.
    public boolean rulesAllowAddingThisCard(Card card) 
    {
        return true;
    }  
    //===================================================================== size
    public int size() 
    {
        return _cards.size();
    }  
    //====================================================================== pop
    public Card pop() 
    {
        /*if (!isRemovable()) {
            throw new UnsupportedOperationException("Illegal attempt to remove.");
        }*/
        return popIgnoreRules();
    }
    //================================================================== shuffles the cards
    public void shuffle() 
    {
        Collections.shuffle(_cards);
    }
    //================================================================== peekTop
    public Card peekTop() //gets the top card of the pile
    {
        return _cards.get(_cards.size() - 1);
    }
    //================================================================= iterator
    public Iterator<Card> iterator() 
    {
        return _cards.iterator();
    }  
    //========================================================== reverseIterator
    public ListIterator<Card> reverseIterator() 
    {
        return _cards.listIterator(_cards.size());
    }
    //==================================================================== clear
    public void clear() 
    {
        _cards.clear();
    } 
    //============================================================== isRemovable
    public boolean isRemovable() 
    {
        return true;
    }
}