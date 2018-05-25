
import java.io.*;
import java.io.File;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2005
 */
public class SourceCodeFileFilter implements FilenameFilter {

  /**
   *  Description of the Method
   *
   *@param  dir   Description of the Parameter
   *@param  name  Description of the Parameter
   *@return       Description of the Return Value
   */
  public boolean accept(File dir, String name) {
    if (name.endsWith(".c") || name.endsWith(".h") || name.endsWith(".inc")) {
      return true;
    }
    return false;
  }
}

