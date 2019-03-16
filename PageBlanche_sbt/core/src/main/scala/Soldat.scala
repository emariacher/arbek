/**
 * Created by emariacher on 17.03.2016.
 */
package pageblanche

class Soldat(couleur: String, rayon: Int, fourmiliere: Fourmiliere) extends Bleu(couleur, rayon, fourmiliere) {
  override val role = Role.SOLDAT
}
