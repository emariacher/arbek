package graphlayout

import java.awt.{Color, Graphics2D}

class GNode(val lbl: String) {
  var x = .0
  var y = .0

  var fixed = false

  override def toString: String = "{" + lbl + "}"

  override def hashCode: Int = lbl.hashCode

  override def equals(anyo: Any): Boolean = {
    anyo match {
      case g: GNode => hashCode.equals(g.hashCode)
      case _ => false
    }
  }

  def paint(g: Graphics2D) {
    val w = g.getFontMetrics.stringWidth(lbl) + 10
    val h = g.getFontMetrics.getHeight() + 4
    g.setColor(Color.orange)
    g.fillRect(x.toInt - w / 2, y.toInt - h / 2, w, h)
    g.setColor(Color.black)
    g.drawRect(x.toInt - w / 2, y.toInt - h / 2, w - 1, h - 1)
    g.drawString(lbl, x.toInt - (w - 10) / 2, (y.toInt + (h - 8) / 2))
  }

  def pasLoin(mx: Int, my: Int): Double = {
    //MyLog.myPrintIt(toString, mx, x, my, y, Math.sqrt(((mx - x) * (mx - x)) + ((my - y) * (my - y))))
    Math.sqrt(((mx - x) * (mx - x)) + ((my - y) * (my - y)))
  }

  def remetsDansLeTableau(largeur: Int, hauteur: Int, border: Int): Unit = {
    x = Math.max(Math.min(largeur - border, x), border)
    y = Math.max(Math.min(hauteur - border, y), border)
  }
}
