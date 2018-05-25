import java.io.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    March 2, 2006
 */
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
   *@param  xlsfile        Description of the Parameter
   *@exception  Exception  Description of the Exception
   */
  void xlsfileheader(int xlsfile) throws Exception {
    L.f.writeFile(xlsfile, "<?xml version=\"1.0\"?><?mso-application progid=\"Excel.Sheet\"?>\n<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
    L.f.writeFile(xlsfile, " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
    L.f.writeFile(xlsfile, " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">\n");
    L.f.writeFile(xlsfile, " <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">");
    L.f.writeFile(xlsfile, " <Title>Change Trend</Title>");
    L.f.writeFile(xlsfile, " <Subject>Change Trend</Subject>");
    L.f.writeFile(xlsfile, " <Author>Eric Mariacher</Author>");
    L.f.writeFile(xlsfile, " </DocumentProperties>\n");
    L.f.writeFile(xlsfile, " <Styles>\n");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"Default\" ss:Name=\"Normal\">");
    L.f.writeFile(xlsfile, "   <Alignment ss:Vertical=\"Bottom\" ss:WrapText=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Borders/>");
    L.f.writeFile(xlsfile, "   <Font/>");
    L.f.writeFile(xlsfile, "   <Interior/>");
    L.f.writeFile(xlsfile, "   <NumberFormat/>");
    L.f.writeFile(xlsfile, "   <Protection/>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"sdefined\">");
    L.f.writeFile(xlsfile, "   <Interior ss:Color=\"#f5ffb3\" ss:Pattern=\"Solid\"/>");
    L.f.writeFile(xlsfile, "   <Borders>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "   </Borders>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"head\">");
    L.f.writeFile(xlsfile, "   <Font ss:Bold=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Interior ss:Color=\"#f5ffb3\" ss:Pattern=\"Solid\"/>");
    L.f.writeFile(xlsfile, "   <Borders>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "   </Borders>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"headf\">");
    L.f.writeFile(xlsfile, "   <Font ss:Bold=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Alignment ss:Vertical=\"Center\" ss:WrapText=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Borders>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "   </Borders>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"headv\">");
    L.f.writeFile(xlsfile, "   <Font ss:Bold=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Alignment ss:Vertical=\"Bottom\" ss:Rotate=\"90\"/>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, " </Styles>\n");
  }
}

