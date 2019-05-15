package fitting

import scala.util.Random

class RouleLesDes(val nombreDeFaces: Int, val nombreDeDes: Int, val seed: Int) {
    val rnd = new Random(seed)

    def getSerie(n: Int) = {
        val range = (0 until nombreDeDes).toList
        (0 until n).toList.map((j: Int) => range.map((i: Int) => rnd.nextInt(nombreDeFaces) + 1).sum.toDouble)
    }
}