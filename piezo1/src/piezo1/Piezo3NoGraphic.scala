package piezo1

import kebra._
import kebra.MyLog._
import Math._

class Piezo3NoGraphic {
  myAssert2(128, FFT.findPow2(130))

  myPrintln("Hello World!")
  val f = (new MyFileChooser("GetArduinoLogs")).justChooseFile("log");
  val lraw = copyFromFile(f.getCanonicalPath).split("\n").toList
  val lraw2 = (lraw.reverse.take(10) ++ lraw.reverse.drop(10).takeWhile(s => s.indexOf("t") < 0 && s.length > 2)).reverse
  val lpartition = lraw2.partition(s => s.indexOf("t") < 0 && s.length > 2)
  val headfoot = lpartition._2
  val l = lpartition._1.map(_.toDouble)
  myPrintIt(headfoot.mkString("\n"))
  val periodSample = headfoot.filter(_.indexOf("meant mus:") == 0).head.split(" ").toList.last.reverse.tail.reverse.toDouble / 1000
  myPrintIt(f.getName, periodSample)
  val curveName = f.getName
  val groupSize = 5
  val sampleSize = l.size
  val decimation = sampleSize / 2048
  val ld50k = sample(l.takeRight(sampleSize).zipWithIndex, curveName, decimation)
  val filtered = getPulses(ld50k, groupSize)
  val ffted = getPulseRate(filtered)

  val ffted2 = ffted.map(z => if (z.absolute < 200.0) { FFT.Complex(0.0) } else { z })
  //myPrintIt(ffted2.map(z => if (z.absolute < 25.0) { 0 } else { z.absolute.toInt }))
  val iffted2 = FFT.ifft(ffted2).map(z => if (z.absolute < 25.0) { 0 } else { z.absolute.toInt })
  //myPrintln(iffted2)
  val iffted3 = iffted2.zipWithIndex.map(z => (z._1.toDouble, z._2 * decimation))

  val highPass = 30
  val ffted4 = FFT.filtre(ffted2, 1, highPass)
  val iffted4 = FFT.ifft(ffted4)
  val iffted5 = iffted4.zipWithIndex.map(z => (z._1.absolute.toInt, z._2 * decimation))

  val ffted6 = FFT.fftD(ld50k._1.map(_._1))
  val ffted7 = FFT.filtre(ffted6, 1, highPass)
  val pic = ffted7.map(_.absolute).max
  val maxIndex = ffted7.indexWhere(z => z.absolute > pic * 0.72) // 0.70 trop petit pour eric25_raw1, 0.76 trop grand pour eric25_raw2
  val iffted8 = FFT.ifft(ffted6)
  val iffted9 = iffted8.zipWithIndex.map(z => (z._1.absolute.toInt, z._2 * decimation))
  val iffted10 = FFT.ifft(ffted7)
  val iffted11 = iffted10.zipWithIndex.map(z => (z._1.absolute.toInt, z._2 * decimation))
  myPrintIt(ld50k._1.head, ld50k._1.tail.head, ld50k._1.last, ld50k._1.length)
  myPrintIt(iffted9.head, iffted9.tail.head, iffted9.last, iffted9.length)
  myPrintIt(iffted11.head, iffted11.tail.head, iffted11.last, iffted11.length)

  //val maxIndex = getEnergy(ffted7.take(highPass).map(_.absolute).zipWithIndex, 2) - 0.5
  myPrintIt(pic, maxIndex, ffted7.take(highPass).map(_.absolute.toInt).zipWithIndex.mkString("\n"))
  myPrintIt(FFT.findPow2(ld50k._1.length))
  val bpm = ((maxIndex * 60000) / (FFT.findPow2(ld50k._1.length) * periodSample * decimation)).toInt
  myPrintIt(bpm)

  //myPrintIt(l.takeRight(sampleSize).zipWithIndex.filter(_._1 < 275.0).mkString("\n")) // pour calculer à la main avec le 1er enregistrement de Paul

  def sample(l: List[(Double, Int)], name: String, sampling: Int) = {
    val z = l.filter(_._2.toInt % sampling == 0)
    myPrintIt(name + sampling + z.take(10))
    //z.foreach(c => data.addValue(c._1, name + sampling, c._2))
    (z, name + sampling)
  }

  //def delta(ld: List[Double], zmean: Double) = max(abs(ld.max - zmean), abs(ld.min - zmean))
  def delta(ld: List[Double], zmean: Double) = abs(ld.max - zmean)

  def getPulses(c: (List[(Double, Int)], String), groupSize: Int) = {
    val l = c._1.map(_._1)
    val mean = l.sum / l.length

    val slices = c._1.grouped(groupSize).toList.map(z => (delta(z.map(_._1), mean), z.head._2)).map(z => if (z._1 < 40.0) { (0.0, z._2) } else { (z._1, z._2) })
    //slices.foreach(z => data.addValue(z._1, c._2+" filtered", z._2))
    myPrintIt(slices.length, slices.filter(_._1 > 0.0))
    slices
  }

  def getPulseRate(lraw: List[(Double, Int)]) = {
    val l = lraw.take(FFT.findPow2(lraw.length))
    FFT.doZeJob(l.map(d => FFT.Complex(d._1)))
  }

  def getEnergy(l: List[(Double, Int)], width: Int) = {
    val sliding = l.sliding(width).toList.map(z => (z.head._2, z.map(_._1).sum))
    val mean = sliding.map(_._2).sum / sliding.length
    myPrintIt(l.mkString("\n"))
    myPrintIt(mean, sliding.mkString("\n"))
    val sliding2 = sliding.map(z => if (z._2 < mean * 1.25) { (z._1, 0.0) } else { z })
    myPrintIt(mean, sliding2.mkString("\n"))
    val sliding3 = sliding2.dropWhile(_._2 == 0).takeWhile(_._2 > 0)
    myPrintIt(mean, sliding3.mkString("\n"))
    sliding3.max._1
  }
}