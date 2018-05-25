
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 13, 2005
 */
class FileVersionLabel {
  String filename;
  String sVersion;
  int iVersion;
  String label;


  /**
   *  Constructor for the FileVersionLabel object
   *
   *@param  filename  Description of the Parameter
   *@param  sVersion  Description of the Parameter
   *@param  label     Description of the Parameter
   */
  FileVersionLabel(String filename, String sVersion, String label) {
    this.filename = filename;
    this.sVersion = sVersion;
    iVersion = Integer.valueOf(sVersion).intValue();
    this.label = label;
  }


  /**
   *  Constructor for the FileVersionLabel object
   *
   *@param  filename  Description of the Parameter
   *@param  iVersion  Description of the Parameter
   *@param  label     Description of the Parameter
   */
  FileVersionLabel(String filename, int iVersion, String label) {
    this.filename = filename;
    sVersion = (new String(iVersion + " ")).trim();
    this.iVersion = iVersion;
    this.label = label;
  }


  /**
   *  Description of the Method
   *
   *@param  name  Description of the Parameter
   *@return       Description of the Return Value
   */
  boolean matchName(String name) {
    return filename.equals(name);
  }


  /**
   *  Description of the Method
   *
   *@param  fvl  Description of the Parameter
   *@return      Description of the Return Value
   */
  boolean matchName(FileVersionLabel fvl) {
    return filename.equals(fvl.filename);
  }


  /**
   *  Description of the Method
   *
   *@param  obj  Description of the Parameter
   *@return      Description of the Return Value boolean equals(FileVersionLabel
   *      fvl) { System.err.println(" " + toString() + " ? " + fvl.toString());
   *      return (filename.equals(fvl.filename) && label.equals(fvl.label) &&
   *      (iVersion == fvl.iVersion)); }
   */

  /**
   *  Description of the Method
   *
   *@param  obj  Description of the Parameter
   *@return      Description of the Return Value
   */
  public boolean equals(Object obj) {
    FileVersionLabel fvl  = (FileVersionLabel) obj;
    System.err.println("    " + toString() + " ? " + fvl.toString());
    return (filename.equals(fvl.filename) && label.equals(fvl.label) && (iVersion == fvl.iVersion));
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    return new String(filename + ", " + iVersion + ", " + sVersion + ", " + label);
  }

}

