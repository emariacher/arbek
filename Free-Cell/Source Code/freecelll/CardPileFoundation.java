// Purpose: Represents a Foundation card pile.

package freecelll;

/////////////////////////////////////////////////////// Class CardPileFoundation
public class CardPileFoundation extends CardPile {
    private boolean isremovable = false;

    public CardPileFoundation(boolean foundationCardsRemovable) {
        isremovable = foundationCardsRemovable;
    }

    //================================================= rulesAllowAddingThisCard
    //... Accept card if pile is empty, or
    //    if face value is one lower and it's the opposite color.
    @Override
    public boolean rulesAllowAddingThisCard(Card card) {
        //... Accept any ace on an empty pile.
        if ((this.size() == 0) && (card.getFace() == Face.ACE)) {
            return true;
        }

        if (size() > 0) {
            Card top = peekTop();
            if ((top.getSuit() == card.getSuit() &&
                    (top.getFace().ordinal() + 1 == card.getFace().ordinal()))) {
                return true;
            } else if ((top.getSuit() == card.getSuit() &&
                    (top.getFace() == Face.KING) && (card.getFace() == Face.ACE))) {
                System.out.println("ACE on KING! " + top.getSuit());
                return true;
            }
        }
        return false;
    }

    //============================================================== isRemovable
    @Override
    public boolean isRemovable() {
        return isremovable;
    }
}