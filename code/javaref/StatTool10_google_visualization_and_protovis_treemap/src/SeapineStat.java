import java.util.*;

import static ch.lambdaj.Lambda.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    May 2, 2005
 *$Log$
 *emariacher - Wednesday, January 12, 2011 1:55:32 PM
 *yet an effort on labelcolor
 *emariacher - Wednesday, January 12, 2011 1:32:37 PM
 *make labels more visible
 *emariacher - Wednesday, January 12, 2011 12:50:06 PM
 *still need to change windows.alert by something less intrusive
 */
public class SeapineStat implements DefVar {
	ArrayList<Defect> defects = new ArrayList<Defect>();
	SortedSet<Project> products_byname = new TreeSet<Project>(new Project.compareName());
	SortedSet<Project> products_bybugrank = new TreeSet<Project>(new Project.compareBugRank());
	SortedSet<Project> allproducts_bybugrank = new TreeSet<Project>(new Project.compareBugRank());


	SeapineStatTool10Parser ztp;
	Log.MyFile xlsfile_bug;
	Log.MyFile xmlfile_treemap;
	Log.MyFile htmlfile_treemap;
	Log.MyFile htmlfile_dynamic_views;
	StringBuffer NamedRanges;
	Log L;
	UtilsXLS ux;
	int PROJECTS_DISPLAY_LIMIT=6;
	ProjectGroup pg_eQuadDJPlatform;
	Calendar c_start = UtilsDate.get1erJour(UtilsDate.getYear(UtilsDate.get1erJourDaysAgo(100)),
			UtilsDate.getMonth(UtilsDate.get1erJourDaysAgo(100)));


	/**
	 *  Constructor for the SeapineStat object
	 *
	 *@param  zt  Description of the Parameter
	 *@param  L   Description of the Parameter
	 */
	SeapineStat(SeapineStatTool10Parser ztp) {
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
		xmlfile_treemap = L.getMyFile(wrkdir + "\\CDBUBugsTreeMap.xml");
		htmlfile_treemap = L.getMyFile(wrkdir + "\\treemap.html");
		htmlfile_dynamic_views = L.getMyFile(wrkdir + "\\dynamic_views.html");
	}







	void xlsAfficheCalendrier(Calendar c, Log.MyFile xlsfile) throws Exception {
		ux.xlsfileheader(xlsfile);
		NamedRanges = new StringBuffer("<Names>");
		//		pg_eQuadDJPlatform.xlsAfficheWorksheetResolution(c, xlsfile);
		// stacked area worksheet for 10 products that are the most "active"
		//		for (Project xt_project : products_bybugrank) {
		//			xt_project.xlsAfficheWorksheetStackedArea(c, xlsfile);
		//		}
		forEach(products_bybugrank).xlsAfficheWorksheetStackedArea(c, xlsfile);
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
		int CptRowC = products_bybugrank.toArray().length;
		//		for (Project xt_project : products_bybugrank) {
		//			CptRowC++;
		//			xlsfile.writeFile(xt_project.displayRowColumn());
		//		}
		forEach(products_bybugrank).displayRowColumn(xlsfile);
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
		//		for (Project xt_project : products_bybugrank) {
		//			if (xt_project.passFilter(prefix)) {
		//				sb.append(xt_project.displayProjectTreeMap(NO_HUE));
		//			}
		//		}
		List<Project> l_prefix = select(products_bybugrank, having(on(Project.class).passFilter(prefix)));
		if(!l_prefix.isEmpty()) {
			forEach(l_prefix).displayProjectTreeMap(sb,NO_HUE);
		}
		sb.append("</branch>\n");
		return sb.toString();
	}

	String displayGroupProjectTreeMap(String prefix, String label, double hue) throws Exception {
		StringBuffer sb = new StringBuffer("<branch><label>"+label+"</label>\n");
		//		for (Project xt_project : products_bybugrank) {
		//			if (xt_project.passFilter(prefix)) {
		//				sb.append(xt_project.displayProjectTreeMap(hue));
		//			}
		//		}
		List<Project> l_prefix = select(products_bybugrank, having(on(Project.class).passFilter(prefix)));
		if(!l_prefix.isEmpty()) {
			forEach(l_prefix).displayProjectTreeMap(sb,hue);
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
		//L.myErrPrintln(L.getJarFileCompilationDate());
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
		/* add projects which are a bundle of product
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
				"RQM18_IbizaDJ","TOG");
		products_byname.add(pg_eQuadDJMSEs);*/
		L.myPrintln("\nAvant:");

		//		for (Project xt_project : products_byname) {
		//			xt_project.cropDefects(defects, c_start);
		//			L.myPrint(", " + xt_project.projectName);
		//		}
		forEach(products_byname).cropDefects(defects, c_start);
		int i_displayLimit = java.lang.Math.min(PROJECTS_DISPLAY_LIMIT,products_byname.size()-1);
		L.myErrPrintln("\nOnly top "+i_displayLimit+" most \"active\" projects are displayed:");
		allproducts_bybugrank.addAll(products_byname);
		L.myPrintln(products_byname.size()+" --- "+products_byname.toString());
		products_bybugrank.addAll(allproducts_bybugrank.headSet((Project) (Arrays.asList(allproducts_bybugrank.toArray())).get(i_displayLimit)));
		L.myErrPrint(products_bybugrank.toString());
		L.myErrPrintln("  ...");
		xlsAfficheCalendrier(c_start, xlsfile_bug);
		xlsAfficheTreeMap(xmlfile_treemap);
//		googleChartSummaryImg(htmlfile_static_overview);
		googleChartsJS(c_start, htmlfile_dynamic_views);
		htmlfile_treemap.writeFile(ProtovisTreeMap());
	}


	private String WordCloud(Log.MyFile file) throws Exception {
		String s_div = new String("s_div_"+(new Random()).nextInt());
		file.writeFile("google.setOnLoadCallback(drawWordCloud);\n");
		file.writeFile("function drawWordCloud() {\n");
		file.writeFile("var data = new google.visualization.DataTable();\n");
		file.writeFile("var raw_data = [\n");
		forEach(products_byname).jsNewBugsText(file);
		file.writeFile("\n];\n");
		file.writeFile("data.addColumn('string', 'Product');");
		file.writeFile("data.addColumn('string', 'Summary');");
		file.writeFile("data.addRows(raw_data.length);\n");
		file.writeFile("for (var i = 0; i  < raw_data.length; ++i) {\n");
		file.writeFile("  for (var j = 0; j  < 2; ++j) {\n");
		file.writeFile("    data.setCell(i, j, raw_data[i][j]);\n}}\n");
		file.writeFile("var outputDiv = document.getElementById('"+s_div+"');");
		file.writeFile("var wc = new WordCloud(outputDiv);");
		file.writeFile("wc.draw(data, null);");	
		file.writeFile("\n}\n");
		return s_div;
	}
	
//	private String TreeMap(Log.MyFile file) throws Exception {
//		String s_div = new String("s_div_"+(new Random()).nextInt());
//		file.writeFile("google.setOnLoadCallback(drawTreeMap);\n");
//		file.writeFile("function drawTreeMap() {\n");
//		file.writeFile("var data = new google.visualization.DataTable();\n");
//		file.writeFile("data.addColumn('string', 'Product');");
//		file.writeFile("data.addColumn('string', 'Parent');");
//		file.writeFile("data.addColumn('number', 'Number of bugs (size)');");
//		file.writeFile("data.addColumn('number', 'Bug risk (color)');");
//		file.writeFile("data.addRows([\n");
//		file.writeFile("[\"CDBU_FW_issues\",null,0,0],\n");
//		forEach(products_byname).jsTreeMap(file);
//		file.writeFile("\n]);\n");
//		file.writeFile("var tree = new google.visualization.TreeMap(document.getElementById('"+s_div+"'));");
//		file.writeFile("tree.draw(data, {minColor: '#0d0',midColor: '#ddd',maxColor: '#f00',headerHeight: 15,fontColor: 'black',showScale: true});");
//		file.writeFile("\n}\n");
//		return s_div;
//	}
	
	private void googleChartsJS(Calendar c_start, Log.MyFile file) throws Exception {
		file.writeFile("<html><head>");
		file.writeFile("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.css\"/>");
		file.writeFile("<script type=\"text/javascript\" src=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.js\"></script>");
		file.writeFile("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>\n");
		file.writeFile("<script type=\"text/javascript\">\n");
		file.writeFile("google.load(\"visualization\", \"1\");");
		String s_wcdiv = WordCloud(file);
		file.writeFile("</script>\n");		
		file.writeFile("<TITLE>TTPro Issues "+Log.printToday("ddMMMyy HH:mm")+"</TITLE>\n");
		file.writeFile("</head><body>\n");		
		file.writeFile("<h1>TTPro Issues "+Log.printToday("ddMMMyy HH:mm")+"</h1>\n");
		file.writeFile("<h2>Active Projects</h2>\n");
		googleChartSummaryImg(file);
		file.writeFile("<h2>Cloud of Keywords appearing in issues opened during last month</h2>\n");
		file.writeFile("<table border=\"1\"><tr><td>\n");
		file.writeFile("Bigger font means more keyword occurence.\n");
		file.writeFile("</td></tr><tr><td>\n");
		file.writeFile("<div id=\""+s_wcdiv+"\"></div>\n");
		file.writeFile("</td></tr></table>\n");
		file.writeFile("<h2>Project trends</h2>\n");
		file.writeFile("<table border=\"1\">\n");
		forEach(products_bybugrank).jsProjectTrend(c_start, file);
		file.writeFile("</body></html>\n");
	}


//	private void HistoryPQAHotProjects(Log.MyFile file) {
//		String s_div = new String("s_div_"+(new Random()).nextInt());
//		file.writeFile("<script type=\"text/javascript\">\n");
//		file.writeFile("google.load(\"visualization\", \"1\", {packages:[\"motionchart\"]});");
//		file.writeFile("google.setOnLoadCallback(drawMotion);\n");
//		file.writeFile("function drawMotion() {\n");
//		file.writeFile("var data = new google.visualization.DataTable();\n");
//		file.writeFile("data.addColumn('string', 'Project');\n");
//		file.writeFile("data.addColumn('date', 'Date');\n");
//		file.writeFile("data.addColumn('number', 'open Bugs Activity');\n");
//		file.writeFile("data.addColumn('number', 'All Bugs');\n");
//		file.writeFile("data.addColumn('string', 'Project Type');\n");
//		file.writeFile("data.addRows([\n");
//		forEach(products_bybugrank).jsMotion(c_start, file);
//		file.writeFile("\n]);\n");
//		file.writeFile("var Motion = new google.visualization.MotionChart(document.getElementById('"+s_div+"'));");
//		file.writeFile("Motion.draw(data, {width: 600, height:300});");	
//		file.writeFile("\n}\n");
//		file.writeFile("</script>\n");
//		file.writeFile("<div id=\""+s_div+"\" style=\"width: 600px; height: 300px;\"></div>\n");
//	}


	private void googleChartSummaryImg(Log.MyFile file) throws Exception {
		String s_div = new String("s_div_"+(new Random()).nextInt());
		file.writeFile("<script type=\"text/javascript\">\n");
		file.writeFile("google.load(\"visualization\", \"1\", {packages:[\"imagebarchart\"]});");
		file.writeFile("google.setOnLoadCallback(drawChart);\n");
		file.writeFile("function drawChart() {\n");
		file.writeFile("var data = new google.visualization.DataTable();\n");
		file.writeFile("var raw_data = [\n");
		forEach(products_bybugrank).jsBugStack(file);
		file.writeFile("\n];\n");
		file.writeFile("data.addColumn('string', 'Product');");
		file.writeFile("data.addColumn('number', 'Open P0');");
		file.writeFile("data.addColumn('number', 'Open P1');");
		file.writeFile("data.addColumn('number', 'Open P2');");
		file.writeFile("data.addColumn('number', 'Open P3');");
		file.writeFile("data.addColumn('number', 'QA');");
		file.writeFile("data.addColumn('number', 'Deferred');\n");
		file.writeFile("data.addRows(raw_data.length);\n");
		file.writeFile("for (var i = 0; i  < raw_data.length; ++i) {\n");
		file.writeFile("  for (var j = 0; j  < 7; ++j) {\n");
		file.writeFile("    data.setValue(i, j, raw_data[i][j]);\n}}\n");
		file.writeFile("var chart = new google.visualization.ImageBarChart(document.getElementById('"+s_div+"'));");
		file.writeFile("chart.draw(data, {title:\"Issues by project\", width: 1000, height: 2000, min: 0, isVertical: true, ");
		file.writeFile("colors: ['#FF0000','#993333','#DD6666','#FF9999','#FF9900','#009900']});}\n");
		file.writeFile("</script>\n");
		file.writeFile("<div id=\""+s_div+"\"></div>\n");
	}
	
	private String ProtovisTreeMap() throws Exception {
		StringBuffer sb = new StringBuffer("<html><head>");
		sb.append("<TITLE>Bugs Treemap "+Log.printToday("ddMMMyy HH:mm")+"</TITLE>\n");
		sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://vis.stanford.edu/protovis/ex/ex.css?3.2\"/>\n");		
		sb.append("<script type=\"text/javascript\" src=\"http://vis.stanford.edu/protovis/protovis-r3.2.js\"></script>\n");		
		sb.append("<style type=\"text/css\">\n");		
		sb.append("#fig {  width: 860px;}\n");		
		sb.append("#title {  float: right;  text-align: right;}\n");		
		sb.append("</style>\n");		
		sb.append("</head><body><h1>Bugs Treemap "+Log.printToday("ddMMMyy HH:mm")+"</h1>\n");		
		sb.append("<div id=\"fig\">\n<script type=\"text/javascript+protovis\">\n");		
		sb.append("var tree = {\n");
		int i_biggestBugRank = products_bybugrank.first().BugRank;
		SortedSet<Project> l_products_bybugrank = new TreeSet<Project>(allproducts_bybugrank);
		l_products_bybugrank = treeGroup4pv("RB", sb, l_products_bybugrank, i_biggestBugRank);		
		l_products_bybugrank = treeGroup4pv("RQ", sb, l_products_bybugrank, i_biggestBugRank);		
		treeGroup4pv(null, sb, l_products_bybugrank, i_biggestBugRank);		
		sb.append("};\n");		
		sb.append("function title(d) { return d.parentNode ? (title(d.parentNode) + \".\" + d.nodeName) : d.nodeName;}\n");		
		sb.append("function getDarkness(d) {if(d.nodeName.lastIndexOf(\"_\")<0) {return 0;} else " +
		"{return parseInt(d.nodeName.substr(d.nodeName.lastIndexOf(\"_\")+1))/100;}}\n");		
		sb.append("function getAlpha(d) {if(d.nodeName.lastIndexOf(\"_\")<0) {return 0.1;} else " +
		"{return 0.2 + parseInt(d.nodeName.substr(d.nodeName.lastIndexOf(\"_\")+1))/50;}}\n");		
		sb.append("function getLabel(d) { return d.nodeName.substr(0,d.nodeName.indexOf(\"ç\"));}\n");		
		sb.append("function getLabelColor(d) { return getDarkness(d)>.35 ? pv.rgb(255, 255, 255, 1) : pv.rgb(0, 0, 0, 1); }\n");		
		sb.append("function message(d) { return getLabel(d)+\", total number of bugs: \"+d.nodeValue+\", Open bugs: \"+getBugs(d); }\n");		
		sb.append("function getBugs(d) { a_bugs = d.nodeName.split(\"ç\"); return \"P0:\"+a_bugs[1]+\", P1:\"+a_bugs[2]+\", P2:\"+a_bugs[3]+\", P3:\"+a_bugs[4]; }\n");		
		sb.append("function getColor(d) { return color(d).darker(getDarkness(d)).darker(getDarkness(d)).darker(getDarkness(d)).alpha(getAlpha(d)); }\n");		
		sb.append("pv.Colors.categoryEric = function() {\n");		
		sb.append(" var scale = pv.colors( \"#ff0000\", \"#00ff00\", \"#0000ff\", \"#ff00ff\", \"#ffff00\",\"#00ffff\", \"#ffffff\", \"#ffff7f\", \"#7fffff\", \"#ff7fff\");\n");		
		sb.append(" scale.domain.apply(scale, arguments);\n");		
		sb.append(" return scale;};\n");		
		sb.append("var re = \"\",\n");		
		sb.append(" color = pv.Colors.categoryEric().by(function(d) d.parentNode.nodeName)\n");		
		sb.append(" nodes = pv.dom(tree).root(\"tree\").nodes();\n");		
		sb.append("var vis = new pv.Panel()\n");		
		sb.append(" .width(860)\n");		
		sb.append(" .height(568);\n");		
		sb.append("var treemap = vis.add(pv.Layout.Treemap)\n");		
		sb.append(" .nodes(nodes)\n");		
		sb.append(" .round(true);\n");		
		sb.append("treemap.leaf.add(pv.Panel)\n");		
		sb.append(" .fillStyle(getColor)\n");		
		sb.append(" .strokeStyle(\"#fff\")\n");		
		sb.append(" .lineWidth(1)\n");		
		sb.append(" .title(message)\n");		
		sb.append(" .event(\"click\", function() self.location = \"dynamic_views.html\")\n");		
		sb.append(" .antialias(false);\n");		
		sb.append("treemap.label.add(pv.Label)\n");		
		sb.append(" .textStyle(getLabelColor)\n");		
		sb.append(" .text(getLabel);\n");		
		sb.append("vis.render();\n");		
		sb.append("</script></div>\n");		
		sb.append("</body></html>\n");
		return sb.toString();
	}


	private SortedSet<Project> treeGroup4pv(String s_techno,StringBuffer sb, SortedSet<Project> l_products_bybugrank, int i_biggestBugRank) throws Exception {
		ArrayList<String> al_type = new ArrayList<String>(Arrays.asList("M","K","R","T"));
		List<Project> l_prefix;
		if(s_techno == null) {
			l_prefix = select(l_products_bybugrank, having(on(Project.class).passFilter("")));
		} else {
			l_prefix = select(l_products_bybugrank, having(on(Project.class).passFilter(s_techno)));
		}
		
		if(!l_prefix.isEmpty()) {
			sb.append(s_techno+"_:{\n");
			for(String s_type : al_type) {
				List<Project> l_projectType = select(l_products_bybugrank, having(on(Project.class).passFilter(s_techno+s_type)));
				if(!l_projectType.isEmpty()) {
					sb.append(" "+s_techno+s_type+":{\n");
					forEach(l_projectType).square4pv(sb, i_biggestBugRank);
					sb.append("  },\n");
					l_prefix.removeAll(l_projectType);
				}
			}
			// process non MKRT devices that have been removed from list a few lines above
			if(!l_prefix.isEmpty()) {
				sb.append("other:{\n");
				forEach(l_prefix).square4pv(sb, i_biggestBugRank);
				sb.append("  },\n");
			}
			sb.append(" },\n");
			l_prefix = select(l_products_bybugrank, having(on(Project.class).passFilter(s_techno)));
			l_products_bybugrank.removeAll(l_prefix);
		}
		return l_products_bybugrank;
	}
}

