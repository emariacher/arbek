package kbdmatrix
import java.util.regex.Pattern
import scala.util.matching._
import kebra.MyLog._


class XlsXMl2003Kbd(val f: java.io.File) {
	var km = new KbdMatrix
	var i_row = 0
	val src = scala.io.Source.fromFile(f);
	val cpa = scala.xml.parsing.ConstructingParser.fromSource(src, false); // fromSource initializes automatically
	val doc = cpa.document();
	val root = doc.docElem
	List("Keymatrix", "Keymatrix1").find(getMainMatrix(root, KbdKey.i_mainMatrix, _, matchRow))
	List("Fn table").find(getMainMatrix(root, KbdKey.i_fnMatrix, _, matchFnRow))	
	km

	def matchRow(r: scala.xml.NodeSeq) {
		List("Row (\\d+)", "FW index", "Key number").find(findRowEntry(r,_))
	}	

	def findRowEntry(r: scala.xml.NodeSeq, s_rowKeyword: String): Boolean = {			
			val s = (r \\ "Cell" \\ "Data").text
			val m = Pattern.compile(s_rowKeyword).matcher(s)
			if (m.find) { 
				extractKeys(r); true
			} else {
				false
			}
	}

	def matchFnRow(r: scala.xml.NodeSeq) {
		val Name = new Regex("""(\d+)""")
		val s = (r \\ "Cell" \\ "Data").text
		s match {
		case Name(s) => km.fnupdate(r)
		case _ => 
		}
	}

	def extractKeys(r: scala.xml.NodeSeq) {
		var i_col = 0
		(r \\ "Cell").foreach((c: scala.xml.NodeSeq) => i_col+= km.update(i_row,i_col,(c \\ "Data").text)) 
		i_row+=1
	}

	def matchName(n: scala.xml.NodeSeq, s_attributeValue: String): Boolean = {
			n match {
			case xml.Elem(_, "Worksheet", xml.PrefixedAttribute("ss", "Name", v, _), _, _*) => if(v.text==s_attributeValue) true else false
			case _ => false 
			}
	}

	def getMainMatrix(xml_root: scala.xml.NodeSeq, i_wstype: Int, s_wsname: String, matchRow: scala.xml.NodeSeq => Unit): Boolean = {
			val s_wstype = List("Main","Fn")(i_wstype)
			L.myPrintln("Looking for "+s_wstype+" Matrix which has Worksheet Name ["+s_wsname+"]");
			val w_keymatrix = (xml_root \\ "Worksheet").find((w: scala.xml.NodeSeq) => matchName(w, s_wsname))
			w_keymatrix match {
			case Some(w) => (w \\ "Row").foreach((r: scala.xml.NodeSeq) => matchRow(r)); km.complete(i_wstype); true
			case None => L.myErrPrintln(s_wstype+" Matrix ["+s_wsname+"] NOGOOD!"); false
			}		
	}
}