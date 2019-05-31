package graphlayout

import graphlayout.Tableaux.tbx
import kebra.MyLog._

class Resultat {
  var ltimestamps = scala.collection.mutable.Map[StateMachine, Int]()
  var lretourne = List.empty[Int]
  var lcompteurState = scala.collection.mutable.Map[FourmiStateMachine, Int]()

  def log1(lts: scala.collection.mutable.Map[StateMachine, Int]
           , lr: List[Int]
           , lcpts: scala.collection.mutable.Map[FourmiStateMachine, Int]
          ) = {
    ltimestamps = lts
    lretourne = lr
    lcompteurState = lcpts
  }

  def printlog1 = {
    myErrPrintDln(ltimestamps.mkString("", ", ", ""))
    myErrPrintln(lretourne.mkString("  ", ", ", ""))
    myErrPrintln(lcompteurState.mkString(" ", ",", ""))
  }
}
