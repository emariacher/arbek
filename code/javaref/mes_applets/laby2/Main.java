// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Main.java

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends Applet
    implements Runnable, Include
{

    public void init()
    {
        thread = new Thread(this);
        setLayout(new BorderLayout());
        laby = new Laby(this);
        add("Center", laby);
        Panel panel = new Panel();
        add("South", panel);
        panel.setLayout(new GridLayout(1, 6));
        runStop.addActionListener(new runListener(this));
        panel.add(runStop);
        step.addActionListener(new stepListener(this));
        panel.add(step);
        boost.addActionListener(new boostListener(this));
        panel.add(boost);
        slow.addActionListener(new slowListener(this));
        panel.add(slow);
	labelCpt.setAlignment(Label.CENTER);
	panel.add(labelCpt);
        Panel panelStat = new Panel();
        add("North", panelStat);
        panelStat.setLayout(new GridLayout(1, MAXPION));
	for(int i=0;i<MAXPION;i++) {
	    Label label = new Label();
	    labelStat[i] = label;
	    panelStat.add(labelStat[i]);
	}
    }

    public void run()
    {
        laby.run();
    }

    public void start()
    {
        thread.start();
    }

    public void stop()
    {
        thread.stop();
    }

    public Main()
    {
        runStop = new Button("Stop");
        step    = new Button("Step");
        back    = new Button("Back");
        boost   = new Button("Boost");
        slow    = new Button("Slow");
        runFreely = true;
	labelStat = new Label[MAXPION];
	labelCpt  = new Label();
    }

    Thread thread;
    Laby laby;
    Button runStop;
    Button step;
    Button back, boost, slow;
    Label  labelStat[];
    Label  labelCpt;
    boolean runFreely;
}


class boostListener
    implements ActionListener
{

    boostListener(Main main)
    {
        c = main;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        c.laby.speedFactor -= 10;
    }

    Main c;
}


class slowListener
    implements ActionListener
{

    slowListener(Main main)
    {
        c = main;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        c.laby.speedFactor += 10;
    }

    Main c;
}
