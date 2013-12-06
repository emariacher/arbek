package euler

import kebra.MyLog._
import kebra.MyLog
import kebra.MyFileChooser
import scala.collection.immutable.TreeSet
import java.util.Calendar
import scala.language.postfixOps
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import scala.io.Source
import java.io.BufferedInputStream
import scala.collection.JavaConversions._
import scala.collection.immutable.ListSet
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import _root_.JFplot._
import _root_.JFplot.jFigure._
import java.awt.Color

object EulerMainNoScalaTest extends App {
    //MyLog.newMyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        myPrintDln("Hello World!")

        timeStampIt(new Euler265)
    } catch {
        case ex: Exception => {
            println("\n" + ex)
            println("\n" + ex.getStackTrace.mkString("\n  ", "\n  ", "\n  ") +
                "\n")
            printTimeStampsList
        }
    } finally {
        println("\nHere!")
        printTimeStampsList
        MyLog.closeFiles
        println("\nThere!")
    }
}

class Euler265 {

    doZeStuff("00010111", 3)
    doZeStuff("00011101", 3)
    doZeStuff("01010101", 3)
    bestial(3)
    doZeJob4(4)
    doZeJob3(4)
    bestial(4)
    doZeJob(4)
    doZeJob5(4)
    doZeJob5(5)

    def revert(s: String) = s.reverse.mkString
    def combperm(s: String, length: Int) = s.combinations(length).toList.map(_.permutations.toList)
    def rotations(s: String, length: Int) = {
        val sz = s + s.substring(0, length - 1)
        (0 to sz.length - length).map(i => sz.substring(i, i + length)).toList
    }

    def doZeStuff(s: String, length: Int) = {
        val sz = (1 to length).map(z => "01").mkString
        val cp = combperm(sz, length)
        val cpf = cp.flatten.sorted
        myPrintln(s, "\n  "+cpf)
        val r = rotations(s, length)
        val result = (r.intersect(cpf).sorted == cpf, r.length == cpf.length)
        myPrintln("\n  "+r, "\n  "+r.sorted, "\n  "+(ListSet.empty[String] ++ r.sorted), "\n  "+s, result)
        result
    }

    def doZeJob5(length: Int) = {
        val tl = Math.pow(2, length).toInt
        val rep0 = (1 to length).map(z => "0").mkString
        val sz = (1 to length).map(z => "01").mkString
        val cp = combperm(sz, length)
        val cpf = cp.flatten.sorted
            def checkZeStuff(s: String) = {
                val r = rotations(s, length)
                val result = (r.intersect(cpf).sorted == cpf && r.length == cpf.length)
                result
            }
        // looking for rep0+rep1+0xxxxx patterns
        val rep0Prep1 = ((tl - (2 * length)) until (tl - length))
        val nrep0Prep1 = rep0Prep1.map(Math.pow(2, _)).sum.toInt
        printIt(1 until 10, 1 to 10)
        val offSetFin = Math.pow(2, tl - ((2 * length) + 1)).toInt
        printIt(rep0Prep1, nrep0Prep1, offSetFin)
        val zrange = ((nrep0Prep1 + 1) to (nrep0Prep1 + offSetFin) by 2)
        printIt(zrange)
        timeStampIt(myPrintln(zrange.map(z => (z, z.toBinaryString)).filter(z => checkZeStuff(rep0 + z._2))))
        //timeStampIt(myPrintln(zrange.map(z => (z, z.toBinaryString)).filter(z => uniquePatterns(rep0 + z._2,length))))

        // looking for rep0+1xxxxxxxx  until rep0+repm1+xxxxxx patterns knowing that repm1 is not possible
        val nrep0P1 = Math.pow(2, tl - (length + 1)).toInt
        val rep0P1Pre0m1P1 = 0
        val rep0Prep1m2 = ((tl + 2 - (2 * length)) until (tl - length))
        val rep0P1rep0m1 = ((tl + 2 - (2 * length)) until (tl - length))
        val rep0Prep1m2P0Prep1P0P1u1 = rep0Prep1m2 ++ ((tl + 1 - (3 * length)) until (tl + 1 - (2 * length))) ++ (0 until (tl - (3 * length)))
        printIt(nrep0P1, rep0P1Pre0m1P1, rep0Prep1m2, rep0Prep1m2P0Prep1P0P1u1)
    }

    def doZeJob4(length: Int) = {
        val sz = (1 to length).map(z => "01").mkString
        val cp = combperm(sz, length)
        val cpf = cp.flatten.sorted
        myPrintDln(length+" ***************************************")
        printIt(cpf)
            def checkZeStuff(s: String) = {
                val r = rotations(s, length)
                val result = (r.intersect(cpf).sorted == cpf && r.length == cpf.length)
                /*if (result) {
                    myPrintDln("\n  "+r, "\n  "+r.sorted, "\n  "+(ListSet.empty[String] ++ r.sorted), "\n  "+s, result)
                }*/
                result
            }
        generatePotentialSolutions4(length)
        //val potsol = generatePotentialSolutions4(length).filter(checkZeStuff(_)).sorted

    }

    def generatePotentialSolutions4(length: Int) = {
        val tl = Math.pow(2, length).toInt
        val rep0 = (1 to length).map(z => "0").mkString //a
        val repm0 = "1"+rep0.tail+"1"
        val repm20 = rep0.tail.tail //b
        val rep1 = (1 to length).map(z => "1").mkString //m
        val repm1 = "0"+rep1.tail+"0"
        val repm21 = rep1.tail.tail //n
        myPrintDln(length, tl, rep0, repm0, rep1, repm1)

            /** For each element x in List xss, returns (x, xss - x) */
            def selections3[A](xss: List[A]): List[(A, List[A])] = {
                //myPrintln("    xss: "+xss)
                var q = List.empty[(A, List[A])]
                q = xss match {
                    case Nil => Nil
                    case x :: xs =>
                        (x, xs) :: (for ((y, ys) <- selections2(xs))
                            yield (y, x :: ys))
                }
                //myPrintln("    q: "+q.map(z => (z._1, z._2.mkString)))
                (ListSet.empty[(A, List[A])] ++ q).toList
            }

            /** Returns a list containing all permutations of the input list */
            def permute3[A](xs: List[A]): List[List[A]] = {
                //myPrintln("  xs: "+xs.mkString)
                var r = List.empty[List[A]]
                r = xs match {
                    case Nil           => List(Nil)

                    //special case lists of length 1 and 2 for better performance
                    case t :: Nil      => List(xs)
                    case t :: u :: Nil => List(xs, List(u, t))

                    case _ =>
                        for ((y, ys) <- selections3(xs); ps <- permute3(ys))
                            yield y :: ps
                }
                r = r.filter(z => z.mkString.indexOf("b0") < 0 && z.mkString.indexOf("0b") < 0 && z.mkString.indexOf("1n") < 0 && z.mkString.indexOf("n1") < 0)
                r = r.filter(z => z.mkString.indexOf(repm0) < 0 && z.mkString.indexOf(repm1) < 0 && z.mkString.indexOf(rep0) < 0 && z.mkString.indexOf(rep1) < 0)
                r = r.filter(z => uniquePatterns(z.mkString, length))
                /*if (r.isEmpty) {
                    myPrintln("   r: "+r.length+" "+r.take(10).map(_.mkString))
                } else {
                    myPrintln("   r: "+r.head.length+" "+r.length+" "+r.take(10).map(_.mkString))
                }*/
                (ListSet.empty[List[A]] ++ r).toList
            }

        // for rep0rep1-------
        val root1 = "bn"+(1 to ((tl / 2) - ((2 * length) - 2))).map(z => "01").mkString
        //val root1 = (1 to ((tl / 2) - length)).map(z => "01").mkString
        val p1 = permute3(root1.toList).map(_.mkString)
        myPrintDln(root1, p1.length, p1)
        val p11 = p1.filter(z => "0b".indexOf(z.head.toString) >= 0 && "1n".indexOf(z.last.toString) >= 0)
        //val p11 = p1.filter(z => z.indexOf("0")==0 && z.reverse.indexOf("1")==0)
        myPrintDln(root1, p11.length, p11)
        val p12 = p1.map(z => rep0 + rep1 + z.replaceAll("b", repm20).replaceAll("n", repm21)).filter(z => uniquePatterns(z.mkString, length))
        myPrintDln(root1, p12.length, p12) // YES!
    }

    def doZeJob3(length: Int) = {
        val sz = (1 to length).map(z => "01").mkString
        val cp = combperm(sz, length)
        val cpf = cp.flatten.sorted
        myPrintDln(length+" ***************************************")
        printIt(cpf)
            def checkZeStuff(s: String) = {
                val r = rotations(s, length)
                val result = (r.intersect(cpf).sorted == cpf && r.length == cpf.length)
                /*if (result) {
                    myPrintDln("\n  "+r, "\n  "+r.sorted, "\n  "+(ListSet.empty[String] ++ r.sorted), "\n  "+s, result)
                }*/
                result
            }
        val potsol = generatePotentialSolutions3(length).filter(checkZeStuff(_)).sorted
        //val result = potsol.map(z => (z, Integer.parseInt(z, 2))).sortBy(_._2).map(z => (z._1, z._2, new EulerDiv(z._2).primes))
        val result = potsol.map(z => (z, Integer.parseInt(z, 2))).sortBy(_._2).map(z => (z._1, z._2))
        myPrintDln(length, "\n"+result.mkString("\n  ", "\n  ", "\n  "), "\n"+result.map(_._2).sum)
        //myPrintDln(length, potsol.length, potsol.mkString("\n"))
    }

    def doZeJob(length: Int) = {
        val sz = (1 to length).map(z => "01").mkString
        val cp = combperm(sz, length)
        val cpf = cp.flatten.sorted
        myPrintDln(length+" ***************************************")
        printIt(cpf)
            def checkZeStuff(s: String) = {
                val r = rotations(s, length)
                val result = (r.intersect(cpf).sorted == cpf && r.length == cpf.length)
                /*if (result) {
                    myPrintDln("\n  "+r, "\n  "+r.sorted, "\n  "+(ListSet.empty[String] ++ r.sorted), "\n  "+s, result)
                }*/
                result
            }
        val potsol = generatePotentialSolutions2(length).filter(checkZeStuff(_)).sorted
        val result = potsol.map(z => (z, Integer.parseInt(z, 2))).sortBy(_._2)
        myPrintDln(length, "\n"+result.mkString("\n  ", "\n  ", "\n  "), "\n"+result.map(_._2).sum)
        //myPrintDln(length, potsol.length, potsol.mkString("\n"))
    }

    def bestial(length: Int) = {
        val sz = (1 to length).map(z => "01").mkString
        val cp = combperm(sz, length)
        val cpf = cp.flatten.sorted
        myPrintDln(length+" ***************************************")
        val tl = Math.pow(2, length).toInt
        val rep0 = (1 to length).map(z => "0").mkString
        val rep1 = (1 to length).map(z => "1").mkString
        val root = length match {
            case 3 => "u10"
            case 4 => "u10101010"
            case _ => ""
        }
        val perms = root.permutations.toList.filter(z => z.indexOf("u1") < 0 && z.indexOf("1u") < 0)
        myPrintIt(perms.length, perms)
        val perms2 = perms.map(z => rep0 + z.replaceAll("u", rep1))
        myPrintIt(perms2.length, perms2)
            def checkZeStuff(s: String) = {
                val r = rotations(s, length)
                val result = (r.intersect(cpf).sorted == cpf && r.length == cpf.length)
                /*if (result) {
                    myPrintDln("\n  "+r, "\n  "+r.sorted, "\n  "+(ListSet.empty[String] ++ r.sorted), "\n  "+s, result)
                }*/
                result
            }
        val potsol = perms2.filter(checkZeStuff(_)).sorted
        val result = potsol.map(z => (z, Integer.parseInt(z, 2))).sortBy(_._2)
        myPrintDln(length, "\n"+result.mkString("\n  ", "\n  ", "\n  "), "\n"+result.map(_._2).sum)
        //myPrintDln(length, potsol.length, potsol.mkString("\n"))
    }

    def generatePotentialSolutions3(length: Int) = {
        val tl = Math.pow(2, length).toInt
        val rep0 = (1 to length).map(z => "0").mkString
        val rep1 = (1 to length).map(z => "1").mkString

        val root = (1 to (tl / 2)).map(z => "1").mkString + (1 to ((tl / 2) - length)).map(z => "0").mkString

        myPrintDln(length, tl, root, rep0, rep1)
        val q = permute2(root.toList, length, rep0, rep1+"1").map(z => rep0 + z.mkString)
        printIt(q)
        q
    }

    def generatePotentialSolutions2(length: Int) = {
        val tl = Math.pow(2, length).toInt
        val rep0 = (1 to length).map(z => "0").mkString
        val rep1 = (1 to length).map(z => "1").mkString

        val root = length match {
            case 4 => "1011"
            case 5 => "101010101010101011"
            case _ => ""
        }

        myPrintln(length, tl, root, rep0, rep1)
        // u=rep1, v=u0, w=0u0
        val q = (root + 0).permutations.toList
        //val q = permute2((root + 0).toList, length, rep0, rep1+"1").map(_.mkString)
        printIt(q)
        val xu0 = q.map(z => "u0"+z)
        val x0u = q.map(z => z+"0u")
        val x0u0 = (root+"x").permutations.toList.map(z => z.replaceAll("x", "0u0"))
        //val x0u0 = permute2((root+"x").toList, length, rep0, rep1+"1").map(_.mkString).map(z => z.replaceAll("x", "0u0"))
        myPrintDln(rep1+"0", xu0.length, xu0)
        myPrintDln("0"+rep1, x0u.length, x0u)
        myPrintDln("0"+rep1+"0", x0u0.length, x0u0)

        val root2 = length match {
            case 4 => "01010"
            case 5 => "0101010101010101010"
            case _ => ""
        }

        val y0u = (root2+"x").permutations.toList.map(z => z.replaceAll("x", "0u")).filter(z => z.indexOf("u1") < 0)
        //val y0u = permute2((root2+"x").toList, length, rep0, rep1+"1").map(_.mkString).map(z => z.replaceAll("x", "0u")).filter(z => z.indexOf("u1") < 0)
        myPrintDln("0"+rep1, y0u.length, y0u)
        val y1 = y0u.map(z => rep0+"11"+z.replaceAll("u", rep1)).sorted
        myPrintIt(y1.length, y1)
        val y2 = y0u.map(z => rep0 + revert("11"+z.replaceAll("u", rep1))).sorted
        myPrintIt(y2.length, y2)

        val x = ListSet.empty[String] ++ xu0 ++ x0u ++ x0u0
        myPrintln(x.toList.length, x)

        val z1 = x.toList.map(z => rep0+"10"+z.replaceAll("u", rep1)).sorted
        myPrintIt(z1.length, z1)
        val z2 = x.toList.map(z => rep0 + revert("10"+z.replaceAll("u", rep1))).sorted
        myPrintIt(z2.length, z2)
        (ListSet.empty[String] ++ z1 ++ z2 ++ y1 ++ y2).toList.sorted
    }

    def uniquePatterns(s: String, length: Int) = {
        //myPrintDln(s, s.sliding(length).toList, s.sliding(length).toList.length, (ListSet.empty[String] ++ s.sliding(length)).toList, (ListSet.empty[String] ++ s.sliding(length)).toList.length)
        if (s.length > length) {
            (ListSet.empty[String] ++ s.sliding(length)).toList.length == s.length + 1 - length
        } else {
            true
        }
    }

    /** For each element x in List xss, returns (x, xss - x) */
    def selections2[A](xss: List[A]): List[(A, List[A])] = {
        //myPrintln("    xss: "+xss)
        var q = List.empty[(A, List[A])]
        q = xss match {
            case Nil => Nil
            case x :: xs =>
                (x, xs) :: (for ((y, ys) <- selections2(xs))
                    yield (y, x :: ys))
        }
        //q = q.filter(_._2.mkString.indexOf("11111") < 0)
        //myPrintln("    q: "+q.map(z => (z._1, z._2.mkString)))
        (ListSet.empty[(A, List[A])] ++ q).toList
    }

    /** Returns a list containing all permutations of the input list */
    def permute2[A](xs: List[A], length: Int, rep0: String, rep1: String): List[List[A]] = {
        //myPrintln("  xs: "+xs.mkString)
        var r = List.empty[List[A]]
        //if (xs.mkString.indexOf("11111") < 0) {
        r = xs match {
            case Nil           => List(Nil)

            //special case lists of length 1 and 2 for better performance
            case t :: Nil      => List(xs)
            case t :: u :: Nil => List(xs, List(u, t))

            case _ =>
                for ((y, ys) <- selections2(xs); ps <- permute2(ys, length, rep0, rep1))
                    yield y :: ps
        }
        //}

        val repm1 = "0"+rep1.tail.tail+"0"
        val repm0 = "1"+rep0.tail+"1"
        r = r.filter(z => z.mkString.indexOf(repm0) < 0 && z.mkString.indexOf(repm1) < 0 && z.mkString.indexOf(rep0) < 0 && z.mkString.indexOf(rep1) < 0)
        r = r.filter(z => uniquePatterns(z.mkString, length))
        /*if (r.isEmpty) {
            myPrintln("   r: "+r.length+" "+r.take(10).map(_.mkString))
        } else {
            myPrintln("   r: "+r.head.length+" "+r.length+" "+r.take(10).map(_.mkString))
        }*/
        (ListSet.empty[List[A]] ++ r).toList
    }

    /** For each element x in List xss, returns (x, xss - x) */
    def selections[A](xss: List[A]): List[(A, List[A])] = xss match {
        case Nil => Nil
        case x :: xs =>
            (x, xs) :: (for ((y, ys) <- selections(xs))
                yield (y, x :: ys))
    }

    /** Returns a list containing all permutations of the input list */
    def permute[A](xs: List[A]): List[List[A]] = xs match {
        case Nil           => List(Nil)

        //special case lists of length 1 and 2 for better performance
        case t :: Nil      => List(xs)
        case t :: u :: Nil => List(xs, List(u, t))

        case _ =>
            for ((y, ys) <- selections(xs); ps <- permute(ys))
                yield y :: ps
    }
}