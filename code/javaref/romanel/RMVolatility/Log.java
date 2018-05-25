import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 19, 2006
 */
public class Log implements DefVar {
  JTextArea err             = null;
  JLabel LogLabel           = null;
  MesFichiers f             = new MesFichiers();
  int logfile               = INVALID;
  int errfile               = INVALID;
  String working_directory  = null;
  String name_no_ext        = null;


  /**
   *  Constructor for the Log object
   *
   *@param  err       Description of the Parameter
   *@param  LogLabel  Description of the Parameter
   */
  Log(JTextArea err, JLabel LogLabel) {
    this.err = err;
    this.LogLabel = LogLabel;
  }


  /**
   *  Constructor for the Log object
   *
   *@param  L  Description of the Parameter
   */
  Log(Log L) {
    this.err = L.err;
    this.LogLabel = L.LogLabel;
    this.f = L.f;
    this.logfile = L.logfile;
    this.errfile = L.errfile;
  }


  /**  Constructor for the Log object */
  Log() { }


  /**
   *  Description of the Method
   *
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String initLogFiles() throws Exception {
    logfile = f.addFichier(working_directory + "\\logfile_" + printToday("ddMMMyy_HH_mm") + ".log");
    errfile = f.addFichier(working_directory + "\\errfile_" + printToday("ddMMMyy_HH_mm") + ".log");
    System.err.println("initLogFiles():" + working_directory);
    return working_directory;
  }


  /**
   *  Description of the Method
   *
   *@param  fmt            Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printToday(String fmt) throws Exception {
    Calendar today  = Calendar.getInstance();
    return printZisday(today, fmt);
  }


  /**
   *  Description of the Method
   *
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printToday() throws Exception {
    return printToday("ddMMMyy");
  }


  /**
   *  Description of the Method
   *
   *@param  fmt            Description of the Parameter
   *@param  zisday         Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printZisday(Calendar zisday, String fmt) throws Exception {
    return printZisday(zisday.getTime(), fmt);
  }


  /**
   *  Description of the Method
   *
   *@param  date           Description of the Parameter
   *@param  fmt            Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printZisday(Date date, String fmt) throws Exception {
    SimpleDateFormat datefmt2  = new SimpleDateFormat(fmt);
    String sdate2              = new String(datefmt2.format(date));
    return sdate2;
  }


  /**
   *  Description of the Method
   *
   *@param  date           Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printZisday(Date date) throws Exception {
    return printZisday(date, "ddMMMyy");
  }


  /**
   *  Description of the Method
   *
   *@param  zisday         Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printZisday(Calendar zisday) throws Exception {
    return printZisday(zisday.getTime());
  }


  /**
   *  Description of the Method
   *
   *@param  in   Description of the Parameter
   *@param  out  Description of the Parameter
   */
  void copyCal(Calendar out, Calendar in) {
    out.setTime(in.getTime());
  }


  /**
   *  Description of the Method
   *
   *@param  filename       Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String knowPath(String filename) throws Exception {
    File fil              = new File(filename);
    String name           = fil.getName();
    String CanonicalPath  = fil.getCanonicalPath();
    int index             = CanonicalPath.indexOf(name);
    String ext            = Utils.getExtension(fil);
    if((ext != null) && (name.lastIndexOf(ext) > 1)) {
      name_no_ext = name.substring(0, name.lastIndexOf(ext) - 1);
    }
    working_directory = new String(CanonicalPath.substring(0, index));
    return working_directory;
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void closeLogFiles() throws Exception {
    f.cleanfos(logfile);
    logfile = INVALID;
    f.cleanfos(errfile);
    errfile = INVALID;
    myErrPrintln("log, err files closed.");
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void displayLogMsg(String s) throws Exception {
    if(LogLabel != null) {
      LogLabel.setText(s);
    }
    myPrintln(s);
  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myPrint(String s) throws Exception {
    if(logfile != INVALID) {
      f.writeFile(logfile, s);
    }
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
    if(err != null) {
      err.append(s);
      if(errfile != INVALID) {
        f.writeFile(errfile, s);
      }
      myPrint(s);
      if(logfile != INVALID) {
        f.writeFile(logfile, s);
      }
    } else {
      System.err.print(s);
    }
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
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void dbg(String s) {
    System.out.println(s);
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  s      Description of the Parameter
   */
  void dbg(boolean trace, String s) {
    if(trace) {
      dbg(s);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  s      Description of the Parameter
   *@param  n      Description of the Parameter
   */
  void dbg(boolean trace, String s, int n) {
    if(trace) {
      dbg(s, n);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  v      Description of the Parameter
   */
  void dbg(boolean trace, Vector v) {
    StringBuffer sb  = new StringBuffer();
    if(trace) {
      for(int i = 0; i < v.size(); i++) {
        sb.append("[" + i + "][" + ((String)v.elementAt(i)) + "] ,");
      }
      dbg(sb.toString());
    }
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  s      Description of the Parameter
   *@param  v      Description of the Parameter
   */
  void dbg(boolean trace, String s, Vector v) {
    StringBuffer sb  = new StringBuffer();
    if(trace) {
      sb.append("--[" + s + "]");
      for(int i = 0; i < v.size(); i++) {
        sb.append("[" + i + "][" + ((String)v.elementAt(i)) + "] ,");
      }
      dbg(sb.toString());
    }
  }


  /**
   *  Description of the Method
   *
   *@param  p  Description of the Parameter
   *@param  s  Description of the Parameter
   */
  void dbg(String p, String s) {
    System.out.println(p + "[" + s + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  p      Description of the Parameter
   *@param  s      Description of the Parameter
   */
  void dbg(boolean trace, String p, String s) {
    if(trace) {
      dbg(p, s);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  p  Description of the Parameter
   *@param  s  Description of the Parameter
   *@param  n  Description of the Parameter
   */
  void dbg(String p, String s, int n) {
    System.out.println(p + "[" + s + "][" + n + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  n  Description of the Parameter
   *@param  s  Description of the Parameter
   */
  void dbg(int n, String s) {
    System.out.println("[" + n + "][" + s + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   *@param  n  Description of the Parameter
   */
  void dbg(String s, int n) {
    System.out.println("[" + s + "][" + n + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  n      Description of the Parameter
   *@param  s      Description of the Parameter
   */
  void dbg(boolean trace, int n, String s) {
    if((trace) && (s.length() > 0)) {
      dbg(n, s);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  n  Description of the Parameter
   *@param  i  Description of the Parameter
   */
  void dbg(int n, int i) {
    System.out.println("[" + n + "][" + i + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  p      Description of the Parameter
   *@param  s      Description of the Parameter
   *@param  n      Description of the Parameter
   */
  void dbg(boolean trace, String p, String s, int n) {
    if(trace) {
      dbg(p, s, n);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  n  Description of the Parameter
   *@param  s  Description of the Parameter
   *@param  t  Description of the Parameter
   *@param  r  Description of the Parameter
   */
  void dbg(int n, String r, String s, String t) {
    System.out.println("[[" + n + "][" + r + "=" + s + "]] vs " + t);
  }


  /**
   *  Adds a feature to the XmlHeaderAndFooter attribute of the Log object
   *
   *@param  i              The feature to be added to the XmlHeaderAndFooter
   *      attribute
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  int addXmlHeaderAndFooter(File i) throws Exception {
    BufferedReader inputf  = new BufferedReader(new FileReader(i));
    int fileIndex          = f.addFichier();
    String linef;
    f.writeFile(fileIndex, "<eric_mariacher>");
    while((linef = inputf.readLine()) != null) {
      f.writeFile(fileIndex, linef.replaceAll("&", "&amp;") + "\n");
    }
    f.writeFile(fileIndex, "</eric_mariacher>");
    f.cleanfos(fileIndex);
    return fileIndex;
  }

}

