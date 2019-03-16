package pageblanche

/*
 * libraries needed: akka.actor and config-0.4.0.jar from typesafe
 */
import akka.actor._
import akka.routing.RoundRobinPool

import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import kebra.MyLog._
import Tableaux._

object AkkaJeton {
    sealed trait AvanceJeton
    case object Demarre extends AvanceJeton
    case class GoJeton(j: Jeton) extends AvanceJeton
    case class Resultat(c: Couleur, cnt: Int) extends AvanceJeton
    case class ResultatsJetons(lrjc: List[(Couleur, Int, Int)])

    class Worker extends Actor {

        def echappeJeton(j: Jeton): Int = {
            while (j.avance != StateMachine.termine) {
                //myPrintDln("    ("+j+", "+j.cnt+")")
            }
            //myPrintDln("   -("+j+", "+j.cnt+")")
            j.cnt
        }

        def receive = {
            case GoJeton(j) =>
                //myPrintDln("  ("+j+")")
                sender ! Resultat(j.couleur, echappeJeton(j))
        }
    }

    class Master(nrOfWorkers: Int, lj: List[Jeton], listener: ActorRef) extends Actor {

        var nrOfResults: Int = _
        var lrjc = List.empty[(Couleur, Int, Int)]

        val workerRouter = context.actorOf(
            Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

        def receive = {
            case Demarre =>
                lj.foreach((j: Jeton) =>
                    workerRouter ! GoJeton(j))
            case Resultat(c, cnt) =>
                //myPrintDln(" --("+c+", "+cnt+")")
                lrjc = lrjc :+ (c, cnt, 0)
                nrOfResults += 1
                if (nrOfResults == lj.size) {
                    //myPrintDln("Send the result to the listener")
                    listener ! ResultatsJetons(lrjc)
                    //myPrintDln("Stops this actor and all its supervised children")
                    context.stop(self)
                }
        }

    }

    class Listener extends Actor {
        def receive = {
            case ResultatsJetons(lrjc) =>
                tbx.updateStats(lrjc)
                context.system.terminate()
        }
    }

    def goJetons(nrOfWorkers: Int, lj: List[Jeton]) {
        // Create an Akka system
        val system = ActorSystem("goJetons")

        //myErrPrintDln("create the result listener, which will print the result and shutdown the system")
        val listener = system.actorOf(Props[Listener], name = "listener")

        //myErrPrintDln("create the master")
        val master = system.actorOf(Props(new Master(
            nrOfWorkers, lj, listener)),
            name = "master")

        //myErrPrintDln("start the calculation")
        master ! Demarre

    }
}