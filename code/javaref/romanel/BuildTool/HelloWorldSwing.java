/*
 * HelloWorldSwing.java is a 1.4 example that
 * requires no other files.
 */
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    February 14, 2005
 */
public class HelloWorldSwing {
  /**
   *  Create the GUI and show it. For thread safety, this method should be
   *  invoked from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
          //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);

          //Create and set up the window.
    JFrame frame  = new JFrame("HelloWorldSwing");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

          //Add the ubiquitous "Hello World" label.
    JLabel label  = new JLabel("Hello World");
    frame.getContentPane().add(label);

          //Display the window.
    frame.pack();
    frame.setVisible(true);
  }


  /**
   *  The main program for the HelloWorldSwing class
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

