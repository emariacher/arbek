import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 8, 2004
 */
public class Descriptor extends XmlTag {
  Vector data;
  Device d;
  Configuration cfg;
  InterfaceCfg icfg;
  EndPoint ep;


  /**
   *  Constructor for the Protocol object
   *
   *@param  d  Description of the Parameter
   */
  Descriptor(Device d) {
    this.d = d;
    tagName = new String("Descriptor");
    childType = TYPE_DEVDESCRIPTOR;
  }


  /**
   *  Constructor for the Descriptor object
   *
   *@param  cfg  Description of the Parameter
   */
  Descriptor(Configuration cfg) {
    this.cfg = cfg;
    tagName = new String("Descriptor");
    childType = TYPE_CFGDESCRIPTOR;
  }


  /**
   *  Constructor for the Descriptor object
   *
   *@param  icfg  Description of the Parameter
   */
  Descriptor(InterfaceCfg icfg) {
    this.icfg = icfg;
    tagName = new String("Descriptor");
    childType = TYPE_ITFDESCRIPTOR;
  }


  /**
   *  Constructor for the Descriptor object
   *
   *@param  ep  Description of the Parameter
   */
  Descriptor(EndPoint ep) {
    this.ep = ep;
    tagName = new String("Descriptor");
    childType = TYPE_EPDESCRIPTOR;
  }


  /**
   *  Adds a feature to the Descriptor attribute of the Protocol object
   *
   *@param  s  The feature to be added to the Data attribute
   */
  void addData(String s) {
          //data = new String(s);
    dbg(toString(), s);
  }


  /**
   *  Adds a feature to the Data attribute of the Descriptor object
   *
   *@param  vd  The feature to be added to the Data attribute
   */
  void addData(Vector vd) {
    data = vd;
    dbg(false, toString(), vd);
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  int getDataFormat() {
    for (int i = 0; i < data.size(); i++) {
      String s  = (String) data.elementAt(i);
      if (s.indexOf("//") > 0) {
        return TABLE_2COLS;
      }
    }
    return TABLE_RAW;
  }


  /**
   *  Description of the Method
   *
   *@param  title      Description of the Parameter
   *@param  tableType  Description of the Parameter
   *@return            Description of the Return Value
   */
  String DescriptorTableHeader(String title, int tableType) {
    StringBuffer sb  = new StringBuffer("<Descriptor><TableTitle>" + title +
        " Descriptor</TableTitle><Table Type=\"" + tableType + "\">");
    return sb.toString();
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String DescriptorTableFooter() {
    return new String("</Table></Descriptor>\n");
  }


  /**
   *  Gets the comment attribute of the XmlAsInput object
   *
   *@param  s  Description of the Parameter
   *@return    The comment value
   */
  String getComment(String s) {
    return s.substring(s.indexOf("//") + 2).trim();
  }


  /**
   *  Gets the value attribute of the XmlAsInput object
   *
   *@param  s  Description of the Parameter
   *@return    The value value
   */
  String getValue(String s) {
    if (s.indexOf(",") > 0) {
      return s.substring(0, s.indexOf(",")).trim();
    } else {
      return s.substring(0, s.indexOf("//")).trim();
    }
  }


  /**
   *  Gets the title attribute of the Descriptor object
   *
   *@return    The title value
   */
  String getTitle() {
    switch (childType) {
      case TYPE_DEVDESCRIPTOR:
        return new String("");
      case TYPE_CFGDESCRIPTOR:
        return new String(" " + cfg.getAttributeValue("ID"));
      case TYPE_ITFDESCRIPTOR:
        return new String(" " + icfg.getAttributeValue("ID"));
      case TYPE_EPDESCRIPTOR:
        return new String(" " + ep.getAttributeValue("ID") + " [" +
            ep.getAttributeValue("Type") + "]");
      default:
        return new String("Unknown type [" + childType + "]");
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String descriptorTable() {
    int tableType    = getDataFormat();
    StringBuffer sb  = new StringBuffer(
        DescriptorTableHeader(getAttributeValue("Type") + getTitle(),
        tableType));
    switch (tableType) {
      case TABLE_2COLS:
        for (int i = 0; i < data.size(); i++) {
          String s  = (String) data.elementAt(i);
          sb.append("<Row><Comment>" + getComment(s) + "</Comment><Value>" +
              getValue(s) + "</Value></Row>\n");
        }
        break;
      case TABLE_RAW:
      default:
        sb.append("<Row><Value>");
        for (int i = 0; i < data.size(); i++) {
          sb.append(((String) data.elementAt(i)).trim());
        }
        sb.append("</Value></Row>\n");
        break;
    }
    sb.append(DescriptorTableFooter());
    return sb.toString();
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String printAllDescriptor() {
    StringBuffer sb  = new StringBuffer();
    sb.append(descriptorTable());
    dbg(false, "  Descriptor.printAllDescriptor", sb.toString());
    return sb.toString();
  }

}

