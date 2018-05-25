import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 21, 2004
 */
public class Defect extends Sortable {
  String summary;
  int defectnumber;
  int defectstatus;
  String product      = null;
  String release      = null;
  String releaseType  = null;
  String description  = null;
  XmlTag xt;


  /**
   *  Constructor for the Defect object
   *
   *@param  xt  Description of the Parameter
   */
  Defect(XmlTag xt) {
    this.xt = xt;
    defectstatus = getDefectStatus(xt.find("defect-status", "", "").noTag);
    product = xt.find("product", "", "").noTag;
    summary = xt.find("summary", "", "").noTag;
    if (xt.find("description", "", "") != null) {
      description = xt.find("description", "", "").noTag;
    }
    defectnumber = Integer.valueOf(xt.find("defect-number", "", "").noTag).intValue();
    int indextruc        = summary.indexOf("[");
    int indexwhitespace  = summary.indexOf(" "); // no white space in label_name

    if ((indextruc > 0) && ((indexwhitespace < 0) || (indexwhitespace > indextruc))) {
      release = new String(summary.substring(0, summary.indexOf("[")));
      releaseType = new String(summary.substring(summary.indexOf("[") + 1, summary.indexOf("]")));
      XmlAttribute a  = new XmlAttribute("isrelease", releaseType);
      xt.addAttribute(a);
    }
    htmlizeDescription();
  }


  /**
   *  Gets the release attribute of the Defect object
   *
   *@return    The release value
   */
  boolean isRelease() {
    return (release != null);
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    return new String("[[" + defectnumber + "] " +
        summary + " ]");
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toHtmlString() {
    return new String("<tr><th>" + defectnumber + "</th><td><font color=\"blue\">" +
        summary + "</font></td><td>" + product + "</td><td>" + defectStatusToString(defectstatus) + "</td></tr>");
  }


  /**  Description of the Method */
  void htmlizeDescription() {
    if (description == null) {
    } else if (description.indexOf("<") == 0) { // already html do nothing
    } else if (description.indexOf("***") >= 0) {
      String[] lines   = description.split("\n");
      String tmpline;
      StringBuffer sb  = new StringBuffer("<pre>");
      for (int i = 0; i < Array.getLength(lines); i++) {
        int threestars  = lines[i].indexOf("***");
        if (threestars >= 0) {
          int warning  = lines[i].indexOf("arning");
          if (warning > 0) {
            tmpline = new String("</pre><b><font color=\"red\">" + lines[i] + "</font></b><pre>\n");
          } else {
            tmpline = new String("</pre><b>" + lines[i] + "</b><pre>\n");
          }
        } else {
          tmpline = new String(lines[i] + "\n");
        }
        sb.append(tmpline);
      }
      sb.append("</pre>");
      System.out.println("description4[" + sb.toString() + "]");
      xt.find("description", "", "").noTag = new String(sb.toString());
    } else {
      description = new String("<pre>" + description + "</pre>");
      xt.find("description", "", "").noTag = new String(description);
    }
  }


  /**
   *  Gets the defectStatus attribute of the Defect object
   *
   *@param  s  Description of the Parameter
   *@return    The defectStatus value
   */
  int getDefectStatus(String s) {
    if (s.equals("Fixed")) {
      return STATUS_FIXED;
    } else if (s.indexOf("Closed") == 0) {
      return STATUS_CLOSED;
    } else if (s.indexOf("Open") == 0) {
      return STATUS_OPEN;
    } else if (s.indexOf("Released") == 0) {
      return STATUS_RELEASED;
    } else if (s.indexOf("Verified") == 0) {
      return STATUS_VERIFIED;
    } else if (s.indexOf("New") == 0) {
      return STATUS_NEW;
    } else if (s.indexOf("Rejected") == 0) {
      return STATUS_REJECTED;
    } else {
      return 0;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  zestatus  Description of the Parameter
   *@return           Description of the Return Value
   */
  String defectStatusToString(int zestatus) {
    switch (zestatus) {
      case STATUS_FIXED:
        return new String("Fixed");
      case STATUS_CLOSED:
        return new String("Closed");
      case STATUS_OPEN:
        return new String("Open");
      case STATUS_RELEASED:
        return new String("Released");
      case STATUS_VERIFIED:
        return new String("Verified");
      case STATUS_NEW:
        return new String("New");
      case STATUS_REJECTED:
        return new String("Rejected");
      default:
        return new String("Unknown Status[" + zestatus + "]");
    }
  }


  /**
   *  Description of the Method
   *
   *@param  other  Description of the Parameter
   *@return        Description of the Return Value
   */
  public int compare(Object other) {
    Defect d  = ((Defect) other);
    if (d.defectnumber > defectnumber) {
      return -1;
    } else if (d.defectnumber < defectnumber) {
      return 1;
    } else { // same defect number -> look for defect status
      if (d.defectstatus > defectstatus) {
        return -1;
      } else if (d.defectstatus < defectstatus) {
        return 1;
      } else {
        return 0;
      }
    }
  }
}

