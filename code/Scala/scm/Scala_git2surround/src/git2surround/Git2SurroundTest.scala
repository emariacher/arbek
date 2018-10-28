package git2surround
import java.io.File;
import org.scalatest._;
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers;

// scala org.scalatest.tools.Runner -p . -o -s git2surround.Git2SurroundTest1
	val s_directoryDoesntExist = "cowabunga";
	val s_directoryExistsButDoesntBelongToGit = "C:\\SandBox"

			test("Check bad directories" ) {
		evaluating { new Git2Surround(L, List[String]()) } should produce [java.lang.IndexOutOfBoundsException];
		evaluating { new Git2Surround(L, List("",s_directoryDoesntExist)) } should produce [java.lang.Exception];
		evaluating { new Git2Surround(L, List("",s_directoryDoesntExist,"")) } should produce [java.lang.Exception]
	}	
	test("Check Not a git repository" ) {
		evaluating { new Git2Surround(L, List("",s_directoryExistsButDoesntBelongToGit,"a","b:c")) } should produce [java.lang.Exception]
	} 
	test("bad server name" ) {
		val g2s = new Git2Surround(L, List("zorglub",s_myWorkingDirectory,"a","b:c"))
		g2s.i_rc should equal (errorCode.notGitRepository)
		g2s.l_gitRepositories.size should equal (0)
	}	
	var i_testFailed = 0;
	var i_testRunned = 0
			override def apply(event: Event): Unit = {
			event match {
			case e: org.scalatest.events.TestStarting => L.myPrintln("[32m*****TestStarting*******"+ e.testName+"][0m"); i_testRunned +=1
			case e: org.scalatest.events.TestSucceeded => L.myErrPrintln("[32m*****TestSucceeded*******"+ e.testName+"][0m")
			case e: org.scalatest.events.TestFailed => L.myErrPrintln("[31m*****TestFailed*******"+ e.testName +"["+ e.message+"][0m");
			i_testFailed +=1; 
			//			org.scalatest.events.TestFailed(e.ordinal, e.testName, e.message, Some(s_suiteName), s_suiteName, e.throwable);
			throw new TestFailedException(1)
			case e: org.scalatest.events.InfoProvided => L.myPrintln("[33m*****InfoProvided*******"+ e.message+"][0m")
			case e => L.myPrintln("*****SomethingElse*******"+ e)
			}
	}
};