import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 26, 2006
 */
public class MesFichiers implements DefVar {
  MonFichier[] file        = new MonFichier[MAXFILE];
  int fileCpt              = 0;
  String path              = null;
  String zeFileName        = null;
  private String rootName;
  private String extName;


  /**
   *  Sets the rootName attribute of the TableTache object
   *
   *@param  file_name  The new rootName value
   */
  void setRootName(String file_name) {
    rootName = new String(file_name.substring(0, file_name.indexOf(".")));
    extName = new String(file_name.substring(file_name.indexOf(".")));
  }


  /**
   *  Gets the rootName attribute of the TableTache object
   *
   *@return    The rootName value
   */
  String getRootName() {
    return rootName;
  }


  /**
   *  Gets the extName attribute of the TableTache object
   *
   *@return    The extName value
   */
  String getExtName() {
    return extName;
  }


  /**
   *  Description of the Method
   *
   *@param  fos            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public void cleanfos(int fos) throws Exception {
    file[fos].clean();
  }


  /**
   *  Gets the name attribute of the MesFichiers object
   *
   *@param  fos            Description of the Parameter
   *@return                The name value
   *@exception  Exception  Description of the Exception
   */
  public String getName(int fos) throws Exception {
    return file[fos].fil.getName();
  }


  /**
   *  Adds a feature to the Fichier attribute of the TableTache object
   *
   *@param  nom            The feature to be added to the Fichier attribute
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  public int addFichier(String nom) throws Exception {
    file[fileCpt] = new MonFichier(nom);
    if(!file[fileCpt].fil.canWrite()) {
      throw new Exception("Cannot write to file[" +
          file[fileCpt].fil.getCanonicalPath() + "]");
    }
    zeFileName = file[fileCpt].fil.getName();
    path = file[fileCpt].getPath();
    int zab  = fileCpt;
    fileCpt++;
    return zab;
  }


  /**
   *  Adds a feature to the Fichier attribute of the TableTache object
   *
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  public int addFichier() throws Exception {
    return addFichier("zob" + fileCpt);
  }


  /**
   *  Description of the Method
   *
   *@param  index          Description of the Parameter
   *@param  ofs            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public void catdel(int index, FileOutputStream ofs) throws Exception {
    if(index != INVALID) {
      file[index].catdel(ofs);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  index          Description of the Parameter
   *@param  indexOut       Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public void cat(int index, int indexOut) throws Exception {
    try {
      if((index != INVALID) && (indexOut != INVALID)) {
        file[index].cat(file[indexOut]);
      }
    } catch(Exception e) {
      System.err.println("index=" + index + ", indexOut=" + indexOut + ". ");
      throw e;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  index          Description of the Parameter
   *@param  s              Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  public int writeFile(int index, String s) throws Exception {
    try {
      file[index].writeFile(s);
    } catch(Exception e) {
      System.err.println("index=" + index + ".");
      throw e;
    }
    return s.length();
  }


  /**
   *  Gets the file attribute of the MesFichiers object
   *
   *@param  index          Description of the Parameter
   *@return                The file value
   *@exception  Exception  Description of the Exception
   */
  public File getFile(int index) throws Exception {
    return file[index].fil;
  }

}

