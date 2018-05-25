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
 *@created    April 15, 2005
 */
public class BuildDefineTable extends Thread {
  JTextArea log, err;
  CodeQualTool zt;
  ProcessCmd prcscmd;
  java.util.List files             = new ArrayList();
  java.util.List defines_diese_if  = new ArrayList();
  int logfile;
  int errfile;
  int xlsfile;
  MesFichiers f                    = new MesFichiers();
  BufferedReader inputf;


  /**
   *  Constructor for the BuildDefineTable object
   *
   *@param  zt  Description of the Parameter
   */
  BuildDefineTable(CodeQualTool zt) {
    this.zt = zt;
    this.log = zt.log;
    this.err = zt.err;
    prcscmd = new ProcessCmd();
    start();
  }


  /**
   *  Constructor for the add object
   *
   *@param  def            Description of the Parameter
   *@param  af             Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void add(AFile af, String def) throws Exception {
    myPrintln("  " + def);
    String zdef  = new String(def.replace('(', ' ').replace(')', ' ').replace('!', ' '));
    if (zdef.indexOf("/") > 0) {
      zdef = zdef.substring(0, zdef.indexOf("/"));
    }
    while (zdef.indexOf("|") > 0) {
      String z2def  = new String(zdef.substring(0, zdef.indexOf("|")));
      add1dDefine(af, z2def);
      zdef = zdef.substring(zdef.indexOf("|") + 1);
    }
    add1dDefine(af, zdef);
  }


  /**
   *  Description of the Method
   *
   *@param  def            Description of the Parameter
   *@param  af             Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void add1dDefine(AFile af, String def) throws Exception {
    myPrintln("    " + def);
    ADefine ad  = new ADefine(def.trim());
    int u       = defines_diese_if.indexOf(ad);
    if (u >= 0) {
      ad = (ADefine) defines_diese_if.get(u);
    } else {
      defines_diese_if.add(ad);
    }
    ad.addFile(af);
    af.addDefine(ad);
    myPrintln(ad.toString());
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void listSourceFiles() throws Exception {
    File trunkdir  = new File(zt.jtfDirectory.getText());
    if (trunkdir.isDirectory()) {
      myPrintln(trunkdir + " is a directory.");
      File scefile[]  = trunkdir.listFiles(new SourceCodeFileFilter());
      for (int i = 0; i < Array.getLength(scefile); i++) {
        AFile af  = new AFile(scefile[i]);
        files.add(af);
      }
      for (ListIterator li = files.listIterator(); li.hasNext(); ) {
        AFile af  = (AFile) li.next();
      }
    } else {
      throw new Exception(trunkdir + " is not a directory.");
    }
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void listdiese_if() throws Exception {
    String diese_if    = new String("\\p{Blank}*#if[\\p{Blank}|(]");
    String diese_elif  = new String("\\p{Blank}*#elif[\\p{Blank}|(]");
    for (ListIterator li = files.listIterator(); li.hasNext(); ) {
      AFile af      = (AFile) li.next();
      zt.jlblAnalyze.setText(af.name);
      myPrintln(af.name);
      inputf = new BufferedReader(new FileReader(af.f));
      String linef;
      while ((linef = inputf.readLine()) != null) {
        if (linef.matches(diese_if + ".*")) {
          add(af, linef.replaceFirst(diese_if, "").trim());
        } else if (linef.matches(diese_elif + ".*")) {
          add(af, linef.replaceFirst(diese_elif, "").trim());
        }
      }
      inputf.close();

    }
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void generateReport() throws Exception {
    Collections.sort(defines_diese_if);
    int cpt  = 0;
    for (ListIterator li = defines_diese_if.listIterator(); li.hasNext(); ) {
      ADefine ad  = (ADefine) li.next();
      myErrPrintln(ad.toString());
      cpt++;
    }
    myErrPrintln(cpt + " Define entries*******************************");
    cpt = 0;
    for (ListIterator li = files.listIterator(); li.hasNext(); ) {
      AFile af  = (AFile) li.next();
      myErrPrintln(af.toString());
      cpt++;
    }
    myErrPrintln(cpt + " File entries*******************************");
  }


  /**  Description of the Method */
  public void run() {
    try {
      initLogFiles();

          /// list all C source code files
      listSourceFiles();
          /// parse files: list all defines referenced with #diese_if
      listdiese_if();
          /// generate report
      generateReport();
      generatexmlxlsfile();

      closeLogFiles();
      zt.jlblAnalyze.setText("Analyzed!");
    } catch (Exception e) {
      zt.jlblAnalyze.setText("*******Errors: not Built!********");
      try {
        PipedInputStream piErr   = new PipedInputStream();
        PipedOutputStream poErr  = new PipedOutputStream(piErr);
        System.setErr(new PrintStream(poErr, true));
        e.printStackTrace();
        myErrPrintln(e.toString());
        int len                  = 0;
        byte[] buf               = new byte[1024];
        while (true) {
          len = piErr.read(buf);
          if (len == -1) {
            break;
          }
          err.append(new String(buf, 0, len));
        }
      } catch (Exception ze) {}
    }

  }


  /**
   *  Description of the Method
   *
   *@param  s              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void myPrint(String s) throws Exception {
    if (log != null) {
      log.append(s);
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
    if (err != null) {
      err.append(s);
      f.writeFile(errfile, s);
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
  void myErrPrintln(String s) throws Exception {
    myErrPrint(s + "\n");
  }



  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void initLogFiles() throws Exception {
    Calendar c                   = Calendar.getInstance();
    Date date                    = c.getTime();
    SimpleDateFormat datefmt     = new SimpleDateFormat("ddMMMyy_HH_mm");
    SimpleDateFormat datefmtday  = new SimpleDateFormat("ddMMMyy");
    String sdate                 = new String(datefmt.format(date));
    String sdateday              = new String(datefmtday.format(date));
    logfile = f.addFichier("logfile_" + sdate + ".log");
    errfile = f.addFichier("errfile_" + sdate + ".log");
    xlsfile = f.addFichier("xls_" + sdateday + ".xml");
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void closeLogFiles() throws Exception {
    f.cleanfos(logfile);
    f.cleanfos(errfile);
    f.cleanfos(xlsfile);
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  void generatexmlxlsfile() throws Exception {
    f.writeFile(xlsfile, "<?xml version=\"1.0\"?><?mso-application progid=\"Excel.Sheet\"?>\n<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
    f.writeFile(xlsfile, " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
    f.writeFile(xlsfile, " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">\n");
    f.writeFile(xlsfile, " <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">");
    f.writeFile(xlsfile, " <Title>Define Matrix</Title>");
    f.writeFile(xlsfile, " <Subject>Define Matrix</Subject>");
    f.writeFile(xlsfile, " <Author>Eric Mariacher</Author>");
    f.writeFile(xlsfile, " </DocumentProperties>\n");
    f.writeFile(xlsfile, "<Worksheet  ss:Name=\"Define_Matrix\"><Table>\n");
          //header row
    f.writeFile(xlsfile, "<Row>\n");
    f.writeFile(xlsfile, "<Cell/>\n");
    for (ListIterator li = defines_diese_if.listIterator(); li.hasNext(); ) {
      ADefine ad  = (ADefine) li.next();
      f.writeFile(xlsfile, "<Cell><Data ss:Type=\"String\">" + ad.define + "</Data></Cell>");
    }
    f.writeFile(xlsfile, "</Row>\n");
    for (ListIterator li = files.listIterator(); li.hasNext(); ) {
      AFile af  = (AFile) li.next();
      f.writeFile(xlsfile, "<Row><Cell><Data ss:Type=\"String\">" + af.name + "</Data></Cell>");
      for (ListIterator lj = defines_diese_if.listIterator(); lj.hasNext(); ) {
        ADefine ad  = (ADefine) lj.next();
        if (af.d.contains(ad)) {
          f.writeFile(xlsfile, "<Cell><Data ss:Type=\"String\">X</Data></Cell>");
        } else {
          f.writeFile(xlsfile, "<Cell/>\n");
        }
      }
      f.writeFile(xlsfile, "</Row>\n");
    }
    f.writeFile(xlsfile, "</Table></Worksheet></Workbook>\n");
  }

}

