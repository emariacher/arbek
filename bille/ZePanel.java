import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.applet.Applet;
import java.util.*;
import java.io.*;

public class ZePanel extends Panel
                     implements Runnable,
                                BInclude {
    Tableau  tableau;
    Thread   thread;
    Color    backgroundColor = Color.lightGray;
    private Image     image;
    private Dimension dim;
    private Graphics  graphics;

    ZePanel(Tableau tableau) {
        this.tableau = tableau;
        thread       = new Thread(this);
    }


    public void update(Graphics g) {
    String trace = new String("0 ");
    try {
        //******* debut
        Dimension d = getSize();
        if ((image == null) || (d.width != dim.width) || (d.height != dim.height)) {
            image    = createImage(d.width, d.height);
            dim      = d;
            graphics = image.getGraphics();
            graphics.setFont(getFont());
        }
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, d.width, d.height);

        //******* dessine
        for(int bi=0; bi<tableau.bille.size(); bi++) {
            // System.out.println("  bi=" + bi);
            ((Bille)tableau.bille.elementAt(bi)).draw(graphics);
        }
        for(int bo=0; bo<tableau.obstacle.size(); bo++) {
            // System.out.println("  bo=" + bo);
            ((Obstacle)tableau.obstacle.elementAt(bo)).draw(graphics);
        }

        //******* fin
        g.drawImage(image, 0, 0, null);
        } catch(Exception e) {
            System.err.println(e + " " + trace);
            e.printStackTrace();
        }
    }

    public void run()   {
        while(true) {
            repaint();
            try { thread.sleep(150); } catch(Exception e) { }
        }
    }
}
