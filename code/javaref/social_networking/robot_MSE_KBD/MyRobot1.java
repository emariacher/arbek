import java.awt.Robot;
import java.awt.event.*;
import java.awt.*;

/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    January 15, 2007
 */
public class MyRobot1 {

	/**
	 *  The main program for the MyRobot1 class
	 *
	 *@param  args           The command line arguments
	 *@exception  Exception  Description of the Exception
	 */
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 1000; i++) {
			dozestuff(15000, 2000);
		}
	}


	/**
	 *  Constructor for the dozestuff object
	 *
	 *@param  delaymse  Description of the Parameter
	 *@param  delaykbd  Description of the Parameter
	 */
	static void dozestuff(int delaymse, int delaykbd) {
		try {
			Robot robot = new Robot();
			Thread.sleep(delaymse);

			// Simulate a mouse click
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);

			Thread.sleep(delaykbd);

			// Simulate a key press
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
		}

	}
}


