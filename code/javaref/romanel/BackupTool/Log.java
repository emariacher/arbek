import java.io.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    December 1, 2004
 */
public class Log implements DefVar {

  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   */
  void dbg(String s) {
    System.out.println(s);
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  s      Description of the Parameter
   */
  void dbg(boolean trace, String s) {
    if (trace) {
      dbg(s);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  s      Description of the Parameter
   *@param  n      Description of the Parameter
   */
  void dbg(boolean trace, String s, int n) {
    if (trace) {
      dbg(s, n);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  v      Description of the Parameter
   */
  void dbg(boolean trace, Vector v) {
    StringBuffer sb  = new StringBuffer();
    if (trace) {
      for (int i = 0; i < v.size(); i++) {
        sb.append("[" + i + "][" + ((String) v.elementAt(i)) + "] ,");
      }
      dbg(sb.toString());
    }
  }


  /**
   *  Description of the Method
   *
   *@param  p  Description of the Parameter
   *@param  s  Description of the Parameter
   */
  void dbg(String p, String s) {
    System.out.println(p + "[" + s + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  p      Description of the Parameter
   *@param  s      Description of the Parameter
   */
  void dbg(boolean trace, String p, String s) {
    if (trace) {
      dbg(p, s);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  p  Description of the Parameter
   *@param  s  Description of the Parameter
   *@param  n  Description of the Parameter
   */
  void dbg(String p, String s, int n) {
    System.out.println(p + "[" + s + "][" + n + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  n  Description of the Parameter
   *@param  s  Description of the Parameter
   */
  void dbg(int n, String s) {
    System.out.println("[" + n + "][" + s + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  s  Description of the Parameter
   *@param  n  Description of the Parameter
   */
  void dbg(String s, int n) {
    System.out.println("[" + s + "][" + n + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  n      Description of the Parameter
   *@param  s      Description of the Parameter
   */
  void dbg(boolean trace, int n, String s) {
    if ((trace) && (s.length() > 0)) {
      dbg(n, s);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  n  Description of the Parameter
   *@param  i  Description of the Parameter
   */
  void dbg(int n, int i) {
    System.out.println("[" + n + "][" + i + "]");
  }


  /**
   *  Description of the Method
   *
   *@param  trace  Description of the Parameter
   *@param  p      Description of the Parameter
   *@param  s      Description of the Parameter
   *@param  n      Description of the Parameter
   */
  void dbg(boolean trace, String p, String s, int n) {
    if (trace) {
      dbg(p, s, n);
    }
  }

}

