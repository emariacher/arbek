



package piezo1

import kebra._
import kebra.MyLog._

object GoogleCharts {

    val htmlHeaderJustZeGraphs = "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.css\"/>\n"+
        "<script type=\"text/javascript\" src=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.js\">\n</script><script type=\"text/javascript\""+
        " src=\"https://www.google.com/jsapi\"></script>\n"

    def lineChart(title: String, rangees: List[List[String]], rangeeMaitresse: List[String], isStacked: Boolean, lineWidth: Int, pointSize: Int): String = {
        //https://google-developers.appspot.com/chart/interactive/docs/gallery/linechart
        val name = title.replaceAll(" ", "").replaceAll("\\.", "") + printToday("_dd_HH_mm_ss_SSS_")
        var s = "\n<script type=\"text/javascript\">\n"
        s += "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});google.setOnLoadCallback(draw"+name+"BugTrend);\n"
        s += "function draw"+name+"BugTrend() {\n"
        s += "  var data = new google.visualization.DataTable();\n"
        s += "  data.addColumn('string', 'Time');\n"
        s += rangeeMaitresse.map(ds => "  data.addColumn('number', '"+ds+"');\n").mkString("")
        s += "  data.addRows([\n"
        s += rangees.map(r => "    ['"+r.head+"', "+r.tail.mkString(", ")+"],").mkString("\n")
        s += "\n  ]);\n"
        s += "  var "+name+"BugTrend = new google.visualization.LineChart(document.getElementById('div_sc_"+name+"'));\n  "+name+
            "BugTrend.draw(data, {width: 1000, height: 600, isStacked: "+isStacked+", title: '"+title+
            "', lineWidth: "+lineWidth+", pointSize: "+pointSize+", colors: ['#000000','#993333','#DD6666','#FF9999','#0000FF','#00FF80']});\n"
        s += "}\n</script>\n<div id=\"div_sc_"+name+"\"></div>\n"
        s
    }
}

object D3Charts {

    def ForceDiagram(nodenames: List[(String, String, String)], links: List[(String, String, String)]) = {
        //http://bl.ocks.org/mbostock/4062045
        var s2 = "{\n"
        s2 += nodenames.map(z => "{\"nodeName\":\""+z._1+"\", \"group\":"+z._2+", \"diameter\":"+z._3+"}").mkString("\"nodes\":[\n  ", ",\n  ", "]\n")
        s2 += ",\n"
        s2 += links.map(z => "{\"source\":"+z._1+", \"target\":"+z._2+", \"value\":"+z._3.toInt+"}").mkString("\"links\":[\n  ", ",\n  ", "]\n")
        s2 += "}\n"
        copy2File("miserables.json", s2)
        copyFromFile("ProtovisForceDiagramHeader.html")
    }

    def lineChart(series: List[(String, List[Double])], rangeeMaitresse: List[String]) = {
        //http://bl.ocks.org/mbostock/3884955
        copy2File("chartSeries.csv", rangeeMaitresse.map(_.replaceAll("\\W", "_")).mkString(",")+"\n"+
            series.map(row => row._1.replaceAll("\\W", "_")+","+row._2.mkString(",")).mkString("\n"))
        copyFromFile("ProtovisLineChartHeader.html").replaceAll("xminx", series.head._1.toInt.toString).replaceAll("xmaxx", series.last._1.toInt.toString)
    }

}