package fastlogsparsers

import scala.collection.immutable.ListSet

class WarnFatals(val lt: ListSet[Test], val warnFatal: String) extends Test("Summary.Summary.Occurences."+warnFatal+"_Summary") {  
  
	val lByTest = lt.map((t: Test) => (t,t.lines.filter(_.level==warnFatal))).filter(!_._2.isEmpty)
	//MyLog.L.myPrintDln(lByTest.map((c: (Test, List[Line])) => "\n***** "+c._1.name+c._2.map(_.lino).mkString("\n  ","\n  ","\n  ")).mkString("\n"))
	//MyLog.L.myPrintDln(lByTest.mkString("\n  ","\n  ","\n  "))
	val lFatals = lByTest.map((c: (Test, List[Line])) => c._2.map((_,c._1))).flatten
	//MyLog.L.myPrintDln(lFatals.map((c: (Line, Test)) => c._1.lino+" "+c._2.name).mkString("\n  ","\n  ","\n  "))
	//MyLog.L.myPrintDln(lFatals.mkString("\n  ","\n  ","\n  "))
	val lByFatal = lFatals.groupBy{_._1.lino}.toList.map((c: (String, ListSet[(Line, Test)])) => (c._1,c._2.map(_._2))).sortBy{_._2.size}.reverse
	//MyLog.L.myPrintDln(lByFatal.mkString("\n  ","\n  ","\n  "))
	
	override def toHtmlString(display: String): String = {  
	  val fglevel = Line.mfglevels.getOrElse(warnFatal, "#000000")
	  var s = "<div id=\""+hname+"\" style=\"display:"+display+"\">\n  <h2>"+name+" occurences</h2><tt>\nSuccessful tests appear in green table cells, "+
	  "while unsuccessful tests appear in red table cells. "
	  s += "</table>\n<hr/>\n<table border=\"1\">\n  "
	  s += lByFatal.map((c: (String, ListSet[Test])) => { 
	    val successesAndFailures = c._2.partition(_.result=="Success")
	    "<tr><th colspan=\"2\"><span style=\"color:"+fglevel+"\">"+c._1+"</span></th></tr>\n     "+
	    "<tr><td bgcolor=\"#e0f0e0\">"+successesAndFailures._1+"</td>\n     "+
	    "<td bgcolor=\"#f0e0e0\">"+successesAndFailures._2.map((t: Test) => withlinkString(t.hname))+"</td></tr>"
	  }).mkString("\n    ")
	  s += "</table></tt></div>"
	  s
	}  
	def toHtml2String(args: Any): (String, Int, List[String]) = {  
	  val fglevel = Line.mfglevels.getOrElse(warnFatal, "#000000")
	  var s = "\n<table border=\"1\">\n  "
	  var lf = lByFatal.filter(_._1.indexOf(args.toString)>0)
	  s += lf.map((c: (String, ListSet[Test])) => { 
	    val successesAndFailures = c._2.partition(_.result=="Success")
	    "<tr><th colspan=\"2\"><span style=\"color:"+fglevel+"\">"+c._1+"</span></th></tr>\n     "+
	    "<tr><td bgcolor=\"#e0f0e0\">"+successesAndFailures._1+"</td>\n     "+
	    "<td bgcolor=\"#f0e0e0\">"+successesAndFailures._2.map((t: Test) => withlinkString(t.hname))+"</td></tr>"
	  }).mkString("\n    ")
	  s += "</table></tt>"
	  (s, lf.map(_._2.size).sum, lf.map(_._1))
	}  
	
	def uri2open = "file:///"+MyLog.L.errfileName.replaceAll("\\\\","/")
	
	def withlinkString(s: String): String = {
	  "<a href=\""+uri2open+"?menuItem="+s+"\">"+s+"</a>"
	}
}

