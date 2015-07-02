package bugstats

import java.util.Calendar

object ChartsGoogle {
  val colors = List("'#FF0000'","'#993333'","'#DD6666'","'#FF9999'","'#FF9900'","'#009900'")

  def htmlHeaderJustZeGraphs: String = "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.css\"/>" +
    "<script type=\"text/javascript\" src=\"http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/wc.js\"></script><script type=\"text/javascript\"" +
    " src=\"https://www.google.com/jsapi\"></script>"


  def wordCloud(input: List[(String, String)], title: String): String = {
    //http://visapi-gadgets.googlecode.com/svn/trunk/wordcloud/doc.html
    val name = title.replaceAll("\\W", "")
    var s = "<script type=\"text/javascript\">\n"
    s += "google.load(\"visualization\", \"1\");google.setOnLoadCallback(draw" + name + "WordCloud);\n"
    s += "function draw" + name + "WordCloud() {\n"
    s += "var data = new google.visualization.DataTable();\n"
    s += "var raw_data = [\n"
    s += input.map(c => "  ['" + c._1 + "','" + c._2 + "']").mkString("", ",\n", "")
    s += "];\n"
    s += "data.addColumn('string', 'text1');data.addColumn('string', 'text2');\n"
    s += "data.addRows(raw_data.length);\n"
    s += "for (var i = 0; i  < raw_data.length; ++i) {\n"
    s += "  for (var j = 0; j  < 2; ++j) {\n"
    s += "    data.setCell(i, j, raw_data[i][j]);\n"
    s += "}}\n"
    s += "var " + name + "WordCloud = document.getElementById('s_div_" + name + "WordCloud');var wc = new WordCloud(" + name + "WordCloud);wc.draw(data, null);\n}\n"
    s += "</script>\n<div id=\"s_div_" + name + "WordCloud\"></div>\n"
    s
  }

  def stackedCurve(title: String, rangees: List[(String, List[Int])], rangeeMaitresse: List[String]): String = {
    //https://google-developers.appspot.com/chart/interactive/docs/gallery/areachart
    val name = "_" + title.replaceAll("\\W", "")
    var s = "<script type=\"text/javascript\">\n"
    s += "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});google.setOnLoadCallback(draw" + name + "BugTrend);\n"
    s += "function draw" + name + "BugTrend() {\n"
    s += "var data = new google.visualization.DataTable();\n"
    s += "data.addColumn('string', 'Time');\n"
    s += rangeeMaitresse.map(ds => "data.addColumn('number', '" + ds + "');\n").mkString("")
    s += "data.addRows([\n"
    s += rangees.map(r => "  ['" + r._1 + "', " + r._2.mkString(", ") + "]").mkString("", ",\n", "")
    s += "]);\n"
    s += "var " + name + "BugTrend = new google.visualization.AreaChart(document.getElementById('div_sc_" + name + "'));" + name +
      "BugTrend.draw(data, {width: 700, height: 400, isStacked: true, title: '" + title +
      "', lineWidth: 1, pointSize: 4, colors: ["+colors.takeRight(rangeeMaitresse.length).mkString(",")+"]});\n"
    s += "}\n</script>\n<div id=\"div_sc_" + name + "\"></div>\n"
    s
  }

  def columnChart(title: String, rangees: List[(String, List[Int])], rangeeMaitresse: List[String]): String = {
    //https://google-developers.appspot.com/chart/interactive/docs/gallery/columnchart
    val name = "_" + title.replaceAll("\\W", "")
    var s = "<script type=\"text/javascript\">\n"
    s += "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});google.setOnLoadCallback(draw" + name + "ColumnChart);\n"
    s += "function draw" + name + "ColumnChart() {\n"
    s += "var data = google.visualization.arrayToDataTable([\n"
    s += "  ['Project', "
    s += rangeeMaitresse.map(ds => "'" + ds + "'").mkString(", ") + "],\n"
    s += rangees.map(r => "  ['" + r._1 + "', " + r._2.mkString(", ") + "]").mkString("", ",\n", "")
    s += "]);\n"
    s += "var options = {\n"
    s += "  title: '" + title + "',\n"
    s += "  vAxis: {title: 'Number of issues',  titleTextStyle: {color: 'black'}},\n"
    s += "  isStacked: true, width: 1000, height: 600,\n"
    s += "  colors: ["+colors.takeRight(rangeeMaitresse.length).mkString(",")+"]\n"
    s += "};\n"
    s += "var " + name + "ColumnChart = new google.visualization.ColumnChart(document.getElementById('div_cc_" + name + "'));\n"
    s += name + "ColumnChart.draw(data, options);\n}\n"
    s += "</script>\n<div id=\"div_cc_" + name + "\"></div>\n"
    s
  }

  def barChart(title: String, rangees: List[(String, List[Int])], rangeeMaitresse: List[String]): String = {
    //https://developers.google.com/chart/interactive/docs/gallery/imagebarchart
    val name = "_" + title.replaceAll("\\W", "")
    var s = "<script type=\"text/javascript\">\n"
    s += "google.load(\"visualization\", \"1\", {packages:[\"imagebarchart\"]});google.setOnLoadCallback(draw" + name + "BarChart);\n"
    s += "function draw" + name + "BarChart() {\n"
    s += "var data = new google.visualization.DataTable();\n"
    s += "var raw_data = [\n"
    s += rangees.map(r => "  ['" + r._1 + "', " + r._2.mkString(", ") + "]").mkString("", ",\n", "")
    s += "];\n"
    s += "data.addColumn('string', 'Project');\n"
    s += rangeeMaitresse.map(ds => "data.addColumn('number', '" + ds + "');\n").mkString("")
    s += "data.addRows(raw_data.length);\n"
    s += "for (var i = 0; i  < raw_data.length; ++i) {\n"
    s += "  for (var j = 0; j  < 7; ++j) {\n"
    s += "   data.setValue(i, j, raw_data[i][j]);\n"
    s += "}}\n"
    s += "var " + name + "BarChart = new google.visualization.ImageBarChart(document.getElementById('div_bc_" + name + "'));" +
      name + "BarChart.draw(data, {width: 600, height: 300, isStacked: true, title: '" +
      title + "', width: 1000, height: 2000, min: 0, isVertical: true, colors: ["+colors.takeRight(rangeeMaitresse.length).mkString(",")+"]});\n}\n"
    s += "</script>\n<div id=\"div_bc_" + name + "\"></div>\n"
    s
  }


}