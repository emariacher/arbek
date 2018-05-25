
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    February 14, 2005
 */
public class SeapineBuildReleaseTool extends JPanel implements ActionListener, DefVar {
  JFrame buildFrame;
  JPanel buildPanel;
  JTextField SCMServerIPAddress, SCMServerIPPort, mainline, branch, TTProDBName,
      username, zeSeapineLbl_;
  JLabel SCMServerIPAddressLabel
      , SCMServerIPPortLabel, mainlineLabel, branchLabel,
      TTProDBNameLabel, usernameLabel, pwdLabel, jlblFileChooser4SeapineExportXmlFile,
      jlblFileChooser4BaseReleaseNote, BuildLabel, AnalyzeLabel, zeSeapineLbl_Label,
      toolTitleLabel1, toolTitleLabel2;
  JButton jbBuild, jbOpenSeapineExportXmlFile, jbOpenBaseReleaseNote, jbAnalyze;
  JPasswordField pwd;
  JFileChooser JFileChooser4SeapineExportXmlFile, JFileChooser4BaseReleaseNote;
  JTextArea err;
  JTextPane html;
  File defvardiff;
  BufferedReader inputf;
  FileOutputStream outputf;
  File file                                       = null;
  File SeapineExportXmlFile                       = null;
  File BaseReleaseNote                            = null;
  boolean goBuild                                 = false;
  String server;
  BuildRelease br;


  /**  Constructor for the SeapineBuildReleaseTool object */
  public SeapineBuildReleaseTool() {
    super(new BorderLayout());
          //Create and set up the window.
    buildFrame = new JFrame("Build File Release Labeled and based on Changes");
    buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    buildFrame.setSize(new Dimension(200, 300));

          //Create and set up the panel.
    buildPanel = new JPanel(new GridLayout(13, 2));
          //Create the log first, because the action listeners
          //need to refer to it.
    /*log = new JTextArea(5, 60);
    log.setMargin(new Insets(1, 1, 1, 1));
    log.setEditable(false);
    JScrollPane logScrollPane   = new JScrollPane(log);*/
    err = new JTextArea(10, 60);
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

          //Set the default button.
    buildFrame.getRootPane().setDefaultButton(jbBuild);

          //Add the panel to the window.
    GridBagLayout gridbag       = new GridBagLayout();
    GridBagConstraints c        = new GridBagConstraints();
    buildFrame.getContentPane().setLayout(gridbag);
    c.fill = GridBagConstraints.BOTH;
    c.gridheight = 2;
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



  /**  Description of the Method */
  void enableBuildButton() {
    jbBuild.setEnabled(true);
    BuildLabel.setEnabled(true);
  }


  /**  Create and add the widgets. */
  private void addWidgets() {
    Font bigFont                  = new Font(null, Font.BOLD, 16);
          //Create widgets.
    String toolTitleLabel2Txt;
    String zeSeapineLbl_LabelTxt;
    String toolTitleLabel1Txt;
    String BuildButtonTxt;
    switch (getBuildMode()) {
      case BUILDMODE_BUGBASED:
        zeSeapineLbl_LabelTxt = new String("Compare with Label (optional)");
        toolTitleLabel1Txt = new String("Build Release Note and ");
        toolTitleLabel2Txt = new String("label files based on Bugs");
        BuildButtonTxt = new String("Build and Label based on BUGS");
        break;
      case BUILDMODE_LABELBASED:
        zeSeapineLbl_LabelTxt = new String("Base Label");
        toolTitleLabel1Txt = new String("Build Release Note ");
        toolTitleLabel2Txt = new String("based on Labels");
        BuildButtonTxt = new String("Build based on LABELS");
        break;
      default:
        zeSeapineLbl_LabelTxt = new String("Invalid build Mode! Call Eric Mariacher");
        toolTitleLabel1Txt = new String("Invalid build Mode! ");
        toolTitleLabel2Txt = new String("Call Eric Mariacher");
        BuildButtonTxt = new String("Invalid build Mode! Call Eric Mariacher");
        break;
    }
    toolTitleLabel1 = new JLabel(toolTitleLabel1Txt, SwingConstants.LEFT);
    toolTitleLabel1.setFont(bigFont);
    toolTitleLabel2 = new JLabel(toolTitleLabel2Txt, SwingConstants.LEFT);
    toolTitleLabel2.setFont(bigFont);
    SCMServerIPAddress = new JTextField("ch01s20", 16);
    SCMServerIPAddressLabel = new JLabel("SCM Server Address (IP)", SwingConstants.LEFT);
    SCMServerIPPort = new JTextField("4900", 7);
    SCMServerIPPortLabel = new JLabel("SCM Server Port (IP)", SwingConstants.LEFT);
    mainline = new JTextField(20);
    mainlineLabel = new JLabel("mainline", SwingConstants.LEFT);
    branch = new JTextField(20);
    branchLabel = new JLabel("branch", SwingConstants.LEFT);
    TTProDBName = new JTextField("CDBU_CH1", 10);
    TTProDBNameLabel = new JLabel("TestTrack Pro Configuration name", SwingConstants.LEFT);
    username = new JTextField(16);
    usernameLabel = new JLabel("username", SwingConstants.LEFT);
    pwd = new JPasswordField(15);
    pwd.setEchoChar('#');
    pwdLabel = new JLabel("Password", SwingConstants.LEFT);
    zeSeapineLbl_ = new JTextField(10);
    zeSeapineLbl_Label = new JLabel(zeSeapineLbl_LabelTxt, SwingConstants.LEFT);

          //Create 2 file choosers
    JFileChooser4SeapineExportXmlFile = new JFileChooser();
    XmlFileFilter filter          = new XmlFileFilter();
    JFileChooser4SeapineExportXmlFile.setFileFilter(filter);
    jbOpenSeapineExportXmlFile = new JButton("Seapine Export File");
    jbOpenSeapineExportXmlFile.addActionListener(this);
    jlblFileChooser4SeapineExportXmlFile = new JLabel("no Seapine Export File Selected yet", SwingConstants.LEFT);

    JFileChooser4BaseReleaseNote = new JFileChooser();
    JFileChooser4BaseReleaseNote.setFileFilter(filter);
    jbOpenBaseReleaseNote = new JButton("Base Release Note");
    jbOpenBaseReleaseNote.addActionListener(this);
    jlblFileChooser4BaseReleaseNote = new JLabel("no Base Release Note Selected", SwingConstants.LEFT);

    jbAnalyze = new JButton("Analyze!");
    jbAnalyze.addActionListener(this);
    AnalyzeLabel = new JLabel("not analyzed yet", SwingConstants.LEFT);

    jbBuild = new JButton(BuildButtonTxt);
    jbBuild.addActionListener(this);
    BuildLabel = new JLabel("not built yet", SwingConstants.LEFT);

          //Listen to events from the Convert button.
    jbBuild.addActionListener(this);

          //Add the widgets to the container.
    buildPanel.add(toolTitleLabel1);
    buildPanel.add(toolTitleLabel2);
    buildPanel.add(SCMServerIPAddress);
    buildPanel.add(SCMServerIPAddressLabel);
    buildPanel.add(SCMServerIPPort);
    buildPanel.add(SCMServerIPPortLabel);
    buildPanel.add(mainline);
    buildPanel.add(mainlineLabel);
    buildPanel.add(branch);
    buildPanel.add(branchLabel);
    buildPanel.add(TTProDBName);
    buildPanel.add(TTProDBNameLabel);
    buildPanel.add(username);
    buildPanel.add(usernameLabel);
    buildPanel.add(pwd);
    buildPanel.add(pwdLabel);
    buildPanel.add(zeSeapineLbl_);
    buildPanel.add(zeSeapineLbl_Label);
    buildPanel.add(jbOpenSeapineExportXmlFile);
    buildPanel.add(jlblFileChooser4SeapineExportXmlFile);
    buildPanel.add(jbOpenBaseReleaseNote);
    buildPanel.add(jlblFileChooser4BaseReleaseNote);
    buildPanel.add(jbAnalyze);
    buildPanel.add(AnalyzeLabel);
    buildPanel.add(jbBuild);
    buildPanel.add(BuildLabel);
    jbAnalyze.setEnabled(false);
    AnalyzeLabel.setEnabled(false);
    jbBuild.setEnabled(false);
    BuildLabel.setEnabled(false);

    toolTitleLabel2.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    SCMServerIPAddressLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    SCMServerIPPortLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    mainlineLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    branchLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    TTProDBNameLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    usernameLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    pwdLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    jlblFileChooser4SeapineExportXmlFile.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    jlblFileChooser4BaseReleaseNote.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    AnalyzeLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    BuildLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
          //read default variables file
    try {
      defvardiff = new File("SeapineBuildReleaseTool");
      if (defvardiff.canRead()) {
        inputf = new BufferedReader(new FileReader(defvardiff));
        String linef;
        int cpt       = 0;
        while ((linef = inputf.readLine()) != null) {
          switch (cpt) {
            case 0:
              mainline.setText(linef);
              break;
            case 1:
              branch.setText(linef);
              break;
            case 2:
              TTProDBName.setText(linef);
              break;
            case 3:
              username.setText(linef);
              break;
            case 4:
              zeSeapineLbl_.setText(linef);
              break;
            case 5:
              JFileChooser4SeapineExportXmlFile.setCurrentDirectory(new File(linef));
              break;
            case 6:
              JFileChooser4BaseReleaseNote.setCurrentDirectory(new File(linef));
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
    if (event.getSource() == jbOpenSeapineExportXmlFile) {
      int returnVal  = JFileChooser4SeapineExportXmlFile.showOpenDialog(SeapineBuildReleaseTool.this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        SeapineExportXmlFile = JFileChooser4SeapineExportXmlFile.getSelectedFile();
        jlblFileChooser4SeapineExportXmlFile.setText(SeapineExportXmlFile.getName());
        jbAnalyze.setEnabled(true);
        AnalyzeLabel.setEnabled(true);
      }
    } else if (event.getSource() == jbOpenBaseReleaseNote) {
      int returnVal  = JFileChooser4BaseReleaseNote.showOpenDialog(SeapineBuildReleaseTool.this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        BaseReleaseNote = JFileChooser4BaseReleaseNote.getSelectedFile();
        jlblFileChooser4BaseReleaseNote.setText(BaseReleaseNote.getName());
      }
    } else if (event.getSource() == jbAnalyze) {
      try {
        server = new String(SCMServerIPAddress.getText() + ":" +
            SCMServerIPPort.getText());
        if (BaseReleaseNote != null) {
          file = new File(SeapineExportXmlFile.getCanonicalPath() + "_merged");
          int b;
          FileInputStream fis   = new FileInputStream(SeapineExportXmlFile);
          FileOutputStream fos  = new FileOutputStream(file);
          while ((b = fis.read()) != -1) {
            fos.write(b);
          }
          fis.close();
          fis = new FileInputStream(BaseReleaseNote);
          while ((b = fis.read()) != -1) {
            fos.write(b);
          }
          fis.close();
          fos.close();
        } else {
          file = SeapineExportXmlFile;
        }
        br = new BuildRelease(this);
        outputf = new FileOutputStream(defvardiff);
        outputf.write((new String(mainline.getText() + "\n")).getBytes());
        outputf.write((new String(branch.getText() + "\n")).getBytes());
        outputf.write((new String(TTProDBName.getText() + "\n")).getBytes());
        outputf.write((new String(username.getText() + "\n")).getBytes());
        outputf.write((new String(zeSeapineLbl_.getText() + "\n")).getBytes());
        outputf.write((new String(JFileChooser4SeapineExportXmlFile.getCurrentDirectory().toString() + "\n")).getBytes());
        outputf.write((new String(JFileChooser4BaseReleaseNote.getCurrentDirectory().toString() + "\n")).getBytes());
        outputf.close();
        AnalyzeLabel.setText("Analyzing!");
        jbOpenSeapineExportXmlFile.setEnabled(false);
        jbOpenBaseReleaseNote.setEnabled(false);
        jbAnalyze.setEnabled(false);
      } catch (Exception e) {
        System.err.println(e);
        e.printStackTrace();
      }
    } else if (event.getSource() == jbBuild) {
      BuildLabel.setText("Building!");
      goBuild = true;
      jbBuild.setEnabled(false);
    } else {
      BuildLabel.setText("???!!!!");
    }
  }


  /**
   *  Create the GUI and show it. For thread safety, this method should be
   *  invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
          //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);
    SeapineBuildReleaseTool build  = new SeapineBuildReleaseTool();
  }


  /**
   *  Gets the getBuildMode() attribute of the SeapineBuildReleaseTool object
   *
   *@return    The getBuildMode() value
   */
  int getBuildMode() {
System.out.println("label");
    return BUILDMODE_LABELBASED;
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

