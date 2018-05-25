import java.text.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2006
 */
class Req extends XmlTag implements DefVar {
	ChildList cells = new ChildList();
	ChildList xt_cells = new ChildList();
	UtilsDate UD = new UtilsDate();
	//XmlTag xt;
	RMVolatility zt;
	Log L;
	long milliseconds2days;


	/**
	 *  Constructor for the DataRow object
	 *
	 *@param  xt             Description of the Parameter
	 *@param  zt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	Req(XmlTag xt, RMVolatility zt) throws Exception {
		this.zt = zt;
		//this.xt = xt;
		XmlTag2(xt);
		L = zt.L;
		xt.findAll("date", xt_cells);
		for (int i = 0; i < xt_cells.size(); i++) {
			Cell c = new Cell((XmlTag) xt_cells.elementAt(i));
			//L.myPrint(", -3-" + c.toString());
			cells.add(c);
		}
		milliseconds2days = 1000 * 60 * 60 * 24;
	}


	/**
	 *  Gets the reqNumber attribute of the Req object
	 *
	 *@return    The reqNumber value
	 */
	String getReqNumber() {
		XmlTag x = find("id");
		if (x == null) {
			return new String("id not found: " + toString() + "\n**********");
		} else {
			return new String(x.noTag);
		}
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
		XmlTag x = find(tagNameFilter, noTagFilter);
		//L.myPrintln("              +++++" + tagNameFilter + ", " + noTagFilter + ", " + xt);
		if (x == null) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  now            Description of the Parameter
	 *@param  ago            Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean changedBetween(Calendar ago, Calendar now) throws Exception {
		for (int i = 0; i < cells.size(); i++) {
			Cell c = (Cell) cells.elementAt(i);
			//L.myPrintln("(" + c.toString2() + ",ago " + UD.print(ago) + ",now " + UD.print(now) + ")" + c.changedBetween(ago, now));
			if (c.changedBetween(ago, now)) {
				//L.myPrintln("(" + c.toString2() + ",ago " + UD.print(ago) + ",now " + UD.print(now) + ")" + c.changedBetween(ago, now));
				return true;
			}
		}
		return false;
	}


	/**
	 *  Description of the Method
	 *
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	int durationBetween1stAndLastUpdate() throws Exception {
		long min = Long.MAX_VALUE;
		long max = 0;
		for (int i = 0; i < cells.size(); i++) {
			Cell c = (Cell) cells.elementAt(i);
			if (c.getTimeInMillis() < min) {
				min = c.getTimeInMillis();
			}
			if (c.getTimeInMillis() > max) {
				max = c.getTimeInMillis();
			}
		}
		int diff = (int) ((max - min) / milliseconds2days);
		return diff;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  dgogate        Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	int durationBetweenGoGateAndLastUpdate(Calendar dgogate) throws Exception {
		long lgogate = dgogate.getTimeInMillis();
		long min = Long.MAX_VALUE;
		long max = 0;
		for (int i = 0; i < cells.size(); i++) {
			Cell c = (Cell) cells.elementAt(i);
			if (c.getTimeInMillis() < min) {
				min = c.getTimeInMillis();
			}
			if (c.getTimeInMillis() > max) {
				max = c.getTimeInMillis();
			}
		}
		if (min < lgogate) {
			min = lgogate;
		}
		int diff = (int) ((max - min) / milliseconds2days);
		return diff;
	}


	/**
	 *  Gets the priorityFinality attribute of the Req object
	 *
	 *@return                The priorityFinality value
	 *@exception  Exception  Description of the Exception
	 */
	int getPriorityFinality() throws Exception {
		int priority = PXOTHER;
		if (find("Priority") == null) {
			return PXOTHER;
		}
		boolean b_changed = (find("changed") != null);
		boolean b_dropped = (find("dropped") != null);
		//L.myPrintln("b_changed=" + b_changed);
		//L.myPrintln("b_dropped=" + b_dropped);
		if (b_changed | b_dropped) {
			L.myPrintln("getPriorityFinality()2![" + toString() + "]");
		}
		if (b_changed) {
			return P0CHANGED;
		}
		if (b_dropped) {
			return P0DROPPED;
		}
		String s_priority = find("Priority").noTag;
		//L.myPrintln("s_priority=" + s_priority);
		if (s_priority != null) {
			if (s_priority.compareTo("Required") == 0) {
				priority = 0;
			} else if (s_priority.compareTo("Obsolete") == 0) {
				L.myPrintln("getPriorityFinality()1![" + toString() + "]");
				return P0DROPPED;
			}
		}
		int finality = 0;
		//L.myPrintln("priority=" + priority);
		if (find("Finality") != null) {
			String s_finality = find("Finality").noTag;
			//L.myPrintln("s_finality=" + s_finality);
			if (s_finality != null && s_finality.compareTo("Agreed") == 0) {
				finality = 100;
			}
		}
		//L.myPrintln("finality=" + finality);

		switch (priority) {
				case 0:
					if (finality == 100) {
						return P0FINAL;
					}
					return P0DRAFT;
		}
		return PXOTHER;
	}


	/**
	 *  Gets the type attribute of the Req object
	 *
	 *@return    The type value
	 */
	String getType() {
		return find("type").noTag;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  d  Description of the Parameter
	 *@return    Description of the Return Value
	 */
	boolean sameID(Req d) {
		return find("id").noTag.compareTo(d.find("id").noTag) == 0;
	}


	/**
	 *  Description of the Method
	 *
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean hasPriority() throws Exception {
		return getPriorityFinality() != PXOTHER;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  bs             Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean fromPreviousBaseline(Baseline bs) throws Exception {
		Baseline bsl = new Baseline(parent);
		return bsl.compare(bs) < 0 && bsl.type(bs.type) && hasPriority();
	}


	/**
	 *  Gets the priority attribute of the Req object
	 *
	 *@return                The priority value
	 *@exception  Exception  Description of the Exception
	 */
	int getPriority() throws Exception {
		if (find("Priority") == null) {
			return PXOTHER;
		}
		String s_priority = find("Priority").noTag;
		int priority = PXOTHER;
		//L.myPrintln("s_priority=" + s_priority);
		if (s_priority != null) {
			if (s_priority.compareTo("Required") == 0) {
				priority = 0;
			} else if (s_priority.compareTo("Desired") == 0) {
				priority = 1;
			} else if (s_priority.compareTo("Nice to Have") == 0) {
				priority = 2;
			}
		}
		return priority;
	}


	/**
	 *  Gets the risk attribute of the Req object
	 *
	 *@return                The risk value
	 *@exception  Exception  Description of the Exception
	 */
	int getRisk() throws Exception {
		if (find("Risk") == null) {
			return PXOTHER;
		}
		String s_risk = find("Risk").noTag;
		int risk = PXOTHER;
		//L.myPrintln("s_risk=" + s_risk);
		if (s_risk != null) {
			if (s_risk.compareTo("High") == 0) {
				risk = 0;
			} else if (s_risk.compareTo("Medium") == 0) {
				risk = 1;
			} else if (s_risk.compareTo("Low") == 0) {
				risk = 2;
			}
		}
		return risk;
	}


	/**
	 *  Gets the riskString attribute of the Req object
	 *
	 *@param  risk           Description of the Parameter
	 *@return                The riskString value
	 *@exception  Exception  Description of the Exception
	 */
	String getRiskString(int risk) throws Exception {
		switch (risk) {
				case 0:
					return new String("<Cell ss:StyleID=\"head4\"><Data ss:Type=\"String\">High</Data></Cell>");
				case 1:
					return new String("<Cell ss:StyleID=\"head5\"><Data ss:Type=\"String\">Medium</Data></Cell>");
				case 2:
					return new String("<Cell ss:StyleID=\"head6\"><Data ss:Type=\"String\">Low</Data></Cell>");
				default:
					return new String("<Cell ss:StyleID=\"head4\"><Data ss:Type=\"String\">No Good!</Data></Cell>");
		}
	}


	/**
	 *  Gets the nUD attribute of the Req object
	 *
	 *@return                The nUD value
	 *@exception  Exception  Description of the Exception
	 */
	int getNUD() throws Exception {
		if (find("nud") != null) {
			return 1;
		}
		return 0;
	}


	/**
	 *  Description of the Method
	 *
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	int getProtoNeeded() throws Exception {
		if (find("Proto_NeedTest") != null) {
			return 1;
		}
		return 0;
	}

}

