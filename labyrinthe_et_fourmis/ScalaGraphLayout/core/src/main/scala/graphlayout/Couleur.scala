package graphlayout

import java.awt.Color

class Couleur(val couleur: String) {
  val color = couleur match {
    case "rouge" => Color.red
    case "orange" => Color.orange
    case "vertClair" => Color.green
    case "bleu" => Color.blue
    case "bleuClair" => Color.cyan
    case "pourpre" => Color.magenta
    case "grisFonce" => Color.darkGray
    case "grisClair" => Color.lightGray
    case "violet" => new Color(0x900090)
    case "marron" => new Color(0xb00050)
    case "vertFonce" => new Color(0x008000)
    case _ => throw new Exception("NOGOOD!")
  }

  override def toString = couleur
}

