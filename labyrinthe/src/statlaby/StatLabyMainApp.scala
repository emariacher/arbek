package statlaby

/**
 * objectif: verifier que les distributions des differents jetons suivent des lois de Poisson
 * 
 * afficher graphiquement les distributions OK
 * evaluer l'incertitude sur la coherence entre la theorie et la pratique suivant le nombre de runs disponibles
 * trouver les parametres des distributions
 */

import _root_.scalaSci.math.plot.plotTypes._
import _root_.scalaSci.math.plot.plot._
import _root_.scalaSci.math.plot.canvas._
import scala.swing.Label
import scala.actors.Actor
import kebra.MyLog._
import kebra.MyPlotUtils._
import labyrinthe._
import labyrinthe.Tableaux._
import labyrinthe.LL._
import java.io.File
import java.awt.Color

object StatLabyMainApp extends App with Actor {
    println("Hello World!")

    newMyLL(this.getClass.getName,new File("out\\cowabunga"), "htm", false)
    newTbx(null, new RowCol(60,60), null, null)

    val fig1 = figure()
    title("Hello World1!")
    myPrintln("  fig("+fig1+")")

    val fig2 = figure()
    title("Hello World2!")
    myPrintln("  fig2("+fig2+")")

    start
    var afficheOnlyOnce = true
    def act() {
        loop {
            reactWithin(0) {              
            case _ => 
            tbx.doZeJob2
            val nrOfRuns = tbx.mjs.map((cj:(Couleur,StatJeton)) => cj._2.cnt).max
            if(nrOfRuns%20==1) {
                if(afficheOnlyOnce) {
                    affiche
                    afficheOnlyOnce = false
                    myPrintDln("\n"+tbx.mjs.map((cj:(Couleur,StatJeton)) => (cj._1,cj._2.history.drop(12))).toList.mkString("List(\n  ",",\n  ","\n)"))
                }                
            } else {
                afficheOnlyOnce = true
            }
            }
        }
    }

    def getOptimalLength(ll: List[List[Double]]): Int = {
            if(!ll.isEmpty) {
                ll.map((l: List[Double]) => 
                if(l.length>1) {
                    var inc = Math.max(l.max - l.min,1.0)
                            // requirement 1: Pour que cette approximation soit tres bonne et bien que le test du c2 s'avere robuste, 
                            //il est conseille  que les produits ti = n*pi, c'est a dire les effectifs theoriques ti, soient egaux ou superieurs a 5 et de 
                            //regrouper les classes adjacentes lorsque ce minimum est rencontre.
                            if(l.length>=5) {
                                inc = (((l.max - l.min)/l.length)*5)
                                        var splitted = Map.empty[Int,List[Double]]
                                                var serieY = List.empty[Int]
                                                        var OK = false
                                                        inc -= 1
                                                        inc = Math.max(inc,0)
                                                        while(!OK) {
                                                            inc += 1
                                                                    splitted = l.groupBy{(d: Double) => (d/inc).toInt}
                                                            serieY = splitted.toList.sortBy(_._1).map((c:(Int,List[Double])) => c._2.length)
                                                                    OK = serieY.min >=5         
                                                        }
                                // requirement 2: il ne faudrait pas qu'une barre ait plus de 30% des samples
                                if(l.length>=10) {
                                    while(serieY.max.toDouble/serieY.sum.toDouble>0.3) {
                                        inc -= 1
                                                splitted = l.groupBy{(d: Double) => (d/inc).toInt}
                                        serieY = splitted.toList.sortBy(_._1).map((c:(Int,List[Double])) => c._2.length)
                                    }
                                }
                            }
                    //myPrintDln("l: "+l)
                    printIt(inc)
                    myAssert(inc>0.0)
                    //kebra.MyLog.myPrintIt((l.max - l.min)/inc)
                    (l.max - l.min)/inc 
                } else {
                    myPrintDln("empty l: "+l)
                    0.0
                }).max.toInt
            } else {
                myPrintDln("empty ll: "+ll)
                0
            }
    }

    def getX4BestFitting(l: List[Double]): (List[Double],List[Double])  = {
        // requirement 1: Pour que cette approximation soit tres bonne et bien que le test du c2 s'avere robuste, 
        //il est conseille  que les produits ti = n*pi, c'est a dire les effectifs theoriques ti, soient egaux ou superieurs a 5 et de 
        //regrouper les classes adjacentes lorsque ce minimum est rencontre.
        if(l.length>=5) {
            var inc = (((l.max -l.min)/l.length)*5)
                    var splitted = Map.empty[Int,List[Double]]
                            var serieY = List.empty[Int]
                                    var OK = false
                                    inc -= 1
                                    myRequire(inc>=0)
            while(!OK) {
                inc += 1
                        splitted = l.groupBy{(d: Double) => (d/inc).toInt}
                serieY = splitted.toList.sortBy(_._1).map((c:(Int,List[Double])) => c._2.length)
                        OK = serieY.min >=5    		
            }
            // requirement 2: il ne faudrait pas qu'une barre ait plus de 30% des samples
            if(l.length>=10) {
                while(serieY.max.toDouble/serieY.sum.toDouble>0.3) {
                    inc -= 1
                            splitted = l.groupBy{(d: Double) => (d/inc).toInt}
                    serieY = splitted.toList.sortBy(_._1).map((c:(Int,List[Double])) => c._2.length)
                }
            }
            var serieX = List.empty[Double]
                    serieX = new Range(0,serieY.length,1).toList.map((i: Int) => i*inc +(l.min+(inc/2)))
                    myRequire(serieX.length==serieY.length)
                    //myPrintln("  "+inc+" "+l.min+" "+l.max+" "+serieX)
                    (serieX,serieY.map(_.toDouble))
        } else {
            (List.empty[Double],List.empty[Double])
        }
    }

    def affiche {
        clf(fig1)
        figure(fig1)        
        linePlotsOn
        val lljs = tbx.mjs.map((cj:(Couleur,StatJeton)) => cj._2.history.map(_.toDouble))
        val lengthjs = getOptimalLength(lljs.toList)
        title("Vitesse d echappement des jetons ["+lengthjs+"]")
        //kebra.MyLog.myPrintIt(lengthjs.toDouble)
        //myErrPrintln(""+tbx.mjs)
        tbx.mjs.foreach((cj:(Couleur,StatJeton)) => {
            val couples = getCouples(lengthjs, cj._2.history.map(_.toDouble))
                    //val couples = getX4BestFitting(cj._2.history.map(_.toDouble))
                    if(couples._1.length>0) {
                        plot(couples._1.toArray, couples._2.toArray, cj._1.color,
                                "["+cj._1.toString+"] "+cj._2.cnt+" %3.5f".format(cj._2.mean))
                    }
        })     
        clf(fig2)
        figure(fig2)
        //scatterPlotsOn
        linePlotsOn
        val llperfs = tbx.mperfs.map((ip:(Int,List[Double])) => ip._2)
        if(!llperfs.isEmpty) {
            val lengthperfs = getOptimalLength(llperfs.toList)
                    title("Vitesse des workers akka ["+lengthperfs+"/"+(llperfs.map(_.length).sum/llperfs.size)+"]")
                    //myErrPrintln(""+tbx.mperfs)
                    var hue = 0.1.toFloat
                    val incHue = 1.0.toFloat/tbx.mperfs.size
                    tbx.mperfs.toList.sortBy{_._1}.foreach((ip:(Int,List[Double])) => {
                        val couples = getCouples(lengthperfs, ip._2)
                                //val couples = getX4BestFitting(ip._2)
                                if(couples._1.length>0) {
                                    plot(couples._1.toArray, couples._2.toArray, getColor(hue),
                                            "["+ip._1.toString+"] %3.2f".format(ip._2.sum/ip._2.length))    
                                }
                        hue += incHue
                    })   
        }
    }
}
