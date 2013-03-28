package kbdmatrix
import scala.collection.immutable.TreeSet
import java.io.File
import kebra.MyLog._
import kebra._



object CheckKbdMatrix {

	def main(args: Array[String]): Unit = {
			// get excel input KBD Matrix file
			val f = (new MyFileChooser("Get KBD Matrix Excel File saved in xml-excel 2003 format")).justChooseFile("xml");
			// create log and html/error files
			val L = newMyLog(this.getClass.getName,f,"htm")

			// read excel file that contains matrix definition and create matrix
			val xxk = new XlsXMl2003Kbd(f)
			val km = KbdMatrix.newKbdMatrix(xxk.km)
			//var km = xxk.km
			// display matrices (regular and fn)
			km.printMatrices()
			km.CGSprintMatrices()
			// check duplicates and missing keys
			val m_keysByCode = km.ts_keys.groupBy(_.i_code)
			L.myHErrPrintln("<div id=\"NonGaminglistkeys\" style=\"display:none\"><h3>Duplicate keys :</h3><pre>\n"+m_keysByCode.filter((p:(Int, TreeSet[KbdKey])) => p._2.size>1).mkString("  ","\n  ",""))
			L.myHErrPrintln("</pre><h3>Missing keys :</h3><pre>")
			(0 to KeyDoc.i_maxWHQL).filter(KeyDoc.m_keyref.contains(_)).foreach((i_code: Int) => km.find(i_code) match {
			case Some(k) => 
			case None => L.myHErrPrintln(KbdMatrix.tostring(i_code))
			})
			L.myHErrPrintln("</pre><h3>Non WHQL keys :</h3><pre>"+km.ts_keys.filter((k: KbdKey) => k.i_code>KeyDoc.i_maxWHQL).mkString("  ","\n  ","</pre></div>"))

			// build quartets list
			km.generateQuartets2()
			// check all relevant rules with old 3 ghost keys algorythms
			km.checkGhostKeysRules(3)
			// check all relevant rules with new 4 ghost keys algorythms
			km.checkGhostKeysRules(4)
			// check all gaming relevant rules results are compatible for 3 and 4 ghost key algo
			new checkGamingGhostKeysRules(3)
			// wrap up logs
			L.myHErrPrintln("</pre></body></html>");
			L.hcloseFiles(L.working_directory + File.separatorChar + "htmlheader.html", logPostProcess.htmlPostProcess)
	}		
}
