//import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 10, 2004
 */
public class ChildList {
  Vector<XmlTag> childs     = new Vector<XmlTag>();
  boolean explored;
  Log L             = new Log();



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
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      if(xt.find(tagName, attributeName, attributeValue) != null) {
        return xt.find(tagName, attributeName, attributeValue);
      }
    }
    return null;
  }


  /**
   *  Description of the Method
   *
   *@param  tagName   Description of the Parameter
   *@param  tagValue  Description of the Parameter
   *@return           Description of the Return Value
   */
  XmlTag find(String tagName, String tagValue) {
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      if(xt.find(tagName, tagValue) != null) {
        return xt.find(tagName, tagValue);
      }
    }
    return null;
  }


  /**
   *  Description of the Method
   *
   *@param  xt_test  Description of the Parameter
   *@return          Description of the Return Value
   */
  XmlTag find(XmlTag xt_test) {
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      if(xt.find(xt_test) != null) {
        return xt.find(xt_test);
      }
    }
    return null;
  }


  /**
   *  Description of the Method
   *
   *@param  zeTagName       Description of the Parameter
   *@param  zeTestTagName   Description of the Parameter
   *@param  zeTestTagValue  Description of the Parameter
   *@return                 Description of the Return Value
   */
  XmlTag find2(String zeTagName, String zeTestTagName, String zeTestTagValue) {
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      if(xt.find2(zeTagName, zeTestTagName, zeTestTagValue) != null) {
        return xt.find2(zeTagName, zeTestTagName, zeTestTagValue);
      }
    }
    return null;
  }


  /**
   *  Description of the Method
   *
   *@param  tagName  Description of the Parameter
   *@return          Description of the Return Value
   */
  XmlTag find(String tagName) {
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      if(xt.find(tagName) != null) {
        return xt.find(tagName);
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
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      xt.findAll(zeTagName, found);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  zeTagName       Description of the Parameter
   *@param  zeTestTagName   Description of the Parameter
   *@param  zeTestTagValue  Description of the Parameter
   *@param  found           Description of the Parameter
   */
  void find2All(String zeTagName, String zeTestTagName, String zeTestTagValue, ChildList found) {
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      xt.find2All(zeTagName, zeTestTagName, zeTestTagValue, found);
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    L.dbg(false, " ChildList.toString()", childs.size());
    StringBuffer sb  = new StringBuffer();
    for(int i = 0; i < childs.size(); i++) {
      XmlTag xt  = (XmlTag)childs.elementAt(i);
      sb.append(xt.toString());
    }
    return sb.toString();
  }

}

