/* $Id: RichIntegral.scala 73 2011-03-04 01:53:24Z mepcotterell@gmail.com $ */

package scalation.rich

/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/**
 * Defines some rich methods (mostly defined with Unicode) that can be used 
 * by an integral type.
 * @author Michael Cotterell
 */
class RichIntegral[A: Integral](elem: A) extends scalation.ScalaTion {

	private val evidence = implicitly[Integral[A]]
	
}