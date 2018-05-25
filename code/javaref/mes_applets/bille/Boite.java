import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.applet.Applet;
import java.util.*;
import java.io.*;

public class Boite extends Applet implements Runnable, BInclude {
    TableauBoite tableau;
    ZePanel      zePanel;
    Thread       thread;

    public void init() {
        thread = new Thread(this);

        setLayout(new BorderLayout());

        tableau = new TableauBoite(size().width, size().height, 1.01);
        zePanel = new ZePanel(tableau);
        add("Center", zePanel);
    }

    public void run() {
        zePanel.run();
    }

    public void start() {
        thread.start();
        tableau.start();
    }
    public void stop()  {
        tableau.stop();
        thread.stop();
    }

}
