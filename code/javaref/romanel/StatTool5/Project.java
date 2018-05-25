import java.util.ArrayList;
import java.util.Calendar;


public class Project extends XmlTag implements DefVar {
	UtilsDate UD = new UtilsDate();
	SeapineStatTool5 zt =null;
	Log L=null;
	UtilsXLS ux=null;
	ChildList defects=null;


	public Project(XmlTag xt, SeapineStatTool5 zt, Log l, ChildList defects) {
		XmlTag2(xt);
		this.zt = zt;
		L = l;
		this.defects = defects;
		ux = new UtilsXLS(L);
	}

	public Project(XmlTag xt) {
		XmlTag2(xt);
	}

	int activeProject(Calendar now, java.util.List<Integer> stateArray) throws Exception {
		if (stateArray==null){
			stateArray = new ArrayList<Integer>();
		}
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		for (int defectIndex = 0; defectIndex < defects.size(); defectIndex++) {
			Defect d = (Defect) defects.elementAt(defectIndex);
			if (d.passFilter(this)) {
				zt.AnalyzeLabel.setText(noTag + " - " + UD.print(now) + " - " + d.getDefectNumber());
				int state = d.getState(now, false);
				int counter = stateArray.get(state);
				stateArray.set(state, ++counter);
			}
		}
		L.myPrintln("  -_-_-_" + stateArray.toString());
		// check if some bugs are already opened
		int atLeastABugHasAlreadyBeenOpened = STATUS_DONTCARE;
		for (int countIndex = STATUS_DONTCARE; countIndex >=0; countIndex--) {
			if (stateArray.get(countIndex) > 0) {
				atLeastABugHasAlreadyBeenOpened = countIndex;
			}
		}
		return atLeastABugHasAlreadyBeenOpened;
	}

	boolean inactiveProjectClosedDelayed() throws Exception {
		// do not generate any worksheet for Z_, Y_ and x products
		if(noTag.startsWith("Z_") ||
				noTag.startsWith("Y_") || 
				noTag.startsWith("x")) {
			return true;
		}
		Calendar today = Calendar.getInstance();
//		displayRowStackedArea(today, xfilter);
		L.myPrint("[" + noTag + "]");
		if (activeProject(today,null)>STATUS_RELEASED) {
			L.myPrintln(" INACTIVE");
			return true;
		} 
		L.myPrintln(" ACTIVE");
		return false;
	}

	String getWorksheetName(char chart_type) {
		String WorkSheetName;
		if ((tagName == null) || (noTag == null)) {
			WorkSheetName = new String("all_changes");
		} else if (noTag.length() > 20) {
			WorkSheetName = new String(noTag.substring(0, 20));
		} else {
			WorkSheetName = new String(noTag);
		}
		// replace space with _
		WorkSheetName = new String(WorkSheetName.trim().replaceAll("\\W", ""));
		// add _ at start if name starts with digit
		WorkSheetName = new String("_" + WorkSheetName + chart_type);
		return WorkSheetName;
	}

	boolean passFilter(String prefix) throws Exception {
		return (noTag.startsWith(prefix) && !inactiveProjectClosedDelayed());
	}

	String displayProjectTreeMap(double hue) throws Exception {
		int number_of_bugs=0;
		int activity_last_week=0;
		Calendar today = Calendar.getInstance();
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		for (int defectIndex = 0; defectIndex < defects.size(); defectIndex++) {
			Defect d = (Defect) defects.elementAt(defectIndex);
			if (d.passFilter(this)) {
				number_of_bugs++;
				zt.AnalyzeLabel.setText(noTag + " - - " + d.getDefectNumber());
				activity_last_week += d.getActivity(today, 7);
				int state = d.getState(today, false);
				int counter = stateArray.get(state);
				stateArray.set(state, ++counter);
			}
		}
		StringBuffer sb = new StringBuffer("<leaf><label>"+noTag+"</label>\n");
		sb.append("<weight>"+number_of_bugs+"</weight><value>"+activity_last_week+"</value>\n");
		sb.append("<details>\n");
		sb.append(stateArray.get(STATUS_OPEN_P0)+" open P0 bugs,");
		sb.append(stateArray.get(STATUS_OPEN_P1)+" open P1 bugs,");
		sb.append((stateArray.get(STATUS_OPEN_P2)+stateArray.get(STATUS_OPEN_P3))+" open P2 and P3 bugs,");
		sb.append(stateArray.get(STATUS_RELEASED)+" owned by QA bugs,");
		sb.append((stateArray.get(STATUS_DEFERRED)+stateArray.get(STATUS_CLOSED))+" closed or deferred bugs,");
		sb.append(activity_last_week+" TTPro events last week,");
		sb.append("</details>\n");
		if(hue<NO_HUE){
			sb.append("<hue>"+hue+"</hue>\n");			
		}
		sb.append("</leaf>\n");
		return sb.toString();
	}

	String displayRowColumn() throws Exception {
		Calendar today = Calendar.getInstance();
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		for (int defectIndex = 0; defectIndex < defects.size(); defectIndex++) {
			Defect d = (Defect) defects.elementAt(defectIndex);
			if (d.passFilter(this)) {
				zt.AnalyzeLabel.setText(noTag + " - - " + d.getDefectNumber());
				int state = d.getState(today, false);
				int counter = stateArray.get(state);
				stateArray.set(state, ++counter);
			}
		}
		StringBuffer sb = new StringBuffer("<Row>" + ux.printXLS(noTag));
		sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P0)));
		sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P1)));
		sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P2)));
		sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P3)));
		sb.append(ux.printXLS(stateArray.get(STATUS_RELEASED)));
		sb.append(ux.printXLS(stateArray.get(STATUS_DEFERRED)));
		sb.append("</Row>\n");
		return sb.toString();
	}

	String displayRowStackedArea(Calendar now, SeapineStat zs) throws Exception {
		L.myPrintln("-_-_-_-_" + noTag + " " + UD.print(now));
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		StringBuffer sb = new StringBuffer("");
		if (activeProject(now, stateArray)<STATUS_DONTCARE) {
			zs.CptRow++;
			sb = new StringBuffer("<Row>" + UD.printXLS(now));
			sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P0)));
			sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P1)));
			sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P2)));
			sb.append(ux.printXLS(stateArray.get(STATUS_OPEN_P3)));
			sb.append(ux.printXLS(stateArray.get(STATUS_RELEASED)));
			sb.append(ux.printXLS(stateArray.get(STATUS_DEFERRED)+stateArray.get(STATUS_CLOSED)));
			sb.append("</Row>\n");
		} else {
			//L.myPrintln("no bug opened!");
		}
		return sb.toString();
	}

}
