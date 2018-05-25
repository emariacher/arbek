
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    March 24, 2005
 */
public class Pluie extends Applet implements Runnable, BInclude {
  TableauPluie tableau;
  ZePanel zePanel;
  Thread thread;


  /**  Description of the Method */
  public void init() {
    thread = new Thread(this);

    setLayout(new BorderLayout());
    int nombrebilles  = Integer.valueOf(getParameter("nombrebilles")).intValue();
    tableau = new TableauPluie(size().width, size().height, 0.10, nombrebilles);
    zePanel = new ZePanel(tableau);
    add("Center", zePanel);
  }


  /**  Main processing method for the Pluie object */
  public void run() {
    zePanel.run();
  }


  /**  Description of the Method */
  public void start() {
    thread.start();
    tableau.start();
  }


  /**  Description of the Method */
  public void stop() {
    tableau.stop();
    thread.stop();
  }

}

