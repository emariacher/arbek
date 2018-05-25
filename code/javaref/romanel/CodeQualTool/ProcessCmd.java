import java.io.*;
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    March 14, 2005
 */
public class ProcessCmd {
  /**  Constructor for the ProcessCmd object */
  ProcessCmd() { }


  /**
   *  Description of the Method
   *
   *@param  cmdline        Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  BufferedReader ExecuteCmd(String cmdline) throws Exception {
    Process p  = Runtime.getRuntime().exec(cmdline);
    return new BufferedReader(new InputStreamReader(p.getInputStream()));
  }
}


