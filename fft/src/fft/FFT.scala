package fft
import kebra.MyLog._
object FFT extends App {
    import scala.math._

    case class Complex(re: Double, im: Double = 0.0) {
        def +(x: Complex): Complex = Complex((this.re + x.re), (this.im + x.im))
        def -(x: Complex): Complex = Complex((this.re - x.re), (this.im - x.im))
        def *(x: Complex): Complex = Complex(this.re * x.re - this.im * x.im, this.re * x.im + this.im * x.re)
        def /(d: Double): Complex = Complex(this.re / d, this.im / d)
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

    def doZeJob(l: List[Complex]) {
        myPrintln("*******************************")
        myPrintln(l)
        val f = fft(l)
        myPrintln(f.zipWithIndex.filter(!_._1.isZero))
        val invf = ifft(f)
        myPrintln(invf.map(_.re.toInt))
        myPrintln(l.zip(invf.map(_.toString2)))
        myPrintln(l.zip(invf.map(_.re.toInt)))
    }

    doZeJob((0 to 63).map(i => Complex(((i + 4) / 8) % 2)).toList)
    doZeJob((0 to 63).map(i => Complex(((i + 2) / 4) % 2)).toList)
    doZeJob((0 to 63).map(i => Complex(((i + 1) / 2) % 2)).toList)
    doZeJob((0 to 63).map(i => Complex(i % 2)).toList)
    doZeJob((0 to 127).map(i => Complex(((i + 4) / 8) % 2) - Complex((i / 8) % 4) + Complex(((i + 6) / 8) % 4)).toList)

}