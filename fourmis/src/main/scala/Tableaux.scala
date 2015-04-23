package labyrinthe

import scala.util.Random
import java.awt.Dimension
import java.io.File
import java.util.Calendar
import kebra._
import kebra.MyLog._
import java.awt.Color
import labyrinthe.LL._
import statlaby.AkkaJeton

object Tableaux {
    var tbx: Tableaux = _
    def newTbx(zp: ZePanel, maxRC: RowCol, size: Dimension, origin: Dimension) {
        tbx = new Tableaux(zp, maxRC, size, origin)
    }
}

class Tableaux(val zp: ZePanel, val maxRC: RowCol, val size: Dimension, val origin: Dimension) {
    val maxRow = maxRC.r
    val maxCol = maxRC.c
    var state = StateMachine.reset
    var oldstate = StateMachine.reset
    var seedIndex = 0
    //val seeds = List(0, -1258712602, -2003116831, -2000188942, -2003116831, -1172155944) // valeurs interessantes pour un 10X10
    val seeds = (0 until 11).toList :+ (-1171074276) // valeurs interessantes pour un 20X20
    var seed: Int = _
    var rnd: Random = _
    var countGenere = 0
    var countAvance = 0
    var lc = List.empty[Carre]
    var lj = List(new Rouge("rouge", 80), new Orange("orange", 75),
        new VertFonce("vertFonce", 70), new VertClair("vertClair", 65),
        new Bleu("bleu", 60), new BleuClair("bleuClair", 55))
    val mj = lj.map((j: Jeton) => (j.couleur, j)).toMap
    val mjs = lj.map((j: Jeton) => (j.couleur, new StatJeton(j.couleur))).toMap
    var ltimestamps = List[Long](0)
    val maxWorkers = 5
    val mperfs = scala.collection.mutable.Map.empty[Int, List[Double]]
    var t_startAkka: Calendar = _

    def doZeJob(command: String, graphic: Boolean) {
        l.myPrintDln(" state: " + state + " cg: " + countGenere + " ca: " + countAvance + " " + command)
        if (graphic) { zp.lbl.text = "Seed: " + seed + ", CountSteps: " + countGenere }
        state match {
            case StateMachine.genere =>
                state = genere
                countGenere += 1
            case StateMachine.nettoie => state = nettoie
            case StateMachine.avance =>
                state = avance
                countAvance += 1
            case StateMachine.reset => state = reset
            case StateMachine.termine =>
                if (graphic) {
                    l.myErrPrintln(zp.lbl.text)
                    if ((command == "step") || zp.run) {
                        zp.pause = false
                    } else {
                        zp.pause = true
                    }
                }
                state = StateMachine.reset
            case _ =>
        }
    }

    def doZeJob2 {
        //if(oldstate!=state) L.myPrintDln(" state: "+state)
        oldstate = state
        state match {
            case StateMachine.genere =>
                state = genere
                countGenere += 1
            case StateMachine.nettoie => state = nettoie
            case StateMachine.avance  => state = avance2
            case StateMachine.attend  =>
            case StateMachine.reset   => state = reset2
            case StateMachine.termine => state = StateMachine.reset
            case _                    =>
        }
    }

    def nettoie: StateMachine = {
        lc.foreach(_.nettoie)
        lj.foreach(_.init)

        StateMachine.avance
    }

    def avance: StateMachine = {
        if (lj.map(_.avance).filter((sm: StateMachine) => sm == StateMachine.avance).isEmpty) {
            StateMachine.termine
        } else {
            StateMachine.avance
        }
    }

    var nrOfWorkers = 4
    def avance2: StateMachine = {
        if (seedIndex > 10) {
            nrOfWorkers = rnd.nextInt(maxWorkers) + 5
            t_startAkka = Calendar.getInstance();
        }
        AkkaJeton.goJetons(nrOfWorkers, lj)
        StateMachine.attend
    }

    def genere: StateMachine = {
        l.myPrintln(MyLog.tag(1) + " genere")
        val notFulls = lc.filter(_.notFull == true).map(_.genere).filter(_.notFull == true)
        l.myPrintln(MyLog.tag(1) + " genere " + notFulls.size)
        if (notFulls.isEmpty) StateMachine.nettoie else StateMachine.genere
    }

    def reset: StateMachine = {
        seed = getNextSeed
        rnd = new Random(seed)
        countGenere = 0
        countAvance = 0
        l.myPrintln(seed)
        lc = (0 until maxRow).map((row: Int) => (0 until maxCol).map((col: Int) => new Carre(row, col))).flatten.toList
        mj.foreach((cj: (Couleur, Jeton)) => {
            val cnt = cj._2.cnt
            val js = mjs.getOrElse(cj._1, new StatJeton())
            if (cnt != 0) {
                cj._2.label.text = js.toString
            }
            js.update(cnt)
            cj._2.resetLocal
        })
        QA
        StateMachine.genere
    }

    def reset2: StateMachine = {
        seed = getNextSeed
        rnd = new Random(seed)
        countGenere = 0
        countAvance = 0
        l.myPrintln(seed)
        lc = (0 until maxRow).map((row: Int) => (0 until maxCol).map((col: Int) => new Carre(row, col))).flatten.toList
        lj.foreach(_.resetLocal)
        QA
        StateMachine.genere
    }

    def getNextSeed: Int = {
        seedIndex += 1
        if (seedIndex < seeds.size) {
            seeds.apply(seedIndex - 1)
        } else {
            Calendar.getInstance.getTimeInMillis.toInt
        }
    }
    def updateStats(lrjc: List[(Couleur, Int)]) {
        lrjc.foreach((ci: (Couleur, Int)) => {
            mjs.getOrElse(ci._1, new StatJeton()).update(ci._2)
        })
        if (seedIndex > 10) {
            val ljArrivesAuBout = lrjc.filter((ci: (Couleur, Int)) => ci._2 > 0 && ci._2 < StatJeton.limit).map((ci: (Couleur, Int)) => ci._2)
            val timeStamp = MyLog.timeStamp(t_startAkka)
            val perf = ljArrivesAuBout.sum * 1000.0 / (nrOfWorkers * ljArrivesAuBout.length * timeStamp)
            mperfs(nrOfWorkers) = mperfs.getOrElse(nrOfWorkers, List.empty[Double]) :+ perf
            //L.myPrintDln(seedIndex+" "+mperfs)
        }
        state = StateMachine.termine
    }
    def QA {
        /*if((seed==10)&(maxRC.r==20)&(maxRC.c==20)&(StatJeton.limit==200)) {                                                
                                                                val ljs = mjs.toList.sorted(CompareStatJeton)
                                                                        L.myErrPrintDln(""+ljs)
                                                                        myAssert(ljs.head._2.min==43)
                                                                        myAssert(ljs.last._2.max==90)
                                                                        myAssert(ljs.last._2.couleur.toString=="turquoise")
                                                                        myAssert(ljs.head._2.cntDepasse==0)
                                                                        myAssert(ljs.apply(1)._2.cntDepasse==2)
                                                                        myAssert(ljs.apply(2)._2.cnt==10)
                                                                        //exit
                                                            } */
        L.myPrint(".")
    }
}

case class StateMachine private (state: String) {
    override def toString = "State_" + state
}

object StateMachine {
    val genere = StateMachine("genere")
    val getJetons = StateMachine("getJetons")
    val nettoie = StateMachine("nettoie")
    val avance = StateMachine("avance")
    val attend = StateMachine("attend")
    val termine = StateMachine("termine")
    val reset = StateMachine("reset")
}

object CompareStatJeton extends Ordering[(Couleur, StatJeton)] {
    def compare(x: (Couleur, StatJeton), y: (Couleur, StatJeton)): Int = {
        if (y._2.max == x._2.max) y._2.min - x._2.min
        else y._2.max - x._2.max
    }
}
