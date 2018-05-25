
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;


/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 26, 2006
 */
public class SeapineStatTool7 extends JPanel implements ActionListener, DefVar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel buildPanel;
	private JLabel jlblFileChooser4SeapineExportTxtFile,
	toolTitleLabel1, toolTitleLabel2;
	JLabel AnalyzeLabel;
	private JButton jbOpenSeapineExportTxtFile, jbAnalyze;
	private JFileChooser JFileChooser4SeapineExportTxtFile;
	JTextArea err;
	private File defvardiff;
	File SeapineReportHtmlFile = null;


	/**  Constructor for the SeapineStatTool6 object */
	public SeapineStatTool7() {
		super(new BorderLayout());
		//Create and set up the window.
		JFrame buildFrame = new JFrame("Bug trends statistics");
		buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildFrame.setSize(new Dimension(200, 800));

		//Create and set up the panel.
		buildPanel = new JPanel(new GridLayout(3, 2));
		err = new JTextArea(20, 60);
		err.setMargin(new Insets(1, 1, 1, 1));
		err.setEditable(false);
		JScrollPane errScrollPane   = new JScrollPane(err);
		errScrollPane.setPreferredSize(new Dimension(200, 600));
		errScrollPane.setMinimumSize(new Dimension(200, 600));
		err.setForeground(Color.red);

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
		String toolTitleLabel1Txt  = new String("AoxomoxoA");
		String toolTitleLabel2Txt  = new String("Eric Mariacher");
		toolTitleLabel1 = new JLabel(toolTitleLabel1Txt, SwingConstants.LEFT);
		toolTitleLabel1.setFont(bigFont);
		toolTitleLabel2 = new JLabel(toolTitleLabel2Txt, SwingConstants.LEFT);
		toolTitleLabel2.setFont(bigFont);
		JFileChooser4SeapineExportTxtFile = new JFileChooser();
		ZFileFilter filter       = new ZFileFilter("html");
		JFileChooser4SeapineExportTxtFile.setFileFilter(filter);
		jbOpenSeapineExportTxtFile = new JButton("Seapine Export File");
		jbOpenSeapineExportTxtFile.addActionListener(this);
		jlblFileChooser4SeapineExportTxtFile = new JLabel("no Seapine Export File Selected yet", SwingConstants.LEFT);
		AbstractButton button = SwingUtils.getDescendantOfType(AbstractButton.class,
				JFileChooser4SeapineExportTxtFile, "Icon", UIManager.getIcon("FileChooser.detailsViewIcon"));
		button.doClick();
		jbAnalyze = new JButton("Analyze!");
		jbAnalyze.addActionListener(this);
		AnalyzeLabel = new JLabel("not analyzed yet", SwingConstants.LEFT);

		//Add the widgets to the container.
		buildPanel.add(toolTitleLabel1);
		buildPanel.add(toolTitleLabel2);
		buildPanel.add(jbOpenSeapineExportTxtFile);
		buildPanel.add(jlblFileChooser4SeapineExportTxtFile);
		buildPanel.add(jbAnalyze);
		buildPanel.add(AnalyzeLabel);
		jbAnalyze.setEnabled(false);
		AnalyzeLabel.setEnabled(false);

		toolTitleLabel2.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		AnalyzeLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		//read default variables file
		try {
			defvardiff = new File("SeapineStatTool6");
			if(defvardiff.canRead()) {
				Scanner sc = new Scanner(defvardiff);
				int cpt       = 0;
				while (sc.hasNextLine()) {
					String s = sc.nextLine();
					switch (cpt) {
					case 0:
						JFileChooser4SeapineExportTxtFile.setCurrentDirectory(new File(s));
						break;
					}
					cpt++;
				}
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
		//log.append(event.toString() + "$$$$$$$$$$");
		if(event.getSource() == jbOpenSeapineExportTxtFile) {
			int returnVal  = JFileChooser4SeapineExportTxtFile.showOpenDialog(SeapineStatTool7.this);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				SeapineReportHtmlFile = JFileChooser4SeapineExportTxtFile.getSelectedFile();
				jlblFileChooser4SeapineExportTxtFile.setText(SeapineReportHtmlFile.getName());
				jbAnalyze.setEnabled(true);
				AnalyzeLabel.setEnabled(true);
			}
			//} else if(event.getSource() == jbAnalyze) {
			try {
				new SeapineStatTool7Parser(this);
				FileOutputStream outputf = new FileOutputStream(defvardiff);
				outputf.write((new String(JFileChooser4SeapineExportTxtFile.getCurrentDirectory().toString() + "\n")).getBytes());
				outputf.close();
				AnalyzeLabel.setText("Analyzing!");
				jbAnalyze.setEnabled(false);
			} catch(Exception e) {
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
		// SeapineStatTool6 build  = new SeapineStatTool6();
		new SeapineStatTool7();
	}



	/**
	 *  The main program for the SeapineStatTool6 class
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

