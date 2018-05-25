import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     emariach
 *@created    April 11, 2008
 */
public class SeapineStat extends XmlTag implements DefVar {
	ChildList defects_xt = new ChildList();
	ChildList defects = new ChildList();
	ChildList products_xt = new ChildList();
	ChildList products = new ChildList();
	int CptRow = 0;
	Pattern p = Pattern.compile("\\d.*");
	//illegal worksheet name if starting with a digit
	boolean atLeastABugHasAlreadyBeenOpened = false;
	UtilsDate UD = new UtilsDate();
	Calendar start_lundi = UD.get1erLundiDaysAgo(100);

	SeapineStatTool4 zt;
	int xlsfile_bug;
	StringBuffer NamedRanges;
	Defect dc, dcsave;
	Cell opened, fixed, qaed, delayed, closed;
	int start_year;
	int start_mois;
	Log L;
	UtilsXLS ux;


	/**
	 *  Constructor for the SeapineStat object
	 *
	 *@param  zt  Description of the Parameter
	 *@param  L   Description of the Parameter
	 */
	SeapineStat(SeapineStatTool4 zt, Log L) {
		this.zt = zt;
		this.L = L;
		start_year = UD.getYear(start_lundi);
		start_mois = UD.getMonth(start_lundi);
		ux = new UtilsXLS(L);
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	void initLogFiles2() throws Exception {
		String wrkdir = L.initLogFiles();
		L.myErrPrintln("Starting Stats[" + start_year + "/" + start_mois + "]");
		L.myErrPrintln("Trying to open file[" +
			wrkdir + "\\" + L.name_no_ext + ".xml] for write access.");
		zt.AnalyzeLabel.setText("error!");
		xlsfile_bug = L.f.addFichier(wrkdir + "\\" + L.name_no_ext + ".xml");
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
	 *@param  annee          Description of the Parameter
	 *@param  mois           Description of the Parameter
	 *@param  xlsfile        Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void xlsAfficheCalendrier(int annee, int mois, int xlsfile) throws Exception {
		ux.xlsfileheader(xlsfile);
		NamedRanges = new StringBuffer("<Names>");
		// worksheet for each product
		for (int i = 0; i < products.size(); i++) {
			XmlTag xt = (XmlTag) products.elementAt(i);
			// do not generate any worksheet for Z_ and Y_ products
			String product = new String(xt.noTag);
			if (projectNot2BeStated(product)) {
				continue;
			}
			if (!inactiveProjectClosedDelayed(product)) {
				xlsAfficheWorksheet(annee, mois, product,
					"Incorrect FunctionalityCrash - SystemCrash - ApplicatoinFeature not implemented", "F", xlsfile);
				xlsAfficheWorksheet(annee, mois, product,
					"CosmeticUsability", "U", xlsfile);
				xlsAfficheWorksheet(annee, mois, product,
					"Feature RequestNon-ConformanceOtherObservation", "I", xlsfile);
				xlsAfficheWorksheet(annee, mois, product,
					null, "A", xlsfile);
			}
		}
		L.f.writeFile(xlsfile, NamedRanges.toString() + "</Names></Workbook>\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  product        Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	boolean inactiveProjectClosedDelayed(String product) throws Exception {
		// rows for each week
		Calendar today = Calendar.getInstance();
		displayRow(today, product, null);
		L.myPrint(product);
		if (!atLeastABugHasAlreadyBeenOpened) {
			L.myPrintln(" INACTIVE");
			return true;
		} else {
			L.myPrintln(" ACTIVE");
			return false;
		}
	}


	/**
	 *  do not generate any worksheet for Z_ and Y_ products
	 *
	 *@param  s  Description of the Parameter
	 *@return    Description of the Return Value
	 */
	boolean projectNot2BeStated(String s) {
		return (s.startsWith("Z_") || s.startsWith("Y_") || s.startsWith("x"));
	}


	/**
	 *  Description of the Method
	 *
	 *@param  annee          Description of the Parameter
	 *@param  mois           Description of the Parameter
	 *@param  xlsfile        Description of the Parameter
	 *@param  product        Description of the Parameter
	 *@param  allowedTypes   Description of the Parameter
	 *@param  id             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void xlsAfficheWorksheet(int annee, int mois, String product,
		String allowedTypes, String id, int xlsfile) throws Exception {
		// start of worksheet
		String WorkSheetName;
		int TabColorIndex = 44;
		if (product.length() > 19) {
			WorkSheetName = new String(product.substring(product.length() - 19, product.length()) + id + "P");
		} else {
			WorkSheetName = new String(product + id + "P");
		}
		// replace space with _
		WorkSheetName = new String(WorkSheetName.trim().replaceAll("\\W", "_"));
		// add _ at start if name starts with digit
		Matcher m = p.matcher(WorkSheetName);
		if (m.find()) {
			WorkSheetName = new String("_" + WorkSheetName);
		}
		Defect dc = (Defect) defects.elementAt(0);
		L.f.writeFile(xlsfile, "<Worksheet  ss:Name=\"" + WorkSheetName +
			"\"><WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\"><TabColorIndex>" + TabColorIndex +
			"</TabColorIndex></WorksheetOptions><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"80\"/>\n");
		//header rows
		L.f.writeFile(xlsfile, "<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"" +
			dc.counters.size() + "\"/>\n");
		L.f.writeFile(xlsfile, "<Row>\n<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">date</Data></Cell>");
		for (int i = 1; i <= NUMBER_OF_PRIO; i++) {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Open_P" + i + "</Data></Cell>");
		}
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">QA</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Closed</Data></Cell>");
		L.f.writeFile(xlsfile, "</Row>\n");
		// rows for each week
		Calendar today = Calendar.getInstance();
		Calendar c = UD.get1erLundi(annee, mois);
		L.myPrintln("-_-_-_-_" + product + " [" + allowedTypes + "] " + UD.print(c));
		CptRow = 0;
		while (c.before(today)) {
			L.f.writeFile(xlsfile, displayRow(c, product, allowedTypes));
			c.add(Calendar.DAY_OF_MONTH, 7);
			if (c.after(today)) {
				L.f.writeFile(xlsfile, displayRow(today, product, allowedTypes));
				break;
			}
		}
		L.f.writeFile(xlsfile, "</Table></Worksheet>\n");
		NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
			"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
			(CptRow + 1) + "C" + (NUMBER_OF_PRIO + 3) + "\"/>\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  now            Description of the Parameter
	 *@param  product        Description of the Parameter
	 *@param  allowedTypes   Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String displayRow(Calendar now, String product, String allowedTypes) throws Exception {
		// list all seapine transition_types: 1st defect holds the counters
		dc.resetCounters();
		//openedArray used for P0, P1, P2, P3, QA, closed
		java.util.List openedArray = new ArrayList();
		for (int i = 0; i < NUMBER_OF_PRIO; i++) {
			openedArray.add(new Cell());
		}

		/*L.myPrintln("-*-*-*-*-" + UD.print(now) + "-*-*-*-*-" + product + "-*-*-*-*-"
			 + allowedTypes + "-*-*-*-*-");*/
		// for each defect update relevant counters (in 1st defect)
		for (int defectIndex = 0; defectIndex < defects.size(); defectIndex++) {
			Defect d = (Defect) defects.elementAt(defectIndex);

			/*L.myPrintln("[" + defectIndex + "][" + d.getDefectNumber() + "] (" +
			     d.cells.find("product").noTag + "/" + d.cells.find("type").noTag + ") " +
                 d.passFilter2(product, allowedTypes));*/
			if (d.passFilter2(product, allowedTypes)) {
				zt.AnalyzeLabel.setText(product + " - " + UD.print(now) +
					" - " + d.getDefectNumber());
				//zt.AnalyzeLabel.setText(report_bugdev + "-" + product);
				int stateIndexStart = d.getState(start_lundi, false);
				// get state at 1st monday
				Cell cStart = (Cell) dc.counters.elementAt(stateIndexStart);
				//L.myPrintln("\n@@@@@@@@@@@@@@@@@@@@@@@@@" + UD.print(start_lundi) + "@" + stateIndexStart + "@" + cStart.tagName + "\n" + d.toString2());
				if (cStart.isClosedState()) {
					continue;
				}
				int stateIndex = d.getState(now, false);
				Cell c = (Cell) dc.counters.elementAt(stateIndex);
				if (c.isOpenState()) {
					/*L.myPrintln("   ---------------- " + d.getDefectNumber() + " [" + stateIndex +
					   "/" + d.getPriority() + "] " + c.tagName + "-------------------" + UD.print(now) + "-");*/
					((Cell) openedArray.get(d.getPriority())).counter++;
				} else {
					/*
					 *  L.myPrintln("   ---------------- " + d.getDefectNumber() + " [" + stateIndex +
					 *  "] " + c.tagName + "-------------------" + UD.print(now) + "-");
					 */
					c.counter++;
				}
			}
		}
		// check if some bugs are already opened
		atLeastABugHasAlreadyBeenOpened = false;
		for (int countIndex = 0; countIndex < dc.counters.size(); countIndex++) {
			Cell c = (Cell) dc.counters.elementAt(countIndex);
			if ((c.isBugState()) && (c.getCounter() > 0)) {
				atLeastABugHasAlreadyBeenOpened = true;
				break;
			}
		}
		for (int i = 0; i < NUMBER_OF_PRIO; i++) {
			if (((Cell) openedArray.get(i)).counter > 0) {
				atLeastABugHasAlreadyBeenOpened = true;
				break;
			}
		}
		// print Row End
		StringBuffer sb = new StringBuffer("");
		if (atLeastABugHasAlreadyBeenOpened) {
			CptRow++;
			sb = new StringBuffer("<Row>" + UD.printXLS(now));
			qaed = new Cell();
			closed = new Cell();
			for (int countIndex = 0; countIndex < dc.counters.size(); countIndex++) {
				Cell c = (Cell) dc.counters.elementAt(countIndex);
				if (c.isQAState()) {
					qaed.incCounter(c.counter);
				} else if (c.isClosedState()) {
					closed.incCounter(c.counter);
				}
			}
			for (ListIterator li = openedArray.listIterator(); li.hasNext(); ) {
				Cell zc = (Cell) li.next();
				sb.append(zc.Counter2Xls());
			}
			sb.append(qaed.Counter2Xls() + closed.Counter2Xls());
			sb.append("</Row>\n");
		} else {
			//L.myPrintln("no bug opened!");
		}
		return sb.toString();
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
		saxz.getRoot().findAll("defect", defects_xt);
		saxz.getRoot().findAll("product", products_xt);
		L.myPrintln(" " + defects_xt.size());

		dc = new Defect((XmlTag) defects_xt.elementAt(0), this);
		dcsave = new Defect((XmlTag) defects_xt.elementAt(0), this);
		for (int i = 0; i < defects_xt.size(); i++) {
			Defect d = new Defect((XmlTag) defects_xt.elementAt(i), this);
			L.myPrint(i + ", -2-" + d.getDefectNumber());
			defects.add(d);
		}
		L.myPrintln("\nproducts1 ");
		for (int i = 0; i < products_xt.size(); i++) {
			XmlTag xt = (XmlTag) products_xt.elementAt(i);
			if (xt.noTag == null) {
				xt.noTag = new String("NA");
			}
			xt.noTag = new String(xt.noTag.replaceAll("\n", ""));
			if (products.find(xt.tagName, xt.noTag) == null) {
				products.add(xt);
				L.myPrintln(", " + xt.noTag);
			}
		}
		L.myPrintln("\nAvant:");
		for (int i = 0; i < products.size(); i++) {
			XmlTag xt = (XmlTag) products.elementAt(i);
			L.myPrint(", " + xt.noTag);
		}
		QuickSort qs = new QuickSort();
		qs.sort(products.childs);
		L.myPrintln("\nApres:");
		for (int i = 0; i < products.size(); i++) {
			XmlTag xt = (XmlTag) products.elementAt(i);
			L.myPrint(", " + xt.noTag);
		}
		L.myPrintln("  ...");
		xlsAfficheCalendrier(start_year, start_mois, xlsfile_bug);
	}
}

