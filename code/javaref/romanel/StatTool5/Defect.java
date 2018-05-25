import java.util.*;

class Defect extends XmlTag implements DefVar {
	SeapineStat zt;
	Log L;
	UtilsDate UD        = new UtilsDate();


	/**
	 *  Constructor for the DataRow object
	 *
	 *@param  xt             Description of the Parameter
	 *@param  zt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	Defect(XmlTag xt, SeapineStat zt) throws Exception {
		this.zt = zt;
		this.L = zt.L;
		this.a = xt.a;
		this.tagName = xt.tagName;
		this.noTag = xt.noTag;
		this.cl = xt.cl;
		this.parent = xt.parent;
	}


	/**
	 *  Gets the defectNumber attribute of the Defect object
	 *
	 *@return    The defectNumber value
	 */
	String getDefectNumber() {
		return new String(find("defect-number").noTag);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  tagNameFilter  Description of the Parameter
	 *@param  noTagFilter    Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean passFilter(XmlTag xfilter) throws Exception {
		if((xfilter == null) || (xfilter.tagName == null) || (xfilter.noTag == null)) {
			return true;
		}
		XmlTag xt  = cl.find(xfilter);
		//L.myPrintln("              +++++" + tagNameFilter + ", " + noTagFilter + ", " + xt);
		if(xt == null) {
			return false;
		} else {
			return true;
		}
	}

	int interpretState(Cell cs) throws Exception {
		String open = new String("s_open s_Re-Open s_Fix s_RequestMoreInfoorReject");
		String qa = new String("s_ReleasetoTesting");
		String deferred = new String("s_deferred");
		String closed = new String("s_Close");
		String dontcare = new String("s_Comment s_SystemComment");
		if(cs.tagName==null) {
			return STATUS_DONTCARE;
		} else if (open.indexOf(cs.tagName)>=0) {
			return getPriority();
		} else if (qa.indexOf(cs.tagName)>=0){
			return STATUS_RELEASED;
		} else if (deferred.indexOf(cs.tagName)>=0) {
			return STATUS_DEFERRED;
		} else if (closed.indexOf(cs.tagName)>=0) {
			return STATUS_CLOSED;
		} else if (dontcare.indexOf(cs.tagName)>=0) {
			return STATUS_DONTCARE;
		} else {
			throw new Exception("\nUnknown state in defect[" + getDefectNumber() + "]:" + cs.tagName.toString());
		}
	}

	int getActivity(Calendar now, int days) throws Exception {
		int activity_counter=0;
		Calendar ago = Calendar.getInstance();
		ago.add(Calendar.DAY_OF_MONTH, days*(-1));
		for (int i = 0; i < cl.size(); i++) {
			Cell c  = new Cell((XmlTag)cl.elementAt(i));
			if (c.changedBetween(ago, now)) {
				activity_counter++;
			}
		}
		return activity_counter;
	}
	
	

	
	
		int getState(Calendar now, boolean trace) throws Exception {
		//L.myPrintln("**********[" + getDefectNumber() + "] " + UD.print(now));
		Cell czob  = new Cell();
		for(int i = 0; i < cl.size(); i++) {
			Cell c  = new Cell((XmlTag)cl.elementAt(i));
			if(c.isBugState()){
				if(c.before(now) && czob.before(c)) {
					czob = c;
				}
				if(trace) {
					L.myPrintln("    " + UD.print(now) + " vs " + UD.print(c.date) +
							" -> " + c.before(now) + " " + c.toString().trim() + ".");
					L.myPrintln("      " + UD.print(czob.date) +
							" " + czob.toString().trim() + ".");
				}
			}
		}
		return interpretState(czob);
	}


	/**
	 *  Gets the priority attribute of the Defect object
	 *
	 *@return                The priority value
	 *@exception  Exception  Description of the Exception
	 */
	int getPriority() throws Exception {
		XmlTag xt_prio  = cl.find("priority");
		if(xt_prio == null) {
			throw new Exception("\nnull XmlTag " + toString());
		}
		String s_prio   = xt_prio.noTag;
		if(s_prio == null) {
			return STATUS_OPEN_P3; // no priority defined yet is equal to 3
			//throw new Exception("\nnull noTag " + toString2());
		}
		return (STATUS_OPEN_P0 + Integer.valueOf(s_prio.substring(1,2)).intValue());
	}

}

