import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.applet.Applet;
import java.util.*;

public class Colorie extends Applet implements Runnable {
    Thread  thread;
    Carte   carte;
    Button  runStop = new Button("Stop"),
            step    = new Button("Step"),
            back    = new Button("Back");
    boolean runFreely = true;


    public void init() {
        thread = new Thread(this);
        setLayout(new BorderLayout());
        carte = new Carte(this);
        add("Center", carte);
        Panel p = new Panel();
        add("South", p);
        p.setLayout(new GridLayout(1,4));

        runStop.addActionListener(new runListener(this));
        p.add(runStop);

        step.addActionListener(new stepListener(this));
        p.add(step);

        /* back.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    carte.unLog = true;
                    carte.logIndex--;
                    try {
                        carte.unLog();
                    } catch(Exception exception) {
                        System.err.println("back push button (unLog): " + exception);
                    }
                    thread.resume();
                }
            }
        );
        p.add(back); */
    }


    public void start() {
        thread.start();
    }

    public void run() {
        carte.run();
    }


    public void stop() {
        thread.stop();
        System.out.println(carte.getCarteBackInfo());
    }

}


class runListener implements ActionListener {
    Colorie c;

    runListener(Colorie c) {
        this.c = c;
    }

    public void actionPerformed(ActionEvent e) {
        c.carte.unLog = false;
        if(((Button)e.getSource()).getLabel().equals("Stop")) {
            ((Button)e.getSource()).setLabel("Run");
            c.runFreely = false;
        } else {
            ((Button)e.getSource()).setLabel("Stop");
            c.runFreely = true;
            c.thread.resume();
        }
    }
}


class stepListener implements ActionListener {
    Colorie c;

    stepListener(Colorie c) {
        this.c = c;
    }

    public void actionPerformed(ActionEvent e) {
        c.carte.unLog = false;
        c.thread.resume();
    }
}
