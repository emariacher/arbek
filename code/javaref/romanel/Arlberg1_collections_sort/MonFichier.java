
import java.io.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 9, 2004
 */
public class MonFichier {
  FileOutputStream fos;
  File fil;
  File dir;
  String nom            = null;
  String CanonicalPath  = null;


  /**
   *  Constructor for the MonFichier object
   *
   *@param  nom            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  MonFichier(String nom) throws Exception {
    int index  = nom.lastIndexOf("\\");
    if(index > 0) {
      CanonicalPath = nom.substring(0, index);
    }
    if(CanonicalPath != null) {
      if(CanonicalPath.length() != 0) {
        dir = new File(CanonicalPath);
        dir.mkdirs();
      }
    }
    fil = new File(nom);
    try {
    	fos = new FileOutputStream(fil);
    } catch(FileNotFoundException e) {
    	throw new Exception("Cannot write to file2[" +
    	          fil.getCanonicalPath() + "]");
    }
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
    while((b = fis.read()) != -1) {
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
    System.out.println("Fichier " + fil.getName() + " generated.");
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void writeFile(String s) {
    try {
      fos.write(s.getBytes());
    } catch(Exception e) {
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
    String name  = fil.getName();
    CanonicalPath = fil.getCanonicalPath();
    int index    = CanonicalPath.indexOf(name);
    return CanonicalPath.substring(0, index);
  }

}

