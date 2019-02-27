// Description: Free Cell solitaire program.
//         Main program / JFrame.  Adds a few components and the 
//         main graphics area, UICardPanel, that handles the mouse and painting.

package freecelll;

import javax.swing.*;

/////////////////////////////////////////////////////////////// class UIFreeSell
public class UIDoubleFreeCell extends UIFreeSell {
    //=================================================================== fields
    private GameModel _model = new GameModel(6, 10);

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
        UIFreeSellGraphic();
    }

}