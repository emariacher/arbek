import java.text.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    May 3, 2005
 */
class Defect extends XmlTag {
	ChildList cells = new ChildList();
	ChildList counters = new ChildList();
	UtilsDate UD = new UtilsDate();
	// used only by 1st defect to hold counters
	SeapineStat zt;
	int numCounter;
	Log L;


	/**
	 *  Constructor for the DataRow object
	 *
	 *@param  xt             Description of the Parameter
	 *@param  zt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	Defect(XmlTag xt, SeapineStat zt) throws Exception {
		this.zt = zt;
		L = zt.L;
		for (int i = 0; i < xt.cl.size(); i++) {
			Cell c = new Cell((XmlTag) xt.cl.elementAt(i));
			cells.add(c);
			counters.add(c);
		}
		numCounter = xt.cl.size();
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("<Row" + cells.size() + "/" + cl.size() + "/" + counters.size() + ">");
		for (int i = 0; i < cells.size(); i++) {
			Cell c = (Cell) cells.elementAt(i);
			sb.append(c.toString());
		}
		sb.append("</Row>\n");
		return sb.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String toString2() throws Exception {
		StringBuffer sb = new StringBuffer("\n<Row" + cells.size() + "/" + cl.size() + "/" + counters.size() + ">");
		for (int i = 0; i < cells.size(); i++) {
			Cell c = (Cell) cells.elementAt(i);
			sb.append("[" + c.toString2());
			//L.myPrintln("***********" + sb.toString() + "***********");
			sb.append(c.print() + "]");
		}
		sb.append("</Row>\n");
		return sb.toString();
	}


	/**
	 *  Gets the defectNumber attribute of the Defect object
	 *
	 *@return    The defectNumber value
	 */
	String getDefectNumber() {
		Cell defnum = (Cell) cells.elementAt(0);
		//return new String(defnum.tagName + ":" + defnum.noTag);
		return new String(defnum.noTag);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  tagNameFilter  Description of the Parameter
	 *@param  noTagFilter    Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean passFilter(String tagNameFilter, String noTagFilter) throws Exception {
		if ((tagNameFilter == null) || (noTagFilter == null)) {
			return true;
		}
		XmlTag xt = cells.find(tagNameFilter, noTagFilter);
		//L.myPrintln("              +++++" + tagNameFilter + ", " + noTagFilter + ", " + xt);
		if (xt == null) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  product        Description of the Parameter
	 *@param  allowedTypes   Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean passFilter2(String product, String allowedTypes) throws Exception {
		if (product == null) {
			return true;
		} else if (!passFilter("product", product)) {
			return false;
		} else if (allowedTypes == null) {
			return true;
		}
		XmlTag xt = cells.find("type");
		if (xt == null) {
			return false;
		} else if (allowedTypes.indexOf(xt.noTag) >= 0) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 *  Gets the state attribute of the Defect object
	 *
	 *@param  now            Description of the Parameter
	 *@param  trace          Description of the Parameter
	 *@return                The state value
	 *@exception  Exception  Description of the Exception
	 */
	int getState(Calendar now, boolean trace) throws Exception {
		int state = 0;
		//L.myPrintln("**********[" + getDefectNumber() + "] " + UD.print(now));
		Cell czob = new Cell();
		for (int i = 0; i < cells.size(); i++) {
			Cell c = (Cell) cells.elementAt(i);
			if (c.before(now) && czob.before2(c)) {
				state = i;
				if (i != 0) {
					czob = (Cell) cells.elementAt(i);
				}
			}
			if (trace) {
				L.myPrintln("    " + UD.print(now) + " vs " + UD.print(c.date) +
					" -> " + c.before(now) + " [" + state + "] " + c.toString().trim() + ".");
				L.myPrintln("      " + UD.print(czob.date) +
					" [" + state + "] " + czob.toString().trim() + ".");
			}
		}
		return state;
	}


	/**
	 *  Gets the priority attribute of the Defect object
	 *
	 *@return                The priority value
	 *@exception  Exception  Description of the Exception
	 */
	int getPriority() throws Exception {
		XmlTag xt_prio = cells.find("priority");
		if (xt_prio == null) {
			throw new Exception("\nnull XmlTag " + toString2());
		}
		String s_prio = xt_prio.noTag;
		if (s_prio == null) {
			return 3;
			//throw new Exception("\nnull noTag " + toString2());
		}
		return Integer.valueOf(s_prio.substring(0, 1)).intValue()-1;
	}


	/**
	 *  Description of the Method
	 */
	void resetCounters() {
		for (int countIndex = 0; countIndex < counters.size(); countIndex++) {
			Cell c = (Cell) counters.elementAt(countIndex);
			c.counter = 0;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  d  Description of the Parameter
	 *@return    Description of the Return Value
	 */
	boolean equalsCounters(Defect d) {
		for (int countIndex = 0; countIndex < counters.size(); countIndex++) {
			Cell c = (Cell) counters.elementAt(countIndex);
			Cell cd = (Cell) d.counters.elementAt(countIndex);
			if (c.counter != cd.counter) {
				return false;
			}
		}
		return true;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  d  Description of the Parameter
	 */
	void copyCounters(Defect d) {
		for (int countIndex = 0; countIndex < counters.size(); countIndex++) {
			Cell c = (Cell) counters.elementAt(countIndex);
			Cell cd = (Cell) d.counters.elementAt(countIndex);
			c.counter = cd.counter;
		}
	}
}

