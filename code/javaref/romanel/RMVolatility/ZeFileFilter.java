import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 19, 2006
 */
public class ZeFileFilter extends FileFilter {

          //Accept all directories and all gif, jpg, tiff, or png files.
  /**
   *  Description of the Method
   *
   *@param  f  Description of the Parameter
   *@return    Description of the Return Value
   */
  public boolean accept(File f) {
    if(f.isDirectory()) {
      return true;
    }

    String extension  = Utils.getExtension(f);
    if(extension != null) {
      if(extension.equals(Utils.xml)) {
        return true;
      } else {
        return false;
      }
    }

    return false;
  }

          //The description of this filter
  /**
   *  Gets the description attribute of the FileFilter object
   *
   *@return    The description value
   */
  public String getDescription() {
    return "Just xml files";
  }
}

