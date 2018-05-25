import java.text.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 31, 2006
 */
class Cell extends XmlTag {
  Date date    = null;
  int counter  = 0; // used only by cells of 1st defect to hold counters


  /**
   *  Constructor for the Cell object
   *
   *@param  xt  Description of the Parameter
   */
  Cell(XmlTag xt) {
    tagName = new String(xt.tagName);
    if(xt.noTag != null) {
      noTag = new String(xt.noTag);
      SimpleDateFormat formatIn  = new SimpleDateFormat("MM/dd/yyyy");
      date = new Date();
      date = formatIn.parse(xt.noTag, new ParsePosition(0));
    }
  }


  /**  Constructor for the Cell object */
  Cell() { }


  /**
   *  Description of the Method
   *
   *@param  count  Description of the Parameter
   */
  void incCounter(int count) {
    counter += count;
  }


  /**
   *  Gets the bugState attribute of the Cell object
   *
   *@return    The bugState value
   */
  boolean isBugState() {
    return tagName.toLowerCase().startsWith("s_");
  }

  boolean isClosedState() {
    return (tagName.equals("s_closed") || tagName.equals("s_delayed"));
  }

  boolean isOpenState() {
    return (tagName.equals("s_entered") || tagName.equals("s_found") || tagName.equals("s_open") || tagName.equals("s_reopen"));
  }

  boolean isQAState() {
    return (tagName.equals("s_fixed") || tagName.equals("s_released") || tagName.equals("s_verified") || tagName.equals("s_rejected"));
  }


  /**
   *  Description of the Method
   *
   *@param  c  Description of the Parameter
   *@return    Description of the Return Value
   */
  boolean before(Cell c) {
    if(date == null) {
      return false;
    }
    if(c == null) {
      return true;
    }
    if(c.date == null) {
      return true;
    }
    return date.before(c.date);
  }


  /**
   *  Description of the Method
   *
   *@param  c              Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  boolean before2(Cell c) throws Exception {
    if(date == null) {
      return true;
    }
    if(c == null) {
      return true;
    }
    if(c.date == null) {
      return true;
    }
    if(print().equals(c.print())) {
      return true;
    }
    return date.before(c.date);
  }


  /**
   *  Description of the Method
   *
   *@param  now  Description of the Parameter
   *@return      Description of the Return Value
   */
  boolean before(Calendar now) {
    if(date == null) {
      return false;
    }
    Calendar cal  = Calendar.getInstance();
    cal.setTime(date);
    return cal.before(now);
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String toString2() {
    if(date != null) {
      SimpleDateFormat formatOut  = new SimpleDateFormat("ddMMMyyyy");
      return new String("[" + toString() + "," + formatOut.format(date) + "]");
    } else {
      return new String("[" + toString() + "]");
    }
  }


  /**
   *  Description of the Method
   *
   *@return    Description of the Return Value
   */
  String Counter2Xls() {
    return new String("<Cell ss:StyleID=\"Default\" ><Data ss:Type=\"Number\">" + counter + "</Data></Cell>\n");
  }


  /**
   *  Gets the counter attribute of the Cell object
   *
   *@return    The counter value
   */
  int getCounter() {
    return counter;
  }


  /**
   *  Description of the Method
   *
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String print() throws Exception {
    if(date != null) {
      SimpleDateFormat formatOut  = new SimpleDateFormat("ddMMMyyyy");
      /*if(cal == null) {
        throw new Exception("BUG!");
      }
      Date date2                  = cal.getTime();
      if(!date.equals(date2)) {
        throw new Exception("BUG2!");
      }*/
      return new String(formatOut.format(date));
    }
    return new String("invalid_date");
  }

}

