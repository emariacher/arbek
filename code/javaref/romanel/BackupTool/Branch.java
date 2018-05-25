import java.io.*;
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 17, 2005
 */
public class Branch extends Log {
  String mainline;
  String name;
  int type;
  ProcessBat pb;


  /**
   *  Constructor for the Branch object
   *
   *@param  mainline       Description of the Parameter
   *@param  result         Description of the Parameter
   *@param  f              Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  Branch(String mainline, String result, MesFichiers f) throws Exception {
    ProcessBat pb  = new ProcessBat(f);
    this.mainline = mainline;
    if (result.indexOf(" (") < 0) {
      throw new Exception("\n\n*****" + mainline + "******\n\n");
    }
    name = new String(result.substring(0, result.indexOf(" (")));
    if (result.indexOf("(mainline)") > 0) {
      type = BRANCH_MAINLINE;
    } else if (result.indexOf("(baseline)") > 0) {
      type = BRANCH_BASELINE;
    } else if (result.indexOf("(snapshot)") > 0) {
      type = BRANCH_SNAPSHOT;
    } else {
      throw new Exception("invalid branch type[" + type + "]");
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  public String toString() {
    return new String("Branch[" + name + "], Mainline[" + mainline + "], Type[" + type + "]");
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String backupDirectory() {
    return new String(mainline + "\\" + name);
  }
}


