package fastlogsparsers

import java.io.File
import scala.collection.immutable.ListSet
import java.net.URL
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object FastLogsBrowser extends App {

	val L = MyLog.newMyLog(this.getClass.getName,new File("out\\fastlogs"),"html")
			new InspectMutipleLog(RunUtil.toHtml2String,"timestamp","find a specific warn/fatal error")
}

class InspectMutipleLog(func: (RunUtil,Any) => (String, Int, List[String]), args: Any, name: String) {
	val L = MyLog.L
			val mfc = new MyFileChooser("TestOutput.txt")
	Parameters.newParameters(mfc.readSavedParameters())
	var lines = List.empty[String]
			var summary = List.empty[(String, Int, List[String])]
					val url = new URL("http://phonak.fast-cluster.com/fasthosting/phonak/viewer/results/list?category=726&order_by=startedat_dsc&page=5&perpage=10&type=all")
	try {
		val source = scala.io.Source.fromURL(url)
				lines = source.getLines.toList
				source.close
	} catch {
	case e:Exception => throw new Exception(e+" Invalid URL["+url+"]")
	}

	var out= "<html dir=\"ltr\"><head><title>"+name+"["+args+"]</title>\n"
			out += "</head><body>\n"
			out += "<p>Controller Tests Traces: "+name+"["+args+"]</p>\n" 
			L.myErrPrintln(out+"\n")	

			val r_link = """.*href\=\"(/fasthosting/phonak/viewer/results/show/.+)">(.+)</a.*""".r;
	lines.foreach((line: String) => line match {
	case r_link(link,name) =>{
		val run = new Run(link, name)
		val result = func(run, args)
		//L.myErrPrintln("<hr/>\n"+result._1)
		summary = summary :+ (name+" "+run.svn+" "+run.machine, result._2, result._3)
	}
	case _ => // print(".")
	})
	L.myErrPrintln(summary.map((t: (String, Int, List[String])) => t._3).flatten.distinct.mkString("<hr/>Fatal/Warn messages which contains ["+args+"]:<br/>","<br/>",""))
	L.myErrPrintln(summary.map((t: (String, Int, List[String])) => t._1+" --- "+t._2).sorted.mkString("<hr/>Summary["+args+"]:<br/>","<br/>","<hr/>"))
	L.myErrPrintln("</pre></body></html>")
	L.closeFiles

	// open file in default browser
	val desktop = java.awt.Desktop.getDesktop()
	//System.err.println("\n --- "+L.errfileName)
	val uri2open = "file:///"+L.errfileName.replaceAll("\\\\","/")
	//System.err.println("\n --- "+uri2open)
	val uri = new java.net.URI(uri2open)
	desktop.browse(uri)

}