import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Cell extends XmlTag {
  Date date    = null;
 

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



  boolean isBugState() {
    return tagName.toLowerCase().startsWith("s_");
  }
  boolean isEvent() {
    return (tagName.toLowerCase().startsWith("s_") && !tagName.toLowerCase().startsWith("a_"));
  }

  boolean before(Cell c) throws Exception {
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


  boolean before(Calendar now) {
    if(date == null) {
      return false;
    }
    Calendar cal  = Calendar.getInstance();
    cal.setTime(date);
    return cal.before(now);
  }
  
	boolean after(Calendar now) {
		return !before(now);
	}

  
	boolean changedBetween(Calendar ago, Calendar now) throws Exception {
		return (before(now) && after(ago));
	}


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

