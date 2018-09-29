// Description: Free Cell solitaire program.
//         Main program / JFrame.  Adds a few components and the 
//         main graphics area, UICardPanel, that handles the mouse and painting.

package freecelll;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/////////////////////////////////////////////////////////////// class UIFreeSell
public class UIFreeSell extends JFrame 
{
    //=================================================================== fields
    private GameModel _model = new GameModel();
    
    private UICardPanel _boardDisplay;
    
    //===================================================================== main
    public static void main(String[] args) 
    {
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
    public UIFreeSell() 
    {
        _boardDisplay = new UICardPanel(_model);
        
        //... Create buttons and check box.
        JButton newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(new ActionNewGame());
        JButton undoBtn = new JButton("Undo");
        undoBtn.addActionListener(new ActionUndo());

        //... Do layout
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(newGameBtn);
        controlPanel.add(undoBtn);

        //... Create content pane with graphics area in center (so it expands)
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(controlPanel, BorderLayout.NORTH);
        content.add(_boardDisplay, BorderLayout.CENTER);
        
        //... Set this window's characteristics.
        setContentPane(content);
        setTitle("Free Cell");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }   
    ////////////////////////////////////////////////////////////// ActionNewGame
    class ActionNewGame implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            _model.reset();
        }
    }
    class ActionUndo implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            _model.undo();
        }
    }
}