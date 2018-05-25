
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 15, 2005
 */
public class CodeQualTool extends JPanel implements ActionListener, DefVar {
  JFrame buildFrame;
  JPanel buildPanel;
  JTextField jtfDirectory;
  JLabel jlblDirectory, jlblAnalyze;
  JButton jbAnalyze;
  JTextArea log, err;
  File defvardiff;
  BufferedReader inputf;
  FileOutputStream outputf;
  BuildDefineTable x;


  /**  Constructor for the CodeQualTool object */
  public CodeQualTool() {
    super(new BorderLayout());
          //Create and set up the window.
    buildFrame = new JFrame("Eric Mariacher");
    buildFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    buildFrame.setSize(new Dimension(500, 500));

          //Create and set up the panel.
    buildPanel = new JPanel(new GridLayout(0,2));
          //Create the log first, because the action listeners
          //need to refer to it.
    log = new JTextArea(20, 60);
    log.setMargin(new Insets(5, 5, 5, 5));
    log.setEditable(false);
    JScrollPane logScrollPane  = new JScrollPane(log);
    err = new JTextArea(10, 60);
    err.setMargin(new Insets(5, 5, 5, 5));
    err.setEditable(false);
    JScrollPane errScrollPane  = new JScrollPane(err);
    err.setForeground(Color.red);

          //Add the widgets.
    addWidgets();

          //Set the default button.
    buildFrame.getRootPane().setDefaultButton(jbAnalyze);

          //Add the panel to the window.
    buildFrame.getContentPane().add(buildPanel, BorderLayout.NORTH);
    buildFrame.getContentPane().add(errScrollPane, BorderLayout.CENTER);
    buildFrame.getContentPane().add(logScrollPane, BorderLayout.SOUTH);

          //Display the window.
    buildFrame.pack();
    buildFrame.setVisible(true);
  }


  /**  Create and add the widgets. */
  private void addWidgets() {
    jtfDirectory = new JTextField(16);
    jlblDirectory = new JLabel("Directory", SwingConstants.LEFT);

    jbAnalyze = new JButton("Analyze!");
    jbAnalyze.addActionListener(this);
    jlblAnalyze = new JLabel("not analyzed yet", SwingConstants.LEFT);

          //Add the widgets to the container.
    buildPanel.add(jtfDirectory);
    buildPanel.add(jlblDirectory);
    buildPanel.add(jbAnalyze);
    buildPanel.add(jlblAnalyze);

    jlblDirectory.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    jlblAnalyze.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
          //read default variables file
    try {
      defvardiff = new File("CodeQualTool");
      if (defvardiff.canRead()) {
        inputf = new BufferedReader(new FileReader(defvardiff));
        String linef;
        int cpt       = 0;
        while ((linef = inputf.readLine()) != null) {
          switch (cpt) {
            case 0:
              jtfDirectory.setText(linef);
              break;
          }
          cpt++;
        }
        inputf.close();
      }
    } catch (FileNotFoundException e) {
      myPrintln("no File with default values found! expected at[" +
          defvardiff.getName() + "] continuing...");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e);
      System.out.println(e);
      myPrintln(e.toString());
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
        log.setText("");
        x = new BuildDefineTable(this);
        outputf = new FileOutputStream(defvardiff);
        outputf.write((new String(jtfDirectory.getText() + "\n")).getBytes());
        outputf.close();
        jlblAnalyze.setText("Analyzing!");
      } catch (Exception e) {
        err.append(e.toString());
      }
    } else {
      jlblAnalyze.setText("???!!!!");
      log.append(event.toString() + " ???!!!!");
    }
  }


  /**
   *  Create the GUI and show it. For thread safety, this method should be
   *  invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
          //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);
    CodeQualTool build  = new CodeQualTool();
  }


  /**
   *  The main program for the CodeQualTool class
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


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void myPrint(String s) {
    if (log != null) {
      log.append(s);
    }
    System.out.print(s);
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void myPrintln(String s) {
    myPrint(s + "\n");
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myErrPrint(String s) throws Exception {
    if (err != null) {
      err.append(s);
      x.f.writeFile(x.errfile, s);
      x.f.writeFile(x.logfile, s);
    }
    System.out.print(s);
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myErrPrintln(String s) throws Exception {
    myErrPrint(s + "\n");
  }

}

