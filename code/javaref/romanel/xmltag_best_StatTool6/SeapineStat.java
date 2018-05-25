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

	SeapineStatTool6Parser ztp;
	Log.MyFile xlsfile_bug;
	//	int xmlfile_treemap;
	StringBuffer NamedRanges;
	Log L;
	UtilsXLS ux;
	int PROJECTS_DISPLAY_LIMIT=15;
	ProjectGroup pg_eQuadDJPlatform;


	/**
	 *  Constructor for the SeapineStat object
	 *
	 *@param  zt  Description of the Parameter
	 *@param  L   Description of the Parameter
	 */
	SeapineStat(SeapineStatTool6Parser ztp) {
		this.ztp = ztp;
		this.L = ztp.L;
		ux = new UtilsXLS(L);
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	void initLogFiles() throws Exception {
		String wrkdir = L.initLogFiles();
		ztp.zt.AnalyzeLabel.setText("error!");
		xlsfile_bug = L.getMyFile(wrkdir + "\\" + L.name_no_ext + ".xml");
		L.files.add(xlsfile_bug);
		//		xmlfile_treemap = L.f.addFichier(wrkdir + "\\CDBUBugsTreeMap.xml");
	}







	void xlsAfficheCalendrier(int annee, int mois, Log.MyFile xlsfile) throws Exception {
		ux.xlsfileheader(xlsfile);
		NamedRanges = new StringBuffer("<Names>");
		pg_eQuadDJPlatform.xlsAfficheWorksheetResolution(annee, mois, xlsfile);
		// stacked area worksheet for 10 products that are the most "active"
		Iterator<XmlTag> i = products.iterator();
		while(i.hasNext()){
			Project xt_project = (Project)i.next();
			xt_project.xlsAfficheWorksheetStackedArea(annee, mois, xlsfile);
		}
		//radar worksheet for PQA
		xlsAfficheWorksheetColumn(xlsfile);
		xlsfile.writeFile(NamedRanges.toString() + "</Names></Workbook>\n");
	}








	void xlsAfficheWorksheetColumn(Log.MyFile xlsfile) throws Exception {
		String WorkSheetName = (new Project(new XmlTag("column","snapshot"))).getWorksheetName('R');
		xlsfile.writeFile((new Project()).getWorksheetHeader(WorkSheetName, 43));
		//header rows
		xlsfile.writeFile("<Column ss:AutoFitWidth=\"0\" ss:Width=\"25\" ss:Span=\"7\"/>\n");
		xlsfile.writeFile("<Row>\n<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">project</Data></Cell>");
		for (int i = 0; i < 4; i++) {
			xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Open_P" + i + "</Data></Cell>");
		}
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">QA</Data></Cell>");
		xlsfile.writeFile("<Cell ss:StyleID=\"headv\"><Data ss:Type=\"String\">Deferred</Data></Cell>");
		xlsfile.writeFile("</Row>\n");
		// rows for each project
		int CptRowC = 0;
		Iterator<XmlTag> i = products.iterator();
		while(i.hasNext()){
			Project xt_project = (Project)i.next();
			CptRowC++;
			xlsfile.writeFile(xt_project.displayRowColumn());
		}
		// end of worksheet
		xlsfile.writeFile("</Table></Worksheet>\n");
		NamedRanges.append("<NamedRange ss:Name=\"" + WorkSheetName +
				"\" ss:RefersTo=\"=" + WorkSheetName + "!R1C1:R" +
				(CptRowC + 1) + "C7\"/>\n");
	}



	void xlsAfficheTreeMap(Log.MyFile xlsfile) throws Exception {
		xlsfile.writeFile("<?xml version=\'1.0\' encoding=\'ISO-8859-1\'?>\n");
		xlsfile.writeFile("<!DOCTYPE root SYSTEM \"http://jtreemap.sourceforge.net/TreeMap.dtd\" >\n");
		xlsfile.writeFile("<root><label>CDBU</label>\n");
		xlsfile.writeFile("<branch><label>eQuad</label>\n");
		xlsfile.writeFile(displayGroupProjectTreeMap("RQM", "eQuad Mice",0.31));
		xlsfile.writeFile(displayGroupProjectTreeMap("RQK", "eQuad Keyboards", 0.38));
		xlsfile.writeFile(displayGroupProjectTreeMap("RQR", "eQuad Receivers", 0.45));
		xlsfile.writeFile("</branch>\n");	
		xlsfile.writeFile("<branch><label>BlueTooth</label>\n");
		xlsfile.writeFile(displayGroupProjectTreeMap("RBM", "BT Mice",0.59));
		xlsfile.writeFile(displayGroupProjectTreeMap("RBK", "BT Keyboards", 0.66));
		xlsfile.writeFile(displayGroupProjectTreeMap("RBR", "BT Receivers", 0.73));
		xlsfile.writeFile("</branch>\n");	
		xlsfile.writeFile("<branch><label>27Mhz</label>\n");
		xlsfile.writeFile(displayGroupProjectTreeMap("RPM", "27Mhz Mice",0.08));
		xlsfile.writeFile(displayGroupProjectTreeMap("RPK", "27Mhz Keyboards", 0.15));
		xlsfile.writeFile(displayGroupProjectTreeMap("RPR", "27Mhz Receivers",0.22));
		xlsfile.writeFile("</branch>\n");	
		xlsfile.writeFile(displayGroupProjectTreeMap("U", "Corded Devices", 0.90));
		xlsfile.writeFile("</root>\n");
	}


	String displayGroupProjectTreeMap(String prefix, String label) throws Exception {
		StringBuffer sb = new StringBuffer("<branch><label>"+label+"</label>\n");
		Iterator<XmlTag> i = products.iterator();
		while(i.hasNext()){
			Project xt_project = (Project)i.next();
			if (xt_project.passFilter(prefix)) {
				sb.append(xt_project.displayProjectTreeMap(NO_HUE));
			}
		}
		sb.append("</branch>\n");
		return sb.toString();
	}

	String displayGroupProjectTreeMap(String prefix, String label, double hue) throws Exception {
		StringBuffer sb = new StringBuffer("<branch><label>"+label+"</label>\n");
		Iterator<XmlTag> i = products.iterator();
		while(i.hasNext()){
			Project xt_project = (Project)i.next();
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
		ztp.zt.AnalyzeLabel.setText("buildChildListsXmltag...");
		L.myPrintln("buildChildListsXmltag...");
		ChildList defects_xt = new ChildList();
		saxz.getRoot().findAll("defect", defects_xt);
		ChildList products_xt = new ChildList();
		saxz.getRoot().findAll("product", products_xt);
		L.myPrintln(" Number of defects found: " + defects_xt.size());

		Iterator<XmlTag> i = defects_xt.iterator();
		while(i.hasNext()){
			Defect d = new Defect(i.next(), this);
			//			L.myPrint(", -20-" + d.toString());
			//			L.myPrint(", -21-" + d.getDefectNumber());
			defects.add(d);
		}
		L.myPrintln("\nproducts1 ");
		i = products_xt.iterator();
		while(i.hasNext()){
			XmlTag xt = i.next();
			if (xt.noTag == null) {
				xt.noTag = new String("NA");
			}
			xt.noTag = new String(xt.noTag.replaceAll("\n", ""));
			if (products.find(xt.tagName, xt.noTag) == null) {
				products.add(new Project(xt,this));
				L.myPrintln(", " + xt.noTag);
			}
		}
		// add projects which are a bundle of product
		pg_eQuadDJPlatform = new ProjectGroup(
				new XmlTag("product","A_eQuadDJPlatform"),this,
				"eQuadDJ_MSERefDesign","RQK10_Mercury_DJ","RQR12_Tommy","RQR13_Ducky");
		products.add(pg_eQuadDJPlatform);
		ProjectGroup pg_eQuadDJKBDs = new ProjectGroup(
				new XmlTag("product","A_eQuadDJKBDs"),this,
				"RQK10_Mercury_DJ","RQK13_Suzuki Refresh DJ","RQK16_UNI PC DJ",
				"RQK17_UNI MAC DJ","RQK12_Yamaha_DJ","RQK14_Gorilla20_DJ");
		products.add(pg_eQuadDJKBDs);
		ProjectGroup pg_eQuadDJMSEs = new ProjectGroup(
				new XmlTag("product","A_eQuadDJMSEs"),this,
				"RQM16_Java","RQM15_Borneo","RQM17_Flores/Tenerife",
				"RQM18_IbizaDJ");
		products.add(pg_eQuadDJMSEs);
		L.myPrintln("\nAvant:");
		i = products.iterator();
		while(i.hasNext()){
			Project xt_project = (Project)i.next();
			xt_project.cropDefects(defects);
			L.myPrint(", " + xt_project.noTag);
		}
		// QuickSort qs = new QuickSort();
		//		QuickSort.sort(products.childs);
		//		Collections.sort(products);
		//		xfer defects in each project
		//		Collections.sort(products); use default XmlTag compareto
		Collections.sort(products, new Project.compare2());
		L.myErrPrintln("\nOnly top most \"active\" projects are displayed:");
		List<XmlTag> copy = new ArrayList<XmlTag>(products);
		i = copy.iterator();
		int cpt=0;
		while(i.hasNext()){
			Project xt_project = (Project)i.next();
			if((xt_project.BugRank>0)&&cpt<PROJECTS_DISPLAY_LIMIT){
				cpt++;
				L.myErrPrint(", " + xt_project.noTag);
			}else{
				products.remove(xt_project);
			}
		}
		L.myErrPrintln("  ...");
		xlsAfficheCalendrier(UD.getYear(UD.get1erJourDaysAgo(100)),
				UD.getMonth(UD.get1erJourDaysAgo(100)), xlsfile_bug);
		//		xlsAfficheTreeMap(xmlfile_treemap);
		UD.measure();
		L.myPrintln("\n" + UD.toString());
	}
}

