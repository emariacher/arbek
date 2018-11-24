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
        System.out.println("  rulesAllowAddingThisCard 1[" + this + ", " + this.size() + ", " + card + "]");
        if (this.size() == 0) {
            return true;
        } else {
            System.out.println("    rulesAllowAddingThisCard 2[" + this + ", " + this.size() + ", " + card + ", " + rulesIsStack(this.peekTop(), card) + "]");
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