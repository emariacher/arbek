package graphlayout

import kebra.MyLog._

object ParametresPourFourmi {
  var nombreDefourmisParTribu = 4
  var limiteArrete = 120
  var stabilisationRassemble = 100


  var CEstLaFourmiliere = 20.0
  var influenceDesPheromones = 40.0
  var angleDeReniflage = (Math.PI * 4 / 5)
  var compteurPasDansLesPheromonesLimite = 30
  var tourneEnRondLimite = 10
  var suisLeChemin1 = 10
  var suisLeChemin2 = 20
  var avanceAPeuPresCommeAvantDispersion = .1
  var avanceAPeuPresCommeAvantVitesse = 2
  var avanceDroitVitesse = 3
  var sautsTropGrandsLissage = 30


  def printStuff = { // https://stackoverflow.com/questions/6756442/scala-class-declared-fields-and-access-modifiers
    getClass.getDeclaredFields.toList.foreach(f => {
      //f.setAccessible(true)
      myPrintDln("" + f.toString.split("\\.").last + " [" + f.get(this) + "]")
    })
  }
}
