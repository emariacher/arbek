import java.util.*;

import org.w3c.dom.Element;

class Defect implements DefVar {
	SeapineStat zt;
	Log L;
	Element el;
	SortedSet<Cell> l_events = new TreeSet<Cell>();
	String s_defectNumber;
	String s_projectName;



	public Defect(Element el, SeapineStat zt2) throws Exception {
		this.zt = zt2;
		this.L = zt.L;
		this.el=el;
		s_defectNumber=XmlUtils.getTextValue(el, "defect-number");
		s_projectName=XmlUtils.getTextValue(el, "product");
		ArrayList<Element> l_cells = new ArrayList<Element>();
		l_cells=XmlUtils.getElementsByTagName(el, "s_.+");
		for (Element ele : l_cells) {
			Cell c = new Cell(ele);
			if(c.cal!=null) {
				l_events.add(c);
			}
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
	boolean passFilter(Project projectFilter) throws Exception {
		if((projectFilter == null) || (projectFilter.projectName == null)) {
			return true;
		}
		if(projectFilter.isGroupOfProjects==false){
			return (s_projectName.equals(projectFilter.projectName));
		}else{
			ProjectGroup pg = (ProjectGroup)projectFilter;
			for (Project xt_projectGroup : pg.subproducts) {
				if(passFilter(xt_projectGroup)) {
					return true;
				}
			}
		}
		return false;
	}


	int getActivity(Calendar now, int days) throws Exception {
		int activity_counter=0;
		Calendar ago = Calendar.getInstance();
		ago.add(Calendar.DAY_OF_MONTH, days*(-1));
		for (Cell c : l_events) {
			if (c.changedBetween(ago, now)) {
				activity_counter++;
			}
		}
		return activity_counter;
	}


	int getState(Calendar now, boolean trace) throws Exception {
		//		L.myPrintln("**********[" + s_defectNumber + "] " +
		//				UtilsDate.print(now) + ", "+l_events.toString());
		SortedSet<Cell> lss = l_events.headSet(new Cell(now));
		if(lss.isEmpty()) {
			return STATUS_DONTCARE;
		}
		return lss.last().i_bugState;
	}

	public String toString() {
		return new String(s_defectNumber+" "+s_projectName+" "+l_events.size()+", ");
	}

}

