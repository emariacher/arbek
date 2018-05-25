import java.text.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    February 18, 2008
 */
class Baseline extends Cell {
	String type = null;


	/**
	 *  Constructor for the Baseline object
	 *
	 *@param  xt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	Baseline(XmlTag xt) throws Exception {
		XmlTag2(xt);
		Cell2(getAttributeValue("date"));
		type = find("type").noTag;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  bs  Description of the Parameter
	 *@return     Description of the Return Value
	 */
	boolean is(Baseline bs) {
		if (getAttributeValue("date").compareTo(bs.getAttributeValue("date")) == 0) {
			return true;
		}
		return false;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  testValue  Description of the Parameter
	 *@return            Description of the Return Value
	 */
	boolean type(String testValue) {
		if (type.compareTo(testValue) == 0) {
			return true;
		}
		return false;
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	String toString2() {
		return new String("*" + find("type").noTag + "*" + getAttributeValue("date") + "*[" + getAttributeValue("name") + "]");
	}


	/**
	 *  Gets the name attribute of the Baseline object
	 *
	 *@return    The name value
	 */
	String getName() {
		return new String(getAttributeValue("name"));
	}


	/**
	 *  Description of the Method
	 *
	 *@param  other  Description of the Parameter
	 *@return        Description of the Return Value
	 */
	public int compare(
		Object other) {
		Baseline b = (Baseline) other;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar bcal = Calendar.getInstance();
		bcal.setTime(b.date);
		if (cal.after(bcal)) {
			return 1;
		}
		if (cal.before(bcal)) {
			return -1;
		}
		return 0;
	}

}

