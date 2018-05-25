import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

@SuppressWarnings("serial")
public class Colorie extends Applet implements Runnable {
	Thread  thread;
	private volatile Thread zeThread;
	public boolean threadSuspended;

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
		step.setEnabled(false);

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
		Thread thisThread = Thread.currentThread();
		while (zeThread == thisThread) {
			try {
				Thread.sleep(100);
				synchronized(this) {
					while (threadSuspended && zeThread==thisThread) {
						wait();
					}
				}
			} catch (InterruptedException e) {
			}

			repaint();
		}

	}


	public void stop() {
		//        thread.stop();
		zeThread = null;
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
			c.step.setEnabled(true);
			c.runFreely = false;
		} else {
			((Button)e.getSource()).setLabel("Stop");
			c.step.setEnabled(false);
			c.runFreely = true;
			c.threadSuspended = !c.threadSuspended;

			if (!c.threadSuspended)
				notify();

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
//		c.thread.resume();
		c.threadSuspended = !c.threadSuspended;

		if (!c.threadSuspended)
			notify();

	}
}
