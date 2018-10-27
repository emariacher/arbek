// Purpose: Card pile with the initial cards.
//          Only need to specify rules for adding cards.
//          Default rules apply to remove them.

package freecelll;

//////////////////////////////////////////////////////////////////// Class
public class CardPileTableau extends CardPile {

    //===================================================================== push
    //... Accepts a card, if pile is empty, or
    //    if face value is one lower and it's the opposite color.
    @Override
    public boolean rulesAllowAddingThisCard(Card card) {
        System.out.println("  zob1[" + this + ", " + this.size() + "]");
        if (this.size() == 0) {
            return true;
        } else {
            return rulesIsStack(this.peekTop(), card);
        }
    }

    @Override
    public boolean rulesIsStack(Card cardtop, Card cardbelow) {
        if ((this.size() == 0) ||
                (cardtop.getFace().ordinal() - 1 == cardbelow.getFace().ordinal() &&
                        cardtop.getSuit().getColor() != cardbelow.getSuit().getColor())) {
            return true;
        }
        return false;
    }
}