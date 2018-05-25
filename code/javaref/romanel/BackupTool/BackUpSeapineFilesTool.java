
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    February 18, 2005
 */
public class BackUpSeapineFilesTool extends JPanel implements ActionListener {
  JFrame converterFrame;
  JPanel converterPanel;
  JTextField SCMServerIPAddress, SCMServerIPPort, backupdir, username;
  JLabel SCMServerIPAddressLabel, SCMServerIPPortLabel, backupdirLabel,
      usernameLabel, pwdLabel, BuildLabel;
  JButton convertTemp, openButton;
  JPasswordField pwd;
  JTextArea log;


  /**  Constructor for the BackUPSeapineFilesTool object */
  public BackUpSeapineFilesTool() {
    super(new BorderLayout());
          //Create and set up the window.
    converterFrame = new JFrame("Build File Release Labeled and based on Changes");
    converterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    converterFrame.setSize(new Dimension(1000, 800));

          //Create and set up the panel.
    converterPanel = new JPanel(new GridLayout(10, 2));
          //Create the log first, because the action listeners
          //need to refer to it.
    log = new JTextArea(20, 80);
    log.setMargin(new Insets(5, 5, 5, 5));
    log.setEditable(false);
    JScrollPane logScrollPane  = new JScrollPane(log);

          //Add the widgets.
    addWidgets();

          //Set the default button.
    converterFrame.getRootPane().setDefaultButton(convertTemp);

          //Add the panel to the window.
    converterFrame.getContentPane().add(converterPanel, BorderLayout.PAGE_START);
    converterFrame.getContentPane().add(logScrollPane, BorderLayout.CENTER);

          //Display the window.
    converterFrame.pack();
    converterFrame.setVisible(true);
  }


  /**  Create and add the widgets. */
  private void addWidgets() {
          //Create widgets.
    SCMServerIPAddress = new JTextField("ch01s20", 16);
    SCMServerIPAddressLabel = new JLabel("SCM Server Address (IP)", SwingConstants.LEFT);
    SCMServerIPPort = new JTextField("4900", 7);
    SCMServerIPPortLabel = new JLabel("SCM Server Port (IP)", SwingConstants.LEFT);
    backupdir = new JTextField("D:\\Temp\\", 20);
    backupdirLabel = new JLabel("backupdir", SwingConstants.LEFT);
    username = new JTextField(16);
    usernameLabel = new JLabel("username", SwingConstants.LEFT);
    pwd = new JPasswordField(15);
    pwd.setEchoChar('#');

    pwdLabel = new JLabel("Password", SwingConstants.LEFT);

    convertTemp = new JButton("BackUp!");
    BuildLabel = new JLabel("no back-up yet", SwingConstants.LEFT);

          //Listen to events from the Convert button.
    convertTemp.addActionListener(this);

          //Add the widgets to the container.
    converterPanel.add(SCMServerIPAddress);
    converterPanel.add(SCMServerIPAddressLabel);
    converterPanel.add(SCMServerIPPort);
    converterPanel.add(SCMServerIPPortLabel);
    converterPanel.add(backupdir);
    converterPanel.add(backupdirLabel);
    converterPanel.add(username);
    converterPanel.add(usernameLabel);
    converterPanel.add(pwd);
    converterPanel.add(pwdLabel);
    converterPanel.add(convertTemp);
    converterPanel.add(BuildLabel);

    SCMServerIPAddressLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    SCMServerIPPortLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    backupdirLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    pwdLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    BuildLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }



  /**
   *  Description of the Method
   *
   *@param  event  Description of the Parameter
   */
  public void actionPerformed(ActionEvent event) {
          //Handle open button action.
    try {
      BackUpAllSeapineFiles bu  = new BackUpAllSeapineFiles(SCMServerIPAddress.getText() + ":" + SCMServerIPPort.getText(),
          username.getText(), pwd.getText(), backupdir.getText(), this);
      BuildLabel.setText("Back-Uping...");
    } catch (Exception e) {
          //System.err.println(e);
      e.printStackTrace();
      System.err.println("Failed!");
      log.append("Failed!");
    }
  }


  /**
   *  Create the GUI and show it. For thread safety, this method should be
   *  invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
          //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);

    BackUpSeapineFilesTool converter  = new BackUpSeapineFilesTool();
  }


  /**
   *  The main program for the BackUPSeapineFilesTool class
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

