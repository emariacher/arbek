package kebra

import java.util.Calendar
import java.text.SimpleDateFormat
import kebra.DateDSL._
import kebra.MyLog._
import java.io.File


object TestKebra extends App {

	println("Hello World1!")
	
	val L = newMyLog(this.getClass.getName,new File("out\\cowabunga"),"htm")
	L.myPrintln("Hello World2!")
	
	println(Tomorrow minus 1 month and plus 10 years and plus 1 day)
	println(Today + 2 months and plus 9 years and minus 1 day)
	println(Today - 9 years and minus 1 day)
	println(((Today + 2 months and) + 9 years and) - 1 day)
	println(now is "10/1/2011" plus 3 days)
	println("\n"+printZisday((now is "10/1/2011" plus 3 days).cal,"ddMMMyy"))
	assert(printZisday((now is "10/1/2011" plus 3 days).cal,"MM/dd/yyyy").indexOf("10/04/2011")==0)
	assert((now is "10/1/2011" plus 3 days).before(now is "10/1/2011" plus 4 days))
	assert((now is "10/1/2011" plus 4 days).after(now is "10/2/2011" plus 2 days))
	
	val zendDate = new DateDSL(now is "2/2/2011")
	var ago = new DateDSL(now is "2/2/2011" minus (9*7) days)
	println("***** ago: "+ago+", zendDate: "+zendDate)
	while(ago.before(zendDate)) {
		println("      ago: "+ago)
		ago plus 7 days
	}
	now plus 1 year
	
	println("***** ago: "+ago+", zendDate: "+zendDate)
	assert((ago plus 1 day).after(zendDate))
	println("***** ago: "+ago+", zendDate: "+zendDate)
	assert((ago minus 2 days).before(zendDate))
	println("***** ago: "+ago+", zendDate: "+zendDate)
	
	L.myPrintln("Hello World3!")
	L.hcloseFiles(L.working_directory + File.separatorChar + "htmlheader.html", (hlines: List[String], header: String) => "")
	
	def printZisday(zisday:  Calendar, fmt: String): String = new String(new SimpleDateFormat(fmt).format(zisday.getTime()))
}