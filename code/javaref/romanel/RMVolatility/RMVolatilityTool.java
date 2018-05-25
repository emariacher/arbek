
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2006
 */
public class RMVolatilityTool extends JPanel implements ActionListener, DefVar {
	File RMVolatilityExportTxtFile = null;
	JFrame buildFrame;
	JPanel buildPanel;
	JTextField SCMServerIPAddress, SCMServerIPPort, mainline, branch, TTProDBName,
		username, zeRMVolatilityLbl_;
	JLabel jlblFileChooser4RMVolatilityExportTxtFile,
		jlblFileChooser4BaseReleaseNote, AnalyzeLabel, zeRMVolatilityLbl_Label,
		toolTitleLabel1, toolTitleLabel2;
	JButton jbBuild, jbOpenRMVolatilityExportTxtFile, jbOpenBaseReleaseNote, jbAnalyze;
	JCheckBox xtalButton, volatButton, maturButton;
	JPasswordField pwd;
	JFileChooser JFileChooser4RMVolatilityExportTxtFile, JFileChooser4BaseReleaseNote;
	JTextArea err;
	JTextPane html;
	File defvardiff;
	BufferedReader inputf;
	FileOutputStream outputf;
	RMVolatility sstp;


	/**
	 *  Constructor for the RMVolatilityTool object
	 */
	public RMVolatilityTool() {
		super(new BorderLayout());
		//Create and set up the window.
		buildFrame = new JFrame("RM Volatility [Eric Mariacher]");
		buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildFrame.setSize(new Dimension(200, 800));

		//Create and set up the panel.
		buildPanel = new JPanel(new GridLayout(5, 2));
		err = new JTextArea(20, 60);
		err.setMargin(new Insets(1, 1, 1, 1));
		err.setEditable(false);
		JScrollPane errScrollPane = new JScrollPane(err);
		err.setForeground(Color.red);
		html = new JTextPane();
		html.setMargin(new Insets(1, 1, 1, 1));
		html.setEditable(false);
		html.setContentType("text/html");
		JScrollPane htmlScrollPane = new JScrollPane(html);

		//Add the widgets.
		addWidgets();

		//Add the panel to the window.
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
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



	/**
	 *  Create and add the widgets.
	 */
	private void addWidgets() {
		Font bigFont = new Font(null, Font.BOLD, 16);
		String toolTitleLabel1Txt = new String("RM Volatility");
		String toolTitleLabel2Txt = new String("RM Volatility");
		toolTitleLabel1 = new JLabel(toolTitleLabel1Txt, SwingConstants.LEFT);
		toolTitleLabel1.setFont(bigFont);
		toolTitleLabel2 = new JLabel(toolTitleLabel2Txt, SwingConstants.LEFT);
		toolTitleLabel2.setFont(bigFont);
		JFileChooser4RMVolatilityExportTxtFile = new JFileChooser();
		ZeFileFilter filter = new ZeFileFilter();
		JFileChooser4RMVolatilityExportTxtFile.setFileFilter(filter);
		jbOpenRMVolatilityExportTxtFile = new JButton("Caliber Export File");
		jbOpenRMVolatilityExportTxtFile.addActionListener(this);
		jlblFileChooser4RMVolatilityExportTxtFile = new JLabel("no Caliber Export File Selected yet", SwingConstants.LEFT);

		jbAnalyze = new JButton("Analyze!");
		jbAnalyze.addActionListener(this);
		AnalyzeLabel = new JLabel("not analyzed yet", SwingConstants.LEFT);

		xtalButton = new JCheckBox("Xtalization");
		xtalButton.setMnemonic(KeyEvent.VK_X);
		volatButton = new JCheckBox("Volatility");
		volatButton.setMnemonic(KeyEvent.VK_V);
		maturButton = new JCheckBox("Maturity");
		maturButton.setMnemonic(KeyEvent.VK_M);

		//Add the widgets to the container.
		buildPanel.add(toolTitleLabel1);
		buildPanel.add(toolTitleLabel2);
		buildPanel.add(jbOpenRMVolatilityExportTxtFile);
		buildPanel.add(jlblFileChooser4RMVolatilityExportTxtFile);
		buildPanel.add(jbAnalyze);
		buildPanel.add(AnalyzeLabel);
		jbAnalyze.setEnabled(false);
		AnalyzeLabel.setEnabled(false);
		buildPanel.add(xtalButton);
		buildPanel.add(volatButton);
		buildPanel.add(maturButton);


		toolTitleLabel2.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		AnalyzeLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		//read default variables file
		try {
			defvardiff = new File("RMVolatilityTool");
			if (defvardiff.canRead()) {
				inputf = new BufferedReader(new FileReader(defvardiff));
				String linef;
				int cpt = 0;
				while ((linef = inputf.readLine()) != null) {
					switch (cpt) {
							case 0:
								JFileChooser4RMVolatilityExportTxtFile.setCurrentDirectory(new File(linef));
								break;
							case 1:
								if (linef.compareTo("true") == 0) {
									xtalButton.setSelected(true);
								} else {
									xtalButton.setSelected(false);
								}
								break;
							case 2:
								if (linef.compareTo("true") == 0) {
									volatButton.setSelected(true);
								} else {
									volatButton.setSelected(false);
								}
								break;
							case 3:
								if (linef.compareTo("true") == 0) {
									maturButton.setSelected(true);
								} else {
									maturButton.setSelected(false);
								}
								break;
					}
					cpt++;
				}
				inputf.close();
			}
		} catch (FileNotFoundException e) {
			err.append("             *warning* no File with default values found! expected at[" +
				defvardiff.getName() + "] continuing...");
		} catch (Exception e) {
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
		//log.append(event.toString() + "$$$$$$$$$$");
		if (event.getSource() == jbOpenRMVolatilityExportTxtFile) {
			int returnVal = JFileChooser4RMVolatilityExportTxtFile.showOpenDialog(RMVolatilityTool.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				RMVolatilityExportTxtFile = JFileChooser4RMVolatilityExportTxtFile.getSelectedFile();
				jlblFileChooser4RMVolatilityExportTxtFile.setText(RMVolatilityExportTxtFile.getName());
				jbAnalyze.setEnabled(true);
				AnalyzeLabel.setEnabled(true);
			}
			//} else if(event.getSource() == jbAnalyze) {
			try {
				sstp = new RMVolatility(this);
				outputf = new FileOutputStream(defvardiff);
				outputf.write((new String(JFileChooser4RMVolatilityExportTxtFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.write((new String(xtalButton.isSelected() + "\n")).getBytes());
				outputf.write((new String(volatButton.isSelected() + "\n")).getBytes());
				outputf.write((new String(maturButton.isSelected() + "\n")).getBytes());
				outputf.close();
				AnalyzeLabel.setText("Analyzing!");
				jbAnalyze.setEnabled(false);
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
			}
		} else {
			AnalyzeLabel.setText("???!!!!");
		}
	}


	/**
	 *  Create the GUI and show it. For thread safety, this method should be
	 *  invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		RMVolatilityTool build = new RMVolatilityTool();
	}



	/**
	 *  The main program for the RMVolatilityTool class
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

