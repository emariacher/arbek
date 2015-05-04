package carte
import scala.collection.immutable.ListSet
import java.awt.Color


class Region(val tbx: Tableaux, val canton: Canton) {
	val id = tbx.cb.lregions.lr.size
			var voisins = ListSet[Region]()
			var cantons = ListSet[Canton](canton)
			var couleur = Color.white
			var couleursDejaEssayees = ListSet[Color]()

			override def toString: String = {
					if (voisins.isEmpty) {
						"Region{["+id+"]: pdv}"
					}else {
						"Region{["+id+"]: "+voisins.tail.foldLeft("["+voisins.head.id+"]")(_ + ", ["+_.id+"]")+"}"
					}
			}
			def toStringC: String = {
					"Region{["+id+"]: ("+couleur+") "+couleursDejaEssayees+"}"
			}
			def colorie: StateMachine = {
							var state: StateMachine = StateMachine.colorie
									voisins.foreach((r: Region) => couleursDejaEssayees = couleursDejaEssayees + r.couleur)
									val couleursEncoreLibres = Couleurs.l -- couleursDejaEssayees
									couleur = (if(couleursEncoreLibres.isEmpty) {										
										state = StateMachine.reverse
												Color.black
									} else {										
										couleursDejaEssayees = couleursDejaEssayees + couleursEncoreLibres.head												
												state = StateMachine.colorie
												couleursEncoreLibres.head
									})
									state
					}
					def repand = {
							cantons.find(!_.cantonsVoisinsDefined) match {
							case Some(canton) => canton.repandVoisin(0)
							case _ => 
							}
					}
					def voisinsColories: Int = {
							voisins.filterNot(_.stillBlank).size
					}
					def stillBlank: Boolean = {
								!Couleurs.l.contains(couleur)
							}

}

object Couleurs {
	var l = ListSet(Color.yellow,Color.red,Color.green,Color.cyan)
}
