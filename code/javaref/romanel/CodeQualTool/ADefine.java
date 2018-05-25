
import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2005
 */
class ADefine implements Comparable {
  String define;
  java.util.List f  = new ArrayList();


  /**
   *  Constructor for the ADefine object
   *
   *@param  define  Description of the Parameter
   */
  ADefine(String define) {
    this.define = new String(define);
  }


  /**
   *  Description of the Method
   *
   *@param  other  Description of the Parameter
   *@return        Description of the Return Value
   */
  public int compareTo(Object other) {
    ADefine ad  = (ADefine) other;
    if (ad.f.size() > f.size()) {
      return 1;
    } else if (ad.f.size() == f.size()) {
      return 0;
    } else {
      return -1;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  obj  Description of the Parameter
   *@return      Description of the Return Value
   */
  public boolean equals(Object obj) {
    ADefine d  = (ADefine) obj;
    return d.define.equals(define);
  }


  /**
   *  Adds a feature to the File attribute of the ADefine object
   *
   *@param  file  The feature to be added to the File attribute
   */
  void addFile(AFile file) {
    if (!f.contains(file)) {
      f.add(file);
    }
  }


  /**
   *  Description of the Method
   *
   *@return           Description of the Return Value
   */
  public String toString() {
    StringBuffer sb  = new StringBuffer("[[" + define + "][" + f.size() + "] ");
    for (ListIterator li = f.listIterator(); li.hasNext(); ) {
      AFile file  = (AFile) li.next();
      sb.append(file.name + ", ");
    }
    sb.append("]");
    return sb.toString();
  }
}

