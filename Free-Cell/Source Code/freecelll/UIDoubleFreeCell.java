// Description: Free Cell solitaire program.
//         Main program / JFrame.  Adds a few components and the 
//         main graphics area, UICardPanel, that handles the mouse and painting.

package freecelll;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/////////////////////////////////////////////////////////////// class UIFreeSell
public class UIDoubleFreeCell extends UIAbstractFreeCell {
    //=================================================================== fields
    private GameModel _model = new GameModel(6, 10, 2, true);

    //===================================================================== main
    public static void main(String[] args) {
        //... Do all GUI initialization on EDT thread.  This is the
        //    correct way, but is often omitted because the other
        //    almost always(!) works.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UIDoubleFreeCell();
            }
        });
    }

    //============================================================== constructor
    public UIDoubleFreeCell() {
        _boardDisplay = new UICardPanel(_model);
        UIFreeCellGraphic(new ActionNewGame(), new ActionUndo());
    }

    ////////////////////////////////////////////////////////////// ActionNewGame
    class ActionNewGame implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            _model.reset();
        }
    }

    class ActionUndo implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            _model.undo();
        }
    }
}