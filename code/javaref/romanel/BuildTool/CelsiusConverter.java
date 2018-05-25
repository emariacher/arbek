
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    February 14, 2005
 */
public class CelsiusConverter implements ActionListener {
  JFrame converterFrame;
  JPanel converterPanel;
  JTextField tempCelsius;
  JLabel celsiusLabel, fahrenheitLabel, pwdLabel;
  JButton convertTemp;
  JPasswordField pwd;


  /**  Constructor for the CelsiusConverter object */
  public CelsiusConverter() {
          //Create and set up the window.
    converterFrame = new JFrame("Convert Celsius to Fahrenheit");
    converterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    converterFrame.setSize(new Dimension(120, 40));

          //Create and set up the panel.
    converterPanel = new JPanel(new GridLayout(3, 2));

          //Add the widgets.
    addWidgets();

          //Set the default button.
    converterFrame.getRootPane().setDefaultButton(convertTemp);

          //Add the panel to the window.
    converterFrame.getContentPane().add(converterPanel, BorderLayout.CENTER);

          //Display the window.
    converterFrame.pack();
    converterFrame.setVisible(true);
  }


  /**  Create and add the widgets.  */
  private void addWidgets() {
          //Create widgets.
    tempCelsius = new JTextField(2);
    celsiusLabel = new JLabel("Celsius", SwingConstants.LEFT);
    pwd = new JPasswordField(15);
    pwd.setEchoChar('#');

    pwdLabel = new JLabel("Seapine Password", SwingConstants.LEFT);
    convertTemp = new JButton("Convert");
    fahrenheitLabel = new JLabel("Fahrenheit", SwingConstants.LEFT);

          //Listen to events from the Convert button.
    convertTemp.addActionListener(this);

          //Add the widgets to the container.
    converterPanel.add(tempCelsius);
    converterPanel.add(celsiusLabel);
    converterPanel.add(pwd);
    converterPanel.add(pwdLabel);
    converterPanel.add(convertTemp);
    converterPanel.add(fahrenheitLabel);

    celsiusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    fahrenheitLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }


  /**
   *  Description of the Method
   *
   *@param  event  Description of the Parameter
   */
  public void actionPerformed(ActionEvent event) {
          //Parse degrees Celsius as a double and convert to Fahrenheit.
    int tempFahr  = (int) ((Double.parseDouble(tempCelsius.getText()))
         * 1.8 + 32);
    fahrenheitLabel.setText(tempFahr + " Fahrenheit");
  }


  /**
   *  Create the GUI and show it. For thread safety, this method should be
   *  invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
          //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);

    CelsiusConverter converter  = new CelsiusConverter();
  }


  /**
   *  The main program for the CelsiusConverter class
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

