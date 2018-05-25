import java.io.*;
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 17, 2005
 */
public class Mainline {
  String name;


  /**
   *  Constructor for the Mainline object
   *
   *@param  name  Description of the Parameter
   */
  Mainline(String name) {
    this.name = new String(name);
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    return new String("Mainline: " + name);
  }
}


