package fastlogsparsers

import java.io.File
import scala.collection.immutable.ListSet
import java.net.URL
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object ControllerTestsTraces extends App {
  
	var lines = List.empty[String]
	var L: MyLog = _
	
	// 1st try to read file from URL in clipboard
	var sourcurl: getUrlFromClipboard = _
	try {
	  sourcurl = new getUrlFromClipboard
	  if((sourcurl.lines.isEmpty)&(sourcurl.source!=null)) {
	    lines = sourcurl.source.getLines.toList
	    sourcurl.source.close
	  } else {
	    lines = sourcurl.lines
	  }        
	} catch {
	  case e:Exception => lines = sourcurl.lines
	}
	val run = new RunUtil
	Parameters.newParameters(run.mfc.readSavedParameters())
	run.lines = lines
	run.doZeJob2
	L = MyLog.L
	
	
	var out= "<html dir=\"ltr\"><head><link rel=\"stylesheet\" type=\"text/css\" href=\"http://ajax.googleapis.com/ajax/libs/dojo/1.5/dijit/themes/claro/claro.css\" />\n"
	out += "<style type=\"text/css\">body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }</style>\n"
	out += "<title>"+run.name+" "+run.config+"</title>\n"
	out += "</head><body>\n"
	out += "<p>Controller Tests Traces: "+run.name+" "+run.config+", svn revision: "+run.svn+", "+run.machine+"</p>\n" 
	out += "<h3>Click on menu to display failed tests!</h3>\n" 
	out += MyLog.copy(L.working_directory + File.separatorChar + "htmlheader.html")
	L.myErrPrintln(out+"\n")
	L.myErrPrintln(run.printIt(run.ltfail,"none"))
	
	// wrap up logs
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

