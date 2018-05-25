public interface DefVar {
  final static String fileSeparator        = System.getProperty("file.separator");
  final static int MAXFILE                 = 50;
  final static int MAXHIERARCHYXML         = 20;
  final static int INVALID                 = 9999;
  final static int STATUS_UNBORN           = 200;
  final static int STATUS_NEW              = 201;
  final static int STATUS_OPEN             = 202;
  final static int STATUS_REJECTED         = 203;
  final static int STATUS_FIXED            = 204;
  final static int STATUS_RELEASED         = 205;
  final static int STATUS_VERIFIED         = 206;
  final static int STATUS_CLOSED           = 207;
  final static int REPORT_ALL_STATES       = 301;
  final static int REPORT_SIMPLIFIED       = 302;
  final static int REPORT_SIMPLIFIED_PRIO  = 303;
  final static int REPORT_SIMPLIFIED_DEV   = 304;
  final static int REPORT_SIMPLIFIED_PQA   = 305;
  final static int NUMBER_OF_PRIO          = 4; // priority starting at 1
  final static int REPORT_FUNC             = 401;
  final static int REPORT_USERX            = 402;
  final static int REPORT_IMPR             = 403;
  final static int REPORT_STACKED_CURVE    = 501;
  final static int REPORT_RADAR            = 502;
}

