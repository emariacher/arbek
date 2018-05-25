import java.io.*;
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 17, 2005
 */
public class ProcessBat extends Log {
  MesFichiers f;


  /**
   *  Constructor for the ProcessBat object
   *
   *@param  f  Description of the Parameter
   */
  ProcessBat(MesFichiers f) {
    this.f = f;
  }


  /**
   *  Description of the Method
   *
   *@param  cmdline        Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  BufferedReader BufferedReader(String cmdline) throws Exception {
    dbg(cmdline);
    Process p  = Runtime.getRuntime().exec(cmdline);
    return new BufferedReader(new InputStreamReader(p.getInputStream()));
  }


  /**
   *  Description of the Method
   *
   *@param  cmdline  Description of the Parameter
   *@param  userid   Description of the Parameter
   *@param  pwd      Description of the Parameter
   *@return          Description of the Return Value
   */
  BufferedReader BuildAndExecuteBat(String cmdline, String userid, String pwd) throws Exception {
    int batfile  = f.addFichier();
    f.writeFile(batfile, cmdline);
    f.cleanfos(batfile);
    return BufferedReader(f.getFileName(batfile) + " " + userid + " " + pwd);
  }
}


