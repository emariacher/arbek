import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.applet.Applet;
import java.util.*;
import java.io.*;

public class Boite extends Applet implements Runnable, BInclude {
    TableauBoite tableau;
    ZePanel      zePanel;
    Thread       zeThread;

    public void init() {
        zeThread = new Thread(this);

        setLayout(new BorderLayout());

        tableau = new TableauBoite(getSize().width, getSize().height, 1.01);
        zePanel = new ZePanel(tableau);
        add("Center", zePanel);
    }

    public void start() {
        zeThread.start();
        tableau.start();
    }


    public void stop() {
        tableau.stop();
        zeThread = null;
    }

    public void run() {
        zePanel.run();
        Thread thisThread = Thread.currentThread();
        while (zeThread == thisThread) {
            try {
                thisThread.sleep(10);
            } catch (InterruptedException e) {
            }
            repaint();
        }
    }
}
