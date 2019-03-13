
import java.applet.Applet;
import java.awt.*;

/**
 * Description of the Class
 *
 * @author Eric Mariacher
 * @created March 24, 2005
 */
public class Pluie extends Applet implements Runnable, BInclude {
    TableauPluie tableau;
    ZePanel zePanel;
    Thread thread;


    /**
     * Description of the Method
     */
    public void init() {
        thread = new Thread(this);

        setLayout(new BorderLayout());
        int nombrebilles = 50;
        if (getParameter("nombrebilles") != null) {
            nombrebilles = Integer.valueOf(getParameter("nombrebilles")).intValue();
        }
        tableau = new TableauPluie(getSize().width, getSize().height, 0.10, nombrebilles);
        zePanel = new ZePanel(tableau);
        add("Center", zePanel);
    }


    /**  https://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html */


    /**
     * Description of the Method
     */
    public void start() {
        thread.start();
        tableau.start();
    }


    public void stop() {
        tableau.stop();
        thread = null;
    }

    public void run() {
        zePanel.run();
        Thread thisThread = Thread.currentThread();
        while (thread == thisThread) {
            try {
                thisThread.sleep(10);
            } catch (InterruptedException e) {
            }
            repaint();
        }
    }


}

