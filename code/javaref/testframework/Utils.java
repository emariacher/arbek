import java.io.File;

import javax.swing.ImageIcon;


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
	  
		public static int findNextValidInteger(String s) throws Exception {

			// detect 1st digit after start of string
			int beginIndex=0;
			int endIndex=1;
			int length=s.length();
			while(!s.substring(beginIndex,endIndex).matches("\\d")) {
				beginIndex++;
				endIndex++;
				if(endIndex>length) {
					throw new Exception("No digit found.");
				}
			}
			int i_startNumber= beginIndex;

			// detect 1st non digit after start previously detected 1st digit		
			beginIndex=i_startNumber;
			endIndex=i_startNumber+1;
			while(s.substring(beginIndex,endIndex).matches("\\d")) {
				beginIndex++;
				endIndex++;
				if(endIndex>length) {
					break;
				}
			}	  
			int i_endNumber= beginIndex;

			// return absolute value of integer
			return Integer.valueOf(s.substring(i_startNumber, i_endNumber));
		}

}
