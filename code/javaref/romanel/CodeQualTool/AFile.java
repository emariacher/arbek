
import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2005
 */
class AFile implements Comparable {
  String name;
  File f;
  java.util.List d  = new ArrayList();


  /**
   *  Constructor for the ADefine object
   *
   *@param  name  Description of the Parameter
   */
  AFile(String name) {
    f = new File(name);
    this.name = new String(name.substring(name.lastIndexOf("\\") + 1));
  }


  /**
   *  Constructor for the AFile object
   *
   *@param  f  Description of the Parameter
   */
  AFile(File f) {
    this.name = new String(f.toString().substring(f.toString().lastIndexOf("\\") + 1));
    this.f = f;
  }


  /**
   *  Description of the Method
   *
   *@param  other  Description of the Parameter
   *@return        Description of the Return Value
   */
  public int compareTo(Object other) {
    AFile af  = (AFile) other;
    return af.name.compareTo(name);
  }


  /**
   *  Description of the Method
   *
   *@param  obj  Description of the Parameter
   *@return      Description of the Return Value
   */
  public boolean equals(Object obj) {
    AFile af  = (AFile) obj;
    return af.name.equals(name);
  }


  /**
   *  Adds a feature to the File attribute of the ADefine object
   *
   *@param  ad  The feature to be added to the File attribute
   */
  void addDefine(ADefine ad) {
    if (!d.contains(ad)) {
      d.add(ad);
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    StringBuffer sb  = new StringBuffer("[[" + name + "][" + d.size() + "] ");
    for (ListIterator li = d.listIterator(); li.hasNext(); ) {
      ADefine ad  = (ADefine) li.next();
      sb.append(ad.define + ", ");
    }
    sb.append("]");
    return sb.toString();
  }
}

