import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.applet.Applet;
import java.util.*;
import java.io.*;

/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    April 12, 2007
 */
public class MasterMind extends Applet implements Runnable, MMInclude {
	Button runStop = new Button("Stop"),
		step = new Button("Step"),
		autoMan = new Button("Automatique"),
		solution = new Button("Affiche\nSolution");
	Moteur moteur;
	ZePanel zePanel;
	Random hasard;
	int graine;
	Calendar date;
	Thread thread;
	Panel pb;


	/**
	 *  Description of the Method
	 */
	public void init() {
		thread = new Thread(this);
		date = Calendar.getInstance();
		graine = (date.get(Calendar.SECOND) * date.get(Calendar.SECOND)) + (date.get(Calendar.MINUTE) * date.get(Calendar.MINUTE)) + (date.get(Calendar.HOUR) * date.get(Calendar.HOUR));
		hasard = new Random(graine);

		setLayout(new BorderLayout());

		moteur = new Moteur(hasard.nextInt());
		zePanel = new ZePanel(moteur);
		add("Center", zePanel);

		// push buttons
		pb = new Panel();
		add("South", pb);
		pb.setLayout(new GridLayout(1, 4));

		autoMan.addActionListener(new autoManListener(moteur, runStop, step));
		pb.add(autoMan);

		runStop.addActionListener(new runListener(moteur, zePanel));
		pb.add(runStop);

		step.addActionListener(new stepListener(moteur));
		pb.add(step);

		solution.addActionListener(new solutionListener(zePanel));
		pb.add(solution);

	}


	/**
	 *  Main processing method for the MasterMind object
	 */
	public void run() {
		zePanel.run();
	}


	/**
	 *  Description of the Method
	 */
	public void start() {
		thread.start();
		moteur.start();
	}
}

/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    April 12, 2007
 */
class autoManListener implements ActionListener {


	Moteur c;
	Button b1, b2;


	/**
	 *  Constructor for the autoManListener object
	 *
	 *@param  c   Description of the Parameter
	 *@param  b1  Description of the Parameter
	 *@param  b2  Description of the Parameter
	 */
	autoManListener(Moteur c, Button b1, Button b2) {
		this.c = c;
		this.b1 = b1;
		this.b2 = b2;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		if (((Button) e.getSource()).getLabel().equals("Automatique")) {
			((Button) e.getSource()).setLabel("Manuel");
			b1.setLabel("Nouveau\nProbleme");
			b2.setLabel("Soumet");
			c.automatique = false;
			c.step = true;
		} else {
			((Button) e.getSource()).setLabel("Automatique");
			if (c.runFreely) {
				b1.setLabel("Stop");
			} else {
				b1.setLabel("Run");
			}
			b2.setLabel("Step");
			c.automatique = true;
			c.step = true;
		}
	}
}

/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    April 12, 2007
 */
class runListener implements ActionListener {


	Moteur c;
	ZePanel zp;


	/**
	 *  Constructor for the runListener object
	 *
	 *@param  c   Description of the Parameter
	 *@param  zp  Description of the Parameter
	 */
	runListener(Moteur c, ZePanel zp) {
		this.c = c;
		this.zp = zp;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		if (((Button) e.getSource()).getLabel().equals("Stop")) {
			((Button) e.getSource()).setLabel("Run");
			c.runFreely = false;
		} else if (((Button) e.getSource()).getLabel().equals("Run")) {
			((Button) e.getSource()).setLabel("Stop");
			c.runFreely = true;
			c.step = true;
		} else {
			c.pasTrouve = false;
			c.step = true;
			zp.afficheSolution = false;
		}
	}
}

/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    April 12, 2007
 */
class stepListener implements ActionListener {


	Moteur c;


	/**
	 *  Constructor for the stepListener object
	 *
	 *@param  c  Description of the Parameter
	 */
	stepListener(Moteur c) {
		this.c = c;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		c.step = true;
	}
}

/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    April 12, 2007
 */
class solutionListener implements ActionListener {


	ZePanel c;


	/**
	 *  Constructor for the solutionListener object
	 *
	 *@param  c  Description of the Parameter
	 */
	solutionListener(ZePanel c) {
		this.c = c;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  e  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		c.afficheSolution = !c.afficheSolution;
		if (c.afficheSolution) {
			((Button) e.getSource()).setLabel("Cache\nSolution");
		} else {
			((Button) e.getSource()).setLabel("Affiche\nSolution");
		}
	}
}

