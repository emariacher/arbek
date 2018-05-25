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

	String getProjectName() {
		return new String(find("product").noTag);
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
		XmlTag xt=null;
		if(xfilter.find("isGroupOfProjects")==null){
			xt  = cl.find(xfilter);
		}else{
			ChildList subprojects_xt = new ChildList();
			xfilter.findAll("subproject", null);
			xfilter.findAll("subproject", subprojects_xt);
			Iterator<XmlTag> i = subprojects_xt.iterator();
			while(i.hasNext()){
				XmlTag xtsp = i.next();
				xt = cl.find(new XmlTag("product",xtsp.noTag));
				if(xt!=null) return true;
			}
		}

		//L.myPrintln("              +++++" + tagNameFilter + ", " + noTagFilter + ", " + xt);
		if(xt == null) {
			return false;
		} else {
			return true;
		}
	}

	int interpretState(Cell cs) throws Exception {
		if(cs.tagName==null) {
			return STATUS_DONTCARE;
		} 
		String bugstatus = cs.tagName.toLowerCase();
		if (l_open.contains(bugstatus)) {
			return getPriority();
		} else if (l_qa.contains(bugstatus)){
			return STATUS_RELEASED;
		} else if (l_deferred.contains(bugstatus)) {
			return STATUS_DEFERRED;
		} else if (l_closed.contains(bugstatus)) {
			return STATUS_CLOSED;
		} else if (l_dontcare.contains(bugstatus)) {
			return STATUS_DONTCARE;
		} else {
			throw new Exception("\nUnknown state in defect[" + getDefectNumber() + "]:" + bugstatus);
		}
	}

	int getActivity(Calendar now, int days) throws Exception {
		int activity_counter=0;
		Calendar ago = Calendar.getInstance();
		ago.add(Calendar.DAY_OF_MONTH, days*(-1));
		Iterator<XmlTag> i = cl.iterator();
		while(i.hasNext()){
			Cell c  = new Cell(i.next());
			if (c.changedBetween(ago, now)) {
				activity_counter++;
			}
		}
		return activity_counter;
	}

	Calendar getEarliestCalendar(ArrayList<String> l, Calendar c_now){
		Calendar c_earliest = (Calendar) c_now.clone();
		Iterator<String> it_l = l.iterator();
		while(it_l.hasNext()){
			XmlTag xt =find(it_l.next());
			if(xt==null){
				continue;
			}
			Cell c  = new Cell(xt);
			if(c.before(c_earliest)) {
				c_earliest=c.getCalendar();
			}
		}
		return c_earliest;
	}

	Calendar getLatestCalendar(ArrayList<String> l, Calendar c_now){
		Calendar c_latest = (Calendar) c_now.clone();
		c_latest.add(Calendar.YEAR, -50);
		boolean b_latestFound=false;
		Iterator<String> it_l = l.iterator();
		while(it_l.hasNext()){
			XmlTag xt =find(it_l.next());
			if(xt==null){
				continue;
			}
			Cell c  = new Cell(xt);
			if(c.after(c_latest)&&c.before(c_now)) {
				c_latest=c.getCalendar();
				b_latestFound=true;
			}
		}
		if(b_latestFound){
			return c_latest;	
		} else {
			return c_now;
		}
	}

	Float getResolutionDuration(Calendar c_now){
		Calendar c_open = getEarliestCalendar(l_open, c_now);
		ArrayList<String> l_closed_deferred = new ArrayList<String>(l_closed);
		l_closed_deferred.addAll(l_deferred);
		Calendar c_closed = getLatestCalendar(l_closed_deferred, c_now);
		Float f_milli = new Float(c_closed.getTimeInMillis()- c_open.getTimeInMillis());
		return f_milli/(1000*3600*24); // in days
	}



	int getState(Calendar now, boolean trace) throws Exception {
		//L.myPrintln("**********[" + getDefectNumber() + "] " + UD.print(now));
		Cell czob  = new Cell();
		Iterator<XmlTag> i = cl.iterator();
		while(i.hasNext()){
			Cell c  = new Cell(i.next());
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

	ArrayList<String> l_open = new ArrayList<String>(Arrays.asList(
			"s_open","s_re-open","s_fix","s_requestmoreinfoorreject",
			"s_returnre-open","s_rejectneedmoreinfo","s_returntoeng",
			"s_requestmoreinfo"));
	ArrayList<String> l_qa = new ArrayList<String>(Arrays.asList("s_releasetotesting","s_releasenotes","s_verify","s_requesttoverify"));
	ArrayList<String> l_deferred = new ArrayList<String>(Arrays.asList("s_deferred","s_defer"));
	ArrayList<String> l_closed = new ArrayList<String>(Arrays.asList("s_close"));
	ArrayList<String> l_dontcare = new ArrayList<String>(Arrays.asList("s_comment","s_Systemcomment"));

}

