/*
 * $Log$
 * emariacher - Tuesday, November 17, 2009 11:29:55 AM
 * cleaning html err area
 * emariacher - Tuesday, November 17, 2009 10:45:31 AM
 * err textarea is now in html.
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class Matrix3 extends JPanel implements ActionListener {
	/**
	 * 
	 */
	JFrame buildFrame;
	JPanel buildPanel;
	JLabel jlblFileChooser4MainMatrixFile,jlblFileChooser4FnMatrixFile,
	jlblFileChooser4LanguageStatsFile, AnalyzeLabel, zeSeapineLbl_Label,
	toolTitleLabel1, toolTitleLabel2;
	JButton jbOpenMainMatrixFile, jbOpenFnMatrixFile, jbOpenLanguageStatsFile, jbAnalyze;
	JFileChooser JFileChooser4MainMatrixFile, JFileChooser4FnMatrixFile, JFileChooser4LanguageStatsFile;
	ErrorPane err = new ErrorPane();
	File defvardiff;
	FileOutputStream outputf;
	File MainMatrixFile = null;
	File FnMatrixFile = null;
	File LanguageStatsFile = null;
	AnalyzeThread x;


	public Matrix3() {
		super(new BorderLayout());
		//Create and set up the window.
		buildFrame = new JFrame("Check ghostkey triplet appearance in language stats file");
		buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildFrame.setSize(new Dimension(200, 800));

		//Create and set up the panel.
		buildPanel = new JPanel(new GridLayout(5, 2));
		err.setMargin(new Insets(1, 1, 1, 1));
		err.setEditable(false);
		JScrollPane errScrollPane   = new JScrollPane(err);
		errScrollPane.setPreferredSize(new Dimension(200, 600));
		errScrollPane.setMinimumSize(new Dimension(200, 600));
		err.setForeground(Color.red);
		err.setFont(new java.awt.Font("Courier New", 0, 12));
		err.setContentType("text/html");
//		err.setText("<b>Hello</b> World!");
		err.appendErr("<pre>");

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
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 5;
		c.fill = GridBagConstraints.BOTH;
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
		ZFileFilter filtertxt       = new ZFileFilter("txt");
		ZFileFilter filtercsv       = new ZFileFilter("csv");

		JFileChooser4MainMatrixFile = new JFileChooser();
		JFileChooser4MainMatrixFile.setFileFilter(filtercsv);
		jbOpenMainMatrixFile = new JButton("Main Matrix File");
		jbOpenMainMatrixFile.addActionListener(this);
		jlblFileChooser4MainMatrixFile = new JLabel("no "+jbOpenMainMatrixFile.getText() + " Selected yet", SwingConstants.LEFT);

		JFileChooser4FnMatrixFile = new JFileChooser();
		JFileChooser4FnMatrixFile.setFileFilter(filtercsv);
		jbOpenFnMatrixFile = new JButton("Fn Matrix File");
		jbOpenFnMatrixFile.addActionListener(this);
		jlblFileChooser4FnMatrixFile = new JLabel("no "+jbOpenFnMatrixFile.getText() + " Selected yet", SwingConstants.LEFT);

		JFileChooser4LanguageStatsFile = new JFileChooser();
		JFileChooser4LanguageStatsFile.setFileFilter(filtertxt);
		jbOpenLanguageStatsFile = new JButton("Language Word Frequency File");
		jbOpenLanguageStatsFile.addActionListener(this);
		jlblFileChooser4LanguageStatsFile = new JLabel("no "+jbOpenLanguageStatsFile.getText() + " Selected yet", SwingConstants.LEFT);

		jbAnalyze = new JButton("Analyze!");
		jbAnalyze.addActionListener(this);
		AnalyzeLabel = new JLabel("not analyzed yet", SwingConstants.LEFT);

		//Add the widgets to the container.
		buildPanel.add(toolTitleLabel1);
		buildPanel.add(toolTitleLabel2);
		buildPanel.add(jbOpenMainMatrixFile);
		buildPanel.add(jlblFileChooser4MainMatrixFile);
		buildPanel.add(jbOpenFnMatrixFile);
		buildPanel.add(jlblFileChooser4FnMatrixFile);
		buildPanel.add(jbOpenLanguageStatsFile);
		buildPanel.add(jlblFileChooser4LanguageStatsFile);
		buildPanel.add(jbAnalyze);
		buildPanel.add(AnalyzeLabel);
		jbAnalyze.setEnabled(false);
		AnalyzeLabel.setEnabled(false);

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
						JFileChooser4LanguageStatsFile.setCurrentDirectory(new File(s));
						break;
					case 2:
						JFileChooser4FnMatrixFile.setCurrentDirectory(new File(s));
						break;
					}
					cpt++;
				}
			}
		} catch(FileNotFoundException e) {
			err.appendErr("             *warning* no File with default values found! expected at[" +
					defvardiff.getName() + "] continuing...");
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(e);
			System.out.println(e);
			err.appendErr(e.toString());
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  event  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == jbOpenMainMatrixFile) {
			int returnVal  = JFileChooser4MainMatrixFile.showOpenDialog(this);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				MainMatrixFile = JFileChooser4MainMatrixFile.getSelectedFile();
				jlblFileChooser4MainMatrixFile.setText(MainMatrixFile.getName());
			}
		} else if(event.getSource() == jbOpenLanguageStatsFile) {
			int returnVal  = JFileChooser4LanguageStatsFile.showOpenDialog(this);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				LanguageStatsFile = JFileChooser4LanguageStatsFile.getSelectedFile();
				jlblFileChooser4LanguageStatsFile.setText(LanguageStatsFile.getName());
			}
		} else if(event.getSource() == jbOpenFnMatrixFile) {
			int returnVal  = JFileChooser4FnMatrixFile.showOpenDialog(this);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				FnMatrixFile = JFileChooser4FnMatrixFile.getSelectedFile();
				jlblFileChooser4FnMatrixFile.setText(FnMatrixFile.getName());
			}
		} else {
			AnalyzeLabel.setText("???!!!!");
		}
		if((MainMatrixFile!=null) && (FnMatrixFile!=null) && (LanguageStatsFile!=null)) {
			jbAnalyze.setEnabled(true);
			AnalyzeLabel.setEnabled(true);
			try {
				// do the stuff
				x = new AnalyzeThread(this);
				outputf = new FileOutputStream(defvardiff);
				outputf.write((new String(JFileChooser4MainMatrixFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.write((new String(JFileChooser4LanguageStatsFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.write((new String(JFileChooser4FnMatrixFile.getCurrentDirectory().toString() + "\n")).getBytes());
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
		// Matrix2 build  = new Matrix2();
		new Matrix3();
	}



	/**
	 *  The main program for the Matrix2 class
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

	protected void addStylesToDocument(StyledDocument doc) {
		//Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().
		getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "Arial");

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);

		s = doc.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);

		//        s = doc.addStyle("icon", regular);
		//        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		//        ImageIcon pigIcon = createImageIcon("images/Pig.gif",
		//                                            "a cute pig");
		//        if (pigIcon != null) {
		//            StyleConstants.setIcon(s, pigIcon);
		//        }
		//
		//        s = doc.addStyle("button", regular);
		//        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		//        ImageIcon soundIcon = createImageIcon("images/sound.gif",
		//                                              "sound icon");
		//        JButton button = new JButton();
		//        if (soundIcon != null) {
		//            button.setIcon(soundIcon);
		//        } else {
		//            button.setText("BEEP");
		//        }
		//        button.setCursor(Cursor.getDefaultCursor());
		//        button.setMargin(new Insets(0,0,0,0));
		//        button.setActionCommand(buttonString);
		//        button.addActionListener(this);
		//        StyleConstants.setComponent(s, button);
	}


}

