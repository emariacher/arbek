import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 10, 2004
 */
public class ChildList extends Log {
  Vector childs     = new Vector();
  boolean explored;


  /**  Constructor for the ChildList object */
  ChildList() { }


  /**
   *  Description of the Method
   *
   *@param  xt  Description of the Parameter
   *@return     Description of the Return Value
   */
  boolean add(XmlTag xt) {
    return childs.add(xt);
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  int size() {
    return childs.size();
  }


  /**
   *  Description of the Method
   *
   *@param  index  Description of the Parameter
   *@return        Description of the Return Value
   */
  Object elementAt(int index) {
    return childs.elementAt(index);
  }


  /**
   *  Description of the Method
   *
   *@param  index  Description of the Parameter
   */
  void removeElementAt(int index) {
    childs.removeElementAt(index);
  }


  /**
   *  Description of the Method
   *
   *@param  tagName         Description of the Parameter
   *@param  attributeName   Description of the Parameter
   *@param  attributeValue  Description of the Parameter
   *@return                 Description of the Return Value
   */
  XmlTag find(String tagName, String attributeName, String attributeValue) {
    for (int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag) childs.elementAt(i);
      if (xt.find(tagName, attributeName, attributeValue) != null) {
        return xt.find(tagName, attributeName, attributeValue);
      }
    }
    return null;
  }


  /**
   *  Description of the Method
   *
   *@param  zeTagName  Description of the Parameter
   *@param  found      Description of the Parameter
   */
  void findAll(String zeTagName, ChildList found) {
    for (int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag) childs.elementAt(i);
      xt.findAll(zeTagName, found);
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    dbg(false, " ChildList.toString()", childs.size());
    StringBuffer sb  = new StringBuffer();
    for (int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag) childs.elementAt(i);
      sb.append(xt.toString());
    }
    return sb.toString();
  }

}

