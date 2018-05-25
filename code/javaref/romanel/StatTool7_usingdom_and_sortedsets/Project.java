import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import org.w3c.dom.Element;


public class Project implements DefVar {
	//	UtilsDate UD = new UtilsDate();
	SeapineStat zst =null;
	Log L=null;
	ArrayList<Defect> defects = new ArrayList<Defect>();
	boolean isGroupOfProjects = false;
	int CptRow;
	int BugRank=0;
	String projectName;




	public Project(Element el, SeapineStat zst2) {
		this.zst = zst2;
		L = zst2.L;
		projectName= el.getFirstChild().getNodeValue();
	}

	public Project(String subproject, SeapineStat zst2) {
		this.zst = zst2;
		L = zst2.L;
		projectName= new String(subproject);
	}

	boolean activeProject(Calendar now, java.util.List<Integer> stateArray) throws Exception {
		if (stateArray==null){
			stateArray = new ArrayList<Integer>();
		}
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		for (Defect d : defects) {
			zst.ztp.zt.AnalyzeLabel.setText(projectName + " - " + UtilsDate.print(now) + " - " + d.s_defectNumber);			
			int state = d.getState(now, false);
			int counter = stateArray.get(state);
			stateArray.set(state, ++counter);
			L.myPrintln( UtilsDate.print(now) + "," + state +"," + projectName + "," + d.s_projectName + 
					"," + d.s_defectNumber);
		}
		//L.myPrintln(UD.print(now) + "-_-_-_" + stateArray.toString());
		// count not closed bugs
		int BugOpened = 0;
		for (int countIndex = STATUS_RELEASED; countIndex >=0; countIndex--) {
			BugOpened += stateArray.get(countIndex);
		}
		L.myPrintln(projectName+" "+UtilsDate.print(now) + "BugOpened[" + BugOpened+"] active:"+(BugOpened>0));
		return (BugOpened>0);
	}

	static String getWorksheetName(String wsname, char chart_type) {
		String WorkSheetName;
		if (wsname.length() > 20) {
			WorkSheetName = new String(wsname.substring(0, 20));
		} else {
			WorkSheetName = new String(wsname);
		}
		// replace space with _
		WorkSheetName = new String(WorkSheetName.trim().replaceAll("\\W", ""));
		// add _ at start if name starts with digit
		WorkSheetName = new String("_" + WorkSheetName + chart_type);
		return WorkSheetName;
	}

	boolean passFilter(String prefix) throws Exception {
		return projectName.startsWith(prefix);
	}

	String displayProjectTreeMap(double hue) throws Exception {
		if(isGroupOfProjects) { 
			return new String("");
		}
		int number_of_bugs=0;
		int activity_last_week=0;
		Calendar today = Calendar.getInstance();
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		for (Defect d : defects) {
			number_of_bugs++;
			zst.ztp.zt.AnalyzeLabel.setText(projectName + " - - " + d.s_defectNumber);
			activity_last_week += d.getActivity(today, 7);
			int state = d.getState(today, false);
			int counter = stateArray.get(state);
			stateArray.set(state, ++counter);
		}
		StringBuffer sb = new StringBuffer("<leaf><label>"+projectName+"</label>\n");
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
		for (Defect d : defects) {
			zst.ztp.zt.AnalyzeLabel.setText(projectName + " - - " + d.s_defectNumber);
			int state = d.getState(today, false);
			int counter = stateArray.get(state);
			stateArray.set(state, ++counter);
		}
		StringBuffer sb = new StringBuffer("<Row>" + UtilsXLS.printXLS(projectName));
		sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P0)));
		sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P1)));
		sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P2)));
		sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P3)));
		sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_RELEASED)));
		sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_DEFERRED)));
		sb.append(UtilsXLS.printXLS(BugRank));
		sb.append("</Row>\n");
		return sb.toString();
	}

	private String displayRowStackedArea(Calendar now) throws Exception {
		//L.myPrintln("-_-_-_-_" + noTag + " " + UD.print(now));
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		StringBuffer sb = new StringBuffer("");
		if (activeProject(now, stateArray)) {
			CptRow++;
			sb = new StringBuffer("<Row>" + UtilsDate.printXLS(now));
			sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P0)));
			sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P1)));
			sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P2)));
			sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_OPEN_P3)));
			sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_RELEASED)));
			sb.append(UtilsXLS.printXLS(stateArray.get(STATUS_DEFERRED)+stateArray.get(STATUS_CLOSED)));
			sb.append("</Row>\n");
		} else {
			//L.myPrintln("no bug opened!");
		}
		return sb.toString();
	}


	static String getWorksheetHeader(String WorkSheetName, int TabColorIndex) {
		return new String("<Worksheet  ss:Name=\"" + WorkSheetName +
				"\"><WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\"><TabColorIndex>" +
				TabColorIndex +
		"</TabColorIndex></WorksheetOptions><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"80\"/>\n");
	}

	void xlsAfficheWorksheetStackedArea(Calendar then, Log.MyFile xlsfile) throws Exception {
		String WorkSheetName = getWorksheetName(projectName, 'C');
		xlsfile.writeFile(getWorksheetHeader(WorkSheetName, 44));
		//header rows
		xlsfile.writeFile("<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"6\"/>\n");
		xlsfile.writeFile("<Row>\n<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">date</Data></Cell>");
		for (int i = 0; i < 4; i++) {
			xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Open_P" + i + "</Data></Cell>");
		}
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">QA</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Closed</Data></Cell>");
		xlsfile.writeFile("</Row>\n");
		Calendar c = (Calendar) then.clone();
		Calendar today = Calendar.getInstance();
		// rows for each week
		CptRow = 0;
		while (c.before(today)) {			
			xlsfile.writeFile(displayRowStackedArea(c));
			c.add(Calendar.DAY_OF_MONTH, 7);
		}
		//		xlsfile.writeFile(displayRowStackedArea(today));
		xlsfile.writeFile("</Table></Worksheet>\n");
		zst.NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
				"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
				(CptRow + 1) + "C7\"/>\n");
	}

	static public boolean oldProject(String s_projectName) {
		return (s_projectName.startsWith("Z_") ||
				s_projectName.startsWith("Y_") || 
				s_projectName.startsWith("x"));
	}

	// crop defects owned by project and compute if active
	public void cropDefects(ArrayList<Defect> zdefects, Calendar c) throws Exception {
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		java.util.List<Integer> stateArrayWas = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
			stateArrayWas.add(0);
		}

		for (Defect d : zdefects) {
			if (d.passFilter(this)){
				defects.add(d);
				L.myPrintln(projectName+" "+d.toString());
				int state = d.getState(Calendar.getInstance(), false);
				int counter = stateArray.get(state);
				stateArray.set(state, ++counter);
				L.myPrintln("    ************************");
				state = d.getState(c, false);
				counter = stateArrayWas.get(state);
				stateArrayWas.set(state, ++counter);
			}
		}
		int BugOpened = 0;
		for (int countIndex = STATUS_RELEASED; countIndex >=0; countIndex--) {
			BugOpened += stateArray.get(countIndex);
		}
		int BugOpenedWas = 0;
		for (int countIndex = STATUS_RELEASED; countIndex >=0; countIndex--) {
			BugOpenedWas += stateArrayWas.get(countIndex);
		}
		// count all bugs
		int BugAll = 0;
		for (int countIndex = STATUS_DONTCARE; countIndex >=0; countIndex--) {
			BugAll += stateArray.get(countIndex);
		}
		int BugClosed = stateArray.get(STATUS_CLOSED);
		//		L.myPrintln(noTag+" "+UD.print(Calendar.getInstance()) + "BugClosed[" + BugClosed+"] active:"+(BugClosed>0));
		L.myPrintln(projectName+" "+UtilsDate.print(Calendar.getInstance()) + "BugOpened[" + BugOpened+
				"] BugAll["+BugAll+"]");
		// do not display projects that have opened static bugs
		if(BugClosed<3){
			float f_BugOpened = new Float(BugOpened);
			float f_BugOpenedWas = new Float(BugOpenedWas);
			float f_changeRatio = f_BugOpenedWas / f_BugOpened;
			L.myPrintln(projectName+" "+UtilsDate.print(Calendar.getInstance()) + " BugOpened[" + f_BugOpened+
					"] BugOpenedWas["+f_BugOpenedWas+"]"+f_changeRatio);

			if(f_changeRatio>0.9&&f_changeRatio<1.1) {
				BugRank=0;
				L.myPrintln(projectName+"    "+UtilsDate.print(Calendar.getInstance()) + " BugRank[" + BugRank+"]");
				return;
			}
		}
		BugRank=(BugOpened*1000)+BugAll;
		L.myPrintln(projectName+"    "+UtilsDate.print(Calendar.getInstance()) + " BugRank[" + BugRank+"]");

	}

	public static class  compareBugRank implements Comparator<Project> {
		public int compare(Project p1, Project p2) {
			int diff_BugRank=p2.BugRank-p1.BugRank;
			if(diff_BugRank!=0) {
				return diff_BugRank;
			} else {
				return p1.projectName.compareTo(p2.projectName);
			}
		}
	}
	public static class  compareName implements Comparator<Project> {
		public int compare(Project p1, Project p2) {
			return p1.projectName.compareTo(p2.projectName);
		}
	}

	public String toString() {
		return new String(projectName+" "+defects.size()+", ");
	}

}
