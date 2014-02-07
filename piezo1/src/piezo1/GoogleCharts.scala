package piezo1

object GoogleCharts {

    val htmlHeaderJustZeGraphs = "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.css\"/>\n"+
        "<script type=\"text/javascript\" src=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.js\">\n</script><script type=\"text/javascript\""+
        " src=\"https://www.google.com/jsapi\"></script>\n"

    def lineChart(title: String, rangees: List[List[String]], rangeeMaitresse: List[String], isStacked: Boolean, lineWidth: Int, pointSize: Int): String = {
        //https://google-developers.appspot.com/chart/interactive/docs/gallery/linechart
        val name = title.replaceAll(" ", "").replaceAll("\\.", "")
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
            "BugTrend.draw(data, {width: 1000, height: 500, isStacked: "+isStacked+", title: '"+title+
            "', lineWidth: "+lineWidth+", pointSize: "+pointSize+", colors: ['#000000','#993333','#DD6666','#FF9999','#0000FF','#00FF80']});\n"
        s += "}\n</script>\n<div id=\"div_sc_"+name+"\"></div>\n"
        s
    }
}