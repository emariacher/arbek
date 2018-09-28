// Purpose: Card pile with the initial cards.
//          Only need to specify rules for adding cards.
//          Default rules apply to remove them.

package freecelll;

//////////////////////////////////////////////////////////////////// Class
public class CardPileTableau extends CardPile {
    
    //===================================================================== push
    //... Accepts a card, if pile is empty, or
    //    if face value is one lower and it's the opposite color.
    @Override public boolean rulesAllowAddingThisCard(Card card) 
    {
        if ((this.size() == 0) ||
                (this.peekTop().getFace().ordinal() - 1 == card.getFace().ordinal() &&
                this.peekTop().getSuit().getColor() != card.getSuit().getColor())) {
            return true;
        }
        return false;
    }
}