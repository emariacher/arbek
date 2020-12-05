package carte

import java.awt.Color
import scala.collection.immutable.ListSet
import kebra._
import carte.Tableaux._

class Coloriage2 extends Coloriage {
  var lastBlackregion: Region = _

  //	System.out.println(MyLog.tag(1)+" "+lregions)
  override def reverse: StateMachine = {
    var state: StateMachine = StateMachine.colorie
    if (histoire.isEmpty) {
      histoire = histoire :+ lastBlackregion
      System.out.println(tbx.count.toString + " ***" + MyLog.tag(1) + "***" + lastBlackregion + " " + lastBlackregion.toStringC)
    }
    require(histoire.nonEmpty, lastBlackregion.toString + " " + lastBlackregion.toStringC)
    val r = histoire.last
    //			System.out.println(" ***"+MyLog.tag(1)+"*** region: "+r.toStringC)
    if (r.couleur == Color.black) {
      lastBlackregion = r
    }

    val couleursEncoreLibres = Couleurs.l -- r.couleursDejaEssayees
    r.couleur = (
      if ((r == lastBlackregion) || (lastBlackregion.voisins.contains(r))) {
        if (couleursEncoreLibres.isEmpty) {
          r.couleursDejaEssayees = ListSet[Color]()
          state = StateMachine.reverse
          histoire = histoire.filterNot(_ == r)
          if (r == lastBlackregion) {
            Color.magenta
          } else {
            Color.orange
          }
        } else {
          r.couleursDejaEssayees = r.couleursDejaEssayees + couleursEncoreLibres.head
          state = StateMachine.colorie
          ZePanel.za ! "normal"
          couleursEncoreLibres.head
        }
      } else {
        r.couleursDejaEssayees = ListSet[Color]()
        state = StateMachine.reverse
        histoire = histoire.filterNot(_ == r)
        Color.lightGray
      })
    state
  }
}