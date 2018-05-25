import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;


public class Project extends XmlTag implements DefVar {
	UtilsDate UD = new UtilsDate();
	SeapineStat zst =null;
	Log L=null;
	UtilsXLS ux=null;
	ChildList defects= new ChildList();
	boolean isGroupOfProjects = false;
	int CptRow;
	int BugRank=0;


	public Project(XmlTag xt, SeapineStat zst) {
		XmlTag2(xt);
		this.zst = zst;
		L = zst.L;
		//this.defects = zst.defects;
		ux = new UtilsXLS(L);
	}

	public Project(XmlTag xt) {
		XmlTag2(xt);
	}
	public Project() {
	}

	boolean activeProject(Calendar now, java.util.List<Integer> stateArray) throws Exception {
		if (stateArray==null){
			stateArray = new ArrayList<Integer>();
		}
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}
		// for each defect update relevant counters (in 1st defect)
		Iterator<XmlTag> i = defects.iterator();
		while(i.hasNext()){
			Defect d = (Defect)i.next();
			zst.ztp.zt.AnalyzeLabel.setText(noTag + " - " + UD.print(now) + " - " + d.getDefectNumber());			
			int state = d.getState(now, false);
			int counter = stateArray.get(state);
			stateArray.set(state, ++counter);
			L.myPrintln( UD.print(now) + "," + state +"," + noTag + "," + d.getProjectName() + 
					"," + d.getDefectNumber());
		}
		//L.myPrintln(UD.print(now) + "-_-_-_" + stateArray.toString());
		// count not closed bugs
		int BugOpened = 0;
		for (int countIndex = STATUS_RELEASED; countIndex >=0; countIndex--) {
			BugOpened += stateArray.get(countIndex);
		}
		L.myPrintln(noTag+" "+UD.print(now) + "BugOpened[" + BugOpened+"] active:"+(BugOpened>0));
		return (BugOpened>0);
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
		return noTag.startsWith(prefix);
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
		Iterator<XmlTag> i = defects.iterator();
		while(i.hasNext()){
			Defect d = (Defect)i.next();
			number_of_bugs++;
			zst.ztp.zt.AnalyzeLabel.setText(noTag + " - - " + d.getDefectNumber());
			activity_last_week += d.getActivity(today, 7);
			int state = d.getState(today, false);
			int counter = stateArray.get(state);
			stateArray.set(state, ++counter);
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
		Iterator<XmlTag> i = defects.iterator();
		while(i.hasNext()){
			Defect d = (Defect)i.next();
			zst.ztp.zt.AnalyzeLabel.setText(noTag + " - - " + d.getDefectNumber());
			int state = d.getState(today, false);
			int counter = stateArray.get(state);
			stateArray.set(state, ++counter);
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

	private String displayRowStackedArea(Calendar now) throws Exception {
		//L.myPrintln("-_-_-_-_" + noTag + " " + UD.print(now));
		//openedArray used for counters
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		StringBuffer sb = new StringBuffer("");
		if (activeProject(now, stateArray)) {
			CptRow++;
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


	String getWorksheetHeader(String WorkSheetName, int TabColorIndex) {
		return new String("<Worksheet  ss:Name=\"" + WorkSheetName +
				"\"><WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\"><TabColorIndex>" +
				TabColorIndex +
		"</TabColorIndex></WorksheetOptions><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"80\"/>\n");
	}

	void xlsAfficheWorksheetStackedArea(int annee, int mois, Log.MyFile xlsfile) throws Exception {
		String WorkSheetName = getWorksheetName('C');
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
		Calendar today = Calendar.getInstance();
		// rows for each week
		Calendar c = UD.get1erJour(annee, mois);
		CptRow = 0;
		while (c.before(today)) {			
			xlsfile.writeFile(displayRowStackedArea(c));
			c.add(Calendar.DAY_OF_MONTH, 7);
		}
		xlsfile.writeFile(displayRowStackedArea(today));
		xlsfile.writeFile("</Table></Worksheet>\n");
		zst.NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
				"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
				(CptRow + 1) + "C7\"/>\n");
	}

	void xlsAfficheWorksheetResolution(int annee, int mois, Log.MyFile xlsfile) throws Exception {
		Calendar today = Calendar.getInstance();
		if (!activeProject(today, null)) {
			return;
		}
		String WorkSheetName = getWorksheetName('D');
		xlsfile.writeFile(getWorksheetHeader(WorkSheetName, 44));
		//header rows
		xlsfile.writeFile("<Column ss:AutoFitWidth=\"0\" ss:Width=\"30\" ss:Span=\"8\"/>\n");
		xlsfile.writeFile("<Row>\n<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">date</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Number of Already Closed Bugs</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Number of Not Yet Closed Bugs</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Mean Resolution Duration of Already Closed Bugs</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Mean Resolution Duration of Not Yet Closed Bugs</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">ALREADY_CLOSED_MIN</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">ALREADY_CLOSED_MAX</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">NOTYET_CLOSED_MIN</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">NOTYET_CLOSED_MAX</Data></Cell>");
		xlsfile.writeFile("</Row>\n");
		// rows for each week
		Calendar c = UD.get1erJour(annee, mois);
		CptRow = 0;
		while (c.before(today)) {			
			xlsfile.writeFile(displayRowResolution(c));
			c.add(Calendar.DAY_OF_MONTH, 7);
		}
		xlsfile.writeFile(displayRowResolution(today));
		xlsfile.writeFile("</Table></Worksheet>\n");
		zst.NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
				"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
				(CptRow + 1) + "C5\"/>\n");
	}

	private String displayRowResolution(Calendar now) throws Exception {
		//L.myPrintln("-_-_-_-_" + noTag + " " + UD.print(now));
		//openedArray used for counters
		StringBuffer sb = new StringBuffer("");
		if (activeProject(now, null)) {
			CptRow++;
			sb = new StringBuffer("<Row>" + UD.printXLS(now));
			ArrayList<Float> stateArray = getResolution(now);
			sb.append(ux.printXLS(stateArray.get(ALREADY_CLOSED_BUGS).intValue()));
			sb.append(ux.printXLS(stateArray.get(NOTYET_CLOSED_BUGS).intValue()));
			sb.append(ux.printXLS(stateArray.get(ALREADY_CLOSED_MEAN).intValue()));
			sb.append(ux.printXLS(stateArray.get(NOTYET_CLOSED_MEAN).intValue()));
			sb.append(ux.printXLS(stateArray.get(ALREADY_CLOSED_MIN).intValue()));
			sb.append(ux.printXLS(stateArray.get(ALREADY_CLOSED_MAX).intValue()));
			sb.append(ux.printXLS(stateArray.get(NOTYET_CLOSED_MIN).intValue()));
			sb.append(ux.printXLS(stateArray.get(NOTYET_CLOSED_MAX).intValue()));
			sb.append("</Row>\n");
		} else {
			//L.myPrintln("no bug opened!");
		}
		return sb.toString();
	}

	private ArrayList<Float> getResolution(Calendar c_now) throws Exception {
		L.myPrintln("-_-_-_-_" + noTag + " " + UD.print(c_now));
		ArrayList<Float> stateArray = new ArrayList<Float>();
		stateArray.add(new Float(0 ));
		stateArray.add(new Float(Float.MAX_VALUE ));
		stateArray.add(new Float(0 ));
		stateArray.add(new Float(0 ));
		stateArray.add(new Float(0 ));
		stateArray.add(new Float(Float.MAX_VALUE ));
		stateArray.add(new Float(0 ));
		stateArray.add(new Float(0 ));
		Iterator<XmlTag> i = defects.iterator();
		int i_cptAlreadyClosed=0;
		int i_cptNotYetClosed=0;
		while(i.hasNext()){
			Defect d = (Defect)i.next();
			Float f_duration=d.getResolutionDuration(c_now);
			int i_defectSatus= d.getState(c_now, false);
			if((i_defectSatus!=STATUS_DONTCARE)&&(d.getPriority()<STATUS_OPEN_P2)){
				L.myPrintln("  ["+d.getDefectNumber()+"] dur:" + d.getResolutionDuration(c_now) + " dst:" + i_defectSatus +" ac:"+i_cptAlreadyClosed+" nyc:"+i_cptNotYetClosed);
				if(i_defectSatus>=STATUS_DEFERRED) {
//					f_duration=d.getResolutionDuration(c_now);
					Float f_min = stateArray.get(ALREADY_CLOSED_MIN);
					if(f_duration<f_min){
						stateArray.set(ALREADY_CLOSED_MIN, f_duration);
					}				
					Float f_max = stateArray.get(ALREADY_CLOSED_MAX);
					if(f_duration>f_max){
						stateArray.set(ALREADY_CLOSED_MAX, f_duration);
					}	
					Float f_mean = stateArray.get(ALREADY_CLOSED_MEAN);
					stateArray.set(ALREADY_CLOSED_MEAN, (f_duration+(f_mean*i_cptAlreadyClosed))/(i_cptAlreadyClosed+1));
//					L.myPrintln("  ["+d.getDefectNumber()+"] dur:" + d.getResolutionDuration(c_now) + " dst:" + i_defectSatus +" ac:"+i_cptAlreadyClosed+" nyc:"+i_cptNotYetClosed);
					L.myPrintln("  acmin:" + stateArray.get(ALREADY_CLOSED_MIN) + 
							    " acmean:" + stateArray.get(ALREADY_CLOSED_MEAN) +
							     " acmax:" + stateArray.get(ALREADY_CLOSED_MAX));
					i_cptAlreadyClosed++;
				} else {
					Float f_min = stateArray.get(NOTYET_CLOSED_MIN);
					if(f_duration<f_min){
						stateArray.set(NOTYET_CLOSED_MIN, f_duration);
					}				
					Float f_max = stateArray.get(NOTYET_CLOSED_MAX);
					if(f_duration>f_max){
						stateArray.set(NOTYET_CLOSED_MAX, f_duration);
					}	
					Float f_mean = stateArray.get(NOTYET_CLOSED_MEAN);
					stateArray.set(NOTYET_CLOSED_MEAN, (f_duration+(f_mean*i_cptNotYetClosed))/(i_cptNotYetClosed+1));
					L.myPrintln("                                                        nycmin:" + stateArray.get(NOTYET_CLOSED_MIN) + 
						    	" nycmean:" + stateArray.get(NOTYET_CLOSED_MEAN) +
						         " nycmax:" + stateArray.get(NOTYET_CLOSED_MAX));
					i_cptNotYetClosed++;
				}		
			}
		}
		if(i_cptNotYetClosed==0){
			stateArray.set(NOTYET_CLOSED_MIN, new Float(0));
		}
		if(i_cptAlreadyClosed==0){
			stateArray.set(ALREADY_CLOSED_MIN, new Float(0));
		}
		stateArray.set(NOTYET_CLOSED_BUGS,new Float(i_cptNotYetClosed));
		stateArray.set(ALREADY_CLOSED_BUGS,new Float(i_cptAlreadyClosed));
		return stateArray;
	}

	// crop defects owned by project and compute if active
	public void cropDefects(ChildList zdefects) throws Exception {
		if(noTag.startsWith("Z_") ||
				noTag.startsWith("Y_") || 
				noTag.startsWith("x")) {
			return;
		}
		java.util.List<Integer> stateArray = new ArrayList<Integer>();
		for (int i = 0; i < STATUS_MAX; i++) {
			stateArray.add(0);
		}

		Iterator<XmlTag> i = zdefects.iterator();
		while(i.hasNext()){
			Defect d = (Defect)i.next();
			if (d.passFilter(this)){
				defects.add(d);
				int state = d.getState(Calendar.getInstance(), false);
				int counter = stateArray.get(state);
				stateArray.set(state, ++counter);

			}
		}
		int BugOpened = 0;
		for (int countIndex = STATUS_RELEASED; countIndex >=0; countIndex--) {
			BugOpened += stateArray.get(countIndex);
		}
		// count all bugs
		int BugAll = 0;
		for (int countIndex = STATUS_DONTCARE; countIndex >=0; countIndex--) {
			BugAll += stateArray.get(countIndex);
		}
		L.myPrintln(noTag+" "+UD.print(Calendar.getInstance()) + "BugOpened[" + BugOpened+"] BugAll["+BugAll+"]");
		//		DecimalFormat format = new DecimalFormat("0000");
		//		addAttribute("BugAllAsOfToday",format.format(BugAll)); // used for a compare later
		BugRank=(BugOpened*1000)+BugAll;
	}
	public static class  compare2 implements Comparator<XmlTag> {
		public int compare(XmlTag o1, XmlTag o2) {
			Project p1 = (Project)o1;
			Project p2 = (Project)o2;
			return (p2.BugRank-p1.BugRank);
		}}
}
