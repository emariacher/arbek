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
    myPrintIt(List(1, 2, 3, 4, 5))
    myPrintIt2(now, z.p.targetDate after now)
    println(z.p.targetDate + " before " + now + ": " + (z.p.targetDate before now))
    println(z.p.targetDate + " after " + now + ": " + (z.p.targetDate after now))
    println((now + 1 day) + " after " + now + ": " + ((now + 1 day) after now))
    myPrintIt(L_, F_, C_)
    func2

    val a = 1
    val b = 2
    myAssert2(a + b, 3)

    just4Fun(b + 2)
    just4Fun(a + 3)
    just4Fun(a + 3)

    myPrintDln("Printed at compile time!")
    apply((x: Int) => x + 1)

    TraceFunc(func_1, List(a, b, a + b))
    TraceFunc(func3, List(a, a + b, new Class1(a + b), b, new Class2(b * b)))
    TraceFunc(func3, List("coucou", new Class2(5), 8.0, new Class1(7)))

    // last one because it shall fail
    myAssert2(a + b, 4)

  }

  def func(z: Int, s: String) = myPrintDln("func([" + s + "]: " + z + ")")

  def func2 = myPrintIt(L_, F_, C_)

  def func_1(lany: List[Any]) = myPrintDln("func_1([" + lany + "]: " + lany + ")")

  def func3(lany: List[Any]) = {
    myPrintDln("func3 {\n" +
      lany.map(any => {
        any match {
          case y: Int => "Integer[" + y + "]"
          case y: String => "String[" + y + "]"
          case Class1(y) => "--class1[" + y.toString + "]--"
          case _ => "unsupported type[" + any.toString + "/" + any.getClass + "]"
        }
      })
    )
  }
}

case class Class1(val z: Int) {
  override def toString = this.getClass.getName + "[z=" + z + "]"
}

case class Class2(val z: Int) {
  override def toString = this.getClass.getName + "[z=" + z + "]"
}
