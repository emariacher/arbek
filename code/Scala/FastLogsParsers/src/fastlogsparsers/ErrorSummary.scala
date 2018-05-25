package fastlogsparsers

class ErrorSummary extends Test("Summary.Summary.ErrorsAndFailures_Summary.") {
	var lcause = List.empty[String]

			def addError(test: String, cause: String) = {
		if(cause==null) {
			addErrorLine(test+" -?- ")
		} else {
			addErrorLine(test+" --- "+cause)
			lcause = lcause :+ cause.replaceAll("of (\\d+)ms","of XXXXms")
		}
	}

	override def toHtmlString(display: String): String = {  
		val fglevel = Line.mfglevels.getOrElse("FATAL", "#000000")
				var s = "<div id=\""+hname+"\" style=\"display:"+display+"\">\n  <h2>"+name+" occurences</h2><tt>"
				s += lcause.groupBy{(s: String) => s}.map((c: (String,List[String])) => (c._1,c._2.length)).toList.sortBy{_._2}.reverse.mkString("<br/>\n","<br/>\n","<hr/>\n")
				s += printErrorLines
				s += "</table>\n"
				s += "</tt></div>"
				s
	}  

}