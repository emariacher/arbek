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
import Permutations._
import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object EulerMainNoScalaTest extends App {
    new MyLog("EulerMainNoScalaTest", new File("log"), "log")
    try {
        var t_start = Calendar.getInstance
        myPrintDln("Hello World!")

        new Euler241

        timeStamp(t_start, "Au revoir Monde!")
    } catch {
        case ex: Exception => {
            println("\n" + ex)
            println("\n" + ex.getStackTrace.mkString("\n  ","\n  ","\n  ") + "\n")
        }
    } finally {
        println("\nHere!")
        MyLog.getMylog.closeFiles
        println("\nThere!")
    }
}

class Euler241 {
    /*var result = ListSet[DoZeJob]() ++ loop(List(BigInt(2)), List(BigInt(100000)), List(2))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(4320)), List(BigInt(10000000)), List(8, 9))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(26208)), List(BigInt(1000000), BigInt(1000)), List(32, 9, 13))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(8910720)), List(BigInt(1000000), BigInt(10000)), List(32, 9, 13, 35))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(0)), List(BigInt(1000000), BigInt(1000)), List(32, 9, 11))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(8910720)), List(BigInt(10000000), BigInt(10000)), List(128, 9, 13, 35))
    myErrPrintln(result)
    result = result ++ loop(List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 23), List(BigInt(1000000000), BigInt(10000)), List(1024, 9, 13, 35))
    myErrPrintln(result)
    result = result ++ loop(List(BigInt(0)), List(BigInt(1000000000), BigInt(10000)), List(1024, 9, 11, 35))
    myErrPrintln(result.toList.sortBy(_.bi).mkString("\n"))*/

    val lj = List(
        (List(BigInt(2)), List(BigInt(100000)), List[BigInt](2)),
        (List(BigInt(4320)), List(BigInt(10000000)), List[BigInt](8, 9)),
        (List(BigInt(26208)), List(BigInt(1000000), BigInt(1000)), List[BigInt](32, 9, 13)),
        (List(BigInt(8910720)), List(BigInt(1000000), BigInt(10000)), List[BigInt](32, 9, 13, 35)),
        (List(BigInt(0)), List(BigInt(1000000), BigInt(1000)), List[BigInt](32, 9, 11)),
        (List(BigInt(8910720)), List(BigInt(10000000), BigInt(10000)), List[BigInt](128, 9, 13, 35)),
        (List[BigInt](2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 5, 7, 13, 23), List(BigInt(1000000000), BigInt(10000)), List[BigInt](1024, 9, 13, 35)),
        (List(BigInt(0)), List(BigInt(1000000000), BigInt(10000)), List[BigInt](1024, 9, 11, 35)))

    var nrOfWorkers = 4
    GoLoops(nrOfWorkers, lj)

    class DoZeJob(val bi: BigInt) {
        val div = new EulerDiv(bi)
        val sbi = sigma(bi, div)
        val pfbi = perfquot(bi, sbi)
        val spfbi = pfbi.toString
        val kp5 = spfbi.substring(spfbi.length - 2) == ".5"
        //myPrintln(bi, sbi, pfbi, kp5)

        def toString2 = "[" + bi + "," + div.primes + "," + (new EulerDivisors(div).getFullDivisors).sorted + "," + pfbi + "]"
        override def toString = "[" + bi + "," + div.primes + "," + pfbi + "]"
        override def equals(a: Any) = bi == a.asInstanceOf[DoZeJob].bi
        override def hashCode = bi.toInt
    }

    def loop(start: List[BigInt], end: List[BigInt], inc: List[BigInt]) = {
        var result = ListSet[DoZeJob]()
        var t_start1 = Calendar.getInstance
        printIt(start, end.mkString("*"), inc.mkString("*"))
        var count = start.product
        var count2 = 0
        val end1 = end.product
        val inc1 = inc.product
        while (count < end1) {
            val dzj = new DoZeJob(count)
            if (dzj.kp5) {
                myPrintln("\n    " + dzj)
                result = result + dzj
            }
            if (count2 % 10000 == 99) {
                print(".")
            }

            count += inc1
            count2 += 1
        }
        timeStamp(t_start1, (start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString)
        myErrPrintDln((start.mkString("*"), end.mkString("*"), inc.mkString("*")).toString, result.toList.sortBy(_.bi).mkString("\n", "\n", "\n"))
        result
    }

    def sigma(bi: BigInt) = (new EulerDivisors(new EulerDiv(bi)).getFullDivisors).sum
    def sigma(bi: BigInt, ed: EulerDiv) = (new EulerDivisors(ed).getFullDivisors).sum
    def perfquot(bi: BigInt) = sigma(bi).toDouble / bi.toDouble
    def perfquot(bi: BigInt, sbi: BigInt) = sbi.toDouble / bi.toDouble

    sealed trait akkaDoZeJob
    case object Demarre extends akkaDoZeJob
    case class GoLoop(start: List[BigInt], end: List[BigInt], inc: List[BigInt]) extends akkaDoZeJob
    case class Resultat(result: ListSet[DoZeJob]) extends akkaDoZeJob
    case class ResultatsLoops(results: ListSet[DoZeJob])

    class Worker extends Actor {

        /*def echappeJeton(j: Jeton): Int = {
            while (j.avance != StateMachine.termine) {
                //myPrintDln("    ("+j+", "+j.cnt+")")
            }
            //myPrintDln("   -("+j+", "+j.cnt+")")
            j.cnt
        }*/

        def receive = {
            case GoLoop(start, end, inc) =>
                sender ! Resultat(loop(start, end, inc))
        }
    }

    class Master(nrOfWorkers: Int, lj: List[(List[BigInt], List[BigInt], List[BigInt])], listener: ActorRef) extends Actor {

        var nrOfResults: Int = _
        var results = ListSet.empty[DoZeJob]

        val workerRouter = context.actorOf(
            Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

        def receive = {
            case Demarre =>
                lj.foreach((t: (List[BigInt], List[BigInt], List[BigInt])) =>
                    workerRouter ! GoLoop(t._1, t._2, t._3))
            case Resultat(result) =>
                //myPrintDln(" --("+c+", "+cnt+")")
                results = results ++ result
                nrOfResults += 1
                if (nrOfResults == lj.size) {
                    //myPrintDln("Send the result to the listener")
                    listener ! ResultatsLoops(results)
                    //myPrintDln("Stops this actor and all its supervised children")
                    context.stop(self)
                }
        }

    }

    class Listener extends Actor {
        def receive = {
            case ResultatsLoops(results) =>
                myErrPrintDln(results.toList.sortBy(_.bi).mkString("\n", "\n", "\n"))
                context.system.shutdown()
        }
    }

    def GoLoops(nrOfWorkers: Int, lj: List[(List[BigInt], List[BigInt], List[BigInt])]) {
        // Create an Akka system
        val system = ActorSystem("goLoops")

        //myErrPrintDln("create the result listener, which will print the result and shutdown the system")
        //val listener = system.actorOf(Props[Listener], name = "listener")
        val listener = system.actorOf(Props(new Listener))

        //myErrPrintDln("create the master")
        val master = system.actorOf(Props(new Master(
            nrOfWorkers, lj, listener)),
            name = "master")

        //myErrPrintDln("start the calculation")
        master ! Demarre

    }

}

