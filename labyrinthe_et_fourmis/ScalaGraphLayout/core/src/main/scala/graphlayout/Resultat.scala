package graphlayout

import kebra.MyFile
import kebra.MyLog._

class Resultat {
  var filestatsname = L.working_directory + "filestats.aaa"
  var filestats = new MyFile(filestatsname, true)
  var cpt = 0

  def log1(lts: scala.collection.mutable.Map[StateMachine, Int]
           , lr: List[Int]
           , lcpts: scala.collection.mutable.Map[FourmiStateMachine, Int]
          ) = {
    val lcpts2 = lcpts.map(kv => (kv._1, kv._2.toDouble))
    lcpts2(FourmiStateMachine.ratioTourneEnRondSurLaTrace) = lcpts2.getOrElse(FourmiStateMachine.tourneEnRond, 0.0) / lcpts2.getOrElse(FourmiStateMachine.surLaTrace, 0.0)
    if (filestats.length < 2) {
      filestats.writeFile("date," + ParametresPourFourmi.getFields + ",tourneEnRond/surLaTrace, " +
        "tourneEnRond, surLaTrace, ratioTourneEnRondSurLaTrace" + lts.map(_._1).mkString(", ") + "\n")
    }
    filestats.writeFile(printToday("ddMMMyy_HH_mm") +
      "," + ParametresPourFourmi.getValues + "," + (lcpts2.getOrElse(FourmiStateMachine.tourneEnRond, 0.0)
      / lcpts2.getOrElse(FourmiStateMachine.surLaTrace, 1.0)) + "," +
      lcpts2.getOrElse(FourmiStateMachine.tourneEnRond, 0.0) + "," + lcpts2.getOrElse(FourmiStateMachine.surLaTrace, 0.0) + "," +
      lcpts2.getOrElse(FourmiStateMachine.ratioTourneEnRondSurLaTrace, 0.0) + lts.map(_._2).mkString(", ") + "\n"
    )
    cpt += 1
  }

  def printFinalLog1 = {
    myPrintD(Console.BLUE + "la!\n" + Console.RESET)
    filestats.close
  }
}
