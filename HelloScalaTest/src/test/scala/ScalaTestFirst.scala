import org.scalatest._

class ScalaTestFirst
  extends FlatSpec with Matchers {
    "Test1 " should "be OK" in {
      println("Test1")
      true shouldEqual true
    }
}
