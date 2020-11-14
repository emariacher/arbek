package labyrinthe

import labyrinthe.ZePanel._

class StatJeton(val couleur: Couleur) {
  def this() = this(new Couleur("Couleur du temps"))

  var cnt = 0
  var cntDepasse = 0
  var min: Int = Int.MaxValue
  var max: Int = Int.MinValue
  var mean: Double = Double.MinValue
  var history = List.empty[Int]

  def update(score: Int): Unit = {
    if (score == 0) {
    } else if (score < zp.limit) {
      history = history :+ score
      min = Math.min(min, score)
      max = Math.max(max, score)
      mean = ((mean * cnt) + score) / (cnt + 1)
      cnt += 1
    } else {
      cntDepasse += 1
    }
  }

  override def toString: String = "  [" + cnt + "+" + cntDepasse + " - " + min + "/ %4.3f /".format(mean) + max + "]  "
}

