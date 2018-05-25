/*
$Header: AnalogMatrix1.java: Revision: 17: Author: emariacher: Date: Wednesday, July 08, 2009 6:33:13 PM$

$Log$
emariacher - Wednesday, July 08, 2009 6:33:13 PM
get all ghost keys. not debugged yet.
emariacher - Wednesday, July 08, 2009 2:16:41 PM
do some stats when pressing all the 3 other keys (in a ghost square).
emariacher - Tuesday, July 07, 2009 5:39:50 PM
real matrix scan seems to be somewhat working...
emariacher - Tuesday, July 07, 2009 5:08:11 PM
matrix scan: pas encore au point.
emariacher - Tuesday, July 07, 2009 3:50:59 PM
clean "clear pressed keys". not 1 shot anymore...
emariacher - Thursday, July 02, 2009 6:00:01 PM
check actual vs computed.
emariacher - Thursday, July 02, 2009 3:39:32 PM
just logging changes...
emariacher - Wednesday, July 01, 2009 4:07:44 PM
static ghost key detection with "analyze key pressed" button
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class AnalogMatrix1 extends JPanel implements ActionListener {
	/**
	 * 
	 */
	JFrame buildFrame;
	JPanel buildPanel;
	private JLabel jlblFileChooser4MainMatrixFile, jlblFileChooser4MembraneFile, jlblFileChooser4ADtableFile;
	JLabel jlblQuartoBatch;
	JLabel AnalyzeLabel;
	private JLabel toolTitleLabel1, toolTitleLabel2;
	JButton jbOpenMainMatrixFile, jbOpenMembraneFile, jbOpenADtableFile, jbAnalyze, jbClear,
	jbQuartoBatch, jbQuartoBatchRandom, jbWriteMembraneFile, jbOptimize, jbScan, jb3OtherKeysScan;
	private JFileChooser JFileChooser4MainMatrixFile, JFileChooser4MembraneFile, JFileChooser4ADtableFile;
	JTextArea err;
	private File defvardiff;
	File f_MainMatrixFile = null;
	File f_MembraneFile = null;
	File f_ADtableFile = null;
	AnalyzeThread x=null;
	GraphPanel graphPanel;
	private JLabel ClearLabel;
	JLabel jlblQuartoBatchRandom;
	JTextField jtSeedNumber;
	private JLabel jlblOptimize;



	public AnalogMatrix1() {
		super(new BorderLayout());
		//Create and set up the window.
		buildFrame = new JFrame("Check, Optimize KBD Matrix for AD scanning");
		buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildFrame.setSize(new Dimension(1000, 600));

		//Create and set up the panel.
		buildPanel = new JPanel(new GridLayout(11, 2));
		err = new JTextArea(200, 300);
		err.setMargin(new Insets(1, 1, 1, 1));
		err.setEditable(false);
		JScrollPane errScrollPane   = new JScrollPane(err);
		errScrollPane.setPreferredSize(new Dimension(1000, 600));
		errScrollPane.setMinimumSize(new Dimension(1000, 600));
		err.setForeground(Color.red);
		err.setFont(new java.awt.Font("Courier New", 0, 12));
		graphPanel = new GraphPanel(this);
		graphPanel.setBackground(Color.PINK);
		graphPanel.validate();
		graphPanel.setVisible(true);



		//Add the widgets.
		addWidgets();

		//Add the panel to the window.
		GridBagLayout gridbag       = new GridBagLayout();
		GridBagConstraints c        = new GridBagConstraints();
		buildFrame.getContentPane().setLayout(gridbag);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		gridbag.setConstraints(buildPanel, c);
		buildFrame.getContentPane().add(buildPanel);
		//		c.gridheight = GridBagConstraints.REMAINDER;
		//		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.BOTH;
		gridbag.setConstraints(graphPanel, c);
		buildFrame.getContentPane().add(graphPanel);
		gridbag.setConstraints(errScrollPane, c);
		buildFrame.getContentPane().add(errScrollPane);
		buildFrame.pack();
		buildFrame.setSize(buildFrame.getPreferredSize());
		buildFrame.setVisible(true);
	}



	/**  Create and add the widgets. */
	private void addWidgets() {
		Font bigFont               = new Font(null, Font.BOLD, 16);
		String toolTitleLabel1Txt  = new String("Matrix Tool1");
		String toolTitleLabel2Txt  = new String("Matrix Tool5");
		toolTitleLabel1 = new JLabel(toolTitleLabel1Txt, SwingConstants.LEFT);
		toolTitleLabel1.setFont(bigFont);
		toolTitleLabel2 = new JLabel(toolTitleLabel2Txt, SwingConstants.LEFT);
		toolTitleLabel2.setFont(bigFont);
		ZFileFilter filtercsv       = new ZFileFilter("csv");

		JFileChooser4MainMatrixFile = new JFileChooser();
		JFileChooser4MainMatrixFile.setFileFilter(filtercsv);
		jbOpenMainMatrixFile = new JButton("Main Matrix File");
		jbOpenMainMatrixFile.addActionListener(this);
		jlblFileChooser4MainMatrixFile = new JLabel("no "+jbOpenMainMatrixFile.getText() + " Selected yet", SwingConstants.LEFT);

		JFileChooser4ADtableFile = new JFileChooser();
		JFileChooser4ADtableFile.setFileFilter(filtercsv);
		jbOpenADtableFile = new JButton("AD table File");
		jbOpenADtableFile.addActionListener(this);
		jlblFileChooser4ADtableFile = new JLabel("no "+jbOpenADtableFile.getText() + " Selected yet (optional)", SwingConstants.LEFT);

		JFileChooser4MembraneFile = new JFileChooser();
		JFileChooser4MembraneFile.setFileFilter(filtercsv);
		jbOpenMembraneFile = new JButton("Membrane File");
		jbOpenMembraneFile.addActionListener(this);
		jlblFileChooser4MembraneFile = new JLabel("no "+jbOpenMembraneFile.getText() + " Selected yet", SwingConstants.LEFT);

		jbAnalyze = new JButton("Analyze Key Pressed!");
		jbAnalyze.addActionListener(this);
		AnalyzeLabel = new JLabel("not analyzed yet", SwingConstants.LEFT);

		jbClear = new JButton("Clear Key Pressed!");
		jbClear.addActionListener(this);
		ClearLabel = new JLabel("Clear key pressed", SwingConstants.LEFT);

		jbQuartoBatch = new JButton("Run QuartoBatch!");
		jbQuartoBatch.addActionListener(this);
		jlblQuartoBatch = new JLabel("QuartoBatch not run yet", SwingConstants.LEFT);

		jbQuartoBatchRandom = new JButton("Run QuartoBatchRandom!");
		jbQuartoBatchRandom.addActionListener(this);
		jlblQuartoBatchRandom = new JLabel("QuartoBatchRandom not run yet", SwingConstants.LEFT);

		jbOptimize = new JButton("Search for best Matrix!");
		jbOptimize.addActionListener(this);
		jlblOptimize = new JLabel("Optimize not run yet", SwingConstants.LEFT);

		jbWriteMembraneFile = new JButton("Output Membrane File");
		jbWriteMembraneFile.addActionListener(this);
		jtSeedNumber = new JTextField(5);
		jtSeedNumber.addActionListener(this);

		jbScan = new JButton("Scan Matrix!");
		jbScan.addActionListener(this);

		jb3OtherKeysScan = new JButton("3otherKeys!");
		jb3OtherKeysScan.addActionListener(this);


		//Add the widgets to the container.
		buildPanel.add(toolTitleLabel1);
		buildPanel.add(toolTitleLabel2);
		buildPanel.add(jbOpenMainMatrixFile);
		buildPanel.add(jlblFileChooser4MainMatrixFile);
		buildPanel.add(jbOpenADtableFile);
		buildPanel.add(jlblFileChooser4ADtableFile);
		buildPanel.add(jbOpenMembraneFile);
		buildPanel.add(jlblFileChooser4MembraneFile);
		buildPanel.add(jbAnalyze);
		buildPanel.add(AnalyzeLabel);
		buildPanel.add(jbClear);
		buildPanel.add(ClearLabel);
		jbAnalyze.setEnabled(true);
		AnalyzeLabel.setEnabled(true);
		buildPanel.add(jbQuartoBatch);
		buildPanel.add(jlblQuartoBatch);
		jbQuartoBatch.setEnabled(true);
		jlblQuartoBatch.setEnabled(true);

		buildPanel.add(jbQuartoBatchRandom);
		buildPanel.add(jlblQuartoBatchRandom);
		jbQuartoBatchRandom.setEnabled(true);
		jlblQuartoBatchRandom.setEnabled(true);

		buildPanel.add(jbOptimize);
		buildPanel.add(jlblOptimize);

		buildPanel.add(jbWriteMembraneFile);
		buildPanel.add(jtSeedNumber);

		buildPanel.add(jbScan);
		buildPanel.add(jb3OtherKeysScan);

		toolTitleLabel2.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		AnalyzeLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		//read default variables file
		try {
			defvardiff = new File(getClass().getName());
			if(defvardiff.canRead()) {
				Scanner sc = new Scanner(defvardiff);
				int cpt       = 0;
				while (sc.hasNextLine()) {
					String s = sc.nextLine();
					switch (cpt) {
					case 0:
						JFileChooser4MainMatrixFile.setCurrentDirectory(new File(s));
						break;
					case 1:
						JFileChooser4MembraneFile.setCurrentDirectory(new File(s));
						break;
					case 2:
						JFileChooser4ADtableFile.setCurrentDirectory(new File(s));
						break;
					}
					cpt++;
				}
			}
		} catch(FileNotFoundException e) {
			System.err.println("             *warning* no File with default values found! expected at[" +
					defvardiff.getName() + "] continuing...");
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(e);
			System.out.println(e);
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  event  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent event) {
		try {

			if(event.getSource() == jbOpenMainMatrixFile) {
				int returnVal  = JFileChooser4MainMatrixFile.showOpenDialog(AnalogMatrix1.this);

				if(returnVal == JFileChooser.APPROVE_OPTION) {
					f_MainMatrixFile = JFileChooser4MainMatrixFile.getSelectedFile();
					jlblFileChooser4MainMatrixFile.setText(f_MainMatrixFile.getName());
				}
			} else if(event.getSource() == jbOpenADtableFile) {
				int returnVal  = JFileChooser4ADtableFile.showOpenDialog(AnalogMatrix1.this);

				if(returnVal == JFileChooser.APPROVE_OPTION) {
					f_ADtableFile = JFileChooser4ADtableFile.getSelectedFile();
					jlblFileChooser4ADtableFile.setText(f_ADtableFile.getName());
				}
			} else if(event.getSource() == jbOpenMembraneFile) {
				int returnVal  = JFileChooser4MembraneFile.showOpenDialog(AnalogMatrix1.this);

				if(returnVal == JFileChooser.APPROVE_OPTION) {
					f_MembraneFile = JFileChooser4MembraneFile.getSelectedFile();
					jlblFileChooser4MembraneFile.setText(f_MembraneFile.getName());
				}
			} else if(event.getSource() == jbAnalyze) {
				AnalyzeMatrix am = new AnalyzeMatrix(x.L, graphPanel, true, AnalyzeMatrix.RUNALLQUARTOBATCH);
				x.L.myPrintln("**Analyze*********************************************************************");
				ArrayList<Key> l_keys = am.elecsimu.simplifyNetwork(graphPanel.k_startCol);
				if(graphPanel.k_endRow==null) {
					ArrayList<Key> l_activeRows=am.getActiveRows(l_keys);
					graphPanel.k_endRow=l_activeRows.get(0);
				}
				double d_current = am.elecsimu.compute_current(am.k_startCol, am.k_endRow, l_keys);
				Result r = am.elecsimu.check_actual_vs_computed(am.k_startCol, am.k_endRow, d_current);
				x.L.myErrPrintln(r.s_key_pressed_check);
				AnalyzeLabel.setText(r.s_key_pressed_check);
				graphPanel.backup_and_reset();
				am.kc.addEdges(l_keys);
			} else if(event.getSource() == jb3OtherKeysScan) {
				if(graphPanel.hasKeyPressed()) {
					graphPanel.setI_QuartoBatch(GraphPanel.OTHERKEYS);
				} else {
					graphPanel.setI_QuartoBatch(GraphPanel.GETALLGHOSTS);
				}
			} else if(event.getSource() == jbScan) {
				AnalyzeMatrix am = new AnalyzeMatrix(x.L, graphPanel, true, AnalyzeMatrix.RUNALLQUARTOBATCH);
				x.L.myPrintln("**Scan*********************************************************************");
				ArrayList<Key> l_keys = am.elecsimu.simplifyNetwork(graphPanel.k_startCol);
				ArrayList<Key> l_keys2Press = am.elecsimu.getKeysPressed();
				ArrayList<Key> l_activeRows=am.getActiveRows(l_keys);
				ArrayList<Key> l_activeCols=am.getActiveCols(l_keys);
				for(Key kscol : l_activeCols) {
					for(Key kerow : l_activeRows) {
						graphPanel.clearPressedKeys();
						am.pressKeys(l_keys2Press);
						am.setK_startCol(kscol);
						am.setK_endRow(kerow);
						graphPanel.start_spread_current();
						l_keys = am.elecsimu.simplifyNetwork(kscol);
						double d_current = am.elecsimu.compute_current(am.k_startCol, am.k_endRow, l_keys);
						Result r = am.elecsimu.check_actual_vs_computed(am.k_startCol, am.k_endRow, d_current);
						x.L.myErrPrintln(r.s_key_pressed_check);
						AnalyzeLabel.setText(r.s_key_pressed_check);
					}
				}
			} else if(event.getSource() == jbQuartoBatch) {
				if(graphPanel.hasKeyPressed()) {
					graphPanel.setI_QuartoBatch(GraphPanel.SYNCGRAPH);
				} else {
					graphPanel.setI_QuartoBatch(GraphPanel.ALLNOSYNCGRAPH);
				}
				Thread.sleep(300);
				graphPanel.i_QuartoBatch=0;
			} else if(event.getSource() == jbWriteMembraneFile) {
				int i_seed = Integer.valueOf(jtSeedNumber.getText());
				graphPanel.randomizeResistances(i_seed);
				@SuppressWarnings("unused")
				MembraneFile mf = new MembraneFile(x.L,graphPanel,i_seed);
			} else if(event.getSource() == jbQuartoBatchRandom) {
				if(graphPanel.hasKeyPressed()) {
					graphPanel.setI_QuartoBatch(GraphPanel.ONEQUARTORANDOM);
				} else {
					graphPanel.setI_QuartoBatch(GraphPanel.ALLQUARTORANDOM);
				}
				Thread.sleep(300);
				graphPanel.i_QuartoBatch=0;
			} else if(event.getSource() == jbOptimize) {
				graphPanel.setI_QuartoBatch(GraphPanel.OPTIMIZE);
				Thread.sleep(300);
				graphPanel.i_QuartoBatch=0;
			} else if(event.getSource() == jbClear) {
				graphPanel.restore();
				graphPanel.clearPressedKeys();
				graphPanel.k_startCol=null;
				graphPanel.k_endRow=null;
				x = new AnalyzeThread(this);
				AnalyzeLabel.setText("Pressed keys cleared.");
			} else {
				AnalyzeLabel.setText("???!!!!");
			}
			if((f_MainMatrixFile!=null)&&(f_MembraneFile!=null)&&(x==null)) {
				jbAnalyze.setEnabled(true);
				AnalyzeLabel.setEnabled(true);
				// do the stuff
				x = new AnalyzeThread(this);
				FileOutputStream outputf = new FileOutputStream(defvardiff);
				outputf.write((new String(JFileChooser4MainMatrixFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.write((new String(JFileChooser4MembraneFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.write((new String(JFileChooser4ADtableFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.close();
				graphPanel.start();
				AnalyzeLabel.setText("Building Matrix!");
			}

		} catch(Exception e) {
			System.err.println(e);
			e.printStackTrace();
			AnalyzeLabel.setText("*******Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				x.L.myPrintln(e.toString());
				x.L.myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					x.L.err.append(new String(buf, 0, len));
					x.L.myErrPrintln(new String(buf, 0, len));
					x.L.myPrintln(new String(buf, 0, len));
				}

			} catch (Exception ze) {}
		}

	}


	/**
	 *  Create the GUI and show it. For thread safety, this method should be
	 *  invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		// AnalogMatrix1 build  = new AnalogMatrix1();
		new AnalogMatrix1();
	}



	/**
	 *  The main program for the AnalogMatrix1 class
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


	static class ZFileFilter extends FileFilter {
		String s_type=null;

		public ZFileFilter(String s_extension) {
			s_type = new String(s_extension);
		}

		//Accept all directories and all gif, jpg, tiff, or png files.
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals(s_type)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		//The description of this filter
		public String getDescription() {
			return "Just "+s_type+" files";
		}

		private String getExtension(File f) {
			String ext  = null;
			String s    = f.getName();
			int i       = s.lastIndexOf('.');

			if(i > 0 && i < s.length() - 1) {
				ext = s.substring(i + 1).toLowerCase();
			}
			return ext;
		}

	}



}
