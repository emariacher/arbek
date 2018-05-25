import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 15, 2004
 */
public class XmlAsInput extends XmlTag {
  ChildList defect;
  Vector filelist                            = new Vector();
  QuickSort qs                               = new QuickSort();
  StringBuffer releaseNote                   = new StringBuffer();
  Seapine seapine                            = new Seapine(this);
  int highestDefectNumberWithValidLabelName  = 0;


  /**  Constructor for the XmlAsInput object */
  XmlAsInput() {
    tagName = new String("Root");
    childList.add(new ChildList(TYPE_DEFECT));
    defect = (ChildList) childList.elementAt(childList.size() - 1);
  }


  /**
   *  Adds a feature to the Defect attribute of the XmlAsInput object
   *
   *@param  d  The feature to be added to the Defect attribute
   */
  void addDefect(Defect d) {
    defect.add(d, TYPE_DEFECT);
  }


  /**
   *  Gets the labelDefectNumber attribute of the XmlAsInput object
   *
   *@return    The labelDefectNumber value
   */
  int getLabelDefectNumber() {
    return highestDefectNumberWithValidLabelName;
  }


  /**
   *  Gets the labelName attribute of the Seapine object
   *
   *@param  product        Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void getLabelName(String product) throws Exception {
    String labelName  = null;
    highestDefectNumberWithValidLabelName = 0;
    int index         = INVALID;
    for (int i = 0; i < defect.childs.size(); i++) {
      Defect d  = (Defect) defect.childs.elementAt(i);
      if (!product.equals(d.product)) {
        System.out.println("Fix product stuff");
          //throw new Exception("product[" + product + "] do not match with:" + d.toString());
      }
      if (d.defectstatus < STATUS_FIXED) {
        throw new Exception(d.toString() + " has not been fixed yet.");
      }
      if (d.isRelease() && (d.defectnumber > highestDefectNumberWithValidLabelName)) {
        labelName = d.release;
        highestDefectNumberWithValidLabelName = d.defectnumber;
        index = i;
      }
    }
    if (highestDefectNumberWithValidLabelName == 0) {
      throw new Exception("no valid label name found in defects summary.\nRule 1: is no white space.\nRule 2: label_name is what is before [.");
    } else {
      System.out.println("Label_name[" + labelName + "] found in defect:" +
          ((Defect) defect.childs.elementAt(index)).toString());
      seapine.labelName = new String(labelName);
    }
  }



  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void buildReleaseNote() throws Exception {
    releaseNote.append("<?xml version=\"1.0\"?><?xml-stylesheet type=\"text/xsl\" href=\"releaseNote.xsl\"?>\n");
    releaseNote.append("<release-note release=\"" + seapine.labelName + "\"><defects>\n");
    for (int i = defect.childs.size() - 1; i > 0; i--) {
      releaseNote.append(((Defect) defect.childs.elementAt(i)).toString() + "\n");
    }
    releaseNote.append("</defects></release-note>\n");
  }


  /**  Gets the latestFileVersions attribute of the XmlAsInput object */
  void getLatestFileVersions() {
    String lastName  = new String("");
    for (int i = filelist.size() - 1; i >= 0; i--) {
      Sccfile sf  = (Sccfile) filelist.elementAt(i);
      if (sf.filename.equals(lastName)) {
        sf.notLatest = true;
        filelist.removeElementAt(i);
      } else {
        lastName = new String(((Sccfile) filelist.elementAt(i)).filename);
      }
    }
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void buildSeapineScript() throws Exception {
    qs.sort(filelist);
    getLatestFileVersions();
  }

}

