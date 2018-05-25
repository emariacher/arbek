import java.io.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    April 18, 2006
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
   *@param  s              Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printString(String s) throws Exception {
    return new String("<Cell><Data ss:Type=\"String\">" + s + "</Data></Cell>");
  }


  /**
   *  Description of the Method
   *
   *@param  i              Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  String printNumber(int i) throws Exception {
    return new String("<Cell><Data ss:Type=\"Number\">" + i + "</Data></Cell>");
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
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"head2\">");
    L.f.writeFile(xlsfile, "   <Font ss:Size=\"14\" ss:Bold=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Borders>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "   </Borders>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"head3\">");
    L.f.writeFile(xlsfile, "   <Font ss:Color=\"#FF0000\" ss:Size=\"12\" ss:Bold=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Borders>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "   </Borders>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"head4\">");
    L.f.writeFile(xlsfile, "   <Font ss:Color=\"#FF0000\" ss:Size=\"12\" ss:Bold=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Borders>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "   </Borders>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"head5\">");
    L.f.writeFile(xlsfile, "   <Font ss:Color=\"#FF9900\" ss:Size=\"11\" ss:Bold=\"1\"/>");
    L.f.writeFile(xlsfile, "   <Borders>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
    L.f.writeFile(xlsfile, "   </Borders>");
    L.f.writeFile(xlsfile, "  </Style>");
    L.f.writeFile(xlsfile, "  <Style ss:ID=\"head6\">");
    L.f.writeFile(xlsfile, "   <Font ss:Color=\"#0000FF\" ss:Size=\"10\" ss:Bold=\"1\"/>");
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

