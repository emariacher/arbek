// Purpose: how things work

package freecelll;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

//////////////////////////////////////////////////////////////// Class GameModel
public class GameModel implements Iterable<CardPile> {
    //=================================================================== fields
    private CardPile[] _freeCells;
    private CardPile[] _tableau;
    CardPile[] _foundation;

    private ArrayList<CardPile> _allPiles;

    private ArrayList<ChangeListener> _changeListeners;

    //... Using the Java Deque to implement a stack.
    //    Push the source and destination piles on, every time a move is made.
    //    Pop them off to do the undo.   ...must suppress checking....
    private ArrayDeque<CardPile> _undoStack = new ArrayDeque<CardPile>();
    private ArrayDeque<Boolean> _undoOfStack = new ArrayDeque<Boolean>(); // undoing a whole stack

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

        _undoStack = new ArrayDeque<CardPile>();
        _undoOfStack = new ArrayDeque<Boolean>();

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
    public boolean moveFromPileToPile(CardPile source, CardPile target, CardPile zeStack2move, Card crd2move) {
        boolean result = false;
        if (source.size() > 0) {
            if (target.rulesAllowAddingThisCard(crd2move)) {
                if (zeStack2move != null) {
                    System.out.print("  moveFromPileToPile Movable Stack: ");
                    for (Card crd2 : zeStack2move) {
                        System.out.print(", " + crd2);
                        target.push(crd2);
                        source.pop();
                        _notifyEveryoneOfChanges();
                    }
                    System.out.println("");
                    ArrayDeque<CardPile> stackFreeSpaces = new ArrayDeque<CardPile>();
                    for (int i = zeStack2move.size() - 1; i >= 0; i--) {
                        Card crd3 = zeStack2move.getCard(i);
                        //... Record on undo stack.
                        _undoStack.push(source);
                        _undoOfStack.push(true);
                        CardPile freeSpace = getFirstFreespace();
                        _undoStack.push(freeSpace);
                        _undoOfStack.push(true);
                        stackFreeSpaces.push(freeSpace);
                    }
                    for (CardPile fscrdp2 : stackFreeSpaces) {
                        //... Record on undo stack.
                        _undoStack.push(fscrdp2);
                        _undoOfStack.push(true);
                        _undoStack.push(target);
                        _undoOfStack.push(true);
                    }
                } else {
                    moveAndRecord(source, target, crd2move);
                }
                result = true;
            }
        }
        return result;
    }

    public boolean moveAndRecord(CardPile source, CardPile target, Card crd2move) {
        target.push(crd2move);
        source.pop();
        _notifyEveryoneOfChanges();
        //... Record on undo stack.
        _undoStack.push(source);
        _undoOfStack.push(false);
        _undoStack.push(target);
        _undoOfStack.push(false);
        return true;
    }

    public boolean undo() {
        boolean result = false;
        if (_undoStack.size() >= 2) {
            Boolean isAStack = false;
            System.out.println("**** UNDO **** ");
            do {
                CardPile source = _undoStack.getFirst();
                Boolean sourceB = _undoOfStack.getFirst();
                _undoStack.pop();
                _undoOfStack.pop();
                CardPile target = _undoStack.getFirst();
                Boolean sourceT = _undoOfStack.getFirst();
                _undoStack.pop();
                _undoOfStack.pop();
                Card crd = source.peekTop();
                System.out.println("  **** UNDO[" + _undoStack.size() + ", " + source + ", " + source.size() + " -> " + target + ", " + target.size() + " - " + crd + "]**** " + sourceB + " -> " + sourceT);
                target.pushIgnoreRules(crd);
                source.pop();
                if (!_undoOfStack.isEmpty()) {
                    isAStack = _undoOfStack.getFirst() & sourceB;
                }
            } while (isAStack);
            _notifyEveryoneOfChanges();

            result = true;
        }
        return result;
    }


    // get free spaces where cards could be freely moved i.e. not foundation piles
    public int getFreespaces() {
        int freespaces = 0;
        for (CardPile pile : getFreeCellPiles()) {
            if (pile.size() == 0) {
                freespaces++;
            }
        }
        for (CardPile pile : getTableauPiles()) {
            if (pile.size() == 0) {
                freespaces++;
            }
        }
        return freespaces;
    }

    // get first free space where card could be freely moved i.e. not foundation piles
    public CardPile getFirstFreespace() {
        for (CardPile pile : getFreeCellPiles()) {
            if (pile.size() == 0) {
                return pile;
            }
        }
        for (CardPile pile : getTableauPiles()) {
            if (pile.size() == 0) {
                return pile;
            }
        }
        return null;
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