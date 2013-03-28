package labyrinthe

object StatJeton {
    val limit = 1000
}

class StatJeton(val couleur: Couleur) {
    def this() = this(new Couleur("Couleur du temps"))
            var cnt = 0
            var cntDepasse = 0
            var min = Int.MaxValue
            var max= Int.MinValue
            var mean = Double.MinValue
            var history = List.empty[Int]

                    def update(score: Int) {
        if(score==0) {
        } else if(score<StatJeton.limit) {
            history = history :+ score
                    min = Math.min(min, score)
                    max = Math.max(max, score)
                    mean = ((mean*cnt) + score)/(cnt+1)
                    cnt += 1
        } else {
            cntDepasse += 1
        }
    }
    override def toString = "  ["+cnt+"+"+cntDepasse+" - "+min+"/ %4.3f /".format(mean)+max+"]  "
}

