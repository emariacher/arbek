
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 26, 2006
 */
public class SeapineStatTool5 extends JPanel implements ActionListener, DefVar {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame buildFrame;
  JPanel buildPanel;
  JTextField SCMServerIPAddress, SCMServerIPPort, mainline, branch, TTProDBName,
      username, zeSeapineLbl_;
  JLabel jlblFileChooser4SeapineExportTxtFile,
      jlblFileChooser4BaseReleaseNote, AnalyzeLabel, zeSeapineLbl_Label,
      toolTitleLabel1, toolTitleLabel2;
  JButton jbBuild, jbOpenSeapineExportTxtFile, jbOpenBaseReleaseNote, jbAnalyze;
  JPasswordField pwd;
  JFileChooser JFileChooser4SeapineExportTxtFile, JFileChooser4BaseReleaseNote;
  JTextArea err;
  JTextPane html;
  File defvardiff;
  BufferedReader inputf;
  FileOutputStream outputf;
  File SeapineReportHtmlFile = null;
  SeapineStatTool5Parser sstp;


  /**  Constructor for the SeapineStatTool5 object */
  public SeapineStatTool5() {
    super(new BorderLayout());
          //Create and set up the window.
    buildFrame = new JFrame("Build File Release Labeled and based on Changes");
    buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    buildFrame.setSize(new Dimension(200, 800));

          //Create and set up the panel.
    buildPanel = new JPanel(new GridLayout(3, 2));
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
    String toolTitleLabel1Txt  = new String("Seapine Stat Tool1");
    String toolTitleLabel2Txt  = new String("Seapine Stat Tool5");
    toolTitleLabel1 = new JLabel(toolTitleLabel1Txt, SwingConstants.LEFT);
    toolTitleLabel1.setFont(bigFont);
    toolTitleLabel2 = new JLabel(toolTitleLabel2Txt, SwingConstants.LEFT);
    toolTitleLabel2.setFont(bigFont);
    JFileChooser4SeapineExportTxtFile = new JFileChooser();
    TxtFileFilter filter       = new TxtFileFilter();
    JFileChooser4SeapineExportTxtFile.setFileFilter(filter);
    jbOpenSeapineExportTxtFile = new JButton("Seapine Export File");
    jbOpenSeapineExportTxtFile.addActionListener(this);
    jlblFileChooser4SeapineExportTxtFile = new JLabel("no Seapine Export File Selected yet", SwingConstants.LEFT);

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
      defvardiff = new File("SeapineStatTool5");
      if(defvardiff.canRead()) {
        inputf = new BufferedReader(new FileReader(defvardiff));
        String linef;
        int cpt       = 0;
        while((linef = inputf.readLine()) != null) {
          switch (cpt) {
            case 0:
              JFileChooser4SeapineExportTxtFile.setCurrentDirectory(new File(linef));
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
          //log.append(event.toString() + "$$$$$$$$$$");
    if(event.getSource() == jbOpenSeapineExportTxtFile) {
      int returnVal  = JFileChooser4SeapineExportTxtFile.showOpenDialog(SeapineStatTool5.this);

      if(returnVal == JFileChooser.APPROVE_OPTION) {
      	SeapineReportHtmlFile = JFileChooser4SeapineExportTxtFile.getSelectedFile();
        jlblFileChooser4SeapineExportTxtFile.setText(SeapineReportHtmlFile.getName());
        jbAnalyze.setEnabled(true);
        AnalyzeLabel.setEnabled(true);
      }
          //} else if(event.getSource() == jbAnalyze) {
      try {
        sstp = new SeapineStatTool5Parser(this);
        outputf = new FileOutputStream(defvardiff);
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
    // SeapineStatTool5 build  = new SeapineStatTool5();
    new SeapineStatTool5();
  }



  /**
   *  The main program for the SeapineStatTool5 class
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

