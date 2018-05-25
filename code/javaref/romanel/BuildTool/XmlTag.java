import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 8, 2004
 */
public class XmlTag extends Log {
  Vector a           = new Vector();
  String tagName;
  String noTag       = null;
  ChildList cl       = new ChildList();
  XmlTag parent;
  boolean foundflag  = false;


  /**
   *  Constructor for the XmlTag object
   *
   *@param  parent  Description of the Parameter
   */
  XmlTag(XmlTag parent) {
    this.parent = parent;
  }


  /**  Constructor for the XmlTag object */
  XmlTag() { }


  /**
   *  Gets the attributeValue attribute of the XmlTag object
   *
   *@param  s  Description of the Parameter
   *@return    The attributeValue value
   */
  String getAttributeValue(String s) {
    if (a.size() > 0) {
      for (int i = 0; i < a.size(); i++) {
        String value  = ((XmlAttribute) a.elementAt(i)).getValue(s);
        if (value != null) {
          return new String(value);
        }
      }
      return new String("");
    } else {
      return new String("");
    }
  }


  /**
   *  Sets the attributeValue attribute of the XmlTag object
   *
   *@param  s      The new attributeValue value
   *@param  v      The new attributeValue value
   *@param  force  The new attributeValue value
   *@return        Description of the Return Value
   */
  boolean setAttributeValue(String s, String v, boolean force) {
    if (a.size() > 0) {
      for (int i = 0; i < a.size(); i++) {
        XmlAttribute xa  = ((XmlAttribute) a.elementAt(i));
        String value     = xa.getValue(s);
        if (value != null) {
          xa.setValue(new String(v));
          return true;
        }
      }
    }
    if (force) {
      XmlAttribute xa  = new XmlAttribute(new String(s), new String(v));
      addAttribute(xa);
    }
    return false;
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   *@return    Description of the Return Value
   */
  boolean removeAttribute(String s) {
    if (a.size() > 0) {
      for (int i = 0; i < a.size(); i++) {
        XmlAttribute xa  = ((XmlAttribute) a.elementAt(i));
        String value     = xa.getValue(s);
        if (value != null) {
          a.removeElementAt(i);
          return true;
        }
      }
    }
    return false;
  }


  /**
   *  Adds a feature to the Cdata attribute of the XmlTag object
   *
   *@param  s  The feature to be added to the Cdata attribute
   */
  void addCdata(String s) { }


  /**
   *  Adds a feature to the DocType attribute of the XmlTag object
   *
   *@param  s  The feature to be added to the DocType attribute
   */
  void addDocType(String s) { }


  /**
   *  Adds a feature to the XmlCom attribute of the XmlTag object
   *
   *@param  s  The feature to be added to the XmlCom attribute
   */
  void addXmlCom(String s) { }


  /**
   *  Adds a feature to the Notag attribute of the XmlTag object
   *
   *@param  s  The feature to be added to the Notag attribute
   */
  void addNoTag(String s) {
    if (noTag == null) {
      noTag = new String(s);
      dbg(false, noTag);
    } else {
      noTag = new String(noTag + "\n" + s);
      dbg(false, noTag);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void name(String s) {
    tagName = new String(s);
  }


  /**
   *  Adds a feature to the Child attribute of the XmlTag object
   *
   *@param  xt  The feature to be added to the Child attribute
   */
  void addChild(XmlTag xt) {
    dbg(false, "[" + xt.toString() + "]\n");
    cl.add(xt);
  }


  /**
   *  Description of the Method
   *
   *@param  attributeName   Description of the Parameter
   *@param  attributeValue  Description of the Parameter
   *@return                 Description of the Return Value
   */
  boolean match(String attributeName, String attributeValue) {
    if (attributeValue.equals(getAttributeValue(attributeName))) {
      return true;
    } else {
      return false;
    }
  }



  /**
   *  Adds a feature to the Attribute attribute of the XmlTag object
   *
   *@param  attr  The feature to be added to the Attribute attribute
   */
  void addAttribute(XmlAttribute attr) {
    int i  = a.indexOf(attr);
    if (i < 0) {
      a.add(attr);
    } else {
      XmlAttribute xa  = (XmlAttribute) a.elementAt(i);
      xa.value = new String(attr.getValue());
    }
  }


  /**
   *  Adds a feature to the Attribute2 attribute of the XmlTag object
   *
   *@param  attr  The feature to be added to the Attribute2 attribute
   */
  void addAttribute2(XmlAttribute attr) {
    a.add(attr);
  }


  /**
   *  Description of the Method
   *
   *@param  zeTagName       Description of the Parameter
   *@param  attributeName   Description of the Parameter
   *@param  attributeValue  Description of the Parameter
   *@return                 Description of the Return Value
   */
  XmlTag find(String zeTagName, String attributeName, String attributeValue) {
    if (is(zeTagName) && (match(attributeName, attributeValue))) {
      return this;
    }
    if (cl.find(zeTagName, attributeName, attributeValue) != null) {
      return cl.find(zeTagName, attributeName, attributeValue);
    }
    return null;
  }


  /**
   *  Description of the Method
   *
   *@param  zeTagName  Description of the Parameter
   *@return            Description of the Return Value
   */
  boolean is(String zeTagName) {
    if (zeTagName.equals(tagName)) {
      return true;
    } else {
      return false;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  zeTagName  Description of the Parameter
   *@param  found      Description of the Parameter
   */
  void findAll(String zeTagName, ChildList found) {
    if (zeTagName.equals(tagName)) {
      if (found == null) { //reset foundflag
        foundflag = false;
      } else if (!foundflag) {
        found.add(this);
        foundflag = true;
      }
    }
    cl.findAll(zeTagName, found);
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String endTag() {
    return new String("</" + tagName + ">");
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String beginTag() {
    StringBuffer sb  = new StringBuffer("<" + tagName);
    for (int i = 0; i < a.size(); i++) {
      sb.append(" " + ((XmlAttribute) a.elementAt(i)).toString());
    }
    sb.append(">");
    return sb.toString();
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    StringBuffer sb  = new StringBuffer();
    if (tagName != null) {
      sb.append(beginTag());
    }
    if (noTag != null) {
      sb.append(noTag);
      dbg(true, "[" + noTag + "]");
    }
    if (cl.size() > 0) {
      sb.append(cl.toString());
    }
    if (tagName != null) {
      sb.append(endTag() + "\n");
    }
    return sb.toString();
  }


  /**
   *  Gets the root attribute of the XmlTag object
   *
   *@param  zeTagName  Description of the Parameter
   *@return            The root value
   */
  XmlTag getParent(String zeTagName) {
    XmlTag xt  = this;
    while ((!xt.is(zeTagName)) && (xt.parent != null)) {
      xt = xt.parent;
    }
    if (xt.parent == null) {
      return null;
    } else {
      return xt;
    }
  }


  /**
   *  Gets the root attribute of the XmlTag object
   *
   *@return    The root value
   */
  XmlTag getRoot() {
    XmlTag xt  = this;
    while (xt.parent != null) {
      xt = xt.parent;
    }
    return xt;
  }
}

