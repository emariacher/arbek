/**
 *  Description of the Interface
 *
 *@author     Eric Mariacher
 *@created    December 15, 2004
 */
public interface DefVar {
  final static String fileSeparator      = System.getProperty("file.separator");
  final static int MAXFILE               = 50;
  final static int INVALID               = 9999;
  final static int STATUS_NEW            = 201;
  final static int STATUS_OPEN           = 202;
  final static int STATUS_REJECTED       = 203;
  final static int STATUS_FIXED          = 204;
  final static int STATUS_RELEASED       = 205;
  final static int STATUS_VERIFIED       = 206;
  final static int STATUS_CLOSED         = 207;
  final static int BUILDMODE_BUGBASED    = 301;
  final static int BUILDMODE_LABELBASED  = 302;
}

