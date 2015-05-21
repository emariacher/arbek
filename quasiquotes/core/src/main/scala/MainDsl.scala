import language.postfixOps
import redyellow.DateDSL._
import redyellow.RedYellowDSL._
import kebra.MyLog._
import kebra.qqJust4Fun._
import kebra.M1._

object MainDsl {
  def main(args: Array[String]): Unit = {
    println("Hello, world!\n")
    val z = (Rule green List(7, 8) next nand red List(1, 2) threshold 2 target
      (now is "10/1/2011" minus 9 years and plus 1 day) next nand green List(3, 4)
      )
    myPrintDln(z.p.toString)
    myPrintIt(z.p, now, z.p.targetDate after now)
    myPrintIt(List(1,2,3,4,5))
    myPrintIt2(now, z.p.targetDate after now)
    println(z.p.targetDate + " before " + now + ": " + (z.p.targetDate before now))
    println(z.p.targetDate + " after " + now + ": " + (z.p.targetDate after now))
    println((now + 1 day) + " after " + now + ": " + ((now + 1 day) after now))

    val a = 1
    val b = 2
    myAssert2(a + b, 3)

    just4Fun(b + 2)
    just4Fun(a + 3)
    just4Fun(a + 3)

    myPrintDln("Printed at compile time!")
    apply((x: Int) => x + 1)



    // last one because it shall fail
    myAssert2(a + b, 4)

  }

  def func(z: Int, s: String) = myPrintDln("func([" + s + "]: " + z + ")")
}
