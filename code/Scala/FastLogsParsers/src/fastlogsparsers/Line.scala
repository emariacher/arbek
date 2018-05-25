package fastlogsparsers

object Line {
	val r_traceLine = """([\d\:\.]+) - +(\w+) - +(\w+) - +(\w+)> (.+)""".r;
	val r_traceLino = """\((\S+)\) (.+)\:(\d+)\: (.+)""".r;
	val r_traceLino2 = """\((\S+)\) (.+)""".r;
	val r_StartTestSuite = """(.+) Start test suite "(.+)""".r;
	val r_RunDynTest = """Running dynamic test "(.+)""".r;
	val r_timeStamp = """(\d\d\:\d\d\:\d\d)\.(\d+)""".r;
	val mbglevels = Map("FATAL" -> "#ffe6e6", "ERROR" -> "#ffe6e6", "WARN" -> "#ffe6ff")
			val mfglevels = Map("FATAL" -> "#ff0000", "ERROR" -> "#ff0000", "WARN" -> "#ff00ff")
			val mbolds = List("FATAL","INFO").map((_,true)).toMap
			val mcolors = Map("TALIO" -> "black", "TESTS" -> "blue", "UART" -> "darkgreen")
}

class Line(val lini: String) {
	var lino: String = _
			var timeStamp: String = _
			var ttype: String = _
			var level: String = _
			var origin: String = _
			var valid = false
			var validSource = false
			var prelude = false
			var seconds: String = _
			var millis: Int = _
			var leftRight: String = ""
			var sourceFile: String = ""
			var sourceLine: Int = 0
			var linoo: String = _

			lini match {
			case Line.r_traceLine(ts,typ,lvl,orig,l) => {
				timeStamp = ts
						ttype = typ
						level = lvl
						origin = orig
						lino = l
						valid = true
						lino match {
						case Line.r_traceLino(lr,fi,li,l) => {
							leftRight = lr
									sourceFile = fi
									sourceLine = li.toInt
									linoo = l 
									validSource = true
						}
						case Line.r_traceLino2(lr,l) => {
							leftRight = lr
									linoo = l
						}
						case _ => {
							linoo = lino
						}
				}
			}
			case _ => lino = lini
}


timeStamp match {
case Line.r_timeStamp(s,m) => {
	seconds = s
			millis = m.toInt
}
case _ => 
}

def findStartTestSuite: Boolean = {
		lino match {
		case Line.r_StartTestSuite(a,b) => true
		case Line.r_RunDynTest(a) => true
		case _ => { prelude = true; false }
		}
}

def findBgColor: String = {
		Line.mbglevels.getOrElse(level, {
			var bglevel = if(prelude) "#d0d0d0" else "#f0f0f0"
				val found = Parameters.p.highlight(lino)
				bglevel = if(found.length>0) found else bglevel
				bglevel
		})
}


def toHtmlString: String = {
			if(valid) {
				val bglevel = findBgColor
						val fglevel = Line.mfglevels.getOrElse(level, "#000000")
						val bold = Line.mbolds.getOrElse(level, false)
						val color = Line.mcolors.getOrElse(origin, "black")
						var s = "<tr>"
						s+="<td>"+ttype+"</td>"
						s+="<td><span style=\"color:"+fglevel+"\">"
						s+=bolder(bold, level)
						s+="</span></td><td align=\"right\"><span style=\"color:"+color+"\">"+bolder(bold, origin)+"&gt;</span></td>"
						if(validSource) {
							if(leftRight.length>0) {
								s+=cell(leftRight)						  						    
							} else {
								s+="<td/>"
							}						
							s+=href(sourceFile)							
									s+=cell(sourceLine.toString)							
									s+="<td bgcolor=\""+bglevel+"\" align=\"right\">"+seconds+"."+millis+"</td>"
									s+="<td bgcolor=\""+bglevel+"\"><span style=\"color:"+fglevel+"\">"
									s+= bolder(bold, linoo)
									s+="</span></td>"							
						} else {
							if(leftRight.length>0) {
								s+=cell(leftRight)						  						    
							} else {
								s+="<td/>"
							}
							s+="<td colspan=\"2\"/><td bgcolor=\""+bglevel+"\" align=\"right\">"+seconds+"."+millis+"</td>"
									s+="<td bgcolor=\""+bglevel+"\"><span style=\"color:"+fglevel+"\">"
									s+= bolder(bold, linoo)
									s+="</span></td>"														  
						}
				s+="</tr>"

						def cell(stuff: String): String = "<td>"+stuff+"</td>"
						def href(sourceFile: String): String = "<td align=\"right\">"+
						"<a href=\"../../../../Source/Code/"+sourceFile.replaceAll("\\\\","/")+"\">"+sourceFile+"</a> "+
						//"<a href=\"https://repos.sonova.com/svn/PalioSSW/G30/trunk/Source/Code/"+sourceFile.replaceAll("\\\\","/")+"\">"+sourceFile+"</a>"+
						//"<a href=\"..\\..\\..\\..\\Source\\Code\\"+sourceFile+"\">"+sourceFile+"</a> "+
						//"<a href=\"file:///..\\..\\..\\..\\Source\\Code\\"+sourceFile+"\">"+sourceFile+"</a>"+
						"</td>"

						s
			} else {
				"<tr><td colspan=\"8\">"+lino+"</td></tr>"
			}
		}

		override def toString: String = lini

				def bolder(bold: Boolean, s: String): String = {
				if(bold) { 
					"<b>"+s+"</b>" 
				} else {
					s
				}
		}
}

