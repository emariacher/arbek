import java.io.File;
import javax.swing.ImageIcon;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    May 4, 2005
 */
public class Utils {
  /**  Description of the Field */
  public final static String xml  = "xml";
  /**  Description of the Field */
  public final static String XML  = "XML";
  /**  Description of the Field */
  public final static String Xml  = "Xml";
  /**  Description of the Field */
  public final static String txt  = "txt";
  /**  Description of the Field */
  public final static String html  = "html";


  /**
   *  Gets the extension attribute of the Utils class
   *
   *@param  f  Description of the Parameter
   *@return    The extension value
   */
  public static String getExtension(File f) {
    String ext  = null;
    String s    = f.getName();
    int i       = s.lastIndexOf('.');

    if(i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
  }


  /**
   *  Returns an ImageIcon, or null if the path was invalid.
   *
   *@param  path  Description of the Parameter
   *@return       Description of the Return Value
   */
  protected static ImageIcon createImageIcon(String path) {
    java.net.URL imgURL  = Utils.class.getResource(path);
    if(imgURL != null) {
      return new ImageIcon(imgURL);
    } else {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
  }
}

