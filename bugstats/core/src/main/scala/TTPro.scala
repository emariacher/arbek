package bugstats

import java.io.File
import kebra.MyLog._
import kebra.DateDSL._
import bugstats.DefectState.DefectState
import java.util.Calendar
import java.text.SimpleDateFormat

class TTProReqEta extends TTPro {
  val rangeeMaitresse = List((DefectState.MODIFYING, ""), (DefectState.STABILIZING, ""), (DefectState.STABLE, ""))

  def ParseXml(f: File): List[Defect] = {
    val r_priority = """P(\d+)""".r
    val keywords = List(("product", "product", (s: String) => s),
      ("number", "requirement-number", (s: String) => s),
      ("type", "type", (s: String) => s),
      ("summary", "summary", (s: String) => s.replaceAll("\\p{Punct}", "")),
      ("priority", "priority", (s: String) => s)
    )
    val lstates = List((DefectState.CLOSED, List("Closed", "Force Close")),
      (DefectState.INQA, List("Verify")),
      (DefectState.OPENED, List("Re-Open", "Fix")))

    val src = scala.io.Source.fromFile(f)("UTF-8")
    val cpa = scala.xml.parsing.ConstructingParser.fromSource(src, false)
    val doc = cpa.document()
    val root = doc.docElem

    (root \\ "requirement").map(defect => {
      var m = Map[String, String]()
      keywords.foreach(k => {
        //printIt(k._2,(defect \\ k._2))
        if((defect \\ k._2).toString.length==0) {
          m = m + (k._1 -> k._3("Unknown"))
        } else {
          m = m + (k._1 -> k._3((defect \\ k._2).head.text))
        }
      })

      val h = (defect \\ "historical-event").map(f = de => {
        new DefectEvent(ParseDate((de \ "@date").text, "dd.MM.yyyy"), DefectState.INQA)
      }).toList.sortBy {
        _.c
      }
      new Requirement(m, h)
    }).toList.sortBy{_.number.toInt}
  }
  
  def doZeHtml(endDate: Calendar, projets: List[Projet], bugs: List[Defect]){
	    var s = "<html><head>" + ChartsGoogle.htmlHeaderJustZeGraphs + "\n<title>" + printZisday(endDate, "dMMMyyyy") + " bug trends</title></head><body>\n"

  s += projets.map(p => {
    "\n<h3>" + p.p + "</h3>\n" +
      ChartsGoogle.stackedCurve(p.p, p.rangees.map(r => {
        (r.c2string, r.r)
      }), rangeeMaitresse.map(c => c._1 + " " + c._2))
  }).mkString("")

  s += "</body></html>"

  toFileAndDisplay("tests_status_" + new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime) + ".html", s)
  }
}


class TTProEta extends TTProLogitech {
  override val rangeeMaitresse = List((DefectState.OPENED, "Immediate"), (DefectState.OPENED, "Before Release"),
    (DefectState.INQA, ""), (DefectState.OPENED, "Later"), (DefectState.CLOSED, ""))

  override def ParseXml(f: File): List[Defect] = {
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

class TTProLogitech extends TTPro {
  val rangeeMaitresse = List((DefectState.OPENED, "P0"), (DefectState.OPENED, "P1"), (DefectState.OPENED, "P2"), (DefectState.OPENED, "P3"),
    (DefectState.INQA, ""), (DefectState.CLOSED, ""))

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
	
	def doZeHtml(endDate: Calendar, projets: List[Projet], bugs: List[Defect]) = {
	    var s = "<html><head>" + ChartsGoogle.htmlHeaderJustZeGraphs + "\n<title>" + printZisday(endDate, "dMMMyyyy") + " bug trends</title></head><body>\n"

  s += "\n<h2>Active Projects at " + printZisday(endDate, "dMMMyyyy") + "</h2>\n" + ChartsGoogle.columnChart("ActiveProjects",
    projets.map(p => (p.p, p.rangees.last.r)),
    rangeeMaitresse.map(c => c._1 + " " + c._2))

  s += "\n<h2>Word Cloud</h2>\n" + ChartsGoogle.wordCloud(bugs.takeRight(200).map(b => (b.product, b.summary)), "Word Cloud")

  s += projets.map(p => {
    "\n<h3>" + p.p + "</h3>\n" +
      ChartsGoogle.stackedCurve(p.p, p.rangees.map(r => {
        (r.c2string, r.r)
      }), rangeeMaitresse.map(c => c._1 + " " + c._2))
  }).mkString("")

  s += "</body></html>"

  toFileAndDisplay("tests_status_" + new SimpleDateFormat("ddMMMyy_HH_mm").format(Calendar.getInstance.getTime) + ".html", s)

  var sjson = "{ \"name\": \"flare\", \"children\":\n"
  val projectsbyType = projets.groupBy(p => p.p.substring(0, 2))
  myPrintDln(projectsbyType.map(z => z._1 + " " + z._2.size))
  sjson += projectsbyType.map(z => {
    "{\"name\": \"" + z._1 + "\", \"children\":\n" + z._2.map(p => p.json(Today)).mkString("  [\n    ", ",\n    ", "\n  ]\n  }")
  }).mkString("[\n  ", ",\n  ", "\n]\n}")
  copy2File("flare.json", sjson)
  display(new File("treemap.html"))
	}
}

abstract class TTPro {
  val rangeeMaitresse: List[(DefectState, String)]
  def ParseXml(f: File): List[Defect]
  def doZeHtml(endDate: Calendar, projets: List[Projet], bugs: List[Defect])
}