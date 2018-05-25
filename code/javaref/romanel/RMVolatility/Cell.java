import java.text.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2006
 */
class Cell extends XmlTag {
	Date date = null;


	/**
	 *  Constructor for the Cell object
	 *
	 *@param  xt  Description of the Parameter
	 */
	Cell(XmlTag xt) {
		tagName = new String(xt.tagName);
		if (xt.noTag != null) {
			noTag = new String(xt.noTag);
			SimpleDateFormat formatIn = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			date = new Date();
			date = formatIn.parse(xt.noTag, new ParsePosition(0));
		}
	}


	/**
	 *  Constructor for the Cell object
	 *
	 *@param  s_date  Description of the Parameter
	 */
	void Cell2(String s_date) {
		tagName = new String("date");
		noTag = new String(s_date);
		SimpleDateFormat formatIn = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
		date = new Date();
		date = formatIn.parse(noTag, new ParsePosition(0));
	}


	/**
	 *  Constructor for the Cell object
	 */
	Cell() { }


	/**
	 *  Description of the Method
	 *
	 *@param  now            Description of the Parameter
	 *@param  ago            Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean changedBetween(Calendar ago, Calendar now) throws Exception {
		return (before(now) && after(ago));
	}


	/**
	 *  Gets the timeInMillis attribute of the Cell object
	 *
	 *@return    The timeInMillis value
	 */
	long getTimeInMillis() {
		if (date == null) {
			return 0;
		} else {
			return date.getTime();
		}

	}


	/**
	 *  Description of the Method
	 *
	 *@param  now  Description of the Parameter
	 *@return      Description of the Return Value
	 */
	boolean before(Calendar now) {
		if (date == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.before(now);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  now  Description of the Parameter
	 *@return      Description of the Return Value
	 */
	boolean after(Calendar now) {
		return !before(now);
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	String toString2() {
		if (date != null) {
			SimpleDateFormat formatOut = new SimpleDateFormat("ddMMMyyyy");
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
		if (date != null) {
			SimpleDateFormat formatOut = new SimpleDateFormat("ddMMMyyyy");
			/*
			 *  if(cal == null) {
			 *  throw new Exception("BUG!");
			 *  }
			 *  Date date2                  = cal.getTime();
			 *  if(!date.equals(date2)) {
			 *  throw new Exception("BUG2!");
			 *  }
			 */
			return new String(formatOut.format(date));
		}
		return new String("invalid_date");
	}

}

