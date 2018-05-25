import java.io.*;
import java.lang.Runtime;
import java.util.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 15, 2004
 */
public class Seapine extends Log {
  ChildList filelist;
  StringBuffer releaseNote;
  MesFichiers f             = new MesFichiers();
  String labelName          = null;
  String working_directory  = null;
  SeapineReleaseNote x;
  List doscmd               = new ArrayList();
  String TTProCfgName       = null;


  /**
   *  Constructor for the Seapine object
   *
   *@param  x  Description of the Parameter
   */
  Seapine(SeapineReleaseNote x) {
    this.x = x;
    filelist = x.sccfile;
    releaseNote = x.releaseNote;
  }


  /**
   *  Description of the Method
   *
   *@param  filename       Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void knowPath(String filename) throws Exception {
    File fil              = new File(filename);
    String name           = fil.getName();
    String CanonicalPath  = fil.getCanonicalPath();
    int index             = CanonicalPath.indexOf(name);
          // get working directory from where the Seapine.xml file comes from
    working_directory = new String(CanonicalPath.substring(0, index));
    dbg(true, working_directory);
  }


  /**
   *  Gets the documentationDirectory attribute of the Seapine object
   *
   *@param  s              The feature to be added to the Line attribute
   *@param  fus            The feature to be added to the Line attribute
   *@exception  Exception  Description of the Exception
   */

  /**
   *  Adds a feature to the Line attribute of the Seapine object
   *
   *@param  s              The feature to be added to the Line attribute
   *@param  fus            The feature to be added to the Line attribute
   *@exception  Exception  Description of the Exception
   */
  void addLine(int fus, String s) throws Exception {
    f.writeFile(fus, s + "\n");
    if (s.indexOf("REM") != 0) {
      doscmd.add(s + "\n");
    }
  }


  /**
   *  Description of the Method
   *
   *@param  serverAddr_portNum  Description of the Parameter
   *@param  TTProCfgName        Description of the Parameter
   *@param  forReal             Description of the Parameter
   *@exception  Exception       Description of the Exception
   */
  void buildFiles4Bat(String TTProCfgName, String serverAddr_portNum, boolean forReal) throws Exception {
    x.zt.AnalyzeLabel.setText("buildFiles4Bat...");
    int fos;
    int fos1;
          //Sccfile sf      = (Sccfile) filelist.elementAt(0);
    String workDir  = new String("C:\\temp\\" + x.zt.branch.getText() + "\\" + x.zt.mainline.getText() + "\\Documentation");
    String docuDir  = new String(x.zt.mainline.getText() + "/Documentation");
    this.TTProCfgName = new String(TTProCfgName);
          //build bat file to label each file linked to a defect
    if (forReal) {
      if (x.releaseNoteAlreadyExist) {
        throw new Exception("This Release Note already exists: ask a Seapine administrator to destroy it!");
      }
          // put release note in a file that will be checked-in
      fos1 = f.addFichier(workDir + "\\" + x.releaseNoteFileName.substring(x.releaseNoteFileName.lastIndexOf("/") + 1));
      f.writeFile(fos1, releaseNote.toString());
      f.cleanfos(fos1);
      fos = f.addFichier(working_directory + "\\buildrelease.bat");
    } else {
          // put release note in a file just for debug purposes
      fos1 = f.addFichier(working_directory + "\\" + x.releaseNoteFileName.substring(x.releaseNoteFileName.lastIndexOf("/") + 1));
      myPrintln(working_directory + "\\" + x.releaseNoteFileName.substring(x.releaseNoteFileName.lastIndexOf("/") + 1));
      f.writeFile(fos1, releaseNote.toString());
      f.cleanfos(fos1);
      fos = f.addFichier(working_directory + "\\buildreleaseCheck.bat");
      myPrintln(working_directory + "\\buildreleaseCheck.bat");
    }

          //addLine(fos, new String("sscm lsttdb -z" + serverAddr_portNum));

    if (x.zt.getBuildMode() == BUILDMODE_BUGBASED) {
          //build bat file to label each file not linked to a defect that have version==1
      /* label v1 files before other files because there is a seapine bug:
          history is not always fully listed */
      addLine(fos, "REM tag lastversion==1 files");
      for (ListIterator i = x.v1Files.listIterator(); i.hasNext(); ) {
        String zeFileName  = (String) i.next();
        addLine(fos, new String("sscm label \"" + zeFileName + "\" -b\"" + x.zt.branch.getText() +
            "\" -cc\"Label_" + labelName + "\" -l\"" + labelName + "\" -o -p" + x.zt.mainline.getText() +
            " -v1 -y%1:%2 -z" + serverAddr_portNum));
      }

          //build bat file to label each file not linked to a defect that have version>1
      addLine(fos, "REM tag lastversion>1 files");
      for (ListIterator i = x.nonv1Files.listIterator(); i.hasNext(); ) {
        FileVersionLabel zeFile  = (FileVersionLabel) i.next();
        addLine(fos, new String("sscm label \"" + zeFile.filename + "\" -b\"" + x.zt.branch.getText() +
            "\" -cc\"Label_" + labelName + "\" -l\"" + labelName + "\" -o -p" + x.zt.mainline.getText() +
            " -v" + zeFile.iVersion + " -y%1:%2 -z" + serverAddr_portNum));
      }

      addLine(fos, "REM tag files version attached to bugs ");
      for (int i = 0; i < filelist.size(); i++) {
          //Process p  = Runtime.getRuntime().exec(((Sccfile) filelist.elementAt(i)).toString4SeapineCliLabel(labelName, userid_pwd, serverAddr_portNum));
        addLine(fos, new String(((Sccfile) filelist.elementAt(i)).toString4SeapineCliLabel(labelName,
            serverAddr_portNum)));
      }
    }
    addLine(fos, "REM create and tag release note ");
          // put release note in Documentation Directory
    addLine(fos, new String("sscm workdir \"" + workDir + "\" \"" + docuDir +
        "\" -b\"" + x.zt.branch.getText() + "\" -r -z" + serverAddr_portNum + " -y%1:%2"));
    addLine(fos, new String("sscm add \"" + f.file[fos1].fil.getCanonicalPath() +
        "\" -b\"" + x.zt.branch.getText() + "\" -cc\"ReleaseNote_" + labelName +
        "\" -g- -p\"" + docuDir + "\" -u -z" + serverAddr_portNum + " -y%1:%2"));
    addLine(fos, new String("sscm checkout \"" + f.file[fos1].fil.getName() +
        "\" -b\"" + x.zt.branch.getText() + "\" -cc\"ReleaseNote_" + labelName +
        "\" -f -p\"" + docuDir + "\" -wreplace -z" + serverAddr_portNum + " -y%1:%2"));
    addLine(fos, new String("sscm checkin \"" + f.file[fos1].fil.getName() +
        "\" -b\"" + x.zt.branch.getText() + "\" -cc\"ReleaseNote_" + labelName +
        "\" -f -g- -l\"" + labelName + "\" -o -p\"" + docuDir + "\" -s" + TTProCfgName +
        " -a" + x.getLabelDefectNumber() +
        " -u -z" + serverAddr_portNum + " -i%1:%2 -y%1:%2"));
    if (x.zt.branch.getText() != x.zt.mainline.getText()) {
      addLine(fos, new String("sscm workdir \"" + workDir + "\" \"" + docuDir +
          "\" -b\"" + x.zt.mainline.getText() + "\" -r -z" + serverAddr_portNum + " -y%1:%2"));
      addLine(fos, new String("sscm add \"" + f.file[fos1].fil.getCanonicalPath() +
          "\" -b\"" + x.zt.mainline.getText() + "\" -cc\"ReleaseNote_" + labelName +
          "\" -g- -p\"" + docuDir + "\" -u -z" + serverAddr_portNum + " -y%1:%2"));
      addLine(fos, new String("sscm checkout \"" + f.file[fos1].fil.getName() +
          "\" -b\"" + x.zt.mainline.getText() + "\" -cc\"ReleaseNote_" + labelName +
          "\" -f -p\"" + docuDir + "\" -wreplace -z" + serverAddr_portNum + " -y%1:%2"));
      addLine(fos, new String("sscm checkin \"" + f.file[fos1].fil.getName() +
          "\" -b\"" + x.zt.mainline.getText() + "\" -cc\"ReleaseNote_" + labelName +
          "\" -f -g- -l\"" + labelName + "\" -o -p\"" + docuDir + "\" -s" + TTProCfgName +
          " -a" + x.getLabelDefectNumber() +
          " -u -z" + serverAddr_portNum + " -i%1:%2 -y%1:%2"));
    }
    f.cleanfos(fos);
  }


  /**
   *  Description of the Method
   *
   *@param  userid         Description of the Parameter
   *@param  pwd            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void executeBat(String userid, String pwd) throws Exception {
    executeDosCmds1by1(userid, pwd);
  }


  /**
   *  Description of the Method
   *
   *@param  userid         Description of the Parameter
   *@param  pwd            Description of the Parameter
   *@exception  Exception  Description of the Exception void
   *      executeOneBat(String userid, String pwd) throws Exception { String cmd
   *      = new String(f.file[fos].fil.getName() + " " + userid + " " + pwd);
   *      Process p = Runtime.getRuntime().exec(cmd + "\n"); BufferedReader
   *      input = new BufferedReader(new InputStreamReader(p.getInputStream()));
   *      String line; while ((line = input.readLine()) != null) {
   *      System.out.println(line); if (line.indexOf("-zSCMServerAddr:PortNum")
   *      > 0) { throw new Exception("bat file execution failed!"); } if
   *      (line.indexOf("try again") > 0) { throw new Exception("bat file
   *      execution failed!"); } } }
   */

  /**
   *  Description of the Method
   *
   *@param  userid         Description of the Parameter
   *@param  pwd            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void executeDosCmds1by1(String userid, String pwd) throws Exception {
    myPrintln("executeDosCmds1by1");
    int cpt  = 0;
    for (ListIterator i = doscmd.listIterator(); i.hasNext(); ) {
      cpt++;
      String cmd            = (String) i.next();
      cmd = new String(cmd.replaceAll("%1:%2", userid + ":" + pwd));
      Process p             = Runtime.getRuntime().exec(cmd);
      BufferedReader input  = new BufferedReader(new InputStreamReader(p.getInputStream()));
      myPrint(cmd.replaceAll(pwd, "##PWD##"));
      String line;
      while ((line = input.readLine()) != null) {
        myPrintln(line);
        x.zt.BuildLabel.setText("-" + cpt + "/" + doscmd.size() + "-");
        if (line.indexOf("-zSCMServerAddr:PortNum") > 0) {
          throw new Exception("bat file execution failed!");
        }
        if (line.indexOf("try again") > 0) {
          throw new Exception("bat file execution failed!");
        }
      }
    }
    myPrintln("executeDosCmds1by1:" + cpt);
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myPrint(String s) throws Exception {
    f.writeFile(x.logfile, s);
    System.out.print(s);
  }


  /**
   *  Constructor for the myPrintln object
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myPrintln(String s) throws Exception {
    myPrint(s + "\n");
  }

}

