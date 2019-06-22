package graphlayout

import java.io.{BufferedWriter, File, FileWriter}

import kebra.MyFile
import kebra.MyLog._

class Resultat {
  var filestatsname = L.working_directory + "filestats"
  val fstream = new FileWriter(filestatsname + ".res", true)
  val outfs = new BufferedWriter(fstream)
  var filestats = new MyFile(filestatsname + "res2", true)
  var cpt = 0
  var ltimestamps = List[scala.collection.mutable.Map[StateMachine, Int]]()
  var lretourne = List.empty[List[Int]]
  var lcompteurState = List[scala.collection.mutable.Map[FourmiStateMachine, Double]]()

  def log1(lts: scala.collection.mutable.Map[StateMachine, Int]
           , lr: List[Int]
           , lcpts: scala.collection.mutable.Map[FourmiStateMachine, Int]
          ) = {
    ltimestamps = ltimestamps :+ lts
    lretourne = lretourne :+ lr
    val lcpts2 = lcpts.map(kv => (kv._1, kv._2.toDouble))
    lcpts2(FourmiStateMachine.ratioTourneEnRondSurLaTrace) = lcpts2.getOrElse(FourmiStateMachine.tourneEnRond, 0.0) / lcpts2.getOrElse(FourmiStateMachine.surLaTrace, 0.0)
    lcompteurState = lcompteurState :+ lcpts2
    //lcpts.foreach(kv => lcompteurState(kv._1) = ((lcompteurState.getOrElse(kv._1, 0) * cpt) + kv._2) / (cpt + 1))
    cpt += 1
  }

  def printlog1 = {
    myPrintD(Console.GREEN + "ici!\n" + Console.RESET)
    myErrPrintDln(cpt, ltimestamps.mkString("", "\n  ", ""))
    myErrPrintln(lretourne.mkString("  ", "\n  ", ""))
    myErrPrintln(lcompteurState.mkString("  ", "\n  ", ""))
  }

  def printFinalLog1 = {
    myPrintD(Console.BLUE + "la!\n" + Console.RESET)
    val ltsflatten = ltimestamps.flatten
    var lts = ltsflatten.map(_._1).distinct.map(k => {
      ltsflatten.filter(_._1 == k)
      (k, ltsflatten.filter(_._1 == k).map(_._2).sum / cpt)
    })
    myErrPrintDln(cpt, lts.mkString("", ", ", ""))
    myErrPrintln(lretourne.mkString("  ", ", ", ""))
    val lcsflatten = lcompteurState.flatten
    var lcs = lcsflatten.map(_._1).distinct.map(k => {
      lcsflatten.filter(_._1 == k)
      (k, lcsflatten.filter(_._1 == k).map(_._2).sum / cpt)
    }).toMap
    myErrPrintDln(lcs.mkString(" ", ",", ""))
    myPrintIt("\n", lcs.getOrElse(FourmiStateMachine.tourneEnRond, 0.0) / lcs.getOrElse(FourmiStateMachine.surLaTrace, 0.0),
      lcs.getOrElse(FourmiStateMachine.tourneEnRond, 0.0), lcs.getOrElse(FourmiStateMachine.surLaTrace, 0.0),
      lcs.getOrElse(FourmiStateMachine.ratioTourneEnRondSurLaTrace, 0.0))

    if (filestats.length < 2) {
      filestats.writeFile("date," + ParametresPourFourmi.getFields + ",tourneEnRond/surLaTrace, " +
        "tourneEnRond, surLaTrace, ratioTourneEnRondSurLaTrace\n")
    }
    filestats.writeFile(printToday("ddMMMyy_HH_mm") +
      "," + ParametresPourFourmi.getValues + "," + (lcs.getOrElse(FourmiStateMachine.tourneEnRond, 0.0)
      / lcs.getOrElse(FourmiStateMachine.surLaTrace, 0.0)) + "," +
      lcs.getOrElse(FourmiStateMachine.tourneEnRond, 0.0) + "," + lcs.getOrElse(FourmiStateMachine.surLaTrace, 0.0) + "," +
      lcs.getOrElse(FourmiStateMachine.ratioTourneEnRondSurLaTrace, 0.0) + "\n"
    )
    outfs.write(printToday("ddMMMyy_HH_mm") +
      "," + ParametresPourFourmi.getValues + "," + (lcs.getOrElse(FourmiStateMachine.tourneEnRond, 0.0)
      / lcs.getOrElse(FourmiStateMachine.surLaTrace, 0.0)) + "," +
      lcs.getOrElse(FourmiStateMachine.tourneEnRond, 0.0) + "," + lcs.getOrElse(FourmiStateMachine.surLaTrace, 0.0) + "," +
      lcs.getOrElse(FourmiStateMachine.ratioTourneEnRondSurLaTrace, 0.0) + "\n"
    )
    filestats.close
    outfs.close
  }
}
