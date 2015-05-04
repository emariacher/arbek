package carte

import java.awt.Color
import scala.collection.immutable.ListSet
import kebra._
import carte.Tableaux._

class Coloriage {
	var lregions = tbx.cb.lregions.lr.sorted(CompareRegions)
			var histoire = List[Region]()
//			System.out.println(MyLog.tag(1)+" "+lregions)

			def colorie: StateMachine = {

					lregions.find(_.stillBlank) match {
					case Some(r) => histoire = histoire :+ r
//							System.out.println(MyLog.func(1)+" region: "+r.toStringC)
							r.colorie
					case _ => StateMachine.termine
					}
			}
			def reverse: StateMachine = {
					var state: StateMachine = StateMachine.colorie
							val r = histoire.last
							System.out.println(" ***"+MyLog.func(1)+"*** region: "+r.toStringC)

							val couleursEncoreLibres = Couleurs.l -- r.couleursDejaEssayees
							r.couleur = (if(couleursEncoreLibres.isEmpty) {	
							  r.couleursDejaEssayees = ListSet[Color]()
								state = StateMachine.reverse
										histoire = histoire.filterNot(_==r)
										Color.white
							} else {										
								r.couleursDejaEssayees = r.couleursDejaEssayees + couleursEncoreLibres.head												
										state = StateMachine.colorie
										couleursEncoreLibres.head
							})
							state
			}			
}
object CompareRegions extends Ordering[Region] {
  def compare(x: Region, y: Region): Int = {
    y.voisins.size-x.voisins.size
  }
}
