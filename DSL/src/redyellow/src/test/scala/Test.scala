package redyellow

import org.scalatest.FunSuite
import redyellow.RedYellowDSL._
import redyellow.DateDSL._
import language.postfixOps
import org.scalatest.Matchers._

class Test extends FunSuite {

  test("Test standard scenarios") {
    (Rule red List(1, 2)).p.toString should equal("RedList(1,2) threshold: 0, ")
    (Rule red List(1, 2) next nand yellow List(3, 4) threshold 2 next nand red List(5, 6)
      ).p.toString should equal("RedList(1,2,5,6) threshold: 0, YellowList(3,4) threshold: 2, ")
    (Rule red List(1, 2) threshold 2 next nand green List(3, 4) next nand green List(5, 6)
      ).p.toString should equal("RedList(1,2) threshold: 2, GreenLists(List(3, 4) - List(5, 6)), ")
    (Rule red List(1, 2) threshold 2 next nand green List(3, 4) next nand green List(5, 6) next
      ).p.toString should equal("RedList(1,2) threshold: 2, GreenLists(List(3, 4) - List(5, 6)), ")
    (Rule red List(1, 2) threshold 2 next nand green List(3, 4) next nand green List(5, 6) next nand
      ).p.toString should equal("RedList(1,2) threshold: 2, GreenLists(List(3, 4) - List(5, 6)), ")
    (Rule red List(1, 2) threshold 2 next nand red List(7, 8) threshold 7
      ).p.toString should equal("RedList(1,2,7,8) threshold: 7, ")
    (Rule red List(1, 2) threshold 2 yellow List(7, 8) threshold 7
      ).p.toString should equal("RedList(1,2) threshold: 2, YellowList(7,8) threshold: 7, ")
    (Rule red List(1, 2) threshold 2 green List(3, 4) yellow List(7, 8) threshold 7 green List(5, 6) red List(9, 10) threshold 5
      ).p.toString should equal("RedList(1,2,9,10) threshold: 5, YellowList(7,8) threshold: 7, GreenLists(List(3, 4) - List(5, 6)), ")
    (Rule red List(1, 2) threshold 2 next nand red List(1, 2) threshold 7
      ).p.toString should equal("RedList(1,2,1,2) threshold: 7, ")
  }

  test("Test error cases") {
    an[java.lang.IllegalArgumentException] shouldBe thrownBy {
      Rule threshold 5
    }
    an[java.lang.IllegalArgumentException] shouldBe thrownBy {
      Rule green List(1, 2) threshold 5
    }
    an[java.lang.IllegalArgumentException] shouldBe thrownBy {
      Rule red List(1, 2) next nand threshold 2
    }
  }

  test("Mixing RedYellow and DSLDates") {
    (Rule red List(1, 2) threshold 2 target (now is "10/1/2011" minus 9 years and plus 1 day) next nand green List(3, 4)
      ).p.toString should equal("RedList(1,2) threshold: 2, GreenLists(List(3, 4)), TargetDate(02oct.02), ")
  }

  test("DSLDates") {
    ((now - 1 day) before now) shouldBe true
    ((now + 1 day) after now) shouldBe true
    ((now is "10/1/2011" minus 9 years and plus 1 day) after now) shouldBe false
  }
}
