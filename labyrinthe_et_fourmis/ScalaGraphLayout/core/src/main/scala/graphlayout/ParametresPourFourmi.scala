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


  def printStuff = {
    type Tint = Int
    val lfields = getClass.getDeclaredFields.toList
    lfields.foreach(f => myPrintDln("" + f.toString + " [" + f.getType + "]"))
    lfields.foreach(f => {
      f.setAccessible(true)
      myPrintDln("" + f.toString + " [" + f.getType + "]")
      if (f.getType.toString == "int") {
        myPrintDln("" + f.toString + " [" + f.get(this) + "]")
        //myPrintDln("" + f.toString + " [" + f.asInstanceOf[Int] + "]")
      }
    })
  }

  /*
  https://stackoverflow.com/questions/6756442/scala-class-declared-fields-and-access-modifiers
  https://stackoverflow.com/questions/19386964/i-want-to-get-the-type-of-a-variable-at-runtime
  def printParams = classOf[ParametresPourFourmi].getDeclaredFields foreach { field =>

    field.setAccessible(true)
    myPrintDln(field.get(this))
  }
  */
}
