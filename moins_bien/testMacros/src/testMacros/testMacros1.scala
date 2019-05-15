package testMacros

import java.util.Calendar
import java.text.SimpleDateFormat
import kebra.DateDSL
import kebra.DateDSL._
import kebra.MyLog._
import java.io.File
import org.scalatest._
import org.scalatest.FunSuite
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scalation.ScalaTion
import scalation.stat._
import java.io.FileReader
import language.postfixOps
import kebra.WindowsStuff._
import kebra.ScalaBatshNewRC._

// scala org.scalatest.tools.Runner -o -s testMacros.ScalaBatschTest

object TestScalation extends App with ScalaTion {

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Exponentiation */

  val exp1 = 2 ↑ 2 // 4
  val exp2 = 2 ↑ 2 ↑ 2 // 16

  section("Exponentiation")
  println("2↑2   = %s".format(exp1))
  println("2↑2↑2 = %s".format(exp2))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Roots */

  val root1 = 4 ↓ 2 // 2
  val root2 = 4 ↓ 2 ↓ 2 // 1.41421...
  val test = 4 ↑ 0.5 == 4 ↓ 2 // true

  section("Roots")
  println("4↓2          = %s".format(root1))
  println("4↓2↓2        = %s".format(root2))
  println("4↑0.5 == 4↓2 = %s".format(test))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Factorials */

  val fac1 = 4! // 24
  val fac2 = 3.5! // 11.63172...

  section("Factorials")
  println("4!   = %s".format(fac1))
  println("3.5! = %s".format(fac2))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Rising and Falling Factorials */

  val rising = 4 ⇑ 4 // 840
  val falling = 4 ⇓ 4 // 24

  section("Rising and Falling Factorials")
  println("4 ⇑ 4 = %s".format(rising))
  println("4 ⇓ 4 = %s".format(falling))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Product Series */

  val prod1 = ∏(1 to 3) // 6
  val prod2 = ∏(1 to 3, (i: Int) ⇒ i ↑ 2) // 36

  val vec = Vec(1, 2, 3, 4)

  val prod3 = ∏(vec) // 24
  val prod4 = ∏(0 to 2, (i: Int) ⇒ vec(i)) // 6

  section("Product Series")
  println("∏(1 to 3)             = %s".format(prod1))
  println("∏(1 to 3, i ⇒ i ↑ 2)  = %s".format(prod2))
  println("vec                   = %s".format(vec))
  println("∏(vec)                = %s".format(prod3))
  println("∏(0 to 2, i ⇒ vec(i)) = %s".format(prod4))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Summation Series */

  val sum1 = ∑(1 to 3) // 6
  val sum2 = ∑(1 to 3, (i: Int) ⇒ i ↑ 2) // 14
  val sum3 = ∑(vec) // 10
  val sum4 = ∑(0 to 2, (i: Int) ⇒ vec(i)) // 6

  section("Summation Series")
  println("∑(1 to 3)             = %s".format(sum1))
  println("∑(1 to 3, i ⇒ i ↑ 2)  = %s".format(sum2))
  println("vec                   = %s".format(vec))
  println("∑(vec)                = %s".format(sum3))
  println("∑(0 to 2, i ⇒ vec(i)) = %s".format(sum4))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Definite Integral (Approximation) */

  val int1 = ∫(1 to 4, (i: Double) ⇒ i) // 7.5

  section("Definite Integral (Approximation)")
  println("∫(1 to 4, i) = %s".format(int1))

  val int2 = ∫(1 to 4, (i: Double) ⇒ i ↑ 2) // 21

  section("Definite Integral (Approximation)")
  println("∫(1 to 4, i ⇒ i ↑ 2) = %s".format(int2))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Sets and Set-like Objects */

  val set1 = Set(1, 2, 3, 4)
  val set2 = Set(1.0, 2.0, 3.0, 4.0)

  val setTest1 = 2 ∈ set1 // true
  val setTest2 = 5 ∈ set1 // false
  val setTest3 = 2.0 ∈ set2 // true 

  section("Sets and Set-like Objects")
  println("set1     = %s".format(set1))
  println("set2     = %s".format(set2))
  println("2 ∈ set1 = %s".format(setTest1))
  println("5 ∈ set1 = %s".format(setTest2))
  println("2 ∈ set2 = %s".format(setTest3))

  /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
  /* Numeric Vectors */

  val vec1 = Vec(1, 2, 3)
  val vec2 = Vec(4, 3, 2)
  val vec3 = Vec(1, 2, 3, 4, 5, 6, 7)

  val dp1 = vec1 ⋅ vec2 // 16

  val v2 = vec3(2) // 3
  val v3 = vec3(3) // 4

  val v2_4 = vec3(2 to 4)
  val v2_3 = vec3(2 until 4)

  val v2_4_ = vec3(2 ⋯ 4)

  section("Numeric Vectors")
  println("vec1            = %s".format(vec1))
  println("vec2            = %s".format(vec2))
  println("vec1 ⋅ vec2     = %s".format(dp1))
  println("vec3            = %s".format(vec3))
  println("vec3(2)         = %s".format(v2))
  println("vec3(3)         = %s".format(v3))
  println("vec3(2 to 4)    = %s".format(v2_4))
  println("vec3(2 until 4) = %s".format(v2_3))
  println("vec3(2 ⋯ 4)     = %s".format(v2_4_))

  printIt(Quantile.chiSquareInv(0.95, 4)) // theor 9.48773
  myAssert(math.abs(Quantile.chiSquareInv(0.95, 4) - 9.48773) < 0.02)

  val poisson = new Poisson(1.9, 0)
  printIt(poisson.pf(3)) // theor 17.1%
  myAssert(math.abs(poisson.pf(3) - 0.171) < 0.0001)

  def section(title: String) {
    println()
    println(title.toUpperCase)
    println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
  }

}

object TestKebra extends App {

  println("Hello World2!")

  newMyLog(this.getClass.getName, new File("out\\cowabunga"), "htm")
  newMyLog(this.getClass.getName, new File("out\\cowabunga"), "htm")
  L.myPrintln("Hello World2!")

  println(Tomorrow minus 1 month and plus 10 years and plus 1 day)
  println(Today + 2 months and plus 9 years and minus 1 day)
  println(Today - 9 years and minus 1 day)
  println(((Today + 2 months and) + 9 years and) - 1 day)
  println(now is "10/1/2011" plus 3 days)
  println("\n" + printZisday((now is "10/1/2011" plus 3 days).cal, "ddMMMyy"))
  assert(printZisday((now is "10/1/2011" plus 3 days).cal, "MM/dd/yyyy").indexOf("10/04/2011") == 0)
  assert((now is "10/1/2011" plus 3 days).before(now is "10/1/2011" plus 4 days))
  assert((now is "10/1/2011" plus 4 days).after(now is "10/2/2011" plus 2 days))

  val zendDate = new DateDSL(now is "2/2/2011")
  var ago = new DateDSL(now is "2/2/2011" minus (9 * 7) days)
  println("***** ago: " + ago + ", zendDate: " + zendDate)
  while (ago.before(zendDate)) {
    println("      ago: " + ago)
    ago plus 7 days
  }
  now plus 1 year

  println("***** ago: " + ago + ", zendDate: " + zendDate)
  assert((ago plus 1 day).after(zendDate))
  println("***** ago: " + ago + ", zendDate: " + zendDate)
  assert((ago minus 2 days).before(zendDate))
  println("***** ago: " + ago + ", zendDate: " + zendDate)

  L.myPrintln("Hello World3!")
  L.myPrintln(tag(1) + func(1))
  L.myPrintln("---2---")
  testMacros2
  L.myPrintln("---3---")
  testMacros3

  waiting(1 second)
  printIt(func1(1 + 2))
  printIt((1 + 2) * 55)
  printIt(zendDate)

  L.hcloseFiles(L.working_directory + File.separatorChar + "htmlheader.html", (hlines: List[String], header: String) => "")

  val actual = 4.0
  val expected = 5.0
  myAssert2(actual, expected)

  def printZisday(zisday: Calendar, fmt: String): String = new String(new SimpleDateFormat(fmt).format(zisday.getTime()))
  def testMacros2 {
    __FUNC__
    //__CLASS__
  }
  def testMacros3 {
    testMacros2
  }
  def func1(d: Double): Double = d * d
}

object TestKebraMyLog extends App {
  def func(i_level: Int): String = {
    val s_rien = "functionNotFound"
    try {
      throw new Exception()
    } catch {
      case unknown: Throwable => unknown.getStackTrace.toList.apply(i_level).toString match {
        case MatchFunc(funcs) => funcs.split('.').toList.last
        case _ => s_rien
      }
    }
  }

  def tracedFunction1 = func(1)
  def tracedFunction2 = func(1)
  println(tracedFunction1)
  assert(tracedFunction1 == "tracedFunction1")
  println(tracedFunction2)
  assert(tracedFunction2 == "tracedFunction2")
}

object CopyFromClipBoardest extends App {
  val source = new kebra.getUrlFromClipboard
  val lines = source.source.getLines.toList
  println(lines.mkString("\n  "))
  source.source.close
}

class ScalaBatschTest extends FunSuite with Matchers {
  val s_directoryDoesntExist = "cowabunga"

  test("Check cd directory") {
    exec(2, "cd")._2(1) should equal("C:\\Users\\emariacher\\workspace\\kebra2\\testMacros")
    var result = exec(2, "cd C:\\Users\\emariacher\\workspace\\kebra2\ncd")
    result._1 should equal(0)
    result._2(2) should equal("C:\\Users\\emariacher\\workspace\\kebra2")
    exec(2, "cd " + s_directoryDoesntExist)._1 should equal(1)
  }

  test("Check echo") {
    exec(2, "echo zubzabzub")._2(1).split(" ").last should equal("zubzabzub")
    exec(2, "echo zubzabzub", "zub")._2(1).split(" ").last should equal("##hidden##zab##hidden##")
    exec(2, "echo zubzabzub", "zub")._2(1).split(" ").last should equal("##hidden##zab##hidden##")
  }

  test("time out") {
    exec(3, "notepad.exe")._1 should equal(TIMEOUT3)
    taskKill("notepad.exe") should equal(OK)
  }
}
