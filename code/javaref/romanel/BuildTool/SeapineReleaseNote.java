import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 21, 2004
 */
public class SeapineReleaseNote extends XmlTag {
  ChildList defect_xt                        = new ChildList();
  ChildList sccfile_xt                       = new ChildList();

  ChildList defect                           = new ChildList();
  ChildList sccfile                          = new ChildList();

  int highestDefectNumberWithValidLabelName  = 0;
  String s_buildNumber                       = null;
  String binaryFile                          = null;
  String releaseNoteFileName                 = null;
  Seapine seapine;
  StringBuffer releaseNote;
  JTextArea err;
  String s_lastrev;
  int i_lastrev;
  SeapineBuildReleaseTool zt;
  ProcessCmd prcscmd;
  java.util.List allFiles                    = new ArrayList();
  java.util.List v1Files                     = new ArrayList();
  java.util.List nonv1Files                  = new ArrayList();
  java.util.List nonv1FileNames              = new ArrayList();
  java.util.List manualLabels                = new ArrayList();

  int BugBelongs2SameMainline                = 0;
  int BugStatusFixed                         = 0;
  int CrossFileBugChecked                    = 0;
  int AllFilesAttached2Bugs                  = 0;
  int AllFilesNotV1Attached2Bugs             = 0;
  int CompareLabelFailed                     = 0;
  int numfiles                               = 0;
  int logfile;
  int errfile;
  boolean releaseNoteAlreadyExist            = false;


  /**
   *  Constructor for the SeapineReleaseNote object
   *
   *@param  zt  Description of the Parameter
   */
  SeapineReleaseNote(SeapineBuildReleaseTool zt) {
    this.zt = zt;
    this.err = zt.err;
    seapine = new Seapine(this);
    prcscmd = new ProcessCmd();
  }


  /**  Constructor for the SeapineReleaseNote object */
  SeapineReleaseNote() {
    seapine = new Seapine(this);
  }


  /**
   *  Description of the Method
   *
   *@param  tracenumber     Description of the Parameter
   *@param  line            Description of the Parameter
   *@param  lastrev         Description of the Parameter
   *@param  currepfilename  Description of the Parameter
   *@exception  Exception   Description of the Exception
   */
  void labelVersion(int tracenumber, String line, int lastrev,
      String currepfilename) throws Exception {
    String label  = new String(line.substring(line.indexOf("[") + 1,
        line.indexOf("]")));
    myPrintln("       version" + tracenumber + "[" +
        lastrev + "] labeled[" + label + "].");
    if (label.equals(zt.zeSeapineLbl_.getText())) {
      myPrintln("       Version" + tracenumber + "[" +
          lastrev + "] labeled[" + label +
          "|" + zt.zeSeapineLbl_.getText() + "].");
      FileVersionLabel fvl  = new FileVersionLabel(currepfilename,
          lastrev, zt.zeSeapineLbl_.getText());
      manualLabels.add(fvl);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myPrint(String s) throws Exception {
    seapine.f.writeFile(logfile, s);
    System.out.print(s);
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myPrintln(String s) throws Exception {
    myPrint(s + "\n");
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myErrPrint(String s) throws Exception {
    if (err != null) {
      err.append(s);
      seapine.f.writeFile(errfile, s);
      myPrint(s);
      seapine.f.writeFile(logfile, s);
    }
    System.out.print(s);
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myErrPrintln(String s) throws Exception {
    myErrPrint(s + "\n");
  }


  /**
   *  list the files in working directory that are at version 1 implying that
   *  they are not linked to a defect
   *
   *@exception  Exception  Description of the Exception
   */
  void buildListsV1Files() throws Exception {
    zt.AnalyzeLabel.setText("buildListsV1Files...");
          // list directories
    BufferedReader inputls  = execute("sscm ls " +
        " -b\"" + zt.branch.getText() + "\" -p" + zt.mainline.getText() + " -r" +
        " -y" + zt.username.getText() + ":" + zt.pwd.getText() +
        " -z" + zt.server, zt.pwd.getText());
    String linels;
    String currep           = null;
    while ((linels = inputls.readLine()) != null) {
      myPrintln(linels);
      if (linels.endsWith("M")) {
        numfiles++;
        boolean validDefectNumber  = false;
        String filename            = linels.substring(1, linels.indexOf(" ", 1));
        zt.AnalyzeLabel.setText("-" + filename + "-");
        myPrintln("*********************************************************************");
        myPrintln("**************" + filename);
        if (releaseNoteFileName.indexOf(filename) > 0) {
          releaseNoteAlreadyExist = true;
          myPrintln("************** Release note already exists");
        }
        myPrintln("*********************************************************************");
        BufferedReader inputh      = execute("sscm history \"" +
            currep + "/" + filename +
            "\" -b\"" + zt.branch.getText() + "\" -p" + zt.mainline.getText() +
            " -d20050101:20100101 " +
            " -y" + zt.username.getText() + ":" + zt.pwd.getText() +
            " -z" + zt.server, zt.pwd.getText());
        String lineh;
        int maxrev                 = 0;
        int i_zerev                = 0;
        int i_dnum                 = 0;
        while ((lineh = inputh.readLine()) != null) {
          myPrintln(lineh);
          String dateAndTime       = new String("\\p{Blank}+\\d+/\\d+/2\\d+\\p{Blank}+\\d+:\\d+[\\p{Blank}[A|P]M]*");
          String attachedToDefect  = new String("\\p{Blank}+Comments\\p{Blank}+-\\p{Blank}+Defect\\p{Blank}+Number:\\p{Blank}+");
          String labelAs           = new String("label as\\x5b[^\\x5d]+\\x5d.+");
          String checkinWithlabel  = new String("checkin with label\\x5b[^\\x5d]+\\x5d.*");
          String addToBranch       = new String("add to branch\\x5b[^\\x5d]+\\x5d.+");
          if (lineh.matches(".+\\p{Blank}+\\d+" + dateAndTime)) {
          //myPrintln("*********MATCHES************");
            String zorg     = new String(lineh.replaceFirst(dateAndTime, ""));
            String s_zerev  = new String(zorg.substring(zorg.lastIndexOf(" ")).trim());
            myPrintln("---- " + filename + " version=" + s_zerev + "/" + maxrev);
            i_zerev = Integer.valueOf(s_zerev).intValue();
            if (i_zerev > maxrev) {
              maxrev = i_zerev;
            }
            if (lineh.matches(labelAs)) {
              labelVersion(1, lineh, i_zerev, currep + "/" + filename);
            } else if (lineh.matches(addToBranch)) {
              labelVersion(2, lineh, i_zerev, currep + "/" + filename);
            } else if (lineh.matches(checkinWithlabel)) {
              labelVersion(3, lineh, i_zerev, currep + "/" + filename);
            }
          } else if (lineh.matches(attachedToDefect + "\\d+;")) {
            String zorg    = new String(lineh.replaceFirst(attachedToDefect, ""));
            String s_dnum  = new String(zorg.substring(0, zorg.lastIndexOf(";")).trim());
            i_dnum = Integer.valueOf(s_dnum).intValue();
            myPrintln("      version[" + i_zerev + "] attached to change[" + i_dnum + "].");
            if (!validDefectNumber) {
              validDefectNumber = defectFound(i_dnum);
            }
          } else if (lineh.matches(checkinWithlabel)) {
            labelVersion(4, lineh, i_zerev + 1, currep + "/" + filename);
          }
        }
        allFiles.add(currep + "/" + filename);
        if ((!validDefectNumber) && (maxrev > 1) && (i_dnum == 0)) {
          AllFilesNotV1Attached2Bugs++;
          String level;
          switch (zt.getBuildMode()) {
            case BUILDMODE_BUGBASED:
              level = new String("    *minor*");
              break;
            case BUILDMODE_LABELBASED:
              level = new String("        *warning*");
              break;
            default:
              level = new String("Invalid build Mode! Call Eric Mariacher");
              break;
          }
          myErrPrintln(level + filename + "[lastversion:" +
              maxrev + "] is not attached to any bug");
          nonv1FileNames.add(currep + "/" + filename);
          FileVersionLabel fvl  = new FileVersionLabel(currep + "/" + filename, maxrev, null);
          nonv1Files.add(fvl);
        } else if (!validDefectNumber) {

          myPrintln(maxrev + ">1:" + (maxrev > 1));
          if (maxrev > 1) {
            myErrPrintln("        *warning*" + filename + "[lastversion:" +
                maxrev + "] is not attached to one of bugs: " + validBugList());
            myErrPrintln("      " + filename + "[lastversion:" + maxrev + "] will be labeled.");
            FileVersionLabel fvl  = new FileVersionLabel(currep + "/" + filename, maxrev, null);
            nonv1Files.add(fvl);
            AllFilesNotV1Attached2Bugs++;
          } else {
            myErrPrintln("        *warning*" + filename + "[lastversion:" +
                maxrev + "] is not attached to one of bugs: " + validBugList());
            v1Files.add(currep + "/" + filename);
            AllFilesAttached2Bugs++;
          }
        }
        if (currep.matches("[B|b]inaries")) {
          myPrintln("*********BINARIES************");
          if (linels.toLowerCase().indexOf(s_buildNumber.toLowerCase()) > 0) {
            binaryFile = new String("http://ch01s20/ShadowFolder/" +
                zt.branch.getText() + "/" + currep + "/" + filename);
            myErrPrintln("*************BINARY file: " + binaryFile);
          }
        }
      } else if (linels.startsWith(zt.mainline.getText())) {
        if (linels.indexOf("/") < 0) {
          currep = new String("");
        } else {
          currep = new String(linels.trim().substring(linels.indexOf("/") + 1));
        }
      } else if (linels.startsWith("Either the username or passw")) {
        throw new Exception(linels);
      } else if (linels.startsWith("ls error:")) {
        throw new Exception(linels);
      } else if (linels.startsWith("Syntax:")) {
        throw new Exception(linels);
      }
    }
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void buildChildListsXmltag() throws Exception {
    zt.AnalyzeLabel.setText("buildChildListsXmltag...");
    findAll("defect", defect_xt);
    dbg(false, defect_xt.size(), defect_xt.toString());
    findAll("scc-file", sccfile_xt);

    for (int i = 0; i < defect_xt.size(); i++) {
      Defect d  = new Defect((XmlTag) defect_xt.elementAt(i));
      defect.add(d);
    }
    for (int i = 0; i < sccfile_xt.size(); i++) {
      XmlTag xt              = (XmlTag) sccfile_xt.elementAt(i);
          //setLastRevisionFixed(xt);
      String revfix          = xt.getAttributeValue("revision-fixed");
      String[] array_revfix  = revfix.split(", ");
      int maxrev             = 0;
      for (int j = 0; j < Array.getLength(array_revfix); j++) {
        String s_zerev  = ((String) Array.get(array_revfix, j)).trim();
        int i_zerev     = Integer.valueOf(s_zerev).intValue();
        if (i_zerev > maxrev) {
          maxrev = i_zerev;
        }
      }
      i_lastrev = maxrev;
      s_lastrev = new String((new Integer(i_lastrev)).toString());
      setLastRevisionFixed(xt);
      for (int j = 0; j < Array.getLength(array_revfix); j++) {
        String zerev  = ((String) Array.get(array_revfix, j)).trim();
        Sccfile sf    = new Sccfile(xt, zerev);
        sccfile.add(sf);
      }
    }
  }


  /**
   *  Sets the lastRevisionFixed attribute of the SeapineReleaseNote object
   *
   *@param  xt  The new lastRevisionFixed value
   */
  void setLastRevisionFixed(XmlTag xt) {
    XmlAttribute attr  = new XmlAttribute("lastrevision", s_lastrev);
    xt.addAttribute(attr);
  }


  /**
   *  Gets the labelDefectNumber attribute of the SeapineReleaseNote object
   *
   *@return    The labelDefectNumber value
   */
  int getLabelDefectNumber() {
    return highestDefectNumberWithValidLabelName;
  }


  /**
   *  Gets the labelName attribute of the SeapineReleaseNote object
   *
   *@param  product        Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void getLabelName(String product) throws Exception {
    zt.AnalyzeLabel.setText("getLabelName...");
    String labelName                        = null;
    highestDefectNumberWithValidLabelName = 0;
    Defect highestDefectWithValidLabelName  = null;
    int index                               = INVALID;
    for (int i = 0; i < defect.size(); i++) {
      Defect d  = (Defect) defect.elementAt(i);
      dbg(true, d.toString());
      myPrint(d.toString() + "\n");
      if (d.product.indexOf(product) < 0) {
         {
          /*if (zt.jcbBugBelongs2SameMainline.isSelected()) {
          throw new Exception("mainline[" + product + "] do not match with mainline[" +
              d.product + "] in defect " + d.toString());
        } else*/
          BugBelongs2SameMainline++;
          myErrPrintln("**MAJOR**mainline[" +
              product + "] do not match with mainline[" +
              d.product + "] in defect " + d.toString());
        }
      }
      if (d.defectstatus < STATUS_FIXED) {
         {
          /*if (zt.jcbBugStatusFixed.isSelected()) {
          throw new Exception(d.toString() + " has not been fixed yet.");
        } else*/
          BugStatusFixed++;
          myErrPrintln("    *minor*" +
              d.toString() + " has not been fixed yet.\n");
        }
      }
      if (d.isRelease() && (d.defectnumber > highestDefectNumberWithValidLabelName)) {
        labelName = d.release;
        highestDefectNumberWithValidLabelName = d.defectnumber;
        highestDefectWithValidLabelName = d;
        index = i;
      }
    }
    if (highestDefectNumberWithValidLabelName == 0) {
      throw new Exception("no valid label name found in defects summary.\nRule 1: is no white space.\nRule 2: label_name is what is before [.");
    }
    if (zt.getBuildMode() == BUILDMODE_LABELBASED) {
      labelName = new String(zt.zeSeapineLbl_.getText());
    }
          // find build number
    if (labelName.matches(".+[B|b]\\d\\d\\d\\d")) {
      s_buildNumber = new String(labelName.substring(
          labelName.toLowerCase().lastIndexOf("b")).toUpperCase());
    }
    if (s_buildNumber == null) {
      throw new Exception("no valid build number found.\nRule 1: format is BXXXX or bXXXX.");
    }
    releaseNoteFileName = new String(zt.mainline.getText() +
        "/Documentation/releasenote_" + highestDefectWithValidLabelName.product +
        "_" + s_buildNumber + ".xml");
    myErrPrintln("*************Label_name[" + labelName + "][build " +
        s_buildNumber + "] from defect[" + highestDefectNumberWithValidLabelName +
        "] releaseNoteName[" + releaseNoteFileName + "]");
    seapine.labelName = new String(labelName);
    getDefectList();
  }



  /**
   *  Description of the Method
   *
   *@param  full           Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void buildReleaseNote4html(boolean full) throws Exception {
    StringBuffer releaseNote4html  = new StringBuffer("<html><body><h3>release[<font color=\"red\">" +
        seapine.labelName + "</font>] in file [" + releaseNoteFileName + "]</h3><table border=\"1\">");

    for (int i = defect.size() - 1; i >= 0; i--) {
      Defect d  = (Defect) defect.elementAt(i);
      releaseNote4html.append(d.toHtmlString() + "\n");
    }
    releaseNote4html.append("</table>\n");
    if (full) {
      releaseNote4html.append("<table border=\"1\"><tr><th colspan=\"2\">Traceability level of this release</th></tr>\n");
      releaseNote4html.append("<tr><td>number of files not meeting feature</td><td align=\"center\">feature tested</td></tr>\n");
      releaseNote4html.append("<tr><td>" + BugBelongs2SameMainline + "</td><td>bugs not belonging to same mainline</td></tr>\n");
      releaseNote4html.append("<tr><td>" + BugStatusFixed + "</td><td>bugs are in new or open state</td></tr>\n");
      releaseNote4html.append("<tr><td>" + CrossFileBugChecked + "</td><td>cross file/bug dependencies have been identified</td></tr>\n");
      releaseNote4html.append("<tr><td>" + AllFilesNotV1Attached2Bugs+ "</td><td>files that have version &gt; 1 not attached to bug </td></tr>\n");
      releaseNote4html.append("<tr><td>" + AllFilesAttached2Bugs + "</td><td>files that have version == 1 not attached to bug </td></tr>\n");
      releaseNote4html.append("</table>\n");
      releaseNote4html.append("<p><b>" + CompareLabelFailed + "</b> Files have manual and automatic labels that do not match</p>\n");
    }
    releaseNote4html.append("</body></html>");
    zt.html.setText(releaseNote4html.toString());
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void buildReleaseNote() throws Exception {
    zt.AnalyzeLabel.setText("buildReleaseNote...");

    releaseNote = new StringBuffer("<?xml version=\"1.0\"?>\n<?xml-stylesheet type=\"text/xsl\" ");
    releaseNote.append("href=\"http://ch01s20/ShadowFolder/FW_tools/SeapineTools/BuildTool/releaseNote.xsl\"?>\n");
    releaseNote.append("<release-note release=\"" + seapine.labelName + "\" " +
        "bugbelongs2samemainline=\"" + BugBelongs2SameMainline + "\" " +
        "bugstatusfixed=\"" + BugStatusFixed + "\" " +
        "crossfilebugchecked=\"" + CrossFileBugChecked + "\" " +
        "allfilesattached2bugs=\"" + AllFilesAttached2Bugs + "\" " +
        "allfilesnotv1attached2bugs=\"" + AllFilesNotV1Attached2Bugs + "\" " +
        "numberoffiles=\"" + numfiles + "\" " +
        "buildmode=\"" + zt.getBuildMode() + "\" " +
        ">\n");
    if (binaryFile != null) {
      releaseNote.append("<binaryfile><name>" +
          binaryFile.substring(binaryFile.lastIndexOf("/") + 1) +
          "</name><href>" + binaryFile + "</href></binaryfile>\n");
    }
    releaseNote.append("<thisfile><name>" +
        releaseNoteFileName + "</name><href>http://ch01s20/ShadowFolder/" +
        releaseNoteFileName + "</href></thisfile>\n");
    for (int i = defect.size() - 1; i >= 0; i--) {
      Defect d  = (Defect) defect.elementAt(i);
      releaseNote.append(d.xt.toString() + "\n");
    }
    releaseNote.append("</release-note>\n");
    seapine.releaseNote = releaseNote;
    buildReleaseNote4html(true);
  }


  /**
   *  Description of the Method
   *
   *@param  sf             Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void compareWithManualLabel(Sccfile sf) throws Exception {
    FileVersionLabel fvl   = new FileVersionLabel(sf.zeCurrepName + "/" + sf.zeFileName,
        sf.revision, zt.zeSeapineLbl_.getText());
    int izfvl              = manualLabels.indexOf(fvl);
    FileVersionLabel zfvl  = null;
    if (zt.zeSeapineLbl_.getText().trim().length() > 0) {
      if (izfvl < 0) {
        for (ListIterator li = manualLabels.listIterator(); li.hasNext(); ) {
          zfvl = (FileVersionLabel) li.next();
          if (zfvl.matchName(fvl)) {
            break;
          }
        }
        if (sf.zeCurrepName.toLowerCase().indexOf("source_code") == 0) {
          if (zfvl == null) {
            myErrPrintln("*MAJOR** " + sf.zeCurrepName + "/" +
                sf.zeFileName + "[" + sf.revision +
                "] does not match with label to compare [" +
                zt.zeSeapineLbl_.getText() +
                "]: no version of this file is tagged with this label.");
          } else {
            myPrintln("    " + zfvl.toString() + " vs " + fvl.toString() + " KO");
            myErrPrintln("*MAJOR** " + sf.zeCurrepName + "/" +
                sf.zeFileName + "[" + sf.revision +
                "] does not match with label to compare [" +
                zt.zeSeapineLbl_.getText() + "]: version expected[" +
                zfvl.iVersion + "].");
          }
        } else {
          if (zfvl == null) {
            myErrPrintln("    *minor*" + sf.zeCurrepName + "/" +
                sf.zeFileName + "[" + sf.revision +
                "] does not match with label to compare [" +
                zt.zeSeapineLbl_.getText() +
                "]: no version of this file is tagged with this label.");
          } else {
            myPrintln("    " + zfvl.toString() + " vs " + fvl.toString() + " KO");
            myErrPrintln("    *minor*" + sf.zeCurrepName + "/" +
                sf.zeFileName + "[" + sf.revision +
                "] does not match with label to compare [" +
                zt.zeSeapineLbl_.getText() + "]: version expected[" +
                zfvl.iVersion + "].");
          }
        }
        CompareLabelFailed++;
      } else {
        zfvl = (FileVersionLabel) manualLabels.get(izfvl);
        myPrintln("    " + zfvl.toString() + " vs " + fvl.toString() + " OK");
      }
    }
  }


  /**
   *  Gets the latestFileVersions attribute of the XmlAsInput object
   *
   *@param  branch         Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void getLatestFileVersions(String branch) throws Exception {
    zt.AnalyzeLabel.setText("getLatestFileVersions...");
    int lastVersion  = INVALID;
    String lastName  = new String("");
    for (int i = sccfile.size() - 1; i >= 0; i--) {
      String a               = null;
      Sccfile sf             = (Sccfile) sccfile.elementAt(i);
      FileVersionLabel fvl2  = new FileVersionLabel(sf.zeCurrepName + "/" + sf.zeFileName,
          sf.revision, null);
      myPrintln("__getLatestFileVersions1__ " + sf.zeCurrepName + "/" + sf.zeFileName +
          "[" + sf.revision + "][" + sf.zeBranchName + "]:" + sf.belongsToBranch(branch));
      if (sf.belongsToBranch(branch)) {
        if (sf.filename.equals(lastName)) {
          int expectedVersion  = lastVersion - 1;
          if ((sf.revision != expectedVersion) && (sf.revision != lastVersion)) {
            myErrPrintln("*MAJOR**Cross file / change dependency!*****");
            myErrPrintln("    " + sf.filename + " version[" + expectedVersion +
                "] not found! actual version[" + sf.revision + "]");
            myErrPrintln("    Integrate missing change to get " + sf.filename +
                "[" + expectedVersion + "]");
            CrossFileBugChecked++;
          }
          if (allFiles.contains(sf.zeCurrepName + "/" + sf.zeFileName)) {
            a = new String("false");
          }
        } else {
          if (v1Files.contains(sf.zeCurrepName + "/" + sf.zeFileName)) {
            myErrPrintln("        *warning* " + sf.zeFileName + "[version:" + sf.revision +
                "] belongs to v1 files with no attached bugs list (Seapine Bug!)");
          } else if (nonv1FileNames.contains(sf.zeCurrepName + "/" + sf.zeFileName)) {
            myErrPrintln("        *warning* " + sf.zeFileName + "[version:" + sf.revision +
                "] belongs to files with no attached bugs list (Seapine Bug!)");
          }
          if (allFiles.contains(sf.zeCurrepName + "/" + sf.zeFileName)) {
            a = new String("true");
          } else {
            myErrPrintln("        *warning* " + sf.zeCurrepName + "/" + sf.zeFileName + " does not exist anymore.");
          }
          compareWithManualLabel(sf);
        }
        if ((a != null) && (sf.xt.getAttributeValue("last").length() == 0)) { // all sf having same name share same xt
          sf.xt.setAttributeValue("last", new String(a), true);
        }
      }
      lastName = new String(((Sccfile) sccfile.elementAt(i)).filename);
      lastVersion = sf.revision;
    }
          // check
    listsccfile(1);
  }


  /**
   *  Description of the Method
   *
   *@param  u              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void listsccfile(int u) throws Exception {
    for (int i = sccfile.size() - 1; i >= 0; i--) {
      Sccfile sf  = (Sccfile) sccfile.elementAt(i);
      myPrintln("__listsccfile" + u + "__ " + sf.zeCurrepName + "/" + sf.zeFileName +
          "[" + sf.revision + "][" + sf.zeBranchName + "]---" +
          sf.xt.getAttributeValue("last"));
    }
  }


  /**
   *  Description of the Method
   *
   *@param  branch         Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void buildSeapineScript(String branch) throws Exception {
    if (zt.getBuildMode() == BUILDMODE_BUGBASED) {
      QuickSort qs  = new QuickSort();
      qs.sort(sccfile.childs);
      getLatestFileVersions(branch);
    }
  }


  /**
   *  Gets the defectList attribute of the SeapineReleaseNote object
   *
   *@exception  Exception  Description of the Exception
   */
  void getDefectList() throws Exception {
    QuickSort qs          = new QuickSort();
    qs.sort(defect.childs);
          //clean duplicated defects
    int lastdefectnumber  = 0;
    int defectnumber      = 0;
    for (int i = defect.size() - 1; i >= 0; i--) {
      Defect d  = (Defect) defect.elementAt(i);
      defectnumber = d.defectnumber;
          //myErrPrintln(defectnumber + " -1- " + d.defectStatusToString(d.defectstatus));
      if (defectnumber == lastdefectnumber) { // duplicated defect
        defect.removeElementAt(i);
      }
      lastdefectnumber = defectnumber;
    }
    buildReleaseNote4html(false);
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@param  pwd            Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  BufferedReader execute(String s, String pwd) throws Exception {
    BufferedReader input  = prcscmd.ExecuteCmd(s);
    myPrintln(s.replaceAll(pwd, "##PWD##"));
    return input;
  }


  /**
   *  Description of the Method
   *
   *@param  dnum  Description of the Parameter
   *@return       Description of the Return Value
   */
  boolean defectFound(int dnum) {
    boolean found  = false;
    for (int i = defect.size() - 1; i >= 0; i--) {
      if (((Defect) defect.elementAt(i)).defectnumber == dnum) {
        found = true;
      }
    }
    return found;
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String validBugList() {
    StringBuffer sb  = new StringBuffer();
    for (int i = defect.size() - 1; i >= 0; i--) {
      sb.append(((Defect) defect.elementAt(i)).defectnumber + ", ");
    }
    return sb.toString();
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void initLogFiles() throws Exception {
    Calendar c                = Calendar.getInstance();
    Date date                 = c.getTime();
    SimpleDateFormat datefmt  = new SimpleDateFormat("ddMMMyy_HH_mm");
    String sdate              = new String(datefmt.format(date));
    logfile = seapine.f.addFichier(seapine.working_directory + "\\logfile_" + sdate + ".log");
    errfile = seapine.f.addFichier(seapine.working_directory + "\\errfile_" + sdate + ".log");
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void closeLogFiles() throws Exception {
    seapine.f.cleanfos(logfile);
    seapine.f.cleanfos(errfile);
  }
}

