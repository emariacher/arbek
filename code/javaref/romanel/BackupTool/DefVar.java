/**
 *  Description of the Interface
 *
 *@author     Eric Mariacher
 *@created    January 17, 2005
 */
public interface DefVar {
  final static String fileSeparator  = System.getProperty("file.separator");
  final static int MAXFILE           = 50;
  final static int INVALID           = 9999;
  final static int BRANCH_MAINLINE   = 101;
  final static int BRANCH_BASELINE   = 102;
  final static int BRANCH_SNAPSHOT   = 103;
  final static int BRANCH_WORKSPACE  = 104;
  final static int STATE_IDLE        = 201;
  final static int STATE_FILE        = 202;
}

