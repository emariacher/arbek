import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    May 2, 2005
 */
public class SeapineStat implements DefVar {
	ArrayList<Defect> defects = new ArrayList<Defect>();
	SortedSet<Project> products_byname = new TreeSet<Project>(new Project.compareName());
	SortedSet<Project> products_bybugrank = new TreeSet<Project>(new Project.compareBugRank());


	SeapineStatTool7Parser ztp;
	Log.MyFile xlsfile_bug;
	//	int xmlfile_treemap;
	StringBuffer NamedRanges;
	Log L;
	UtilsXLS ux;
	int PROJECTS_DISPLAY_LIMIT=15;
	ProjectGroup pg_eQuadDJPlatform;
	Calendar c_start = UtilsDate.get1erJour(UtilsDate.getYear(UtilsDate.get1erJourDaysAgo(100)),
			UtilsDate.getMonth(UtilsDate.get1erJourDaysAgo(100)));


	/**
	 *  Constructor for the SeapineStat object
	 *
	 *@param  zt  Description of the Parameter
	 *@param  L   Description of the Parameter
	 */
	SeapineStat(SeapineStatTool7Parser ztp) {
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







	void xlsAfficheCalendrier(Calendar c, Log.MyFile xlsfile) throws Exception {
		ux.xlsfileheader(xlsfile);
		NamedRanges = new StringBuffer("<Names>");
		//		pg_eQuadDJPlatform.xlsAfficheWorksheetResolution(c, xlsfile);
		// stacked area worksheet for 10 products that are the most "active"
		for (Project xt_project : products_bybugrank) {
			xt_project.xlsAfficheWorksheetStackedArea(c, xlsfile);
		}
		//radar worksheet for PQA
		xlsAfficheWorksheetColumn(xlsfile);
		xlsfile.writeFile(NamedRanges.toString() + "</Names></Workbook>\n");
	}








	void xlsAfficheWorksheetColumn(Log.MyFile xlsfile) throws Exception {
		String WorkSheetName = Project.getWorksheetName("Snapshot_All",'R');
		xlsfile.writeFile(Project.getWorksheetHeader(WorkSheetName, 43));
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
		for (Project xt_project : products_bybugrank) {
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
		for (Project xt_project : products_bybugrank) {
			if (xt_project.passFilter(prefix)) {
				sb.append(xt_project.displayProjectTreeMap(NO_HUE));
			}
		}
		sb.append("</branch>\n");
		return sb.toString();
	}

	String displayGroupProjectTreeMap(String prefix, String label, double hue) throws Exception {
		StringBuffer sb = new StringBuffer("<branch><label>"+label+"</label>\n");
		for (Project xt_project : products_bybugrank) {
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
	void buildChildListsXmltag(Document dom) throws Exception {
		ztp.zt.AnalyzeLabel.setText("buildChildListsXmltag...");
		L.myPrintln("buildChildListsXmltag...");
		Element root = dom.getDocumentElement();
		NodeList defects_xt = root.getElementsByTagName("defect");
		L.myPrintln(" Number of defects found: " + defects_xt.getLength());

		if(defects_xt != null && defects_xt.getLength() > 0) {
			for(int i = 0 ; i < defects_xt.getLength();i++) {
				Element el = (Element)defects_xt.item(i);
				Defect d = new Defect(el, this);
				if(Project.oldProject(d.s_projectName)) {
					continue;
				}
				L.myPrintln(d.toString());
				defects.add(d);
			}
		}
		L.myPrintln("\nproducts1 ");
		NodeList products_xt = root.getElementsByTagName("product");
		if(products_xt != null && products_xt.getLength() > 0) {
			for(int i = 0 ; i < products_xt.getLength();i++) {
				Element el = (Element)products_xt.item(i);
				Project p = new Project(el, this);
				if(Project.oldProject(p.projectName)) {
					continue;
				}
				L.myPrintln(p.toString()+" "+products_byname.add(p));
			}
		}
		// add projects which are a bundle of product
		pg_eQuadDJPlatform = new ProjectGroup(
				new Project("A_eQuadDJPlatform", this),
				"eQuadDJ_MSERefDesign","RQK10_Mercury_DJ","RQR12_Tommy","RQR13_Ducky");
		products_byname.add(pg_eQuadDJPlatform);
		ProjectGroup pg_eQuadDJKBDs = new ProjectGroup(
				new Project("A_eQuadDJKBDs", this),
				"RQK10_Mercury_DJ","RQK13_Suzuki Refresh DJ","RQK16_UNI PC DJ",
				"RQK17_UNI MAC DJ","RQK12_Yamaha_DJ","RQK14_Gorilla20_DJ");
		products_byname.add(pg_eQuadDJKBDs);
		ProjectGroup pg_eQuadDJMSEs = new ProjectGroup(
				new Project("A_eQuadDJMSEs", this),
				"RQM16_Java","RQM15_Borneo","RQM17_Flores/Tenerife",
		"RQM18_IbizaDJ");
		products_byname.add(pg_eQuadDJMSEs);
		L.myPrintln("\nAvant:");
		for (Project xt_project : products_byname) {
			xt_project.cropDefects(defects, c_start);
			L.myPrint(", " + xt_project.projectName);
		}
		L.myErrPrintln("\nOnly top "+PROJECTS_DISPLAY_LIMIT+" most \"active\" projects are displayed:");
		SortedSet<Project> copy = new TreeSet<Project>(new Project.compareBugRank());
		copy.addAll(products_byname);
		products_bybugrank.addAll(copy.headSet((Project) (Arrays.asList(copy.toArray())).get(PROJECTS_DISPLAY_LIMIT)));
		L.myErrPrint(products_bybugrank.toString());
		L.myErrPrintln("  ...");
		xlsAfficheCalendrier(c_start, xlsfile_bug);
		//		xlsAfficheTreeMap(xmlfile_treemap);
		//		UtilsDate.measure();
		//		L.myPrintln("\n" + UtilsDate.toString());
	}
}

