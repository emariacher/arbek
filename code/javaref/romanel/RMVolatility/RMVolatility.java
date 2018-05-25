import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2006
 */
public class RMVolatility extends Thread implements DefVar {
	ChildList reqs_xt = new ChildList();
	ChildList reqs = new ChildList();
	ChildList reqbs = null;
	ChildList types_xt = new ChildList();
	ChildList types = new ChildList();
	ChildList baselines_xt = new ChildList();
	ChildList baselines = new ChildList();
	int CptRow = 0;
	Pattern p = Pattern.compile("\\d.*");
	//illegal worksheet name if starting with a digit
	boolean atLeastABugHasAlreadyBeenOpened = false;
	int start_year = 2008;
	int start_mois = Calendar.JANUARY;
	UtilsDate UD = new UtilsDate();
	String projectName = null;
	Calendar plannedGoGate = Calendar.getInstance();
	Calendar plannedPQPEntry = Calendar.getInstance();

	RMVolatilityTool zt;
	int xlsfile_bug;
	StringBuffer NamedRanges;
	Cell opened, fixed, qaed, delayed, closed;
	Log L;
	UtilsXLS ux;
	saxzefile saxz;
	String workdir;



	/**
	 *  Constructor for the RMVolatility object
	 *
	 *@param  zt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	RMVolatility(RMVolatilityTool zt) throws Exception {
		this.zt = zt;
		L = new Log(zt.err, zt.AnalyzeLabel);
		L.knowPath(zt.RMVolatilityExportTxtFile.getCanonicalPath());
		workdir = new String(initLogFiles2());
		ux = new UtilsXLS(L);
		plannedGoGate.set(2000, 0, 1);
		plannedPQPEntry.set(2000, 0, 1);
		start();
	}


	/**
	 *  Main processing method for the RMVolatility object
	 */
	public void run() {
		try {
			// PARSE...
			L.myErrPrintln("parsing " + zt.RMVolatilityExportTxtFile.getCanonicalPath());
			saxz = new saxzefile(zt.RMVolatilityExportTxtFile, L, false);
			// ANALYZE
			L.myErrPrintln("running");
			buildChildListsXmltag(saxz);
			zt.AnalyzeLabel.setText(" and Xls Report Built!");
			closeLogFiles2();
		} catch (Exception e) {
			zt.AnalyzeLabel.setText("*******Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				L.myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					L.err.append(new String(buf, 0, len));
				}

			} catch (Exception ze) {}
		} finally {

		}
	}


	/**
	 *  Description of the Method
	 *
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String initLogFiles2() throws Exception {
		String wrkdir = L.initLogFiles();
		/*
		 *  L.myErrPrintln("Trying to open file[" +
		 *  wrkdir + "\\" + L.name_no_ext + "_.xml] for write access.");
		 *  zt.AnalyzeLabel.setText("error!");
		 *  xlsfile_bug = L.f.addFichier(wrkdir + "\\" + L.name_no_ext + "_.xml");
		 */
		return wrkdir;
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	void closeLogFiles2() throws Exception {
		L.closeLogFiles();
		L.f.cleanfos(xlsfile_bug);
		L.myErrPrintln("xls files closed.");
	}



	/**
	 *  Description of the Method
	 *
	 *@param  s  Description of the Parameter
	 *@return    Description of the Return Value
	 */
	String printXLS(String s) {
		return new String("<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">" + s + "</Data></Cell>");
	}


	/**
	 *  Description of the Method
	 *
	 *@return    Description of the Return Value
	 */
	boolean ggavailable() {
		Calendar secondjanuarytwothousands = Calendar.getInstance();
		secondjanuarytwothousands.set(2000, 0, 2);
		return plannedGoGate.after(secondjanuarytwothousands);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  annee          Description of the Parameter
	 *@param  mois           Description of the Parameter
	 *@param  xlsfile        Description of the Parameter
	 *@param  report_bugdev  Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void xlsAfficheCalendrier(int annee, int mois, int report_bugdev, int xlsfile) throws Exception {
		ux.xlsfileheader(xlsfile);
		NamedRanges = new StringBuffer("<Names>");
		//xlsAfficheWorksheetVolatility(annee, mois, null, null, report_bugdev, xlsfile);
		xlsAfficheWorksheetPFETOC(xlsfile);
		for (int i = 0; i < types.size(); i++) {
			XmlTag xt = (XmlTag) types.elementAt(i);
			if (zt.volatButton.isSelected()) {
				xlsAfficheWorksheetVolatility(annee, mois, xt.tagName, xt.noTag, report_bugdev, xlsfile);
			}
			if (zt.maturButton.isSelected()) {
				xlsAfficheWorksheetMaturity(annee, mois, xt.tagName, xt.noTag, report_bugdev, xlsfile);
			}
		}
		if (zt.xtalButton.isSelected()) {
			xlsAfficheWorksheetXtalization(report_bugdev, xlsfile);
		}
		L.f.writeFile(xlsfile, NamedRanges.toString() + "</Names></Workbook>\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  annee          Description of the Parameter
	 *@param  mois           Description of the Parameter
	 *@param  tagNameFilter  Description of the Parameter
	 *@param  noTagFilter    Description of the Parameter
	 *@param  report_bugdev  Description of the Parameter
	 *@param  xlsfile        Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */

	void xlsAfficheWorksheetVolatility(int annee, int mois, String tagNameFilter,
		String noTagFilter, int report_bugdev, int xlsfile) throws Exception {
		// start of worksheet
		String WorkSheetName;
		if ((tagNameFilter == null) || (noTagFilter == null)) {
			WorkSheetName = new String("all_changesV");
		} else if (noTagFilter.length() > 19) {
			WorkSheetName = new String(noTagFilter.substring(noTagFilter.length() - 19, noTagFilter.length()) + "V");
		} else {
			WorkSheetName = new String(noTagFilter + "V");
		}
		// replace space with _
		WorkSheetName = new String(WorkSheetName.trim().replaceAll("\\W", "_"));
		//WorkSheetName = new String(WorkSheetName.trim().replaceAll("__", "_"));
		// add _ at start if name starts with digit
		Matcher m = p.matcher(WorkSheetName);
		if (m.find()) {
			WorkSheetName = new String("_" + WorkSheetName);
		}
		Req dc = (Req) reqs.elementAt(0);
		L.f.writeFile(xlsfile, "<Worksheet  ss:Name=\"" + WorkSheetName +
			"\"><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"25\"/><Column ss:AutoFitWidth=\"0\" ss:Width=\"80\"/>\n");
		L.f.writeFile(xlsfile, "<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"10\"/>\n");
		L.f.writeFile(xlsfile, "<Row>\n");
		Calendar today = Calendar.getInstance();
		Calendar c = UD.get1erJour(annee, mois);
		// prepare dynamic chart title
		if (today.before(plannedGoGate)) {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">" + WorkSheetName +
				"&#10;*PFE Phase*&#10;GoGate scheduled[" + UD.print(plannedGoGate) + "]</Data></Cell>\n");
		} else if (today.before(plannedPQPEntry)) {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">" + WorkSheetName +
				"&#10;*DEV Phase*&#10;GoGate was[" +
				UD.print(plannedGoGate) + "] PQPEntry scheduled[" + UD.print(plannedPQPEntry) + "]</Data></Cell>\n");
		} else {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">" + WorkSheetName +
				"&#10;*QA Phase*&#10;GoGate was[" +
				UD.print(plannedGoGate) + "] PQPEntry was[" + UD.print(plannedPQPEntry) + "]</Data></Cell>\n");
		}
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">date</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Changed during last week</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Changed during last Month</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Changed during last Quarter</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Did not Changed during last Quarter</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">GoGate " + UD.print(plannedGoGate) + "</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">PQPEntry " + UD.print(plannedPQPEntry) + "</Data></Cell>");
		L.f.writeFile(xlsfile, "</Row>\n");
		CptRow = 0;
		while (c.before(today)) {
			//L.myPrintln("-_-_-_-_" + UD.print(c));
			L.f.writeFile(xlsfile, displayRowVolatility(c, tagNameFilter, noTagFilter, report_bugdev));
			c.add(Calendar.DAY_OF_MONTH, 7);
			if (c.after(today)) {
				L.f.writeFile(xlsfile, displayRowVolatility(today, tagNameFilter, noTagFilter, report_bugdev));
				break;
			}
		}
		L.f.writeFile(xlsfile, "</Table></Worksheet>\n");
		NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
			"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C2:R" +
			(CptRow + 1) + "C8\"/>\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  now            Description of the Parameter
	 *@param  tagNameFilter  Description of the Parameter
	 *@param  noTagFilter    Description of the Parameter
	 *@param  report_bugdev  Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String displayRowVolatility(Calendar now, String tagNameFilter, String noTagFilter, int report_bugdev) throws Exception {
		// is row not empty? <=> has project started?
		boolean rowEmpty = true;
		Calendar oneWeekAgo = (Calendar) now.clone();
		oneWeekAgo.add(Calendar.DAY_OF_MONTH, -WEEK);
		Calendar oneMonthAgo = (Calendar) now.clone();
		oneMonthAgo.add(Calendar.DAY_OF_MONTH, -MONTH);
		Calendar oneQuarterAgo = (Calendar) now.clone();
		oneQuarterAgo.add(Calendar.DAY_OF_MONTH, -QUARTER);
		Calendar tenYearsAgo = (Calendar) now.clone();
		tenYearsAgo.add(Calendar.DAY_OF_MONTH, -TENYEARS);
		for (int reqIndex = 0; reqIndex < reqs.size(); reqIndex++) {
			Req d = (Req) reqs.elementAt(reqIndex);
			/*
			 *  L.myPrintln("1[[" + reqIndex + "][" + d.getReqNumber() + "]" +
			 *  d.passFilter(tagNameFilter, noTagFilter) + ", [" + d.changedBetween(tenYearsAgo, now) +
			 *  "] 10years ago:" + UD.print(tenYearsAgo) + " now:" + UD.print(now) + "]");
			 */
			if (d.passFilter(tagNameFilter, noTagFilter) && d.changedBetween(tenYearsAgo, now)) {
				rowEmpty = false;
				break;
			}
		}
		if (rowEmpty) {
			return new String("");
		} else {
			CptRow++;
		}
		// update counters
		int changedSinceLastWeek = 0;
		int changedSinceLastMonth = 0;
		int changedSinceLastQuarter = 0;
		int changedSinceLastTenYears = 0;

		/*
		 *  L.myPrintln("2[10years ago:" + UD.print(tenYearsAgo) +
		 *  " 1quarter ago:" + UD.print(oneQuarterAgo) +
		 *  " 1month ago:" + UD.print(oneMonthAgo) +
		 *  " 1week ago:" + UD.print(oneWeekAgo) +
		 *  " now:" + UD.print(now) + "]");
		 */
		for (int reqIndex = 0; reqIndex < reqs.size(); reqIndex++) {
			Req d = (Req) reqs.elementAt(reqIndex);
			/*
			 *  L.myPrintln("2[" + reqIndex + "][" + d.getReqNumber() + "]" +
			 *  d.passFilter(tagNameFilter, noTagFilter));
			 */
			if (d.passFilter(tagNameFilter, noTagFilter)) {
				zt.AnalyzeLabel.setText(noTagFilter + " - " + UD.print(now) +
					" - " + d.getReqNumber());
				if (d.changedBetween(oneWeekAgo, now)) {
					changedSinceLastWeek++;
				} else if (d.changedBetween(oneMonthAgo, now)) {
					changedSinceLastMonth++;
				} else if (d.changedBetween(oneQuarterAgo, now)) {
					changedSinceLastQuarter++;
				} else if (d.changedBetween(tenYearsAgo, now)) {
					changedSinceLastTenYears++;
				}
				/*
				 *  L.myPrintln(noTagFilter + " - " + d.getReqNumber() +
				 *  " ["+ changedSinceLastWeek +","+ changedSinceLastMonth +","+
				 *  changedSinceLastQuarter +","+ changedSinceLastTenYears +"]");
				 */
			}
		}
		// crache la rangée
		StringBuffer sb = new StringBuffer();
		sb.append("<Row><Cell/>" + UD.printXLS(now));

		sb.append(
			ux.printNumber(changedSinceLastWeek) + ux.printNumber(changedSinceLastMonth) +
			ux.printNumber(changedSinceLastQuarter) + ux.printNumber(changedSinceLastTenYears));

		// met la gogate ou le PQPentry si ils sont planifiés cette semaine
		if (UD.changedLastPeriod(now, plannedGoGate, WEEK)) {
			sb.append(ux.printNumber(changedSinceLastWeek + changedSinceLastMonth +
				changedSinceLastQuarter + changedSinceLastTenYears));
		} else {
			sb.append("<Cell/>");
		}
		if (UD.changedLastPeriod(now, plannedPQPEntry, WEEK)) {
			sb.append(ux.printNumber(changedSinceLastWeek + changedSinceLastMonth +
				changedSinceLastQuarter + changedSinceLastTenYears));
		} else {
			sb.append("<Cell/>");
		}

		sb.append("</Row>\n");
		return sb.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  report_bugdev  Description of the Parameter
	 *@param  xlsfile        Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void xlsAfficheWorksheetXtalization(int report_bugdev, int xlsfile) throws Exception {
		// start of worksheet
		String WorkSheetName;
		WorkSheetName = new String(projectName.replaceAll(" ", "_") + "X");
		L.f.writeFile(xlsfile, "<Worksheet  ss:Name=\"" + WorkSheetName +
			"\"><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"80\"/>\n");
		//header rows
		L.f.writeFile(xlsfile, "<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"6\"/>\n");
		L.f.writeFile(xlsfile, "<Row>\n<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">date</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Xtalization in less than 1 week</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Xtalization in less than 1 Month</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Xtalization in less than 1 Quarter</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Still liquid after 1 Quarter</Data></Cell>");
		L.f.writeFile(xlsfile, "</Row>\n");
		for (int i = 0; i < types.size(); i++) {
			XmlTag type = (XmlTag) types.elementAt(i);
			L.f.writeFile(xlsfile, displayRowXtalization("type", type.noTag, report_bugdev));
		}
		L.f.writeFile(xlsfile, "</Table></Worksheet>\n");
		NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
			"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
			(types.size() + 1) + "C5\"/>\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  tagNameFilter  Description of the Parameter
	 *@param  noTagFilter    Description of the Parameter
	 *@param  report_bugdev  Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String displayRowXtalization(String tagNameFilter, String noTagFilter, int report_bugdev) throws Exception {
		int changedSinceLastWeek = 0;
		int changedSinceLastMonth = 0;
		int changedSinceLastQuarter = 0;
		int changedSinceLastTenYears = 0;
		for (int reqIndex = 0; reqIndex < reqs.size(); reqIndex++) {
			Req d = (Req) reqs.elementAt(reqIndex);
			/*
			 *  L.myPrintln("2[" + reqIndex + "][" + d.getReqNumber() + "]" +
			 *  d.passFilter(tagNameFilter, noTagFilter) +
			 *  " w:" + changedSinceLastWeek + " m:" + changedSinceLastMonth +
			 *  " q:" + changedSinceLastQuarter + " 10y:" + changedSinceLastTenYears);
			 */
			if (d.passFilter(tagNameFilter, noTagFilter)) {
				zt.AnalyzeLabel.setText(noTagFilter + " - " + d.getReqNumber());
				int diff;
				if (ggavailable()) {
					diff = d.durationBetweenGoGateAndLastUpdate(plannedGoGate);
				} else {
					diff = d.durationBetween1stAndLastUpdate();
				}
				if (diff < WEEK) {
					changedSinceLastWeek++;
				} else if (diff < MONTH) {
					changedSinceLastMonth++;
				} else if (diff < QUARTER) {
					changedSinceLastQuarter++;
				} else if (diff < TENYEARS) {
					changedSinceLastTenYears++;
				}
			}
		}
		// crache la rangée
		return new String("<Row>" + ux.printString(noTagFilter) +
			ux.printNumber(changedSinceLastWeek) + ux.printNumber(changedSinceLastMonth) +
			ux.printNumber(changedSinceLastQuarter) + ux.printNumber(changedSinceLastTenYears) + "</Row>\n");
	}



	/**
	 *  Description of the Method
	 *
	 *@param  saxz           Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void buildChildListsXmltag(saxzefile saxz) throws Exception {
		zt.AnalyzeLabel.setText("buildChildListsXmltag...");
		L.myPrintln("buildChildListsXmltag...");
		saxz.getRoot().findAll("req", reqs_xt);
		saxz.getRoot().findAll("type", types_xt);
		saxz.getRoot().findAll("baseline", baselines_xt);

		// find project name, planned gogate and planned PQP entry
		projectName = saxz.getRoot().find("project").getAttributeValue("name");
		L.myPrintln(" projectname: " + projectName);

		SimpleDateFormat formatIn = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
		Date date = new Date();
		L.myPrintln(" looking for plannedGoGate and plannedpqpentry...");
		XmlTag xdate = saxz.getRoot().find("plannedgogate");
		String sdate = null;
		if (xdate != null) {
			sdate = xdate.noTag;
			if (sdate.length() > 16) {
				date = formatIn.parse(sdate, new ParsePosition(0));
				plannedGoGate.setTime(date);
			}
		}
		L.myPrintln(" plannedGoGate: " + UD.print(plannedGoGate));

		xdate = saxz.getRoot().find("plannedpqpentry");
		if (xdate != null) {
			sdate = xdate.noTag;
			if (sdate.length() > 16) {
				date = formatIn.parse(sdate, new ParsePosition(0));
				plannedPQPEntry.setTime(date);
			}
		}
		L.myPrintln(" plannedPQPEntry: " + UD.print(plannedPQPEntry));

		L.myPrintln(" -" + reqs_xt.size());
		//L.myPrintln("  ["+reqs_xt.toString()+"]");

		for (int i = 0; i < reqs_xt.size(); i++) {
			Req d = new Req((XmlTag) reqs_xt.elementAt(i), this);
			// L.myPrint(", -2-" + d.getReqNumber());
			// when duplicate, add only <dates>
			XmlTag x = null;
			Req r = null;
			for (int k = 0; k < reqs.size(); k++) {
				r = (Req) reqs.elementAt(k);
				x = r.find("id", d.getReqNumber());
				if (x != null) {
					break;
				}
			}
			if (x == null) {
				reqs.add(d);
				// L.myPrintln(" +" + reqs.size());
			} else {
				// L.myPrintln(" Duplicate!");
				for (int j = 0; j < d.cells.size(); j++) {
					Cell c = (Cell) d.cells.elementAt(j);
					r.cells.add(c);
				}
			}
		}

		L.myPrintln("\ntypes1 ");
		for (int i = 0; i < types_xt.size(); i++) {
			XmlTag xt = (XmlTag) types_xt.elementAt(i);
			if (xt.noTag == null) {
				xt.noTag = new String("NA");
			}
			xt.noTag = new String(xt.noTag.replaceAll("\n", ""));
			xt.noTag = new String(xt.noTag.replaceAll(" ", ""));
			if (types.find(xt.tagName, xt.noTag) == null) {
				types.add(xt);
				L.myPrintln(", " + xt.noTag);
			}
		}
		L.myPrintln("\nAvant:");
		for (int i = 0; i < types.size(); i++) {
			XmlTag xt = (XmlTag) types.elementAt(i);
			L.myPrint(", " + xt.noTag);
		}
		QuickSort qs = new QuickSort();
		qs.sort(types.childs);
		L.myPrintln("\nApres:");
		for (int i = 0; i < types.size(); i++) {
			XmlTag xt = (XmlTag) types.elementAt(i);
			L.myPrint(", " + xt.noTag);
		}

		L.myPrintln("\nbaselines1 ");
		for (int i = 0; i < baselines_xt.size(); i++) {
			Baseline b = new Baseline((XmlTag) baselines_xt.elementAt(i));
			baselines.add(b);
		}
		L.myPrintln("\nAvant:");
		for (int i = 0; i < baselines.size(); i++) {
			Baseline b = (Baseline) baselines.elementAt(i);
			L.myPrint("\n, " + b.toString2());
		}
		qs = new QuickSort();
		qs.sort(baselines.childs);
		L.myPrintln("\nApres:");
		for (int i = 0; i < baselines.size(); i++) {
			Baseline b = (Baseline) baselines.elementAt(i);
			L.myErrPrint("\n, " + b.toString2());
		}

		L.myPrintln("  ...");

		L.myErrPrintln("\nTrying to open file[" +
			workdir + "\\" + projectName + ".xml] for write access.");
		zt.AnalyzeLabel.setText("error!");
		xlsfile_bug = L.f.addFichier(workdir + "\\" + projectName + ".xml");
		xlsAfficheCalendrier(start_year, start_mois, INVALID, xlsfile_bug);
		//xlsAfficheCalendrier(start_year, start_mois, REPORT_DEV, xlsfile_dev);
	}



	/**
	 *  find the baseline with right type and right date
	 *
	 *@param  c              Description of the Parameter
	 *@param  filter         Description of the Parameter
	 *@return                The lastBaseline value
	 *@exception  Exception  Description of the Exception
	 */
	Baseline getLastBaseline(Calendar c, String filter) throws Exception {
		Baseline bsl = null;
		// find the baseline with right type and right date
		for (int bsIndex = baselines.size() - 1; bsIndex >= 0; bsIndex--) {
			bsl = (Baseline) baselines.elementAt(bsIndex);
			//L.myPrintln("32[" + bsl.getName() + "][" + bsl.date + "][" + UD.print(c) + "][" +
			//bsl.before(c) + "]     [" + filter + "][" + bsl.type(filter) + "]");
			if (bsl.before(c) && bsl.type(filter)) {
				//L.myPrintln("***************************33[" + filter + "][" + bsl.getName() + "][" +
				//bsl.date + "][" + UD.print(c) + "][" + bsl.before(c) + "][" + filter + "][" + bsl.type(filter) + "]");
				return bsl;
			}
		}
		return null;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  annee          Description of the Parameter
	 *@param  mois           Description of the Parameter
	 *@param  tagNameFilter  Description of the Parameter
	 *@param  noTagFilter    Description of the Parameter
	 *@param  report_bugdev  Description of the Parameter
	 *@param  xlsfile        Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void xlsAfficheWorksheetMaturity(int annee, int mois, String tagNameFilter,
		String noTagFilter, int report_bugdev, int xlsfile) throws Exception {
		// start of worksheet
		String WorkSheetName;
		if ((tagNameFilter == null) || (noTagFilter == null)) {
			WorkSheetName = new String("all_changesM");
		} else if (noTagFilter.length() > 19) {
			WorkSheetName = new String(noTagFilter.substring(noTagFilter.length() - 19, noTagFilter.length()) + "M");
		} else {
			WorkSheetName = new String(noTagFilter + "M");
		}
		// replace space with _
		WorkSheetName = new String(WorkSheetName.trim().replaceAll("\\W", "_"));
		//WorkSheetName = new String(WorkSheetName.trim().replaceAll("__", "_"));
		// add _ at start if name starts with digit
		Matcher m = p.matcher(WorkSheetName);
		if (m.find()) {
			WorkSheetName = new String("_" + WorkSheetName);
		}
		Req dc = (Req) reqs.elementAt(0);
		L.f.writeFile(xlsfile, "<Worksheet  ss:Name=\"" + WorkSheetName +
			"\"><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"25\"/><Column ss:AutoFitWidth=\"0\" ss:Width=\"80\"/>\n");
		//header rows
		L.f.writeFile(xlsfile, "<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"10\"/>\n");
		L.f.writeFile(xlsfile, "<Row>\n");
		Calendar today = Calendar.getInstance();
		Calendar c = UD.get1erJour(annee, mois);
		// prepare dynamic chart title
		if (today.before(plannedGoGate)) {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">" + WorkSheetName +
				"&#10;*PFE Phase*&#10;GoGate scheduled[" + UD.print(plannedGoGate) + "]</Data></Cell>\n");
		} else if (today.before(plannedPQPEntry)) {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">" + WorkSheetName +
				"&#10;*DEV Phase*&#10;GoGate was[" +
				UD.print(plannedGoGate) + "] PQPEntry scheduled[" + UD.print(plannedPQPEntry) + "]</Data></Cell>\n");
		} else {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">" + WorkSheetName +
				"&#10;*QA Phase*&#10;GoGate was[" +
				UD.print(plannedGoGate) + "] PQPEntry was[" + UD.print(plannedPQPEntry) + "]</Data></Cell>\n");
		}
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">date</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">ReqFinal</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">ReqDraft</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">ReqChanged</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">ReqDropped</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">GoGate " + UD.print(plannedGoGate) + "</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">PQPEntry " + UD.print(plannedPQPEntry) + "</Data></Cell>");
		L.f.writeFile(xlsfile, "</Row>\n");

		// rows for each week
		CptRow = 0;
		Baseline bs_old = (Baseline) baselines.elementAt(baselines.size() - 1);
		Baseline bs = null;
		reqbs = null;
		while (c.before(today)) {
			L.myPrintln("-_-_-_-_" + UD.print(c));
			bs = getLastBaseline(c, noTagFilter);
			if (bs != null) {
				reqbs = getreqChildList(bs, bs_old, reqbs);
				//detectDeletedRequirements(reqbs, bs);
				bs_old = bs;
				L.f.writeFile(xlsfile, displayRowMaturity(c, reqbs));
			} else {
				//L.myPrintln("   baselineFound[" + UD.print(c) + "]: " + baselineFound);
			}
			c.add(Calendar.DAY_OF_MONTH, 7);
			if (UD.isToday(c)) {
				bs = getLastBaseline(today, noTagFilter);
				if (bs != null) {
					reqbs = getreqChildList(bs, bs_old, reqbs);
					//detectDeletedRequirements(reqbs, bs);
					bs_old = bs;
					L.f.writeFile(xlsfile, displayRowMaturity(today, reqbs));
				}
				break;
			}
		}
		L.f.writeFile(xlsfile, "</Table></Worksheet>\n");
		NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
			"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C2:R" +
			(CptRow + 1) + "C" + (PXOTHER + 4) + "\"/>\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  now            Description of the Parameter
	 *@param  reqbs          Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String displayRowMaturity(Calendar now, ChildList reqbs) throws Exception {
		// is row not empty? <=> has project started?
		boolean rowEmpty = true;
		CptRow++;

		// update counters
		int maxP = PXOTHER + 1;
		int[] PX = new int[maxP];
		for (int m = 0; m < maxP; m++) {
			PX[m] = 0;
		}
		if (reqbs == null) {
			L.myPrintln("10[" + UD.print(now) + "][nogood!]");
		}
		//L.myPrintln("11[" + UD.print(now) + "][" + reqbs.toString() + "]");
		for (int reqIndex = 0; reqIndex < reqbs.size(); reqIndex++) {
			Req d = (Req) reqbs.elementAt(reqIndex);

			//L.myPrintln("  12[" + UD.print(now) + "][" + reqIndex + "][" + d.getReqNumber() + "]");

			zt.AnalyzeLabel.setText("M1 - " + UD.print(now) +
				" - " + d.getReqNumber());

			if (d.getPriorityFinality() == P0FINAL) {
				L.myPrintln("M1 - " + UD.print(now) + " - " + d.getReqNumber() + " - " + PX[d.getPriorityFinality()]);
				L.myPrintln(d.toString());
			}

			PX[d.getPriorityFinality()]++;

			/*
			 *  if (d.find("Priority").noTag != null) {
			 *  if (d.find("Priority").noTag.compareTo("P0") == 0) {
			 *  L.myPrint("M2 - " + d.getReqNumber() + ": ");
			 *  for (int m = 0; m < PXOTHER; m++) {
			 *  L.myPrint("[" + m + "][" + PX[m] + "] ");
			 *  }
			 *  L.myPrintln("");
			 *  /L.myPrintln(d.toString());
			 *  }
			 *  }
			 */
		}

		// crache la rangée
		StringBuffer sb = new StringBuffer();
		sb.append("<Row><Cell/>" + UD.printXLS(now));

		int total = 0;
		for (int m = 0; m < PXOTHER; m++) {
			sb.append(ux.printNumber(PX[m]));
			total += PX[m];
		}

		// met la gogate ou le PQPentry si ils sont planifiés cette semaine
		if (UD.changedLastPeriod(now, plannedGoGate, WEEK)) {
			sb.append(ux.printNumber(total));
		} else {
			sb.append("<Cell/>");
		}
		if (UD.changedLastPeriod(now, plannedPQPEntry, WEEK)) {
			sb.append(ux.printNumber(total));
		} else {
			sb.append("<Cell/>");
		}

		sb.append("</Row>\n");
		return sb.toString();
	}


	/**
	 *  if baseline is not the same as previous one "recompile" it
	 *
	 *@param  bsl            Description of the Parameter
	 *@param  bsl_old        Description of the Parameter
	 *@param  reqbsl_old     Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	ChildList getreqChildList(Baseline bsl, Baseline bsl_old, ChildList reqbsl_old) throws Exception {
		// if baseline is not the same as previous one "recompile" it
		ChildList reqbsl = null;
		if (!bsl_old.is(bsl)) {
			ChildList reqbsl_xt = new ChildList();
			reqbsl = new ChildList();
			bsl.findAll("req", null);
			bsl.findAll("req", reqbsl_xt);
			L.myPrintln("new baseline was needed: " + bsl.toString2());
			for (int i = 0; i < reqbsl_xt.size(); i++) {
				Req d = new Req((XmlTag) reqbsl_xt.elementAt(i), this);
				if (d.hasPriority()) {
					reqbsl.add(d);
					L.myPrint("before[" + d.find("id").noTag + "], ");
					//update reqs because of a weird corner case if priority of req goes from null to P1
					for (int reqIndex = 0; reqIndex < reqs.size(); reqIndex++) {
						Req d2 = (Req) reqs.elementAt(reqIndex);
						if (d2.sameID(d) && d.getPriorityFinality() < d2.getPriorityFinality()) {
							L.myPrintln(d.find("id").noTag + " Priority " + d2.getPriority() + "->" + d.getPriority());
							if (d2.find("Priority") == null) {
								d2.addChild(new XmlTag("Priority", "Required"));
							} else {
								d2.find("Priority").noTag = new String("Required");
							}
						}
					}
				}
			}
			L.myPrintln("***");
			detectDeletedRequirements(reqbsl, bsl);
			for (int i = 0; i < reqbsl.size(); i++) {
				Req d = new Req((XmlTag) reqbsl.elementAt(i), this);
				L.myPrint("after[" + d.find("id").noTag + "], ");
			}
			L.myPrintln("");
			return reqbsl;
		} else {
			return reqbsl_old;
		}

	}


	/**
	 *  Description of the Method
	 *
	 *@param  reqbs          Description of the Parameter
	 *@param  bs             Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	ChildList detectDeletedRequirements(ChildList reqbs, Baseline bs) throws Exception {
		for (int reqIndex = 0; reqIndex < reqs.size(); reqIndex++) {
			Req d = (Req) reqs.elementAt(reqIndex);
			//L.myPrintln("    QW0[" + d.find("id").noTag + "]->" + d.fromPreviousBaseline(bs));
			if (d.fromPreviousBaseline(bs)) {
				boolean notFound = true;
				for (int reqIndex2 = 0; reqIndex2 < reqbs.size(); reqIndex2++) {
					Req dbs = (Req) reqbs.elementAt(reqIndex2);
					//L.myPrintln("  QW1[" + d.find("id").noTag + "] vs [" + dbs.find("id").noTag + "]->" + d.sameID(dbs));
					if (d.sameID(dbs)) {
						notFound = false;
					}
				}
				if (notFound) {
					d.find("Priority").noTag = new String("Obsolete");
					reqbs.add(d);
					L.myPrintln("QW2 tobeadded[" + d.find("id").noTag + "]");
				}
			}
		}
		return reqbs;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  xlsfile        Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void xlsAfficheWorksheetPFETOC(int xlsfile) throws Exception {
		// start of worksheet
		String WorkSheetName;
		WorkSheetName = new String(projectName.replaceAll(" ", "_") + "P");
		L.f.writeFile(xlsfile, "<Worksheet  ss:Name=\"Table_of_Contents\"><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"100\"/>\n");
		//header rows
		L.f.writeFile(xlsfile, "<Column ss:AutoFitWidth=\"0\" ss:Width=\"100\" ss:Span=\"6\"/>\n");
		Calendar today = Calendar.getInstance();
		L.f.writeFile(xlsfile, "<Row>\n<Cell ss:StyleID=\"head2\"><Data ss:Type=\"String\">Table Of Content</Data></Cell>");
		if (today.before(plannedGoGate)) {
			L.f.writeFile(xlsfile, "<Cell ss:MergeAcross=\"4\" ss:StyleID=\"head2\"><Data ss:Type=\"String\">" + WorkSheetName +
				" *PFE Phase* GoGate scheduled[" + UD.print(plannedGoGate) + "]</Data></Cell>\n");
		} else if (today.before(plannedPQPEntry)) {
			L.f.writeFile(xlsfile, "<Cell ss:MergeAcross=\"4\" ss:StyleID=\"head2\"><Data ss:Type=\"String\">" + WorkSheetName +
				" *DEV Phase* GoGate was[" +
				UD.print(plannedGoGate) + "] PQPEntry scheduled[" + UD.print(plannedPQPEntry) + "]</Data></Cell>\n");
		} else {
			L.f.writeFile(xlsfile, "<Cell ss:MergeAcross=\"4\" ss:StyleID=\"head2\"><Data ss:Type=\"String\">" + WorkSheetName +
				" *QA Phase* GoGate was[" +
				UD.print(plannedGoGate) + "] PQPEntry was[" + UD.print(plannedPQPEntry) + "]</Data></Cell>\n");
		}

		L.f.writeFile(xlsfile, "</Row><Row/><Row><Cell/><Cell/><Cell ss:MergeAcross=\"3\" ss:StyleID=\"head3\"><Data ss:Type=\"String\">Extracted from Doors PFE Brief</Data></Cell></Row><Row>\n<Cell/><Cell/><Cell/>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"head4\"><Data ss:Type=\"String\">NUD and \"Required\" Priority</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"head5\"><Data ss:Type=\"String\">NUD and \"Desired\" Priority</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"head6\"><Data ss:Type=\"String\">NUD and \"Nice to Have\" Priority</Data></Cell>");
		L.f.writeFile(xlsfile, "</Row>\n");
		Baseline bs_current = (Baseline) baselines.elementAt(baselines.size() - 1);
		ChildList reqbsl_xt = new ChildList();
		ChildList reqbsla = new ChildList();
		bs_current.findAll("req", null);
		bs_current.findAll("req", reqbsl_xt);
		for (int i = 0; i < reqbsl_xt.size(); i++) {
			Req d = new Req((XmlTag) reqbsl_xt.elementAt(i), this);
			if (d.hasPriority()) {
				reqbsla.add(d);
			}
		}
		L.f.writeFile(xlsfile, displayTabPFETOC(reqbsla));
		L.f.writeFile(xlsfile, "</Table></Worksheet>\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  reqbs          Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String displayTabPFETOC(ChildList reqbs) throws Exception {
		if (reqbs == null) {
			L.myPrintln("010[nogood!]");
		}
		int[][] t_table = new int[PXOTHER + 1][PXOTHER + 1];

		for (int r = 0; r < MAXRISK; r++) {
			for (int p = 0; p < MAXPRIORITY; p++) {
				t_table[r][p] = 0;
			}
		}
		//L.myPrintln("011[" + UD.print(now) + "][" + reqbs.toString() + "]");
		for (int reqIndex = 0; reqIndex < reqbs.size(); reqIndex++) {
			Req d = (Req) reqbs.elementAt(reqIndex);
			/*L.myPrintln("012[" + d.toString() + "], t_table[" + d.getRisk() + "][" +
				d.getPriority() + "]=[" + t_table[d.getRisk()][d.getPriority()] +
				"] + [" + d.getNUD() + "]");*/
			t_table[d.getRisk()][d.getPriority()] += d.getNUD();
			/*L.myPrintln("013 t_table[" + d.getRisk() + "][" +
				d.getPriority() + "]=[" + t_table[d.getRisk()][d.getPriority()] +
				"] + [" + d.getNUD() + "]");*/
		}
		// crache le tableau
		StringBuffer sb = new StringBuffer();
		for (int r = 0; r < MAXRISK; r++) {
			sb.append("<Row><Cell/><Cell/>" + ((Req) reqbs.elementAt(0)).getRiskString(r));
			for (int p = 0; p < MAXPRIORITY; p++) {
				if (t_table[r][p] > 0) {
					sb.append("<Cell ss:StyleID=\"head2\"><Data ss:Type=\"String\">" +
						t_table[r][p] + "</Data></Cell>");
				} else {
					sb.append("<Cell ss:StyleID=\"Default\"><Data ss:Type=\"String\">0</Data></Cell>");
				}
			}
			sb.append("</Row>\n");
		}

		return sb.toString();
	}

}

