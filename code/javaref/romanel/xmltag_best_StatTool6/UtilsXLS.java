public class UtilsXLS {
	Log L;


	/**
	 *  Constructor for the UtilsXLS object
	 *
	 *@param  L  Description of the Parameter
	 */
	UtilsXLS(Log L) {
		this.L = L;
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

	String printXLS(int i) {
		return new String("<Cell><Data ss:Type=\"Number\">" + i + "</Data></Cell>");
	}

	/**
	 *  Description of the Method
	 *
	 *@param  xlsfile        Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void xlsfileheader(Log.MyFile xlsfile) throws Exception {
		xlsfile.writeFile("<?xml version=\"1.0\"?><?mso-application progid=\"Excel.Sheet\"?>\n<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
		xlsfile.writeFile(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
		xlsfile.writeFile(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">\n");
		xlsfile.writeFile(" <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">");
		xlsfile.writeFile(" <Title>Change Trend</Title>");
		xlsfile.writeFile(" <Subject>Change Trend</Subject>");
		xlsfile.writeFile(" <Author>Eric Mariacher</Author>");
		xlsfile.writeFile(" </DocumentProperties>\n");
		xlsfile.writeFile(" <Styles>\n");
		xlsfile.writeFile("  <Style ss:ID=\"Default\" ss:Name=\"Normal\">");
		xlsfile.writeFile("   <Alignment ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>");
		xlsfile.writeFile("   <Borders/>");
		xlsfile.writeFile("   <Font/>");
		xlsfile.writeFile("   <Interior/>");
		xlsfile.writeFile("   <NumberFormat/>");
		xlsfile.writeFile("   <Protection/>");
		xlsfile.writeFile("  </Style>");
		xlsfile.writeFile("  <Style ss:ID=\"sdefined\">");
		xlsfile.writeFile("   <Interior ss:Color=\"#f5ffb3\" ss:Pattern=\"Solid\"/>");
		xlsfile.writeFile("   <Borders>");
		xlsfile.writeFile("    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("   </Borders>");
		xlsfile.writeFile("  </Style>");
		xlsfile.writeFile("  <Style ss:ID=\"head\">");
		xlsfile.writeFile("   <Font ss:Bold=\"1\"/>");
		xlsfile.writeFile("   <Interior ss:Color=\"#f5ffb3\" ss:Pattern=\"Solid\"/>");
		xlsfile.writeFile("   <Borders>");
		xlsfile.writeFile("    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("   </Borders>");
		xlsfile.writeFile("  </Style>");
		xlsfile.writeFile("  <Style ss:ID=\"headf\">");
		xlsfile.writeFile("   <Font ss:Bold=\"1\"/>");
		xlsfile.writeFile("   <Alignment ss:Vertical=\"Center\" ss:WrapText=\"1\"/>");
		xlsfile.writeFile("   <Borders>");
		xlsfile.writeFile("    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		xlsfile.writeFile("   </Borders>");
		xlsfile.writeFile("  </Style>");
		xlsfile.writeFile("  <Style ss:ID=\"headv\">");
		xlsfile.writeFile("   <Font ss:Bold=\"1\"/>");
		xlsfile.writeFile("   <Alignment ss:Vertical=\"Bottom\" ss:Rotate=\"90\"/>");
		xlsfile.writeFile("  </Style>");
		xlsfile.writeFile(" </Styles>\n");
	}
}

