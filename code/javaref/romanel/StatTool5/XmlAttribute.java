
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 8, 2004
 */
public class XmlAttribute {
  String name   = null;
  String value  = null;


  /**  Constructor for the XmlAttribute object */
  XmlAttribute() { }


  /**
   *  Constructor for the XmlAttribute object
   *
   *@param  name   Description of the Parameter
   *@param  value  Description of the Parameter
   */
  XmlAttribute(String name, String value) {
    this.name = new String(name);
    this.value = new String(value);
  }


  /**
   *  Description of the Method
   *
   *@param  obj  Description of the Parameter
   *@return      Description of the Return Value
   */
  public boolean equals(Object obj) {
    XmlAttribute xa  = (XmlAttribute)obj;
    return (name.equals(xa.name) && value.equals(xa.value));
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   *@return    Description of the Return Value
   */
  String unquote(String s) {
    return s.replaceAll("\"", "");
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    return new String(name + "=\"" + value + "\"");
  }


  /**
   *  Gets the value attribute of the XmlAttribute object
   *
   *@param  s  Description of the Parameter
   *@return    The value value
   */
  String getValue(String s) {
    if(s.equals(name)) {
      return new String(value);
    } else {
      return null;
    }
  }


  /**
   *  Sets the value attribute of the XmlAttribute object
   *
   *@param  s  The new value value
   */
  void setValue(String s) {
    value = new String(s);
  }


  /**
   *  Gets the value attribute of the XmlAttribute object
   *
   *@return    The value value
   */
  String getValue() {
    return new String(value);
  }
}

