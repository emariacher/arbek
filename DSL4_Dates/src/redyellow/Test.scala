package redyellow

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import kebra.MyLog._
import java.io.File
import org.scalatest.BeforeAndAfter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import redyellow.RedYellowDSL._
import kebra.DateDSL._
import language.postfixOps

// scala org.scalatest.tools.Runner -o -s redyellow.Test

class Test extends FunSuite with ShouldMatchers with BeforeAndAfter {
	val L = newMyLog(this.getClass.getName,new File("..\\out\\placeholder.g2s"),"html")

			test("Test standard scenarios") {	
		(Rule red List(1,2)).p.toString should equal ("RedList(1,2) threshold: 0, ")
		(Rule red List(1,2) next nand yellow List(3,4) threshold 2 next nand red List(5,6)
				).p.toString should equal ("RedList(1,2,5,6) threshold: 0, YellowList(3,4) threshold: 2, ")
		(Rule red List(1,2) threshold 2 next nand green List(3,4) next nand green List(5,6)
				).p.toString should equal ("RedList(1,2) threshold: 2, GreenLists(List(3, 4) - List(5, 6)), ")
		(Rule red List(1,2) threshold 2 next nand green List(3,4) next nand green List(5,6) next
				).p.toString should equal ("RedList(1,2) threshold: 2, GreenLists(List(3, 4) - List(5, 6)), ")
		(Rule red List(1,2) threshold 2 next nand green List(3,4) next nand green List(5,6) next nand
				).p.toString should equal ("RedList(1,2) threshold: 2, GreenLists(List(3, 4) - List(5, 6)), ")
	}	

	test("Test error cases") {
		evaluating { Rule threshold 5 } should produce [java.lang.IllegalArgumentException];
		evaluating { Rule green List(1,2) threshold 5 } should produce [java.lang.IllegalArgumentException];
		evaluating { Rule red List(1,2) next nand threshold 2 } should produce [java.lang.IllegalArgumentException];
	}
	
	test("Mixing RedYellow and DSLDates") {
		(Rule red List(1,2) threshold 2 target (now is "10/1/2011" minus 9 years and plus 1 day) next nand green List(3,4)
				).p.toString should equal ("RedList(1,2) threshold: 2, GreenLists(List(3, 4)), TargetDate(02oct.02), ")
	}

	after {
		waiting(1 second)
		L.hcloseFiles(L.working_directory + File.separatorChar + "htmlheader.html", (hlines: List[String], header: String) => "")
		waiting(1 second)
	}

}
