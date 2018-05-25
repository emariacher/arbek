
import java.io.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    September 14, 2004
 */
public class MonFichier implements DefVar {
  FileOutputStream fos;
  File fil;
  String nom;


  /**
   *  Constructor for the MonFichier object
   *
   *@param  nom            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  MonFichier(String nom) throws Exception {
    fil = new File(nom);
    fos = new FileOutputStream(fil);
    this.nom = nom;
  }


  /**
   *  Description of the Method
   *
   *@param  ofs            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public void catdel(FileOutputStream ofs) throws Exception {
    cat(ofs);
    clean();
    fil.delete();
  }


  /**
   *  Description of the Method
   *
   *@param  mf             Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public void cat(MonFichier mf) throws Exception {
    cat(mf.fos);
  }


  /**
   *  Description of the Method
   *
   *@param  ofs            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public void cat(FileOutputStream ofs) throws Exception {
    int b;
    FileInputStream fis  = new FileInputStream(fil);
    while ((b = fis.read()) != -1) {
      ofs.write(b);
    }
    fis.close();
  }


  /**
   *  Description of the Method
   *
   *@exception  Exception  Description of the Exception
   */
  public void clean() throws Exception {
    fos.close();
    System.out.println("Fichier " + fil.getName() + " genere.");
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void writeFile(String s) {
    try {
      fos.write(s.getBytes());
    } catch (Exception e) {
      System.err.println(e + " " + nom);
      e.printStackTrace();
    }
  }


  /**
   *  Gets the path attribute of the MonFichier object
   *
   *@return                The path value
   *@exception  Exception  Description of the Exception
   */
  String getPath() throws Exception {
    String name           = fil.getName();
    String CanonicalPath  = fil.getCanonicalPath();
    int index             = CanonicalPath.indexOf(name);
    return CanonicalPath.substring(0, index);
  }


  /**
   *  Description of the Method
   *
   *@param  fileName       Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  public boolean olderThan(String fileName) throws Exception {
    File zeFile  = new File(fileName);
    System.out.println(zeFile.getName() + ", " + zeFile.lastModified() + " vs " + fil.getName() + ", " + fil.lastModified());
    if (zeFile.lastModified() > fil.lastModified()) {
      //.out.printf("older %d.\n", zeFile.lastModified() - fil.lastModified());
      return true;
    } else {
      //out.printf("newer %d.\n", zeFile.lastModified() - fil.lastModified());
      return false;
    }
  }
}

