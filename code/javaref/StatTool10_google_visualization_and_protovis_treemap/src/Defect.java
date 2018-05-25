import java.text.DecimalFormat;
import java.util.*;

import org.w3c.dom.Element;

public class Defect implements DefVar {
	SeapineStat zt;
	Log L;
	Element el;
	SortedSet<Cell> l_events = new TreeSet<Cell>();
	String s_defectNumber;
	String s_projectName;
	String s_summary;
	String s_priority;
	int i_priority;



	public SeapineStat getZt() {
		return zt;
	}

	public String getS_defectNumber() {
		return s_defectNumber;
	}

	public String getS_projectName() {
		return s_projectName;
	}

	public Defect(Element el, SeapineStat zt2) throws Exception {
		this.zt = zt2;
		this.L = zt.L;
		this.el=el;
		s_defectNumber=XmlUtils.getTextValue(el, "defect-number");
		s_projectName=XmlUtils.getTextValue(el, "product");
		s_summary=XmlUtils.getTextValue(el, "summary");
		s_priority=XmlUtils.getTextValue(el, "priority");
		i_priority = STATUS_OPEN_P0 + Integer.valueOf(s_priority.substring(1,2)).intValue();
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
				} 			}
		}
		return false;
	}

	public int getActivity(Calendar now, int days) throws Exception {
		int activity_counter=0;
		Calendar ago = Calendar.getInstance();
		ago.add(Calendar.DAY_OF_MONTH, days*(-1));
		for (Cell c : l_events) {
			if (c.changedBetween(ago, now)) {
				activity_counter++;
				L.myPrintln("   "+Log.printZisday(now, "ddMMMyy") + " " +s_defectNumber + " " + c.toString()+ " "+activity_counter);
			}
		}
		return activity_counter;
	}


	int getState(Calendar now, boolean trace) throws Exception {
		SortedSet<Cell> lss = l_events.headSet(new Cell(now));
		if(lss.isEmpty()) {
			return STATUS_DONTCARE;
		}
		//		L.myPrintln("**********[" + s_defectNumber + "] " +
		//				UtilsDate.print(now) + ", "+l_events.toString()+" * "+lss.last().i_bugState);
		return lss.last().i_bugState;
	}

	int getDurationSinceLastChange(Calendar now, boolean trace) throws Exception {
		SortedSet<Cell> lss = l_events.headSet(new Cell(now));
		if(lss.isEmpty()) {
			return 0;
		}
		//		L.myPrintln("**********[" + s_defectNumber + "] " +
		//				UtilsDate.print(now) + ", "+l_events.toString()+" * "+lss.last().i_bugState);
		long l_durationInMillis = now.getTimeInMillis() - lss.last().cal.getTimeInMillis();
		int i_durationInDays = (int) (l_durationInMillis/(24*60*60*1000));
		return i_durationInDays;
	}

	public String toString() {
		return new String(s_defectNumber+" "+s_projectName+" "+l_events.size()+", ");
	}

	public void updateBugStatusCounters(Calendar now, List<Integer> stateArray) throws Exception {
		//		L.myPrintln( UtilsDate.print(now) + "," + s_projectName + 
		//				"," + s_defectNumber);
		//		zt.ztp.zt.AnalyzeLabel.setText(s_projectName + " - " + UtilsDate.print(now) + " - " + s_defectNumber);			
		int state = getState(now, false);
		int counter = stateArray.get(state);
		stateArray.set(state, ++counter);
		//		L.myPrintln( UtilsDate.print(now) + "," + state +"," + s_projectName + 
		//				"," + s_defectNumber);
	}

	public void updateBugActivity(List<Integer> stateArray) throws Exception {
		//	zst.ztp.zt.AnalyzeLabel.setText(projectName + " - - " + d.s_defectNumber);
		//	activity_last_week += d.getActivity(today, 7);
		int state = getState(Calendar.getInstance(), false);
		int counter = stateArray.get(state);
		stateArray.set(state, ++counter);
		L.myPrintln(stateArray.toString());
	}

	public void updateWasBugActivity(Calendar c, List<Integer> stateArrayWas) throws Exception {
		int state = getState(c, false);
		int counter = stateArrayWas.get(state);
		stateArrayWas.set(state, ++counter);
		L.myPrintln(stateArrayWas.toString());
	}

	public void getNewBugText(Calendar c_in, StringBuffer sb) throws Exception {
		Calendar c = (Calendar) c_in.clone();
		int stateNow = getState(c, false);
		//		sb.append(UtilsDate.print(c)+"\n");
		if(c.get(Calendar.MONTH)==Calendar.JANUARY)  {
			c.roll(Calendar.YEAR, -1);
			c.set(Calendar.MONTH, Calendar.DECEMBER);
		} else {
			c.roll(Calendar.MONTH, -1);
		}
		//		sb.append(UtilsDate.print(c)+"\n");
		int stateOneMonthBefore = getState(c, false);
		if((stateOneMonthBefore==STATUS_DONTCARE)&&(stateNow<STATUS_DONTCARE)) {
			sb.append("['"+s_projectName+"','"+Filter.filter(s_summary)+"'],\n");
		}
		//		sb.append("['"+s_projectName+"','"+Filter.filter(s_summary)+"'],"+stateOneMonthBefore+","+stateNow+"\n");
	}

	public void getAllBugText(StringBuffer sb) throws Exception {
		sb.append("[' ','"+Filter.filter(s_summary)+"'],\n");
	}

	public void getBugRisk(Calendar today, StringBuffer sb) throws Exception {
		int stateNow = getState(today, false);
		if(stateNow<STATUS_RELEASED) {
			sb.append(" ['[P"+stateNow+" "+s_defectNumber+"] "+s_summary.replaceAll("\\W", " ").toLowerCase()+"',");
			sb.append("new Date("+UtilsDate.print4js(today)+"),");
			int i_priority = (int) java.lang.Math.pow(2,3-stateNow);
			double d_priority = i_priority*(10+(new Random()).nextDouble())/10;
			double d_opened_duration=getDurationSinceLastChange(today, false)*(10+(new Random()).nextDouble())/10;
			sb.append((new DecimalFormat("#.#")).format(d_opened_duration)+",");
			double d_risk = d_priority*d_opened_duration;
			sb.append((new DecimalFormat("#.#")).format(d_risk)+",");
//			sb.append("'P"+stateNow+"',");
			sb.append((new DecimalFormat("#.#")).format(d_risk)+",");
			sb.append(i_priority+",");
			sb.append(l_events.size()+",");
			sb.append("],\n");
		}
	}

	public void updateWasBugActivity4Google(Calendar c, List<Integer> pointer2counter) throws Exception {
		int state = getState(c, false);
		int counter = pointer2counter.get(0);
		int increment = (int) java.lang.Math.pow(2,3-state);
		pointer2counter.set(0, counter+increment);
		//		L.myPrintln(toString()+" * "+state+" - "+increment+" pointer2counter.toString() "+pointer2counter.toString());
	}
	public void updateBugActivity4Google(List<Integer> pointer2counter) throws Exception {
		int state = getState(Calendar.getInstance(), false);
		int counter = pointer2counter.get(0);
		int increment = (int) java.lang.Math.pow(2,3-state);
		pointer2counter.set(0, counter+increment);
		//		L.myPrintln(toString()+" * "+state+" - "+increment+" pointer2counter.toString() "+pointer2counter.toString());
	}
}




























