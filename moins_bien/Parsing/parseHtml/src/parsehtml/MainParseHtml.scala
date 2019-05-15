package parsehtml

import kebra._

object MainParseHtml extends App {
	val f = (new MyFileChooser("GetBugsLog")).justChooseFile("html");
	val L = MyLog.newMyLog(this.getClass.getName,f,"htm")


			val src = scala.io.Source.fromFile(f)
			val cpa = scala.xml.parsing.ConstructingParser.fromSource(src, false)
			val doc = cpa.document()
			val root = doc.docElem
			
			val ldivs = (root \\ "div")
			
			println(ldivs)
			
			L.hcloseFiles("", (a: List[String], String) => "")
}