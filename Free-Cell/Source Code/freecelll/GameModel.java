// Purpose: how things work

package freecelll;

import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//////////////////////////////////////////////////////////////// Class GameModel
public class GameModel implements Iterable<CardPile> {
    //=================================================================== fields
    private CardPile[] _freeCells;
    private CardPile[] _tableau;
    private CardPile[] _foundation;

    private ArrayList<CardPile> _allPiles;

    private ArrayList<ChangeListener> _changeListeners;

    //... Using the Java Deque to implement a stack.
    //    Push the source and destination piles on, every time a move is made.
    //    Pop them off to do the undo.   ...must suppress checking....
    private ArrayDeque<CardPile> _undoStack = new ArrayDeque<CardPile>();

    //============================================================== constructor
    public GameModel() {
        _allPiles = new ArrayList<CardPile>();

        _freeCells = new CardPile[4];
        _tableau = new CardPileTableau[8];
        _foundation = new CardPile[4];

        //... Create empty piles to hold "foundation"
        for (int pile = 0; pile < _foundation.length; pile++) {
            _foundation[pile] = new CardPileFoundation();
            _allPiles.add(_foundation[pile]);
        }
        //... Create empty piles of Free Cells.
        for (int pile = 0; pile < _freeCells.length; pile++) {
            _freeCells[pile] = new CardPileFreeCell();
            _allPiles.add(_freeCells[pile]);
        }
        //... Arrange the cards into piles.
        for (int pile = 0; pile < _tableau.length; pile++) {
            _tableau[pile] = new CardPileTableau();
            _allPiles.add(_tableau[pile]);
        }

        _changeListeners = new ArrayList<ChangeListener>();

        reset();
    }

    //==================================================================== reset
    public void reset() {
        Deck deck = new Deck();
        deck.shuffle();

        //... Empty all the piles.
        for (CardPile p : _allPiles) {
            p.clear();
        }
        //... Deal the cards into the piles.
        int whichPile = 0;
        for (Card crd : deck) {
            _tableau[whichPile].pushIgnoreRules(crd);
            whichPile = (whichPile + 1) % _tableau.length;
        }
        //... Tell interested parties (ex, the View) that things have changed.
        _notifyEveryoneOfChanges();
    }

    //TODO: Needs to be simplified having methods that both 
    //      return a pile by number, and the array of all piles.
    //================================================================= iterator
    public Iterator<CardPile> iterator() {
        return _allPiles.iterator();
    }

    //=========================================================== getTableauPile
    public CardPile getTableauPile(int i) {
        return _tableau[i];
    }

    //========================================================== getTableauPiles
    public CardPile[] getTableauPiles() {
        return _tableau;
    }

    //========================================================= getFreeCellPiles
    public CardPile[] getFreeCellPiles() {
        return _freeCells;
    }

    //========================================================== getFreeCellPile
    public CardPile getFreeCellPile(int cellNum) {
        return _freeCells[cellNum];
    }

    //======================================================= getFoundationPiles
    public CardPile[] getFoundationPiles() {
        return _foundation;
    }

    //======================================================== getFoundationPile
    public CardPile getFoundationPile(int cellNum) {
        return _foundation[cellNum];
    }

    //======================================================= moveFromPileToPile
    public boolean moveFromPileToPile(CardPile source, CardPile target) {
        boolean result = false;
        if (source.size() > 0) {
            Card crd = source.peekTop();
            System.out.println("  Here9[" + crd + "]");
            if (target.rulesAllowAddingThisCard(crd)) {
                System.out.println("  Here0[" + _undoStack.size() + ", " + source + ", " + source.size() + " -> " + target + ", " + target.size() + " - " + crd + "]");
                target.push(crd);
                source.pop();
                _notifyEveryoneOfChanges();
                //... Record on undo stack.
                _undoStack.push(source);
                _undoStack.push(target);

                result = true;
            }
        }
        return result;
    }

    public boolean undo() {
        boolean result = false;
        if (_undoStack.size() >= 2) {
            CardPile source = _undoStack.getFirst();
            _undoStack.pop();
            CardPile target = _undoStack.getFirst();
            _undoStack.pop();
            Card crd = source.peekTop();
            System.out.println("  There1[" + _undoStack.size() + ", " + source + ", " + source.size() + " -> " + target + ", " + target.size() + " - " + crd + "]");
            target.pushIgnoreRules(crd);
            source.pop();
            _notifyEveryoneOfChanges();

            result = true;
        }
        return result;
    }

    //======================================================== addChangeListener
    public void addChangeListener(ChangeListener someoneWhoWantsToKnow) {
        _changeListeners.add(someoneWhoWantsToKnow);
    }

    //================================================= _notifyEveryoneOfChanges
    private void _notifyEveryoneOfChanges() {
        for (ChangeListener interestedParty : _changeListeners) {
            interestedParty.stateChanged(new ChangeEvent("Game state changed."));
        }
    }
}