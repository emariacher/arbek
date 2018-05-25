/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    il y a longtemps
 *$Log$
 *emariacher - Tuesday, January 11, 2011 5:28:54 PM
 *just wait tomorrow
 *emariacher - Friday, January 07, 2011 4:24:36 PM
 *emariacher - Friday, January 07, 2011 3:46:13 PM
 *emariacher - Thursday, January 06, 2011 11:41:16 AM
 *emariacher - Wednesday, January 05, 2011 12:00:47 PM
 *emariacher - Wednesday, January 05, 2011 11:27:52 AM
 *ready 4 production!
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import static ch.lambdaj.Lambda.*;

import org.w3c.dom.Element;

public class Project implements DefVar {

	//	UtilsDate UD = new UtilsDate();
	SeapineStat zst =null;
	Log L=null;
	SortedSet<Defect> defects = new TreeSet<Defect>(new compareDefectProjectName());
	boolean isGroupOfProjects = false;
	int CptRow;
	int BugRank=0;
	String projectName;
	SortedSet<Project> subproducts = new TreeSet<Project>(new Project.compareName());

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

	boolean activeProject(Calendar now, List<Integer> stateArray) throws Exception {
		if (stateArray==null){
			stateArray = new ArrayList<Integer>();
		}
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		//		for (Defect d : defects) {
		//			d.updateBugStatusCounters(now, stateArray);
		//		}
		try {
			if(!defects.isEmpty()) {
				forEach(defects).updateBugStatusCounters(now, stateArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	public boolean passFilter(String prefix) throws Exception {
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
		try {
			number_of_bugs = defects.size();
			if(!defects.isEmpty()) {
				forEach(defects).updateBugActivity(stateArray);
				activity_last_week = sumFrom(defects).getActivity(today, 7);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		//		Calendar today = Calendar.getInstance();
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		//		for (Defect d : defects) {
		//			zst.ztp.zt.AnalyzeLabel.setText(projectName + " - - " + d.s_defectNumber);
		//			int state = d.getState(today, false);
		//			int counter = stateArray.get(state);
		//			stateArray.set(state, ++counter);
		//		}
		if(!defects.isEmpty()) {
			forEach(defects).updateBugActivity(stateArray);
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

	private String displayRowStackedArea4js(Calendar now) throws Exception {
		//L.myPrintln("-_-_-_-_" + noTag + " " + UD.print(now));
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		StringBuffer sb = new StringBuffer();
		if (activeProject(now, stateArray)) {
			sb = new StringBuffer("['" + UtilsDate.print(now)+"', ");
			sb.append(stateArray.get(STATUS_OPEN_P0)+", ");
			sb.append(stateArray.get(STATUS_OPEN_P1)+", ");
			sb.append(stateArray.get(STATUS_OPEN_P2)+", ");
			sb.append(stateArray.get(STATUS_OPEN_P3)+", ");
			sb.append(stateArray.get(STATUS_RELEASED)+", ");
			sb.append(stateArray.get(STATUS_DEFERRED)+stateArray.get(STATUS_CLOSED));
			sb.append("],\n");
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

	public void xlsAfficheWorksheetStackedArea(Calendar then, Log.MyFile xlsfile) throws Exception {
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
		zst.ztp.zt.AnalyzeLabel.setText(projectName);
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		java.util.List<Integer> stateArrayWas = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
			stateArrayWas.add(0);
		}
		defects.addAll(zdefects);
		if(!defects.isEmpty()) {
			L.myPrintln("removing "+defects.last());
			defects.remove(defects.last());
		}

		L.myPrintln(projectName+" "+defects.toString());
		//		for (Defect d : defects) {
		//			if (d.passFilter(this)){
		//				//				defects.add(d);
		//				L.myPrintln(projectName+" "+d.toString());
		//				int state = d.getState(Calendar.getInstance(), false);
		//				int counter = stateArray.get(state);
		//				stateArray.set(state, ++counter);
		//				L.myPrintln("    ************************");
		//				state = d.getState(c, false);
		//				counter = stateArrayWas.get(state);
		//				stateArrayWas.set(state, ++counter);
		//			}
		//		}
		if(!defects.isEmpty()) {
			L.myPrintln(defects.size()+" "+defects.first().toString());
			forEach(defects).updateBugActivity(stateArray);
			forEach(defects).updateWasBugActivity(c,stateArrayWas);
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

	public String toString() {
		return new String(projectName+" "+defects.size()+", ");
	}

	public void print(Log L) throws Exception {
		L.myPrint(", "+projectName);
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

	public class compareDefectProjectName implements Comparator<Defect> {
		public int compare(Defect d1, Defect d2) {
			//				L.myPrintln(projectName+"["+isGroupOfProjects+"] d1["+d1+"] d2["+d2+"]."+defects);
			if(isGroupOfProjects==false) {
				//					L.myPrintln("    noGroup["+compare(d1,d2,projectName)+"]");
				return compare(d1,d2,projectName);
			} else {
				for (Project subproject : subproducts) {
					int i_c=compare(d1,d2,subproject.projectName);
					//						L.myPrintln("    Group["+i_c+"]");
					if(i_c!=0) {
						return i_c;
					}
				}
				return 0;
			}
		}

		public int compare(Defect d1, Defect d2, String s_projname) {
			//			L.myPrintln("    ["+s_projname+"] d1["+d1+"] d2["+d2+"].");
			if((d1.s_projectName.compareTo(s_projname)==0)&&
					(d2.s_projectName.compareTo(s_projname)==0)) {
				if(d1.s_priority.compareTo(d2.s_priority)==0) {
					return (d1.s_defectNumber.compareTo(d2.s_defectNumber)*2);
				} else {
					return d1.s_priority.compareTo(d2.s_priority);
				}
			} else if(d1.s_projectName.compareTo(s_projname)==0) {
				return -1;
			} else if(d2.s_projectName.compareTo(s_projname)==0) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public void displayRowColumn(Log.MyFile xlsfile) throws Exception {
		xlsfile.writeFile(displayRowColumn());

	}

	public void displayProjectTreeMap(StringBuffer sb, double noHue) throws Exception {
		sb.append(displayProjectTreeMap(noHue));

	}

	public void jsBugStack(Log.MyFile file) throws Exception {
		//		Calendar today = Calendar.getInstance();
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		//		for (Defect d : defects) {
		//			zst.ztp.zt.AnalyzeLabel.setText(projectName + " - - " + d.s_defectNumber);
		//			int state = d.getState(today, false);
		//			int counter = stateArray.get(state);
		//			stateArray.set(state, ++counter);
		//		}
		if(!defects.isEmpty()) {
			forEach(defects).updateBugActivity(stateArray);
		}

		StringBuffer sb = new StringBuffer("['" + projectName+"',");
		sb.append(stateArray.get(STATUS_OPEN_P0)+",");
		sb.append(stateArray.get(STATUS_OPEN_P1)+",");
		sb.append(stateArray.get(STATUS_OPEN_P2)+",");
		sb.append(stateArray.get(STATUS_OPEN_P3)+",");
		sb.append(stateArray.get(STATUS_RELEASED)+",");
		sb.append(stateArray.get(STATUS_DEFERRED)+",");
		sb.append("],\n");
		file.writeFile(sb.toString());
	}

	public void jsNewBugsText(Log.MyFile file) throws Exception {
		Calendar today = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		if(!defects.isEmpty()) {
			forEach(defects).getNewBugText(today, sb);
		}
		file.writeFile(sb.toString());
	}

	public void jsAllBugsText(Log.MyFile file) throws Exception {
		StringBuffer sb = new StringBuffer();
		if(!defects.isEmpty()) {
			forEach(defects).getAllBugText(sb);
		}
		file.writeFile(sb.toString());
	}

	public void jsBugsTrend(Calendar then, Log.MyFile file) throws Exception {
		Calendar c = (Calendar) then.clone();
		Calendar today = Calendar.getInstance();
		// rows for each week
		CptRow = 0;
		while (c.before(today)) {			
			file.writeFile(displayRowStackedArea4js(c));
			c.add(Calendar.DAY_OF_MONTH, 7);
		}
	}

	public void jsBugsRisk(Calendar today, Log.MyFile file) throws Exception {
		StringBuffer sb = new StringBuffer();
		if(!defects.isEmpty()) {
			forEach(defects).getBugRisk(today, sb);
		}
		file.writeFile(sb.toString());
	}

	public void jsMotion(Calendar then, Log.MyFile file) {
		if(isGroupOfProjects) { 
			return;
		}
		Calendar c = (Calendar) then.clone();
		Calendar today = Calendar.getInstance();
		// rows for each week
		CptRow = 0;
		while (c.before(today)) {			
			java.util.List<Integer> stateArray = new ArrayList<Integer>();
			for (int i = 0; i < STATUS_MAX; i++) {
				stateArray.add(0);
			}
			int number_of_bugs=0;
			java.util.List<Integer> pointer2counter = new ArrayList<Integer>();
			pointer2counter.add(0);
			try {
				number_of_bugs = defects.size();
				if(!defects.isEmpty()) {
					forEach(defects).updateWasBugActivity(c,stateArray);
					forEach(defects).updateWasBugActivity4Google(c, pointer2counter);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int openBugsToday=pointer2counter.get(0);
			if(openBugsToday>2) {
				file.writeFile("[\""+projectName+"\",new Date("+UtilsDate.print4js(c)+"),"+openBugsToday+","
						+number_of_bugs+",\""+projectType()+"\"],\n");
			}
			c.add(Calendar.DAY_OF_MONTH, 7);
		}
	}

	private String projectType() {
		if((projectName.indexOf("RQM")==0)||
				(projectName.indexOf("RBM")==0)
		) return new String("Mouse");
		else if((projectName.indexOf("RQK")==0)||
				(projectName.indexOf("RBK")==0)
		) return new String("Keyboard");
		else if((projectName.indexOf("RQR")==0)
		) return new String("Receiver");
		else return new String("Other");
	}

	public void jsTreeMap(Log.MyFile file) {
		if(isGroupOfProjects) { 
			return;
		}
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		int number_of_bugs=0;
		java.util.List<Integer> pointer2counter = new ArrayList<Integer>();
		pointer2counter.add(0);
		try {
			number_of_bugs = defects.size();
			if(!defects.isEmpty()) {
				forEach(defects).updateBugActivity(stateArray);
				forEach(defects).updateBugActivity4Google(pointer2counter);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int openBugsToday=pointer2counter.get(0);
		if(openBugsToday>2) {
			file.writeFile("[\""+projectName+
					" open P0: "+stateArray.get(STATUS_OPEN_P0)+
					", P1:  "+stateArray.get(STATUS_OPEN_P1)+
					"\",\"CDBU_FW_issues\","+number_of_bugs+","+openBugsToday+"],\n");
		}
	}

	public void jsProjectTrend(Calendar c_start, Log.MyFile file) throws Exception {		
		file.writeFile("<h3>"+projectName+"</h3>\n");
		file.writeFile("<table border=\"1\"><tr>");
		// wordcloud on all project bugs all statuses
		String s_div = new String("s_div_"+(new Random()).nextInt());
		file.writeFile("<td><script type=\"text/javascript\">\n");
		file.writeFile("google.load(\"visualization\", \"1\");");

		file.writeFile("google.setOnLoadCallback(draw"+projectName+"WordCloud);\n");
		file.writeFile("function draw"+projectName+"WordCloud() {\n");
		file.writeFile("var data = new google.visualization.DataTable();\n");
		file.writeFile("var raw_data = [\n");
		jsAllBugsText(file);
		file.writeFile("\n];\n");
		file.writeFile("data.addColumn('string', 'Product');");
		file.writeFile("data.addColumn('string', 'Summary');");
		file.writeFile("data.addRows(raw_data.length);\n");
		file.writeFile("for (var i = 0; i  < raw_data.length; ++i) {\n");
		file.writeFile("  for (var j = 0; j  < 2; ++j) {\n");
		file.writeFile("    data.setValue(i, j, raw_data[i][j]);\n}}\n");
		file.writeFile("var outputDiv = document.getElementById('"+s_div+"');");
		file.writeFile("var "+projectName+"WordCloud = new WordCloud(outputDiv);");
		file.writeFile(""+projectName+"WordCloud.draw(data, null);");	
		file.writeFile("\n}\n");
		file.writeFile("</script>\n");
		file.writeFile("<div id=\""+s_div+"\"></div></td>\n");
		// bug trend as usual
		s_div = new String("s_div_"+(new Random()).nextInt());
		file.writeFile("<td><script type=\"text/javascript\">\n");
		file.writeFile("google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});");
		file.writeFile("google.setOnLoadCallback(draw"+projectName+"BugTrend);\n");
		file.writeFile("function draw"+projectName+"BugTrend() {\n");
		file.writeFile("var data = new google.visualization.DataTable();\n");
		file.writeFile("data.addColumn('string', 'Time');\n");
		file.writeFile("data.addColumn('number', 'open P0');\n");
		file.writeFile("data.addColumn('number', 'open P1');\n");
		file.writeFile("data.addColumn('number', 'open P2');\n");
		file.writeFile("data.addColumn('number', 'open P3');\n");
		file.writeFile("data.addColumn('number', 'QA');\n");
		file.writeFile("data.addColumn('number', 'Closed');\n");
		file.writeFile("data.addRows([\n");
		jsBugsTrend(c_start, file);
		file.writeFile("\n]);\n");
		file.writeFile("var "+projectName+"BugTrend = new google.visualization.AreaChart(document.getElementById('"+s_div+"'));");
		file.writeFile(""+projectName+"BugTrend.draw(data, {width: 600, height: 300, isStacked: true, title: '"+projectName+" bug trend'"+
		", lineWidth: 1, pointSize: 4, colors: ['#FF0000','#993333','#DD6666','#FF9999','#FF9900','#009900']});");	
		file.writeFile("\n}\n");
		file.writeFile("</script>\n");
		file.writeFile("<div id=\""+s_div+"\"></div></td>\n");
		file.writeFile("</tr>\n");
		// risk associated with each opened issue
		file.writeFile("<tr>\n");
		s_div = new String("s_div_"+(new Random()).nextInt());
		file.writeFile("<th>"+projectName+"</th>\n");
		file.writeFile("<td rowspan=\"2\"><script type=\"text/javascript\">\n");
		file.writeFile("google.load(\"visualization\", \"1\", {packages:[\"motionchart\"]});");
		file.writeFile("google.setOnLoadCallback(drawMotion);\n");
		file.writeFile("function drawMotion() {\n");
		file.writeFile("var data = new google.visualization.DataTable();\n");
		file.writeFile("data.addColumn('string', 'Issue');\n");
		file.writeFile("data.addColumn('date', 'Date');\n");
		file.writeFile("data.addColumn('number', 'Opened Since X Days');\n");
		file.writeFile("data.addColumn('number', 'Risk associated to Issue');\n");
		//		file.writeFile("data.addColumn('string', 'Priority');\n");
		file.writeFile("data.addColumn('number', 'Risk');\n");
		file.writeFile("data.addColumn('number', 'Issue Priority');\n");
		file.writeFile("data.addColumn('number', 'TTPro activity');\n");
		file.writeFile("data.addRows([\n");
		jsBugsRisk(Calendar.getInstance(), file);
		file.writeFile("\n]);\n");
		file.writeFile("var Motion = new google.visualization.MotionChart(document.getElementById('"+s_div+"'));");
		file.writeFile("var options = {};");	
		file.writeFile("options['width'] = 800;");	
		file.writeFile("options['height'] = 400;");	
		file.writeFile("options['showXScalePicker'] = false;");	
		file.writeFile("options['showYScalePicker'] = false;");	
		file.writeFile("options['showChartButtons'] = false;");	
//		file.writeFile("options['state'] = '{\"iconKeySettings\":[],\"stateVersion\":3,\"time\":\"notime\",\"xAxisOption\":\"_NOTHING\"," +
//				"\"playDuration\":15,\"iconType\":\"BUBBLE\",\"sizeOption\":\"_NOTHING\",\"xZoomedDataMin\":null,\"xZoomedIn\":false," +
//				"\"duration\":{\"multiplier\":1,\"timeUnit\":\"none\"},\"yZoomedDataMin\":null,\"xLambda\":1," +
//				"\"colorOption\":\"_NOTHING\",\"nonSelectedAlpha\":0.4,\"dimensions\":{\"iconDimensions\":[]}," +
//				"\"yZoomedIn\":false,\"yAxisOption\":\"_NOTHING\",\"yLambda\":1,\"yZoomedDataMax\":null," +
//		"\"showTrails\":true,\"xZoomedDataMax\":null};';");	
		file.writeFile("Motion.draw(data, options);");	
		file.writeFile("\n}\n");
		file.writeFile("</script>\n");
		file.writeFile("<div id=\""+s_div+"\"></div></td></tr>\n");
		file.writeFile("<tr><td><ul><li>The higher and more red the dot is, the highest the risk for this issue is.</li>\n");
		file.writeFile("<li>Risk for this issue is: \"Priority of the issue\" * \"Days the issue has been in open state\"</li>\n");
		file.writeFile("<li>The size of the dot reflects priority of the issue</li>\n");
		file.writeFile("<li>Mouse over a dot displays: Issue priority, number and summary</li>\n");
		file.writeFile("</ul></td></tr>");
		file.writeFile("</table>\n\n");
	}

	public void square4pv(StringBuffer sb, int i_biggestBugRank) throws Exception {
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		activeProject(Calendar.getInstance(), stateArray);
		int i_brightness = 100*BugRank/i_biggestBugRank;
		sb.append("   "+projectName.replaceAll("\\W", "")+"ç");
		sb.append(stateArray.get(STATUS_OPEN_P0)+"ç");
		sb.append(stateArray.get(STATUS_OPEN_P1)+"ç");
		sb.append(stateArray.get(STATUS_OPEN_P2)+"ç");
		sb.append(stateArray.get(STATUS_OPEN_P3)+"ç");
		sb.append("_"+i_brightness+":"+defects.size()+",\n");
	}
}

