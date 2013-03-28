package testMacros

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import kebra.ScalaBatshNew._
import kebra.ScalaBatshNew2._
import kebra.WindowsStuff._
import kebra.MyLog._
import java.io.File
import scala.concurrent.Future._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

// scala org.scalatest.tools.Runner -o -s testMacros.TestWindowsStuff

class TestWindowsStuff extends FunSuite with ShouldMatchers {
	ignore("getEnvVar") {
		//1 should equal(0)
		myPrintIt(getAllEnvVars.mkString("\n"))
		getEnvVar("OS") should equal("Windows_NT")
		getEnvVar("windir") should equal("C:\\Windows")
		getEnvVar("USERDOMAIN") should equal("EUROPE")
		myPrintIt(getPathAsList)
	}

	ignore("listFiles") {
		myPrintIt(recursiveListFiles(new File("C:\\Users\\emariacher\\workspace\\kebra2\\testMacros"),""".*\.scala""".r))
		myPrintIt(findExecutableInPath("java.exe"))
		myPrintIt(findExecutableInPath("unins000.dat"))
		findExecutableInPath("cowabunga") should equal(None)
	}

	ignore("Paths") {
		getDeltaPath("C:\\Users\\emariacher\\workspace\\kebra2\\testMacros", "C:\\Users\\rehcairam\\workspace\\testMacroz") should 
		equal("..\\..\\..\\emariacher\\workspace\\kebra2\\testMacros")
		getDeltaPath("C:\\Users\\rehcairam\\workspace\\testMacroz", "C:\\Users\\emariacher\\workspace\\kebra2\\testMacros") should 
		equal("..\\..\\..\\..\\rehcairam\\workspace\\testMacroz")
		getDeltaPath("C:\\Users\\emariacher\\workspace\\kebra2", "C:\\Users\\emariacher\\workspace\\kebra2\\testMacros") should 
		equal("..")
		getDeltaPath("C:\\Users\\emariacher\\workspace\\kebra2\\testMacros", "C:\\Users\\emariacher\\workspace\\kebra2") should 
		equal("testMacros")
		getDeltaPath("C:\\Users\\emariacher\\workspace\\kebra2", "C:\\Users\\emariacher\\workspace\\kebra2") should 
		equal(".")
		getDeltaPath("Z:\\Users\\emariacher\\workspace\\kebra2", "C:\\Users\\emariacher\\workspace\\kebra2") should 
		equal("..\\..\\..\\..\\..\\Z:\\Users\\emariacher\\workspace\\kebra2")
	}

	ignore("TaskLists New") {
		taskExist("notepad.exe") should be(false)
		taskKill("notepad.exe") should equal(128)
		scala.concurrent.Future(exec(3,"notepad.exe"))
		taskKill("notepad.exe") should equal(0)
		taskExist("notepad.exe")  should be(false)
	}

	ignore("windowsRegs") {
		regQuery("HKLM\\Software /se #")
	}

	test("ScalaBatshNew2") {
		(scalaBatshActor ! "dir").toString should equal("()")
		(scalaBatshActor !? (1000, "dir /W")).toString should not equal("()")
		(scalaBatshActor !? (1, "dir")) should equal(None)
		(scalaBatshActor !? (1000, (10,"dir /D"))).toString should not equal("()")
		/*(1 until 50).map((i: Int) => {
	    (scalaBatshActor !? (1000, (i,"dir /D"))).toString should not equal("()")
	  })*/
	}

	test("ScalaBatshNew3") {
		val doSomeStuff: PartialFunction[(Int,List[String],List[String]), _] = {
		case x: (Int,List[String],List[String]) => myPrintDln(""+x)
		case _ => myPrintDln("NOGOOD!")
	}

	val future = Future {
		exec(3,"dir")
	} onSuccess {
		myPrintDln("Success!")
		doSomeStuff
		/*} onFailure {
			myErrPrintDln("Failure!")
			doSomeStuff
		} onTimeOut {
			myErrPrintDln("timeOut!")
			doSomeStuff*/
	}
	waiting(1 second)
	}
}