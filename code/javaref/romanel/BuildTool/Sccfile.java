import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 15, 2004
 */
public class Sccfile extends Sortable {
  String filename;
  Defect d;
  String zeBranchName;
  String zeFileName;
  String zePathName;
  String zeRootName;
  String zeCurrepName;
  int revision         = INVALID;
  XmlTag xt;


  /**
   *  Constructor for the Sccfile object
   *
   *@param  xt     Description of the Parameter
   *@param  zerev  Description of the Parameter
   */
  Sccfile(XmlTag xt, String zerev) {
    this.xt = xt;
    filename = new String(xt.noTag);
    zeBranchName = filename.substring(0, filename.indexOf("::"));
    zeFileName = filename.substring(filename.lastIndexOf("/") + 1);
    zePathName = filename.substring(filename.indexOf("::") + 2,
        filename.lastIndexOf("/"));
    zeRootName = filename.substring(filename.indexOf("::") + 2,
        filename.indexOf("/"));
    zeCurrepName = filename.substring(filename.indexOf("/") + 1,
        filename.lastIndexOf("/"));
    revision = Integer.valueOf(zerev).intValue();
    xt.removeAttribute("last");
    //xt.removeAttribute("lastrevision");
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String dump() {
    return new String(", filename=" + filename + ", zeBranchName=" + zeBranchName +
        ", zeFileName=" + zeFileName + ", zeRootName=" + zeRootName +
        ", zePathName=" + zeRootName + ", zeCurrepName=" + zeCurrepName);
  }


  /**
   *  Description of the Method
   *
   *@param  labelName           Description of the Parameter
   *@param  serverAddr_portNum  Description of the Parameter
   *@return                     Description of the Return Value
   */
  String toString4SeapineCliLabel(String labelName, String serverAddr_portNum) {
    if (zeFileName.indexOf("releasenote_") >= 0) {
      return new String("REM " + zeFileName +
          " is a release note and will not be labeled again.");
    } else if (xt.getAttributeValue("last").equals("false")) {
      return new String("REM " + zeFileName + " -v" +
          revision +
          " is not the latest version of this file and will not be labeled.");
    } else if (xt.getAttributeValue("last").length() == 0) {
      return new String("REM " + filename + " is not part of this branch and won't be labeled.");
    } else if (!xt.getAttributeValue("lastrevision").equals(Integer.toString(revision))) {
      return new String("REM " + zeFileName + " -v" +
          revision +
          " is not the latest version of this file and will not be labeled.");
    } else {
      return new String("sscm label \"" + zeFileName + "\" -b\"" + zeBranchName +
          "\" -cc\"Label_" + labelName + "\" -l\"" + labelName + "\" -o -p\"" + zePathName +
          "\" -v" + revision + " -y%1:%2 -z" + serverAddr_portNum);
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    return xt.toString();
  }



  /**
   *  Description of the Method
   *
   *@param  other  Description of the Parameter
   *@return        Description of the Return Value
   */
  public int compare(Object other) {
    Sccfile sf  = ((Sccfile) other);
    int result  = filename.compareTo(sf.filename);
    if (result == 0) {
      if (sf.revision > revision) {
        return -1;
      } else if (sf.revision < revision) {
        return 1;
      } else {
        return 0;
      }
    } else if (result > 0) {
      return 1;
    } else {
      return -1;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   *@return    Description of the Return Value
   */
  boolean compareName(String s) {
    if (filename.compareTo(s) == 0) {
      return true;
    } else {
      return false;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  branch  Description of the Parameter
   *@return         Description of the Return Value
   */
  boolean belongsToBranch(String branch) {
    return branch.equals(zeBranchName);
  }

}

