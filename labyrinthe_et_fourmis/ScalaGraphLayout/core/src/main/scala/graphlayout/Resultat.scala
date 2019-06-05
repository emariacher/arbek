package graphlayout

import kebra.MyLog._

class Resultat {
  var cpt = 0
  var ltimestamps = scala.collection.mutable.Map[StateMachine, Int]()
  var lretourne = List.empty[Int]
  var lcompteurState = scala.collection.mutable.Map[FourmiStateMachine, Int]()

  def log1(lts: scala.collection.mutable.Map[StateMachine, Int]
           , lr: List[Int]
           , lcpts: scala.collection.mutable.Map[FourmiStateMachine, Int]
          ) = {
    ltimestamps = lts
    lretourne = lr
    lcpts.foreach(kv => lcompteurState(kv._1) = ((lcompteurState.getOrElse(kv._1, 0) * cpt) + kv._2) / (cpt + 1))
    cpt += 1
  }

  def printlog1 = {
    myPrintD("ici!\n")
    myErrPrintDln(cpt, ltimestamps.mkString("", ", ", ""))
    myErrPrintln(lretourne.mkString("  ", ", ", ""))
    myErrPrintln(lcompteurState.mkString(" ", ",", ""))
  }
}
