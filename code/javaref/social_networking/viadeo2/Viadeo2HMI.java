
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    September 14, 2005
 */
public class Viadeo2HMI extends JPanel implements ActionListener, DefVar {
	JTextPane webpage = new JTextPane();
	JTextPane email_list = new JTextPane();
	JFrame buildFrame;
	JPanel buildPanel;
	JLabel AnalyzeLabel;
	JButton jbAnalyze;
	JTextArea err;
	BufferedReader inputf;
	String server;
	Viadeo2 br;
	String alreadyInvited;
	File defvardiff;



	/**
	 *  Constructor for the SeapineBuildReleaseTool object
	 */
	public Viadeo2HMI() {
		super(new BorderLayout());
		//Create and set up the window.
		buildFrame = new JFrame("Viadeo2");
		buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildFrame.setSize(new Dimension(200, 700));

		//Create and set up the panel.
		buildPanel = new JPanel(new GridLayout(5, 2));
		//Create the log first, because the action listeners
		//need to refer to it.
		err = new JTextArea(5, 25);
		err.setMargin(new Insets(1, 1, 1, 1));
		err.setEditable(false);
		JScrollPane errScrollPane = new JScrollPane(err);
		err.setForeground(Color.red);

		//Add the widgets.
		addWidgets();

		//Set the default button.
		buildFrame.getRootPane().setDefaultButton(jbAnalyze);

		//Add the panel to the window.
		buildFrame.getContentPane().setLayout(new BoxLayout(buildFrame.getContentPane(), BoxLayout.PAGE_AXIS));
		buildFrame.getContentPane().add(buildPanel);
		buildMyPanel(buildFrame, webpage, new String("Web Page"));
		buildMyPanel(buildFrame, email_list, new String("email list"));
		buildFrame.getContentPane().add(errScrollPane);
		//Display the window.
		buildFrame.pack();
		//buildFrame.setSize(buildFrame.getPreferredSize());
		buildFrame.setVisible(true);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  tp     Description of the Parameter
	 *@param  titre  Description of the Parameter
	 *@param  bf     Description of the Parameter
	 */
	void buildMyPanel(JFrame bf, JTextPane tp, String titre) {
		JPanel mp = new JPanel();
		mp.setLayout(new BoxLayout(mp, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel(titre);
		mp.add(label);
		tp.setMargin(new Insets(1, 1, 1, 1));
		tp.setEditable(true);
		tp.setContentType("text/html");
		JScrollPane sp = new JScrollPane(tp);
		sp.setPreferredSize(new Dimension(200, 100));
		sp.setMinimumSize(new Dimension(200, 50));
		mp.add(sp);
		bf.getContentPane().add(mp);
	}



	/**
	 *  Create and add the widgets.
	 */
	private void addWidgets() {
		Font bigFont = new Font(null, Font.BOLD, 16);
		//Create widgets.

		jbAnalyze = new JButton("Analyze!");
		jbAnalyze.addActionListener(this);
		AnalyzeLabel = new JLabel("not analyzed yet", SwingConstants.LEFT);

		buildPanel.add(jbAnalyze);
		buildPanel.add(AnalyzeLabel);
		//jbAnalyze.setEnabled(false);
		AnalyzeLabel.setEnabled(false);
		AnalyzeLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		//read default variables file
		try {
			defvardiff = new File("Viadeo2");
			if (defvardiff.canRead()) {
				err.append("ya1[" + defvardiff.getName() + "]");
				inputf = new BufferedReader(new FileReader(defvardiff));
				String linef;
				int cpt = 0;
				alreadyInvited = new String("gna");
				while ((linef = inputf.readLine()) != null) {
					err.append("ya2[" + cpt + ":" + linef + "], ");
					alreadyInvited = alreadyInvited.concat(linef);
					err.append("ya3[" + alreadyInvited + "], ");
					cpt++;
				}
				inputf.close();
			}
		} catch (FileNotFoundException e) {
			err.append("             *warning* no File with default values found! expected at[" +
					defvardiff.getName() + "] continuing...");
		} catch (Exception e) {
			/*
			 *  try {
			 *  PipedInputStream piErr   = new PipedInputStream();
			 *  PipedOutputStream poErr  = new PipedOutputStream(piErr);
			 *  System.setErr(new PrintStream(poErr, true));
			 *  e.printStackTrace();
			 *  /myErrPrintln(e.toString());
			 *  int len                  = 0;
			 *  byte[] buf               = new byte[1024];
			 *  while(true) {
			 *  len = piErr.read(buf);
			 *  if(len == -1) {
			 *  break;
			 *  }
			 *  err.append(new String(buf, 0, len));
			 *  }
			 *  } catch(Exception ze) {}
			 */
			System.err.println("*1*" + e);
		} finally {

		}

	}


	/**
	 *  Description of the Method
	 *
	 *@param  event  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent event) {
		//log.append(event.toString() + "$$$$$$$$$$");
		if (event.getSource() == jbAnalyze) {
			try {
				AnalyzeLabel.setText("Analyzing!");
				br = new Viadeo2(this);
			} catch (Exception e) {
				System.err.println("*2*" + e);
				e.printStackTrace();
			}
		}
	}


	/**
	 *  Create the GUI and show it. For thread safety, this method should be
	 *  invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		Viadeo2HMI build = new Viadeo2HMI();
	}


	/**
	 *  The main program for the SeapineBuildReleaseTool class
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
	}
}

