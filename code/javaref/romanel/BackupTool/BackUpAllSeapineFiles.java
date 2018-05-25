
//import java.awt.*;
//import java.awt.event.*;
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
 *@created    January 14, 2005
 */
public class BackUpAllSeapineFiles extends Thread implements DefVar {
  java.util.List branch          = new ArrayList();
  MesFichiers f;
  ProcessCmd pb;
  JTextArea log                  = null;
  int invalidBranch;
  String server, userid, pwd, backupdir;
  boolean done;
  BackUpSeapineFilesTool ZeTool;


  /**
   *  Description of the Method
   *
   *@param  userid         Description of the Parameter
   *@param  pwd            Description of the Parameter
   *@param  server         Description of the Parameter
   *@param  backupdir      Description of the Parameter
   *@param  ZeTool         Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  BackUpAllSeapineFiles(String server, String userid, String pwd, String backupdir, BackUpSeapineFilesTool ZeTool) throws Exception {
    invalidBranch = 0;
    this.server = server;
    this.userid = userid;
    this.pwd = pwd;
    this.backupdir = backupdir;
    this.ZeTool = ZeTool;
    log = ZeTool.log;
    f = new MesFichiers(backupdir);
    pb = new ProcessCmd(f);
    done = false;
    start();
  }


  /**  Main processing method for the BackUpAllSeapineFiles object */
  public void run() {
    try {
          // list mainline
      BufferedReader inputml      = execute("sscm lsmainline -y" +
          userid + ":" + pwd + " -z" + server, pwd);

          // for each mainline list child baseline branches
      String lineml;
      while ((lineml = inputml.readLine()) != null) {
        myPrintln(lineml);
        if ((lineml.length() < 2) || (lineml.indexOf("lsmainline") > 0)) {
          continue;
        }
        BufferedReader inputb  = execute("sscm lsbranch -p" + lineml +
            " -y" + userid + ":" + pwd + " -z" + server, pwd);
        String lineb;
        while ((lineb = inputb.readLine()) != null) {
          myPrintln(lineb);
          if ((lineb.length() < 2) || (lineb.indexOf("lsbranch") > 0)) {
            continue;
          }
          String[] numberOfWord  = lineb.split(" ");
          if (Array.getLength(numberOfWord) > 2) {
            invalidBranch++;
            myPrintln("******************invalid branch********************");
          }

          Branch b               = new Branch(lineml, lineb, f);
          branch.add(b);
        }
        inputb.close();
      }
      inputml.close();

          //for each branch get files and zip them in one file
      Zipper zipper               = new Zipper(log);
      for (ListIterator i = branch.listIterator(); i.hasNext(); ) {
        Branch b               = (Branch) i.next();
        BufferedReader inputf  = execute("sscm get / -b" + b.name +
            " -d" + backupdir + b.backupDirectory() +
            " -f -p" + b.mainline + " -r -wreplace" +
            " -y" + userid + ":" + pwd + " -z" + server, pwd);
        String linef;
        int state              = STATE_IDLE;
        while ((linef = inputf.readLine()) != null) {
          myPrintln(linef);
          if (linef.startsWith("get")) {
            state = STATE_FILE;
            continue;
          }
          if (state == STATE_FILE) {
            if (linef.indexOf(fileSeparator) < 0) {
              state = STATE_IDLE;
              continue;
            }
            zipper.addFiles(linef.trim());
          }
        }
        inputf.close();
      }
      zipper.removeDuplicates();
      Date today                  = new Date();
      SimpleDateFormat formatOut  = new SimpleDateFormat("ddMMMyy");
      zipper.create(backupdir + "Seapine" + formatOut.format(today) + ".zip");
      myPrintln("***************************************************");
      myPrintln(invalidBranch + " invalid Branches (with spaces in their names) not saved.");
      ZeTool.BuildLabel.setText(invalidBranch + " invalid Branches not saved.");
    } catch (Exception e) {
      myPrintln("***************************************************");
      myPrintln(e.toString());
    } finally {

    }
    done = true;
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
    BufferedReader input  = pb.ExecuteCmd(s);
    myPrintln(s.replaceAll(pwd, "##PWD##"));
    return input;
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void myPrint(String s) {
    if (log != null) {
      log.append(s);
    } else {
      System.out.print(s);
    }
  }


  /**
   *  Constructor for the myPrintln object
   *
   *@param  s  Description of the Parameter
   */
  void myPrintln(String s) {
    myPrint(s + "\n");
  }

  /*
   *  Description of the Method
   *
   *@param  argv  Description of the Parameter
  public static void main(String argv[]) {
    try {
      if (argv.length == 4) {
        BackupDir = new String(argv[3]);
      } else {
        BackupDir = new String("D:\\temp\\");
      }
      new BackUpAllSeapineFiles(argv[0], argv[1], argv[2], BackupDir, null);
    } catch (Exception err) {
      System.err.println("usage: java -jar BackUpAllSeapineFiles.jar SCMServerAddr:PortNum Username Password Backupdir");
      System.err.println("  * creates bat files in directory where command is executed");
      System.err.println("  * get all files hierarchically in Backupdir [default:D:\\temp] directory.");
      System.err.println("  * Backupdir D:\\zorg\\ name must be written as D:\\\\zorg\\\\ .");
      err.printStackTrace();
    }
  }*/
}


