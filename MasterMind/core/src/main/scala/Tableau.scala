package mm

import java.util.Calendar

import kebra.MyLog._

import scala.util.Random

object Tableau {
  var tbl: Tableau = _

  def newTbl {
    tbl = new Tableau
  }
}

class Tableau {
  var state = StateMachine.poseProbleme
  var oldstate = StateMachine.poseProbleme
  var maxCouleur = 6
  var maxJeton = 5
  var seed: Int = 0
  var rnd: Random = _
  var countPointInterrogation = 0
  var t_perf: Long = 0
  var probleme: Rangee = _
  var hypotheses = List.empty[Rangee]

  def doZeJob(command: String, graphic: Boolean) {
    myErrPrintDln(" state: " + state + " " + command)
    if (graphic) {
      MasterMindMain.label.text = "Seed: " + seed
    }
    state match {
      case StateMachine.poseProbleme => state = poseProbleme(seed)
        seed += 1
      case StateMachine.emetHypothese => state = emetHypothese
      case StateMachine.repond => state = repond
      case _ =>
    }
  }

  def poseProbleme(seed: Int): StateMachine = {
    rnd = new Random(seed)
    probleme = new Rangee
    probleme.noirs = maxJeton
    hypotheses = List.empty[Rangee]
    t_perf = 0
    myPrintIt(probleme)
    StateMachine.emetHypothese
  }

  def emetHypothese: StateMachine = {
    val t_start = Calendar.getInstance();
    hypotheses = hypotheses :+ findReasonableHypothese1
    val t_end = Calendar.getInstance();
    t_perf += t_end.getTimeInMillis() - t_start.getTimeInMillis
    StateMachine.repond
  }

  def findReasonableHypothese1: Rangee = {
    var hypo = new Rangee
    hypotheses.find((r: Rangee) => !(hypo ? r)) match {
      case Some(y) => hypo = findReasonableHypothese1
      case _ =>
    }
    hypo
  }

  def repond: StateMachine = {

    myPrintDln("\n******************************")
    myPrintIt(hypotheses)
    if (hypotheses.last ? probleme) {
      myErrPrintDln("En " + hypotheses.length + " coups en " + t_perf + " ms")
      sys.exit
      StateMachine.poseProbleme
    }
    else StateMachine.emetHypothese
  }
}

case class StateMachine private(state: String) {
  override def toString = "State_" + state
}

object StateMachine {
  val poseProbleme = StateMachine("poseProbleme")
  val emetHypothese = StateMachine("emetHypothese")
  val repond = StateMachine("repond")
}

