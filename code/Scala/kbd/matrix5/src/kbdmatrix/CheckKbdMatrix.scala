package kbdmatrix
import scala.collection.immutable.TreeSet


object CheckKbdMatrix {

	def main(args: Array[String]): Unit = {
		// get excel input KBD Matrix file
		val f = (new MyFileChooser("Get KBD Matrix Excel File saved in xml-excel 2003 format")).justChooseFile("xml");
		// create log and html/error files
		val L = new MyLog(this.getClass.getName,f,"htm")
		L.myErrPrintln("<html><body>\n")
		L.myErrPrintln("<h2>jar: " + L.getJarFileCompilationDate() +"</h2><pre>")
		L.myErrPrintln("KBD Matrix Excel File saved in xml-excel 2003 format: "+f.toString())

		// read excel file that contains matrix definition and create matrix
		val xxk = new XlsXMl2003Kbd(L, f)
		var km = xxk.km
		// display matrices (regular and fn)
		km.printMatrices()
		// check duplicates and missing keys
		val m_keysByCode = km.ts_keys.groupBy(_.i_code)
		L.myErrPrintln("</pre><h3>Duplicate keys :</h3><pre>\n"+m_keysByCode.filter((p:(Int, TreeSet[KbdKey])) => p._2.size>1).mkString("  ","\n  ",""))
		L.myErrPrintln("</pre><h3>Missing keys :</h3><pre>")
		(0 to KeyDoc.i_maxWHQL).filter(KeyDoc.m_keyref.contains(_)).foreach((i_code: Int) => km.find(i_code) match {
			case Some(k) => 
			case None => L.myErrPrintln(km.tostring(i_code))
			})

		// build quartets list
		if(km.i_maxRow*km.i_maxCol>400) {
			km.generateQuartets()
		} else {
			km.generateQuartetsOld()
		}
		// check all relevant rules with old 3 ghost keys algorythms
		km.checkGhostKeysRules(3)
		// check all relevant rules with new 4 ghost keys algorythms
		km.checkGhostKeysRules(4)
		// check all gaming relevant rules results are compatible for 3 and 4 ghost key algo
		km.checkGamingGhostKeysRules(3)
		// wrap up logs
		L.myErrPrintln("</pre></body></html>");
		L.closeFiles()
	}	  
}
