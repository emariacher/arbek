
/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/**
 * @author  John Miller
 * @version 1.0
 * @date    Wed Aug 26 18:41:26 EDT 2009
 * @see     LICENSE (MIT style license file).
 */

package scalation
package stat

import scala.math._
import advmath._

/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/**
 * The StatVector class provides methods for computing common statistics on
 * a data vector.
 * @param dim  the dimension/size of the vector
 */
class StatVector (dim: Int) extends Vec [Double] (Array.ofDim[Double](dim))
{
	//var min: Double = _
	//var max: Double = _
    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Construct a StatVector from an Array.
     * @param u  the array to initialize StatVector
     */
    def this (u: Array [Double])
    {
        this (u.length)
        setAll (u)
        //min = u.toList.min
        //max = u.toList.max
    } // constructor

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Construct a StatVector from a VectorN.
     * @param u  the vector to initialize StatVector
     */
    def this (u: Vec[Double])
    {
        this (u.length)
        setAll (u.toArray)
    } // constructor
    

   /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
   /**
    * Get the number of samples.
    */
    def num: Int = dim

   /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
   /**
    * Compute the variance of this vector.
    */
    def variance: Double = (norm2 - pow (sum, 2) / dim) / dim 

   /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
   /**
    * Compute the standard deviation of this vector.
    */
    def stddev: Double = sqrt (variance)

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Compute the root mean square.
     */
    def rms: Double = sqrt (norm2 / dim.asInstanceOf [Double])

   /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
   /**
    * Compute the confidence interval half-width for the given confidence level.
    * @param p  the confidence level
    */
    def interval (p: Double = .95): Double =
    {
        val df = dim - 1   // degrees of freedom
        if (df < 1) flaw ("interval", "must have at least 2 observations")
        val pp = 1 - (1 - p) / 2.0            // e.g, .95 --> .975 (two tails)
        val t = Quantile.studentTInv (pp, df)
        println ("t = " + t + " stddev = " + stddev + " sqrt (df) = " + sqrt (df))
        t * stddev / sqrt (df)
    } // interval
    
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Generate a header of statisical labels as a string.
     */
    def labels (): String =
    {
        "| %4s | %9s | %9s | %9s | %9s | %9s |".format("num", "min", "max", "mean", "stdDev", "interval")
    } // labels

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Generate a row of statistical results as a string.
     */
    override def toString: String = 
    {
        "| %4d | %9.3f | %9.3f | %9.3f | %9.3f | %9.3f |".format(num, min, max, mean, stddev, interval ())
    } // toString


} // StatVector class

/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/**
 * Object to test the StatVector class.
 */
object StatVectorTest extends App
{
    val v = new StatVector (Array[Double](1, 2, 3, 4, 5, 6))
    println ("v          = " + v)
    println ("v.min      = " + v.min)
    println ("v.max      = " + v.max)
    println ("v.mean     = " + v.mean) 
    println ("v.variance = " + v.variance)
    println ("v.stddev   = " + v.stddev)
    println ("v.rms      = " + v.rms)
    println ("v.interval = " + v.interval ())

} // StatVectorTest object

