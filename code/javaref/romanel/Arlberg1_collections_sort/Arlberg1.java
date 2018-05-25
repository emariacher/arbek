
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Arlberg1 extends JPanel implements ActionListener {
	/**
	 * 
	 */
	JFrame buildFrame;
	JPanel buildPanel;
	JLabel jlblFileChooser4ArlbergLogFile,
	AnalyzeLabel, zeSeapineLbl_Label,
	toolTitleLabel1, toolTitleLabel2;
	JButton jbBuild, jbOpenArlbergLogFile, jbAnalyze;
	JFileChooser JFileChooser4ArlbergLogFile;
	JTextArea err;
	JTextPane html;
	File defvardiff;
	BufferedReader inputf;
	FileOutputStream outputf;
	File ArlbergLogFile = null;
	AnalyzeThread x;


	/**  Constructor for the Matrix1 object */
	public Arlberg1() {
		super(new BorderLayout());
		//Create and set up the window.
		buildFrame = new JFrame("Check ghostkey triplet appearance in Arlberg log file");
		buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildFrame.setSize(new Dimension(200, 800));

		//Create and set up the panel.
		buildPanel = new JPanel(new GridLayout(4, 2));
		err = new JTextArea(20, 60);
		err.setMargin(new Insets(1, 1, 1, 1));
		err.setEditable(false);
		JScrollPane errScrollPane   = new JScrollPane(err);
		err.setForeground(Color.red);
		html = new JTextPane();
		html.setMargin(new Insets(1, 1, 1, 1));
		html.setEditable(false);
		html.setContentType("text/html");
		JScrollPane htmlScrollPane  = new JScrollPane(html);

		//Add the widgets.
		addWidgets();

		//Add the panel to the window.
		GridBagLayout gridbag       = new GridBagLayout();
		GridBagConstraints c        = new GridBagConstraints();
		buildFrame.getContentPane().setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(buildPanel, c);
		buildFrame.getContentPane().add(buildPanel);
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(htmlScrollPane, c);
		buildFrame.getContentPane().add(htmlScrollPane);
		c.gridy = 3;
		gridbag.setConstraints(errScrollPane, c);
		buildFrame.getContentPane().add(errScrollPane);
		/*c.gridy = 4;
    gridbag.setConstraints(logScrollPane, c);
    buildFrame.getContentPane().add(logScrollPane);*/
		//Display the window.
		buildFrame.pack();
		buildFrame.setSize(buildFrame.getPreferredSize());
		buildFrame.setVisible(true);
	}



	/**  Create and add the widgets. */
	private void addWidgets() {
		Font bigFont               = new Font(null, Font.BOLD, 16);
		String toolTitleLabel1Txt  = new String("Eric Mariacher 1");
		String toolTitleLabel2Txt  = new String("Eric Mariacher 5");
		toolTitleLabel1 = new JLabel(toolTitleLabel1Txt, SwingConstants.LEFT);
		toolTitleLabel1.setFont(bigFont);
		toolTitleLabel2 = new JLabel(toolTitleLabel2Txt, SwingConstants.LEFT);
		toolTitleLabel2.setFont(bigFont);
		TxtFileFilter filter       = new TxtFileFilter();

		JFileChooser4ArlbergLogFile = new JFileChooser();
		JFileChooser4ArlbergLogFile.setFileFilter(filter);
		jbOpenArlbergLogFile = new JButton("Arlberg Log File");
		jbOpenArlbergLogFile.addActionListener(this);
		jlblFileChooser4ArlbergLogFile = new JLabel("no "+jbOpenArlbergLogFile.getText() + " Selected yet", SwingConstants.LEFT);

		jbAnalyze = new JButton("Analyze!");
		jbAnalyze.addActionListener(this);
		AnalyzeLabel = new JLabel("not analyzed yet", SwingConstants.LEFT);

		//Add the widgets to the container.
		buildPanel.add(toolTitleLabel1);
		buildPanel.add(toolTitleLabel2);
		buildPanel.add(jbOpenArlbergLogFile);
		buildPanel.add(jlblFileChooser4ArlbergLogFile);
		buildPanel.add(jbAnalyze);
		buildPanel.add(AnalyzeLabel);
		jbAnalyze.setEnabled(false);
		AnalyzeLabel.setEnabled(false);

		toolTitleLabel2.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		AnalyzeLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		//read default variables file
		try {
			defvardiff = new File("Matrix1");
			if(defvardiff.canRead()) {
				inputf = new BufferedReader(new FileReader(defvardiff));
				String linef;
				int cpt       = 0;
				while((linef = inputf.readLine()) != null) {
					switch (cpt) {
					case 0:
						JFileChooser4ArlbergLogFile.setCurrentDirectory(new File(linef));
						break;
					}
					cpt++;
				}
				inputf.close();
			}
		} catch(FileNotFoundException e) {
			err.append("             *warning* no File with default values found! expected at[" +
					defvardiff.getName() + "] continuing...");
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(e);
			System.out.println(e);
			err.append(e.toString());
		}

	}


	/**
	 *  Description of the Method
	 *
	 *@param  event  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == jbOpenArlbergLogFile) {
			int returnVal  = JFileChooser4ArlbergLogFile.showOpenDialog(Arlberg1.this);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				ArlbergLogFile = JFileChooser4ArlbergLogFile.getSelectedFile();
				jlblFileChooser4ArlbergLogFile.setText(ArlbergLogFile.getName());
			}
		} else {
			AnalyzeLabel.setText("???!!!!");
		}
		if(ArlbergLogFile!=null) {
			jbAnalyze.setEnabled(true);
			AnalyzeLabel.setEnabled(true);
			try {
				// do the stuff
				x = new AnalyzeThread(this);
				outputf = new FileOutputStream(defvardiff);
				outputf.write((new String(JFileChooser4ArlbergLogFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.close();
				AnalyzeLabel.setText("Analyzing!");
				jbAnalyze.setEnabled(false);
			} catch(Exception e) {
				System.err.println(e);
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
		// Arlberg1 build  = new Arlberg1();
		new Arlberg1();
	}



	/**
	 *  The main program for the Arlberg1 class
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

