package fastlogsparsers

class Test(val name: String) {  
	var lines = List.empty[Line]
			var errorLines = List.empty[String]
					var result: String = "Unknown"
					val lancestors = name.split("\\.").toList
					val hname = lancestors.mkString("","_","")

					def addLine(line: String) = {
				if(line.length>0) {
					lines = lines :+ new Line(line)
				}
			}
			def addErrorLine(line: String) = {
				if(line.length>0) {
					errorLines = errorLines :+ line
				}
			}
			def updateResult(resulti: String) = result = resulti
					def updateLines = lines.find(_.findStartTestSuite) 
					def updateTimeStamps = {
				var lastTimeStampSeconds = ""
						var lastFile = ""
						var lastType = ""
						var lastOrigin = ""
						  var lastLevel = ""
						var removeSide = lines.groupBy{_.leftRight}.size==2

						lines.foreach((l: Line) => {
							if(l.seconds==lastTimeStampSeconds) {
								l.seconds = ""
							} else {
								lastTimeStampSeconds = l.seconds
							}
							if(l.sourceFile==lastFile) {
								l.sourceFile = ""
							} else {
								lastFile = l.sourceFile
							}
							if(removeSide) {
								l.leftRight = ""
							}
							if(l.ttype==lastType) {
								l.ttype = ""
							} else {
								lastType = l.ttype
							}
							if(l.origin==lastOrigin) {
								l.origin = ""
							} else {
								lastOrigin = l.origin
							}
							if(l.level==lastLevel) {
								l.level = ""
							} else {
								lastLevel = l.level
							}
							if(removeSide) {
								l.leftRight = ""
							}
						})
			}

			def getLevelLines(lvl: String) = lines.filter(_.level==lvl)

					def resulColor: String = {
					result match {
					case "Success" => "darkgreen"
					case "Unknown" => "orange"
					case _ => "red"
					}
			}

			def printErrorLines: String = {
					if(!errorLines.isEmpty) {
						"</table>\n<table border=\"0\">"+errorLines.mkString("<tr><td colspan=\"5\"><span style=\"color:red\"><b>",
								"</b></span></td></tr><tr><td colspan=\"5\"><span style=\"color:red\"><b>",
								"</b></span></td></tr>")
					} else {
						""
					}
			}

			def toHtmlString(display: String): String = {
					//"[test: " + lancestors.last +"]"
					var s = "<div id=\""+hname+"\" style=\"display:"+display+"\">\n  <h2>"+name+"</h2><h4><span style=\"color:"
							s += resulColor
							s += "\"><b>"+result+"</b></span></h4><tt>\n  "
							s += printErrorLines
							s += "</table>\n<hr/>\n<table border=\"0\">\n     "
							s += lines.map(_.toHtmlString).mkString("\n     ","\n     ","\n   ")
							if(!lines.isEmpty) {
								s += "<tr><td colspan=\"5\"><span style=\"color:"
										s += resulColor
										s += "\"><b>"+result+"</b></span></td></tr>\n  "
										s += printErrorLines
							}
					s += "</table></tt></div>"
							s
			}

			//override def toString: String = "["+name+" "+result+"]"
			override def toString: String = name
}
