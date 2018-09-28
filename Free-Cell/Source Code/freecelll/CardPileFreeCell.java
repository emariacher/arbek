// Purpose: CardPile specialized to add only one card.

package freecelll;

/////////////////////////////////////////////////////////////// CardPileFreeCell
public class CardPileFreeCell extends CardPile 
{
    
    //================================================= rulesAllowAddingThisCard
    //... Accepts a card only if a pile is empty.
    @Override public boolean rulesAllowAddingThisCard(Card card) 
    {
        //... Accept only if the current pile is empty.
        return size() == 0;
    }
}