import java.text.*;
import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class Cell  implements DefVar, Comparable<Cell> {
	Calendar cal    = null;
	int i_bugState=INVALID;
	String s_bugState;
	Element parent;


	Cell(Element el) throws Exception {
		Node firstChild= el.getFirstChild();
		if(firstChild==null) { // bug state with no date
			return; 
		}
		String s_date=el.getFirstChild().getNodeValue();
		s_bugState=el.getTagName().toLowerCase();
		if(isEvent()&&(s_date != null)) {
			s_date = new String(s_date.replaceAll("\\s",""));
			SimpleDateFormat formatIn  = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			date = formatIn.parse(s_date, new ParsePosition(0));
			cal = Calendar.getInstance();
			cal.setTime(date);
			parent= (Element)el.getParentNode();
			i_bugState=interpretState();
		}
	}


	Cell(Calendar cal) { 
		this.cal=cal;
		i_bugState=STATUS_DONTCARE;
	}



	boolean isBugState() {
		return i_bugState!=INVALID;
	}
	boolean isEvent() {
		return s_bugState.toLowerCase().startsWith("s_");
	}

	boolean before(Cell c) throws Exception {
		if(cal == null) {
			return true;
		}
		if(c == null) {
			return true;
		}
		if(c.cal == null) {
			return true;
		}
		if(print().equals(c.print())) {
			return true;
		}
		return cal.before(c.cal);
	}


	boolean before(Calendar now) {
		if(cal == null) {
			return false;
		}
		return cal.before(now);
	}

	boolean after(Calendar now) {
		return !before(now);
	}


	boolean changedBetween(Calendar ago, Calendar now) throws Exception {
		return (before(now) && after(ago));
	}


	String toString2() {
		if(cal != null) {
			SimpleDateFormat formatOut  = new SimpleDateFormat("ddMMMyyyy");
			return new String("[" + toString() + "," + formatOut.format(cal) + "]");
		} else {
			return new String("[" + toString() + "]");
		}
	}

	String print() throws Exception {
		if(cal != null) {
			SimpleDateFormat formatOut  = new SimpleDateFormat("ddMMMyyyy");
			return new String(formatOut.format(cal));
		}
		return new String("invalid_date");
	}

	private int interpretState() throws Exception {
		if((s_bugState==null)&&(!isEvent())) {
			return STATUS_DONTCARE;
		} 
		if (l_open.contains(s_bugState)) {
			return getPriority();
		} else if (l_qa.contains(s_bugState)){
			return STATUS_RELEASED;
		} else if (l_deferred.contains(s_bugState)) {
			return STATUS_DEFERRED;
		} else if (l_closed.contains(s_bugState)) {
			return STATUS_CLOSED;
		} else if (l_dontcare.contains(s_bugState)) {
			return STATUS_DONTCARE;
		} else {
			throw new Exception("\nUnknown state :" + s_bugState);
		}
	}

	int getPriority() throws Exception {
		String s_prio   = XmlUtils.getTextValue(parent, "priority");
		if(s_prio == null) {
			return STATUS_OPEN_P3; // no priority defined yet is equal to 3
			//throw new Exception("\nnull noTag " + toString2());
		}
		return (STATUS_OPEN_P0 + Integer.valueOf(s_prio.substring(1,2)).intValue());
	}

	public String toString() {
		return new String("["+s_bugState+" "+i_bugState+" "+UtilsDate.print(cal)+"]");
	}


	private ArrayList<String> l_open = new ArrayList<String>(Arrays.asList(
			"s_open","s_re-open","s_fix","s_requestmoreinfoorreject",
			"s_returnre-open","s_rejectneedmoreinfo","s_returntoeng",
			"s_requestmoreinfo","s_provideinfo","s_estimate"));
	private ArrayList<String> l_qa = new ArrayList<String>(Arrays.asList("s_releasetotesting","s_releasenotes",
			"s_verify","s_requesttoverify"));
	private ArrayList<String> l_deferred = new ArrayList<String>(Arrays.asList("s_deferred","s_defer"));
	private ArrayList<String> l_closed = new ArrayList<String>(Arrays.asList("s_close"));
	private ArrayList<String> l_dontcare = new ArrayList<String>(Arrays.asList("s_comment","s_Systemcomment"));


	@Override
	public int compareTo(Cell c) {
		int i_compareDate = cal.compareTo(c.cal);
		if(i_compareDate != 0){
			return i_compareDate;
		} else {
			return i_bugState-c.i_bugState;
		}
	}

}

