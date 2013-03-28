package kbdmatrix

import kebra.MyLog._

object logPostProcess {
	val checkGam1ng = """Gam1ng(.+)""".r
			val checkGamingTier1 = """GamingTier1(.+)""".r
			val checkGamingTier2 = """GamingTier2(.+)""".r
			val checkNonGaming = """NonGaming(.+)""".r
			val r_isDiv2 = """.*<div id=\"(.+)\" style.*""".r


			def getIndex(s: String): String = {
			s match {
			case checkGamingTier1(s) => "GamingTier1"
			case checkGamingTier2(s) => "GamingTier2"
			case checkGam1ng(s) => "Gam1ng"
			case checkNonGaming(s) => "NonGaming"
			case _ => "Other"
			}
	}
	def htmlPostProcess(hlines: List[String], header: String): String = {
			var out= ""
					out += "<html dir=\"ltr\"><head><link rel=\"stylesheet\" type=\"text/css\" href=\"http://ajax.googleapis.com/ajax/libs/dojo/1.5/dijit/themes/claro/claro.css\" />"
					out += "<style type=\"text/css\">body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }</style>\n"
					out += "<title>"+L.errfileName+"</title>\n"
					out += "</head><body>\n"
					out += "KBD Matrix Excel File saved in xml-excel 2003 format: "+L.name+"\n"

					out += copy(header)

					out +="\nvar storeData = { \"identifier\": \"tidtoc\", \"label\": \"msg\", \"items\": [\n"
					val ldiv = hlines.filter(_ match { 
					case r_isDiv2(s) => true
					case _ => false})
					val ldiv2 = ldiv.map(_ match { 
					case r_isDiv2(s) => s
					case _ => "bug!"})
					val ldiv3 = ldiv2.map(getIndex(_)+"_All").toSet.toList ++ ldiv2
					val lmenu = ldiv3.groupBy(getIndex(_))
					out += lmenu.tail.foldLeft(printGroup(lmenu.head))(_ + "," + printGroup(_))+"\n]}\n</script>\n"
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