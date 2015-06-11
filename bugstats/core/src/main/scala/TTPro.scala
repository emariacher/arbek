package bugstats

import java.io.File
import kebra.MyLog._

class TTProEta {
  def ParseXml(f: File): List[Defect] = {
    val r_priority = """P(\d+)""".r
    val keywords = List(("product", "product", (s: String) => s),
      ("number", "defect-number", (s: String) => s),
      ("type", "type", (s: String) => s),
      ("summary", "summary", (s: String) => s.replaceAll("\\p{Punct}", "")),
      ("priority", "priority", (s: String) => s)
      /*("priority","priority",(s: String) => {
        s match {
        case r_priority(digits) => digits
        case _ => s
        }
      })*/
    )
    val lstates = List((DefectState.CLOSED, List("Closed", "Force Close")),
      (DefectState.INQA, List("Verify")),
      (DefectState.OPENED, List("Re-Open", "Fix")))

    val src = scala.io.Source.fromFile(f)("UTF-8")
    val cpa = scala.xml.parsing.ConstructingParser.fromSource(src, false)
    val doc = cpa.document()
    val root = doc.docElem

    (root \\ "defect").map(defect => {
      var m = Map[String, String]()
      keywords.foreach(k => {
        //printIt(k._2,(defect \\ k._2))
        if((defect \\ k._2).toString.length==0) {
          m = m + (k._1 -> k._3("Unknown"))
        } else {
          m = m + (k._1 -> k._3((defect \\ k._2).head.text))
        }
      })

      var h = List(new DefectEvent(ParseDate((defect \\ "date-entered").head.text, "MM/dd/yyyy"),
        DefectState.OPENED))
      h = h ++ (defect \\ "defect-event").map(f = de => {
        new DefectEvent(ParseDate((de \\ "event-date").head.text, "MM/dd/yyyy"), {
          val state = (de \\ "event-name").head.text
          lstates.find(c => c._2.contains(state)) match {
            case Some(z) => z._1
            case _ => DefectState.DONTCARE
          }
        })
      }).toList.filter(_.d != DefectState.DONTCARE).sortBy {
        _.c
      }
      new Defect(m, h)
    }).toList
  }
}

class TTProLogitech {
	def ParseXml(f: File): List[Defect] = {
			val r_priority = """P(\d+)""".r
					val keywords = List(("product","product", (s: String) => s), 
							("number","defect-number", (s: String) => s), 
							("type","type", (s: String) => s), 
							("summary","summary", (s: String) => s.replaceAll("\\p{Punct}","")), 
							("priority","priority",(s: String) => s)						
							/*("priority","priority",(s: String) => {						
								s match {
								case r_priority(digits) => digits
								case _ => s
								}
							})*/
							)
							val lstates = List((DefectState.CLOSED,List("Close","defer")),
									(DefectState.INQA,List("Release to Testing")),
									(DefectState.OPENED,List("Re-Open","Request More Info (or Reject)","Fix")))

									val src = scala.io.Source.fromFile(f)("UTF-8")
									val cpa = scala.xml.parsing.ConstructingParser.fromSource(src, false)
									val doc = cpa.document()
									val root = doc.docElem

									(root \\ "defect").map(defect => {
										var m = Map[String, String]()						
												keywords.foreach(k => m = m + (k._1 -> k._3((defect \\ k._2).head.text)))	

												var h = List(new DefectEvent(ParseDate((defect \\ "date-entered").head.text,"MM/dd/yyyy"),
														DefectState.OPENED))
														h = h ++ (defect \\ "defect-event").map(de => {
															new DefectEvent(ParseDate((de \\ "event-date").head.text,"MM/dd/yyyy HH:mm:ss a"),
																	{
																val state = (de \\ "event-name").head.text
																		lstates.find(c => c._2.contains(state)) match {
																		case Some(z) => z._1
																		case _ => DefectState.DONTCARE															    
																}
																	})
														}).toList.filter(_.d!=DefectState.DONTCARE).sortBy{_.c}
														new Defect(m,h)
									}).toList.filter(_.dtype=="Defect")
	}
}