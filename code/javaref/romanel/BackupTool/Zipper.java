
import java.io.*; //
import java.util.*;
import java.util.zip.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 18, 2005
 */
public class Zipper extends Log {
  Vector m_fileNames;
  static boolean m_isWin95;
  JTextArea log             = null;


  /**
   *  Constructor for the Zipper object
   *
   *@param  log  Description of the Parameter
   */
  Zipper(JTextArea log) {
    m_fileNames = new Vector();
    this.log = log;
  }


  /**
   *  Constructor for the myPrint object
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


  /**
   *  Adds a feature to the Files attribute of the Zipper object
   *
   *@param  filespec         The feature to be added to the Files attribute
   *@exception  IOException  Description of the Exception
   */
  void addFilesFromDir(String filespec) throws IOException {
    File dir        = new File(filespec);
    String[] files  = dir.list();
    for (int i = 0; i < files.length; i++) {
      File tempf;
      dbg(true, "   -" + files[i]);

      tempf = new File(filespec + dir.separator + files[i]);
      if (tempf.isDirectory() == false) {
        m_fileNames.addElement(tempf.getCanonicalPath());
      }
    }
  }


  /**
   *  Adds a feature to the Files attribute of the Zipper object
   *
   *@param  filespec         The feature to be added to the Files attribute
   *@exception  IOException  Description of the Exception
   */
  void addFiles(String filespec) throws IOException {
    m_fileNames.addElement(filespec);
  }


  /**  Description of the Method */
  void removeDuplicates() {
    for (int i = 0; i < (m_fileNames.size() - 1); i++) {
      for (int j = i + 1; j < m_fileNames.size(); j++) {
        String s1  = (String) m_fileNames.elementAt(i);
        String s2  = (String) m_fileNames.elementAt(j);
        if (m_isWin95) {
          s1 = s1.toUpperCase();
          s2 = s2.toUpperCase();
        }
        if (s1.compareTo(s2) > 0) {
          Object temp  = m_fileNames.elementAt(i);
          m_fileNames.setElementAt(m_fileNames.elementAt(j), i);
          m_fileNames.setElementAt(temp, j);
        }
      }
    }
    for (int i = 0; i < (m_fileNames.size() - 1); ) {
      String s1  = new String((String) m_fileNames.elementAt(i));
      String s2  = new String((String) m_fileNames.elementAt(i + 1));
      if (m_isWin95) {
        s1 = s1.toUpperCase();
        s2 = s2.toUpperCase();
      }
      if (s1.equals(s2)) {
        m_fileNames.removeElementAt(i + 1);
      } else {
        i++;
      }
    }
  }


  /**
   *  Description of the Method
   *
   *@param  zip_file_name    Description of the Parameter
   *@exception  IOException  Description of the Exception
   */
  void create(String zip_file_name) throws IOException {
    File zip_file      = new File(zip_file_name);
    String full_name   = zip_file.getCanonicalPath();
    ZipOutputStream z  = new ZipOutputStream(new FileOutputStream(zip_file));
    for (int i = 0; i < m_fileNames.size(); i++) {
      File test         = new File((String) m_fileNames.elementAt(i));
      String test_name  = test.getCanonicalPath();
      boolean skip;
      if (m_isWin95) {
        skip = test_name.equalsIgnoreCase(full_name);
      } else {
        skip = test_name.equals(full_name);
      }
      if (skip) {
        myPrintln("Skipping " + zip_file_name);
      } else {
        add(z, (String) m_fileNames.elementAt(i));
      }
    }
    z.close();
  }


  /**
   *  Description of the Method
   *
   *@param  z                Description of the Parameter
   *@param  file_name        Description of the Parameter
   *@exception  IOException  Description of the Exception
   */
  protected void add(ZipOutputStream z, String file_name) throws IOException {
    String entry_name  = new String(file_name);
    if (m_isWin95) {
      if (entry_name.charAt(1) == ':') {
        entry_name = entry_name.substring(2);
      }
    }
    if (entry_name.charAt(0) == File.separatorChar) {
      entry_name = entry_name.substring(1);
    }
    ZipEntry entry     = new ZipEntry(entry_name.replace('\\', '/'));
    z.putNextEntry(entry);
    FileInputStream f  = new FileInputStream(file_name);
    myPrint(entry_name + " ");
    byte[] buf         = new byte[10240];
    for (int i = 0; ; i++) {
      int len  = f.read(buf);
      if (len < 0) {
        break;
      }
      z.write(buf, 0, len);
      switch (i & 3) {
        case 0:
          myPrint("|\b");
          break;
        case 1:
          myPrint("/\b");
          break;
        case 2:
          myPrint("-\b");
          break;
        default:
          myPrint("\\\b");
          break;
      }
    }
    z.closeEntry();
    myPrintln(" ");
  }
  {
    File temp  = new File("C:\\MSDOS.SYS");
    m_isWin95 = temp.exists();
  }
}


