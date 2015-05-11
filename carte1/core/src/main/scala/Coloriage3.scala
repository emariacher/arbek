package carte
import java.awt.Color
import scala.collection.immutable.ListSet
import carte.Tableaux._

class Coloriage3 extends Coloriage2 {
//	System.out.println(MyLog.tag(1)+" "+lregions)
	override def colorie: StateMachine = {
		lregions = tbx.cb.lregions.lr.sorted(CompareRegions3)
				lregions.find(_.stillBlank) match {
				case Some(r) => histoire = histoire :+ r
						//							System.out.println(MyLog.func(1)+" region: "+r.toStringC)
						r.colorie
				case _ => StateMachine.termine
		}
	}
}
object CompareRegions3 extends Ordering[Region] {
	def compare(x: Region, y: Region): Int = {
			(100*(y.voisinsColories-x.voisinsColories))+(y.voisins.size-x.voisins.size)
	}
}
