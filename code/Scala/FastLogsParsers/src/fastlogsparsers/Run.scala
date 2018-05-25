package fastlogsparsers

import java.net.URL
import scala.collection.immutable.ListSet
import java.io.File
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

class Run(val linkI: String, val nameI: String) extends RunUtil {
	val url = new URL("http://phonak.fast-cluster.com"+linkI)
	MyLog.waiting(2 seconds) // I am not sure how the server handles repeated http requests
	try {
		val source = scala.io.Source.fromURL(url)
				lines = source.getLines.toList
				source.close
	} catch {
	case e:Exception => throw new Exception(e+" Invalid URL["+url+"]")
	}
	doZeJob

	override def toString: String = (linkI, nameI).toString
}

class RunUtil {
	var lines = List.empty[String]
			var L: MyLog = _

			var name = "rien"
			var link = "rien"
			var properties = "rien"
			var machine = "rien"
			var svn = "rien"
			var lsummary = List.empty[String]
					var testHeader = new Test("RunTest Information")
	var test = testHeader
	var lt = ListSet.empty[Test]
			var ltfail = ListSet.empty[Test]
					var config = ""
					var fatal = new WarnFatals(ListSet(test),"FATAL")
	var warn = new WarnFatals(ListSet(test),"WARN")
	val lErrorResults = List("Error","Failure","Cancelled")
	val mfc = new MyFileChooser("TestOutput.txt")


	def doZeJob = {
		L = MyLog.L
				lines = getLogLines(lines)
				lines = lines.map(_.replace("1> ",""))
				lt = getTests(lines, StateMachine.header)
				config = testHeader.lines.filter(_.lini.indexOf("ConfigurationLoader")>=0).map(_.lini).mkString
				// do stats on fatals and warns and add the stats to output
				fatal = new WarnFatals(lt,"FATAL")
		warn = new WarnFatals(lt,"WARN")
		processOnlyErrors(lt)
	}

	def doZeJob2 = {
		lines = getLogLines(lines)

				// 2nd open a file chooser to read file with test traces
				if(lines.isEmpty) {
					// get txt traces file
					val f = mfc.justChooseFile("txt");
					// create log and html/error files
					L = MyLog.newMyLog(this.getClass.getName,f,"html")

							val source = scala.io.Source.fromFile(f, "utf-8")
							lines = source.getLines.toList
							source.close
				} else {
					L = MyLog.newMyLog(this.getClass.getName,new File("out\\"+name),"html")
				}

		lines = lines.map(_.replace("1> ",""))


				if(svn=="rien") {
					test = new Test("defaultTest")
					lt = getTests(lines, StateMachine.corpus)		  
					lt = lt + test
					L.myErrPrintln(printIt(lt,"block"))
				} else {
					lt = getTests(lines, StateMachine.header)
				}

		config = testHeader.lines.filter(_.lini.indexOf("ConfigurationLoader")>=0).map(_.lini).mkString
				// do stats on fatals and warns and add the stats to output
				fatal = new WarnFatals(lt,"FATAL")
		warn = new WarnFatals(lt,"WARN")

		processOnlyErrors(lt)
	}

	def getLogLines(linesI: List[String]): List[String] = {
		var lines = List.empty[String]
				val r_name = """.+Test Run Details \((.+)\).+""".r;
		val r_link = """.+Resulttype.+>(.+)</a.+""".r;
		val r_svn1 = """.+SVN_Revision\:(.+)""".r;
		val r_svn2 = """.+>(.+)<.+""".r;
		var stateSvn = false
				linesI.foreach((line: String) => line match {
				case r_name(s) => name = s
				case r_link(s) => {
					link = "http://phonak.fast-cluster.com/fasthosting/phonak/files/"+s+"/BROWSE/TestOutput.txt"
							properties = "http://phonak.fast-cluster.com/fasthosting/phonak/files/"+s+"/BROWSE/result.properties"
				}
				case r_svn1(s) => stateSvn = true
				case r_svn2(s) => if(stateSvn) {
					System.err.println("\n**********svn* ["+s+"]["+line+"]")
					svn = s
					stateSvn = false
				}
				case _ => 
				})
				if(link!="rien") {
					try {
						System.err.println("\n**********link* ["+link+"]")
						//lines = List.empty[String]
						var url = new URL(link)
						var source = scala.io.Source.fromURL(url)
						lines = source.getLines.toList
						source.close 
						// get machine name
						url = new URL(properties)
						source = scala.io.Source.fromURL(url)
						machine = source.getLines.toList.last
						source.close    
					} catch {
					case e:Exception => /*System.err.println(e+" --- "+sourcurl.clipboard)*/
					}
				} else {
					lines = linesI
				}

		lines
	}

	def getTests(linesI: List[String], stateI: StateMachine): ListSet[Test] = {
		var lt = ListSet.empty[Test]
				val r_title1 = """(/\*.+\\)""".r;
		val r_title2 = """(\\\*.+/)""".r;
		val r_test ="""\*\*\*\*\* (.+)""".r; 
		val r_testSuite ="""Starting test suite \[([\d\-]+)\](.+)""".r; 
		val r_result = """Test finished in (.+) seconds with "(\w+)"""".r;
		val r_summary1 = """Tests run\: (\d+), Errors: (\d+), Failures\: (\d+), Inconclusive\: (\d+), Time\: (.+) seconds""".r;
		val r_summary2 = """  Not run: (\d+), Invalid: (\d+), Ignored: (\d+), Skipped: (\d+)""".r;
		val r_summary3 = """Errors and Failures(.*)""".r;
		val r_summary4 = """Tests Not Run(.*)""".r;
		val r_testError = """(\d+)\) (.+) \: (.+)""".r;
		var state = stateI
				lines.foreach((line: String) => {

					line match {
					case r_title1(s)            => state = StateMachine.title
					case r_title2(s)            => state = StateMachine.corpus
					case r_result(s,t)          => {
						L.myPrintln("     result: "+t)
						test.updateResult(t)
						test.addLine(line)
					}
					case r_testSuite(d,ts)      => test = new Test("rien")
					case r_summary1(r,e,f,i,s)  => lsummary = lsummary :+ "<h3>"+line+"</h3>"
					case r_summary2(nr,in,ig,s) => lsummary = lsummary :+ "<h3>"+line+"</h3>"
					case r_summary3(g)          => state = StateMachine.summary
					case r_summary4(g)          => { test = new Test("rien"); state = StateMachine.ignore }
					case _ => state match {
					case StateMachine.title => line match {
					case r_test(s) => {
						L.myPrint("test: "+s+" ")
						test = new Test(s) 
						lt = lt + test 
					}
					case _ => test.addLine(" - confused log! - "+line.replaceAll("\\([\\-\\p{XDigit}]+\\)",""))
					}
					case StateMachine.summary => line match {
					case r_testError(d,etype, tname) => {
						L.myPrintln("testError["+etype+"]: "+tname)
						test = lt.find((t: Test) => t.name==tname ) match {
						case Some(te) => te
						case _ => {
							test = new Test(tname) 
							lt = lt + test                
							test
						}
						}
						test.addErrorLine(line)
					}
					case _ => test.addErrorLine(line)
					}
					case _ => test.addLine(line.replaceAll("\\([\\-\\p{XDigit}]+\\)",""))
					}
					}
				})
				lt
	}

	def processOnlyErrors(lt: ListSet[Test]) {
		lsummary = config :: lsummary

				// process only tests with errors
				ltfail = lt.filter(!_.errorLines.isEmpty)
				if(ltfail.isEmpty) {
					// errorLines based on errors summary at bottom of log
					// let's try to parse based on Error
					ltfail = lt.filter((t: Test) => lErrorResults.contains(t.result))
				}
		//if less than 10 tests, display all tests passed or failed
		if(lt.size<10) {
			ltfail = lt
		} else if(ltfail.isEmpty) {
			// errorLines based on errors summary at bottom of log
			// let's try to parse based on Error
			ltfail = lt.filter((t: Test) => lErrorResults.contains(t.result))
		}

		ltfail.foreach(_.updateLines)
		ltfail.foreach(_.updateTimeStamps)

		// "build a test" with all errors
		val testErrorSummary = new ErrorSummary
		ltfail.map((t: Test) => {
			val size = t.errorLines.length
					if(size>1) {
						testErrorSummary.addError(t.errorLines.head, t.errorLines.tail.head)
					} else if(size>0){
						testErrorSummary.addError(t.errorLines.head, null)
					}
		})
		if(!testErrorSummary.errorLines.isEmpty) {
			ltfail = ltfail+testErrorSummary
		}

		ltfail = ltfail + fatal
				ltfail = ltfail + warn
	}


	def printIt(lt: ListSet[Test], display: String) = {
		var s = "var storeData = { \"identifier\": \"tidtoc\", \"label\": \"msg\", \"items\": [\n"

				// find max depth size
				assert(!lt.isEmpty)
				val maxdepth = lt.map(_.lancestors.length).max

				L.myPrintln("maxdepth = "+maxdepth);
		/*L.myPrintln("*1************************************\n"+lt.map(_.lancestors).mkString("\n"))
		L.myPrintln("*2************************************\n"+lt.map(_.hname).mkString("\n"))
		MyLog.waiting(2 seconds)*/

		val mt2 = lt.filter(_.lancestors.length>2).groupBy(_.lancestors.apply(1))	  
				val mt3 = mt2.map((kv: (String,ListSet[Test])) => {
					(kv._1,kv._2.groupBy((t: Test) => t.lancestors.apply(2)))
				})

				s += mt3.map((kva: (String,Map[String,ListSet[Test]])) => {
					var sa = "\n  { \"tidtoc\": \""+kva._1+"\", \"msg\":\""+kva._1+"\", \"type\":\"title\",\"children\":["
							sa += kva._2.toList.map((kvb: (String,ListSet[Test])) => {
								var sb = ""
										sb += "\n    { \"tidtoc\": \""+kva._1+kvb._1+"\", \"msg\":\""+kvb._1+"\", \"type\":\"title\",\"children\":["
										sb += kvb._2.map((kvc: Test) => {
											var sc = "\n      { \"type\":\"title\", \"msg\":\""+kvc.lancestors.last+"\", \"tidtoc\": \""+kvc.hname+"\"}"
													sc
										}).mkString("",",","")
										sb += "\n    ]}"
										sb
							}).mkString("",",","")
							sa += "\n  ]}"
							sa
				}).mkString("",",","")

				s += "\n]}\n</script>\n"
				s += "<p>Controller Tests Traces: "+name+" "+config+", svn revision: "+svn+", "+machine+"</p>\n"
				s += lsummary.mkString("\n")
				s += lt.map(_.toHtmlString(display)).mkString("\n")
				s
	}	
}

object RunUtil {
	def toHtml2String(run: RunUtil, args: Any): (String, Int, List[String]) = {
		var s = "<p>Controller Tests Traces: "+run.name+" "+run.config+", svn revision: "+run.svn+", "+run.machine+"</p>\n"
				s += run.lsummary.mkString("\n")
				val f = run.fatal.toHtml2String(args)
				val w = run.warn.toHtml2String(args)
				s += f._1 + w._1
				(s, f._2 + w._2, f._3++w._3)
	}

	def toHtmlFilterTests(run: RunUtil, args: Any): (String, Int, List[String]) = {
		var s = "<p>Controller Tests Traces["+args.toString+"]: "+run.name+" "+run.config+", svn revision: "+run.svn+", "+run.machine+"</p>\n"
				s += run.lsummary.mkString("\n")
				s += run.lt.filter(_.name.indexOf(args.toString)>0).map(_.toHtmlString("block")).mkString("\n")
				(s,0,List.empty[String])
	}

}

case class StateMachine private (state: String) {
	override def toString = "State_"+state
}

object StateMachine {
	val header = StateMachine("header")
			val title = StateMachine("title")
			val corpus = StateMachine("corpus")
			val result = StateMachine("result")
			val summary = StateMachine("summary")
			val ignore = StateMachine("ignore")
}
