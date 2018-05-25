import java.util.*;


/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    May 2, 2005
 */
public class SeapineStat implements DefVar {
	ChildList defects = new ChildList();
	ChildList products = new ChildList();
	UtilsDate UD = new UtilsDate();

	SeapineStatTool5 zt;
	int xlsfile_bug, xmlfile_treemap;
	StringBuffer NamedRanges;
	Log L;
	UtilsXLS ux;
	int CptRow;


	/**
	 *  Constructor for the SeapineStat object
	 *
	 *@param  zt  Description of the Parameter
	 *@param  L   Description of the Parameter
	 */
	SeapineStat(SeapineStatTool5 zt, Log L) {
		this.zt = zt;
		this.L = L;
		ux = new UtilsXLS(L);
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	void initLogFiles2() throws Exception {
		String wrkdir = L.initLogFiles();
		L.myErrPrintln("Trying to open file[" +
				wrkdir + "\\" + L.name_no_ext + ".xml] and ["+wrkdir + "\\CDBUBugsTreeMap.xml] for write access.");
		zt.AnalyzeLabel.setText("error!");
		xlsfile_bug = L.f.addFichier(wrkdir + "\\" + L.name_no_ext + ".xml");
		xmlfile_treemap = L.f.addFichier(wrkdir + "\\CDBUBugsTreeMap.xml");
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	void closeLogFiles2() throws Exception {
		L.myErrPrintln("xls files closed.");
		L.closeLogFiles();
		L.f.cleanfos(xlsfile_bug);
		L.f.cleanfos(xmlfile_treemap);
	}





	void xlsAfficheCalendrier(int annee, int mois, int xlsfile) throws Exception {
		ux.xlsfileheader(xlsfile);
		NamedRanges = new StringBuffer("<Names>");
		// stacked area worksheet for each product
		for (int i = 0; i < products.size(); i++) {
			Project xt_project = (Project) products.elementAt(i);
			if (!xt_project.inactiveProjectClosedDelayed()) {
				xlsAfficheWorksheetStackedArea(annee, mois, xt_project, xlsfile);
			}
		}
		//radar worksheet for PQA
		xlsAfficheWorksheetColumn(xlsfile);
		L.f.writeFile(xlsfile, NamedRanges.toString() + "</Names></Workbook>\n");
	}






	String getWorksheetHeader(String WorkSheetName, int TabColorIndex) {
		return new String("<Worksheet  ss:Name=\"" + WorkSheetName +
				"\"><WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\"><TabColorIndex>" +
				TabColorIndex +
		"</TabColorIndex></WorksheetOptions><Table><Column ss:AutoFitWidth=\"0\" ss:Width=\"80\"/>\n");
	}

	void xlsAfficheWorksheetStackedArea(int annee, int mois, Project xt_project, int xlsfile) throws Exception {
		String WorkSheetName = xt_project.getWorksheetName('C');
		L.f.writeFile(xlsfile, getWorksheetHeader(WorkSheetName, 44));
		//header rows
		L.f.writeFile(xlsfile, "<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"6\"/>\n");
		L.f.writeFile(xlsfile, "<Row>\n<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">date</Data></Cell>");
		for (int i = 0; i < 4; i++) {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Open_P" + i + "</Data></Cell>");
		}
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">QA</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Closed</Data></Cell>");
		L.f.writeFile(xlsfile, "</Row>\n");
		Calendar today = Calendar.getInstance();
		// rows for each week
		Calendar c = UD.get1erJour(annee, mois);
		CptRow = 0;
		while (c.before(today)) {			
			L.f.writeFile(xlsfile, xt_project.displayRowStackedArea(c, this));
			c.add(Calendar.DAY_OF_MONTH, 7);
		}
		L.f.writeFile(xlsfile, xt_project.displayRowStackedArea(today, this));
		L.f.writeFile(xlsfile, "</Table></Worksheet>\n");
		NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
				"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
				(CptRow + 1) + "C7\"/>\n");
	}


	void xlsAfficheWorksheetColumn(int xlsfile) throws Exception {
		String WorkSheetName = (new Project(new XmlTag("column","snapshot"))).getWorksheetName('R');
		L.f.writeFile(xlsfile, getWorksheetHeader(WorkSheetName, 43));
		//header rows
		L.f.writeFile(xlsfile, "<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"7\"/>\n");
		L.f.writeFile(xlsfile, "<Row>\n<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">project</Data></Cell>");
		for (int i = 0; i < 4; i++) {
			L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Open_P" + i + "</Data></Cell>");
		}
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">QA</Data></Cell>");
		L.f.writeFile(xlsfile, "<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Deferred</Data></Cell>");
		L.f.writeFile(xlsfile, "</Row>\n");
		// rows for each project
		int CptRowC = 0;
		for (int i = 0; i < products.size(); i++) {
			Project xt_project = (Project) products.elementAt(i);
			if (!xt_project.inactiveProjectClosedDelayed()) {
				CptRowC++;
				L.f.writeFile(xlsfile, xt_project.displayRowColumn());
			}
		}
		// end of worksheet
		L.f.writeFile(xlsfile, "</Table></Worksheet>\n");
		NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
				"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
				(CptRowC + 1) + "C7\"/>\n");
	}



	void xlsAfficheTreeMap(int xlsfile) throws Exception {
		L.f.writeFile(xlsfile, "<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\n");
		L.f.writeFile(xlsfile, "<!DOCTYPE root SYSTEM \"http://jtreemap.sourceforge.net/TreeMap.dtd\" >\n");
		L.f.writeFile(xlsfile, "<root><label>CDBU</label>\n");
		L.f.writeFile(xlsfile, "<branch><label>eQuad</label>\n");
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RQM", "eQuad Mice",0.31));
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RQK", "eQuad Keyboards", 0.38));
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RQR", "eQuad Receivers", 0.45));
		L.f.writeFile(xlsfile, "</branch>\n");	
		L.f.writeFile(xlsfile, "<branch><label>BlueTooth</label>\n");
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RBM", "BT Mice",0.59));
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RBK", "BT Keyboards", 0.66));
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RBR", "BT Receivers", 0.73));
		L.f.writeFile(xlsfile, "</branch>\n");	
		L.f.writeFile(xlsfile, "<branch><label>27Mhz</label>\n");
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RPM", "27Mhz Mice",0.08));
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RPK", "27Mhz Keyboards", 0.15));
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("RPR", "27Mhz Receivers",0.22));
		L.f.writeFile(xlsfile, "</branch>\n");	
		L.f.writeFile(xlsfile, displayGroupProjectTreeMap("U", "Corded Devices", 0.90));
		L.f.writeFile(xlsfile, "</root>\n");
	}


	String displayGroupProjectTreeMap(String prefix, String label) throws Exception {
		StringBuffer sb = new StringBuffer("<branch><label>"+label+"</label>\n");
		for (int i = 0; i < products.size(); i++) {
			Project xt_project = (Project) products.elementAt(i);
			if (xt_project.passFilter(prefix)) {
				sb.append(xt_project.displayProjectTreeMap(NO_HUE));
			}
		}
		sb.append("</branch>\n");
		return sb.toString();
	}
	
	String displayGroupProjectTreeMap(String prefix, String label, double hue) throws Exception {
		StringBuffer sb = new StringBuffer("<branch><label>"+label+"</label>\n");
		for (int i = 0; i < products.size(); i++) {
			Project xt_project = (Project) products.elementAt(i);
			if (xt_project.passFilter(prefix)) {
				sb.append(xt_project.displayProjectTreeMap(hue));
			}
		}
		sb.append("</branch>\n");
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
		ChildList defects_xt = new ChildList();
		saxz.getRoot().findAll("defect", defects_xt);
		ChildList products_xt = new ChildList();
		saxz.getRoot().findAll("product", products_xt);
		L.myPrintln(" Number of defects found: " + defects_xt.size());

		for (int i = 0; i < defects_xt.size(); i++) {
			L.myPrint(i + ", -20-" + ((XmlTag) defects_xt.elementAt(i)).toString());
			Defect d = new Defect((XmlTag) defects_xt.elementAt(i), this);
			L.myPrint(i + ", -21-" + d.getDefectNumber());
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
				products.add(new Project(xt,zt,L,defects));
				L.myPrintln(", " + xt.noTag);
			}
		}
		L.myPrintln("\nAvant:");
		for (int i = 0; i < products.size(); i++) {
			Project xt = (Project) products.elementAt(i);
			L.myPrint(", " + xt.noTag);
		}
		// QuickSort qs = new QuickSort();
		QuickSort.sort(products.childs);
		L.myPrintln("\nApres:");
		for (int i = 0; i < products.size(); i++) {
			Project xt = (Project) products.elementAt(i);
			L.myPrint(", " + xt.noTag);
		}
		L.myPrintln("  ...");
		xlsAfficheCalendrier(UD.getYear(UD.get1erJourDaysAgo(100)),
				UD.getMonth(UD.get1erJourDaysAgo(100)), xlsfile_bug);
		xlsAfficheTreeMap(xmlfile_treemap);
	}
}

