import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    September 14, 2004
 */
public class MesFichiers implements DefVar {
  List file                = new ArrayList();
  String path              = null;
  String zeFileName        = null;
  String defaultdir        = null;
  private String rootName;
  private String extName;


  /**
   *  Constructor for the MesFichiers object
   *
   *@param  defaultdir  Description of the Parameter
   */
  MesFichiers(String defaultdir) {
    this.defaultdir = defaultdir;
  }


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
   *  Gets the aScriptFile attribute of the TableTache object
   *
   *@return    The aScriptFile value
   */
  boolean isAScriptFile() {
    return extName.equals(".scr");
  }


  /**  Description of the Method */
  void forceNotAScriptFile() {
    extName = new String("force_htm");
  }



  /**
   *  Description of the Method
   *
   *@param  fos            Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  public void cleanfos(int fos) throws Exception {
    ((MonFichier) file.get(fos)).clean();
  }


  /**
   *  Adds a feature to the Fichier attribute of the TableTache object
   *
   *@param  nom            The feature to be added to the Fichier attribute
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  public int addFichier(String nom) throws Exception {
    file.add(new MonFichier(nom));
    return (file.size() - 1);
  }


  /**
   *  Adds a feature to the Fichier attribute of the TableTache object
   *
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  public int addFichier() throws Exception {
    return addFichier(defaultdir + "seapinebackup" + file.size() + ".bat");
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
      ((MonFichier) file.get(index)).writeFile(s);
    } catch (Exception e) {
      System.err.println("index=" + index + ".");
      throw e;
    }
    return s.length();
  }


  /**
   *  Gets the fileName attribute of the MesFichiers object
   *
   *@param  index          Description of the Parameter
   *@return                The fileName value
   *@exception  Exception  Description of the Exception
   */
  String getFileName(int index) throws Exception {
    return new String(((MonFichier) file.get(index)).getPath() + ((MonFichier) file.get(index)).fil.getName());
  }
}

