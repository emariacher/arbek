package carte

import scala.util.Random
import java.awt.Dimension
import java.io.File
import java.util.Calendar
import kebra.MyLog._

object Tableaux {
  var tbx: Tableaux = _

  def newTbx(zp: ZePanel, maxRow: Int, maxCol: Int, size: Dimension, origin: Dimension): Unit = {
    tbx = new Tableaux(zp, maxRow, maxCol, size, origin)
    myPrintIt(FrontiereV.values, CantonTypeV.values)
  }
}

class Tableaux(val zp: ZePanel, val maxRow: Int, val maxCol: Int, val size: Dimension, val origin: Dimension) {
  val seeds: List[Int] = List(0, -2002675365, -1995825069, -2000188942, -2003116831, -1996108355, -1995594583)
  var state: StateMachine = StateMachine.generate
  var cb = new CarteBlanche
  var carte: Coloriage = _
  var seedIndex = 0
  var seed: Int = getNextSeed
  var rnd = new Random(seed)
  var count = 0

  def doZeJob(command: String, timeout: Int): Unit = {
    //				System.out.println(MyLog.tag(1)+" state: "+StateMachine.nameOf(state)+" "+count)
    zp.lbl.text = "Seed: " + seed + ",  " + cb + ", CountSteps: " + count
    state match {
      case StateMachine.generate => state = cb.addCarre(timeout)
      case StateMachine.repandRegions => state = cb.repandRegions
      case StateMachine.cleanRegions => state = cb.cleanRegions
      case StateMachine.getRegions => state = cb.getRegions
        carte = new Coloriage3
      case StateMachine.colorie => state = carte.colorie
        zp.slow_timeout = 1
        count += 1
      case StateMachine.reverse => state = carte.reverse
        zp.slow_timeout = 200
        count += 1
      case StateMachine.termine =>
        System.err.println(zp.lbl.text)
        state = reset
        if ((command == "step") || zp.run) {
          zp.pause = false
        } else {
          zp.pause = true
        }
      case _ =>
    }
  }

  def reset: StateMachine = {
    seed = getNextSeed
    rnd = new Random(seed)
    cb = new CarteBlanche
    count = 0
    StateMachine.generate
  }

  def getNextSeed: Int = {
    if (seedIndex < seeds.size) {
      seedIndex += 1
      seeds.apply(seedIndex - 1)
    } else {
      Calendar.getInstance.getTimeInMillis.toInt
    }
  }
}

case class StateMachine private(i: Int)

object StateMachine {
  val generate = StateMachine(0)
  val repandRegions = StateMachine(5)
  val cleanRegions = StateMachine(7)
  val getRegions = StateMachine(10)
  val colorie = StateMachine(20)
  val reverse = StateMachine(25)
  val termine = StateMachine(30)
}