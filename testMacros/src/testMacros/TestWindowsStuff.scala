package testMacros

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import kebra.ScalaBatshNew._
import kebra.ScalaBatshNew2._
import kebra.ScalaBatshNew3
import kebra.ScalaBatshNew4
import kebra.WindowsStuff._
import kebra.MyLog._
import java.io.File
import scala.concurrent.Future._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import kebra.ScalaBatshNewRC._
import scala.sys.process._
import java.io.OutputStream
import java.io.InputStream

// scala org.scalatest.tools.Runner -o -s testMacros.TestWindowsStuff

class TestWindowsStuff extends FunSuite with ShouldMatchers {
	test("getEnvVar") {
		//1 should equal(0)
		myPrintIt(getAllEnvVars.mkString("\n"))
		getEnvVar("OS") should equal("Windows_NT")
		getEnvVar("windir") should equal("C:\\Windows")
		getEnvVar("USERDOMAIN") should equal("EUROPE")
		myPrintIt(getPathAsList)
	}

	test("listFiles") {
		myPrintIt(recursiveListFiles(new File("C:\\Users\\emariacher\\workspace\\kebra2\\testMacros"),""".*\.scala""".r))
		myPrintIt(findExecutableInPath("java.exe"))
		myPrintIt(findExecutableInPath("unins000.dat"))
		findExecutableInPath("cowabunga") should equal(None)
	}

	test("Paths") {
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

	test("TaskLists New") {
		taskExist("notepad.exe") should be(false)
		taskKill("notepad.exe") should equal(128)
		scala.concurrent.Future(exec(3,"notepad.exe"))
		taskKill("notepad.exe") should equal(0)
		taskExist("notepad.exe")  should be(false)
	}

	test("ScalaBatshNew2") {
		(scalaBatshActor ! "dir").toString should equal("()")
		(scalaBatshActor !? (1000, "dir /W")).toString should not equal("()")
		(scalaBatshActor !? (1, "dir")) should equal(None)
		(scalaBatshActor !? (1000, (10,"dir /D"))).toString should not equal("()")
		(scalaBatshActor !? (1000, (10,"notepad.exe"))) should equal(None)
		taskKill("notepad.exe") should equal(0)
		taskKill("notepad.exe") should equal(128)
		/*(1 until 50).map((i: Int) => {
	    (scalaBatshActor !? (1000, (i,"dir /D"))).toString should not equal("()")
	  })*/
		waiting(1 second)
	}

	test("ScalaBatshNew3") {
		new ScalaBatshNew3(5,"dir").result._1 should equal (OK)	
		new ScalaBatshNew3(6,"cowabunga!").result._1 should equal (UNKNOWN1)	
		new ScalaBatshNew3(7,"dir /D", 1 millisecond).result._1 should equal (TIMEOUT3)	
		new ScalaBatshNew3(5,"notepad.exe").result._1 should equal (TIMEOUT3)	
		taskKill("notepad.exe") should equal(OK)
		waiting(1 second)
	}

	test("ScalaBatshNew4") {
		new ScalaBatshNew4("dir").result._1 should equal (OK)
		myPrintIt(new ScalaBatshNew4("dir").result._2.mkString("\n"))
		new ScalaBatshNew4(6,"cowabunga!").result._1 should equal (UNKNOWN1)	
		new ScalaBatshNew4(7,"REG QUERY HKLM\\Software /se #", 1 millisecond).result._1 should equal (TIMEOUT3)	
		new ScalaBatshNew4(8,"notepad.exe").result._1 should equal (TIMEOUT3)	
		taskKill("notepad.exe") should equal(OK)
		new ScalaBatshNew4(2,"echo zubzabzub","zub").result._2(1).split(" ").last should equal ("##hidden##zab##hidden##")
		waiting(1 second)
	}
}