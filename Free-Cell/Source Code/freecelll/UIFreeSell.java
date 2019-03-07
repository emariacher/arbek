// Description: Free Cell solitaire program.
//         Main program / JFrame.  Adds a few components and the 
//         main graphics area, UICardPanel, that handles the mouse and painting.

package freecelll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/////////////////////////////////////////////////////////////// class UIFreeSell
public class UIFreeSell extends UIAbstractFreeCell {
    //=================================================================== fields
    private GameModel _model = new GameModel(4, 8, 1, false);

    //===================================================================== main
    public static void main(String[] args) {
        //... Do all GUI initialization on EDT thread.  This is the
        //    correct way, but is often omitted because the other
        //    almost always(!) works.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UIFreeSell();
            }
        });
    }

    //============================================================== constructor
    public UIFreeSell() {
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