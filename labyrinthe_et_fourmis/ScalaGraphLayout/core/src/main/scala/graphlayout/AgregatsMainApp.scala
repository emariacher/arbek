package graphlayout

import scala.swing.Label

/**
  * Created by ericm_000 on 16.03.2016.
  */
object AgregatsMainApp extends UI(new Agregats)

case class Tribu private(c: Couleur) {
  var label = new Label {
    text = "*"
    foreground = c.color
  }
}

object Tribu {
  val tribus = List(new Tribu(new Couleur("rouge"))/*, new Tribu(new Couleur("violet"))*/)
  /*val tribus = List(new Tribu(new Couleur("orange")), new Tribu(new Couleur("vertFonce")),
    new Tribu(new Couleur("bleu")), new Tribu(new Couleur("rouge")), new Tribu(new Couleur("violet")),
    new Tribu(new Couleur("bleuClair")))*/
}

