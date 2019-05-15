package bugstats

import kebra._

object logPostProcess {
	val r_isDiv = """.*<div id=\"(.+)\">.*""".r
	val r_RBK = """s_div_bg_RBK(.+)""".r


			def getIndex(s: String): String = {
			s match {
			case r_RBK(s) => "BT_KBDs"
			case _ => "Other"
			}
			
	}
	def htmlPostProcess(hlines: List[String], header: String): String = {
			var out= ""
/*					out += "<html dir=\"ltr\"><head><link rel=\"stylesheet\" type=\"text/css\" href=\"http://ajax.googleapis.com/ajax/libs/dojo/1.5/dijit/themes/claro/claro.css\" />"
					out += "<style type=\"text/css\">body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }</style>\n"
					out += "<title>"+MyLog.printToday+" bug trends</title>\n"
					out += "</head><body>\n"

					out += MyLog.copy(header)

					out +="\nvar storeData = { \"identifier\": \"tidtoc\", \"label\": \"msg\", \"items\": [\n"
					val ldiv = hlines.filter(_ match { 
					case r_isDiv(s) => true
					case _ => false})
					val ldiv2 = ldiv.map(_ match { 
					case r_isDiv(s) => s
					case _ => "bug!"})
					val ldiv3 = ldiv2.map(getIndex(_)+"_All").toSet.toList ++ ldiv2
					val lmenu = ldiv3.groupBy(getIndex(_))
					out += lmenu.tail.foldLeft(printGroup(lmenu.head))(_ + "," + printGroup(_))+"\n]}\n</script>\n"*/
					out += hlines.mkString("\n")
					out
	}
	def printGroup(p: (String, List[String])): String = {
		var out = "\n  { \"tidtoc\": \""+p._1+"\", \"msg\":\""+p._1+"\", \"type\":\"title\",\"children\":["
				out += p._2.tail.foldLeft(printMenuItem(p._2.head))(_ + "," + printMenuItem(_))+"]}"
				out
	}
	def printMenuItem(s: String): String = {
		"\n    { \"type\":\"title\", \"msg\":\""+s+"\", \"tidtoc\": \""+s+"\"}"
	}

}