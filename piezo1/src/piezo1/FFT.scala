package piezo1

import kebra.MyLog._
import kebra._
import scala.Stream.canBuildFrom
import scala.Stream.from
import scala.collection.mutable.ListBuffer
import scala.math.Pi
import scala.math.abs
import scala.math.cos
import scala.math.pow
import scala.math.sin

object FFT extends App {
    import scala.math._

    case class Complex(re: Double, im: Double = 0.0) {
        def +(x: Complex): Complex = Complex((this.re + x.re), (this.im + x.im))
        def -(x: Complex): Complex = Complex((this.re - x.re), (this.im - x.im))
        def *(x: Complex): Complex = Complex(this.re * x.re - this.im * x.im, this.re * x.im + this.im * x.re)
        def /(d: Double): Complex = Complex(this.re / d, this.im / d)
        def absolute: Double = Math.sqrt((this.re * this.re) + (this.im * this.im))
        def conjugate: Complex = Complex(this.re, 0.0 - this.im)
        def isZero = abs(re) <= 0.01 && abs(im) <= 0.01
        override def toString = {
            var s = "%1.0f".format(re)
            if (im > 0.0) {
                s += "+i%1.0f".format(im)
            } else if (im < 0.0) {
                s += "-i%1.0f".format(0.0 - im)
            }
            s
        }
        def toString2 = {
            var s = "%1.2f".format(re)
            if (im > 0.0) {
                s += "+i%1.2f".format(im)
            } else if (im < 0.0) {
                s += "-i%1.2f".format(0.0 - im)
            }
            s
        }
        def toStringA = absolute.toInt
    }

    def ifft(f: List[Complex]): List[Complex] = {

        // conjugate the complex numbers
        val g = f.map(_.conjugate)

        // forward fft
        val h = fft(g);

        // conjugate the complex numbers again
        val i = h.map(_.conjugate)

        // scale the numbers
        h.map(_ / h.size)
    }

    def ifft4display(f: List[Complex]): List[Double] = {
        ifft(f).map(c => if (c.re < 0.0) -c.absolute else c.absolute) // bon, y'a pas la phase, mais c'est mieux que rien
    }

    def filtre(f: List[Complex], drop: Int, garde: Int): List[Complex] = {
        myAssert(drop < garde)
        (1 to drop).map(z => Complex(0)).toList ++ f.drop(drop).take(garde - drop) ++ (1 to f.length - garde).map(z => Complex(0)).toList
    }

    def fft(f: List[Complex]): List[Complex] = {
        import Stream._
        require((f.size == 0) || (from(0) map { x => pow(2, x).toInt }).takeWhile(_ < 2 * f.size).toList.exists(_ == f.size) == true, "list size "+f.size+" not allowed!")
        f.size match {
            case 0 => Nil
            case 1 => f
            case n => {
                val cis: Double => Complex = phi => Complex(cos(phi), sin(phi))
                val e = fft(f.zipWithIndex.filter(_._2 % 2 == 0).map(_._1))
                val o = fft(f.zipWithIndex.filter(_._2 % 2 != 0).map(_._1))
                import scala.collection.mutable.ListBuffer
                val lb = new ListBuffer[Pair[Int, Complex]]()
                for (k <- 0 to n / 2 - 1) {
                    lb += Pair(k, e(k) + o(k) * cis(-2 * Pi * k / n))
                    lb += Pair(k + n / 2, e(k) - o(k) * cis(-2 * Pi * k / n))
                }
                lb.toList.sortWith((x, y) => x._1 < y._1).map(_._2)
            }
        }
    }

    def findPow2(num: Int) = (0 to 20).map(Math.pow(2, _)).toList.takeWhile(_ <= num).last.toInt

    def fftD(ld: List[Double]): List[Complex] = fft(ld.take(findPow2(ld.length)).map(d => Complex(d)))

    def doZeJob(l: List[Complex]) = {
        myPrintln("*******************************")
        myPrintln(l)
        val f = fft(l)
        myPrintln(f.zipWithIndex.filter(!_._1.isZero))
        val invf = ifft(f)
        myPrintln(invf.map(_.re.toInt))
        myPrintln(l.zip(invf.map(_.toString2)))
        myPrintln(l.zip(invf.map(_.re.toInt)))
        f
    }

    def doZeJob2(l: List[Complex]) = {
        myPrintln("*******************************")
        //myPrintln(l)
        val f = fft(l)
        myPrintln(f.zipWithIndex.filter(!_._1.isZero).map(c => (c._1.toString2, c._2)))
        val invf = ifft(f)
        /*myPrintln(invf.map(_.re.toInt))
        myPrintln(l.zip(invf.map(_.toString2)))
        myPrintln(l.zip(invf.map(_.re.toInt)))*/
        f
    }

    def toComplex(l: List[String]) = l.map(_.split(";").toList.last.toString).tail.filter(_.length > 2).map(s => Complex(s.toDouble))
    def lowpass(l: List[String], w: Int) = l.map(_.split(";").toList.last.toString).tail.filter(_.length > 2).map(_.toDouble).sliding(w).toList.map(_.sum / w)

    doZeJob((0 to 63).map(i => Complex(((i + 4) / 8) % 2)).toList)
    doZeJob((0 to 63).map(i => Complex(((i + 2) / 4) % 2)).toList)
    doZeJob((0 to 63).map(i => Complex(((i + 1) / 2) % 2)).toList)
    doZeJob((0 to 63).map(i => Complex(i % 2)).toList)
    doZeJob((0 to 127).map(i => Complex(((i + 4) / 8) % 2) - Complex((i / 8) % 4) + Complex(((i + 6) / 8) % 4)).toList)

    val mfc = new MyFileChooser("TestOutput.txt")
    val f = mfc.justChooseFile("txt")

    val l = scala.io.Source.fromFile(f).getLines.toList
    myPrintln(l.take(10).mkString("\n  "))

    myPrintln(lowpass(l, 3).take(10).mkString("\n"))

    val ffted = doZeJob2(toComplex(l.take(1025)))

    myPrintln(ffted.zipWithIndex.filter(!_._1.isZero).sortBy(_._1.absolute).reverse.take(20).map(c => (c._1.toString2, c._2)))
    myPrintln(doZeJob2(toComplex(lowpass(l, 3).take(1025).map(_.toString))).zipWithIndex.filter(!_._1.isZero).sortBy(_._1.absolute).reverse.take(20).map(c => (c._1.toString2, c._2)))
    myPrintln(doZeJob2(toComplex(lowpass(l, 5).take(1025).map(_.toString))).zipWithIndex.filter(!_._1.isZero).sortBy(_._1.absolute).reverse.take(20).map(c => (c._1.toString2, c._2)))
}