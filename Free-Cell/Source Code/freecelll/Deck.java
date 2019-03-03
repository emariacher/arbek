// Description: A Deck is a particular kind of CardPile with 52 Cards in it.

package freecelll;

public class Deck extends CardPile {
    //============================================================== constructor

    /**
     * Creates a new instance of Deck
     */
    public Deck(int numdeck) {
        for (Suit s : Suit.values()) {
            for (int i = 0; i < numdeck; i++)
                for (Face f : Face.values()) {
                    Card c = new Card(f, s);
                    c.turnFaceUp();
                    this.push(c);
                }
        }
        shuffle();
    }
}