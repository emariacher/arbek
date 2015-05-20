import language.postfixOps
import redyellow.DateDSL._
import redyellow.RedYellowDSL._
import kebra.MyLog._
import kebra._

object MainDsl {
  def main(args: Array[String]): Unit = {
    println("Hello, world!\n")
    val z = (Rule green List(7,8) next nand red List(1,2) threshold 2 target
      (now is "10/1/2011" minus 9 years and plus 1 day) next nand green List(3,4)
      )
    myPrintDln(z.p.toString)
    debugqq(z.p,now,z.p.targetDate after now)
    myPrintIt(now,z.p.targetDate after now)
    println(z.p.targetDate+ " before "+now +": "+(z.p.targetDate before now))
    println(z.p.targetDate+ " after "+now +": "+(z.p.targetDate after now))
    println((now + 1 day)+ " after "+now +": "+((now + 1 day) after now))

    val a = 1
    val b = 2
    myAssert2(a+b,3)
    myAssert2(a+b,4)
  }
}
