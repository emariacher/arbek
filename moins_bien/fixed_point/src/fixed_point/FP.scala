package fixed_point

import scala.reflect.macros.Context
import scala.language.experimental.macros
import kebra.MyLog._
import scala.math._

// v is value
// c is comma position
class FP(val v: Int, val c: Int) {
    myRequire(toInt<Int.MaxValue)
    myRequire(toInt>Int.MinValue)

    def this(fp: FP)=this(fp.v, fp.c)
    def this(i: Int)=this(i, 0)
    val d = v / pow(2,c)

    def toInt: Int = {
            if(c>=0) {
                v>>c
            } else {
                v<<(0-c)
            }
    }
    def norm(shift: Int): Int = {
            if(c<shift) {			
                v<<(shift-c)
            } else {
                v
            }
    }
    def +(o: FP): FP = new FP(norm(o.c)+o.norm(c),max(o.c,c))
    def +(o: Int): FP = new FP(this+(new FP(o)))
    def -(o: FP): FP = new FP(norm(o.c)-o.norm(c),max(o.c,c))
    def *(o: FP): FP = new FP(v*o.v,c+o.c)
    def *(o: Int): FP = new FP(v*o,c)
    def /(o: FP): FP = new FP(v/o.v,c-o.c)
    override def equals(o: Any): Boolean = norm(o.asInstanceOf[FP].c)==o.asInstanceOf[FP].norm(c)
    def >(o: FP): Boolean = norm(o.asInstanceOf[FP].c) > o.asInstanceOf[FP].norm(c)
    def rebase: FP = {
            var tfp = new FP(this)
            if(c>0) {
                while(tfp.v%2==0&&tfp.c>0) {
                    tfp = new FP(tfp.v>>1,tfp.c-1)
                }
            } else if(c<0) {
                while(tfp.c<0) {
                    tfp = new FP(tfp.v<<1,tfp.c+1)
                }
            }
            tfp
    }
    override def toString = v+" "+c+" -> %4.10f".format(d)
}

object TestFP extends App {
    println("here!")

    val zero = new FP(0)
    val un = new FP(1)
    val step = new FP(838861, 29) // 838861/2^20 (= 0.8) * 1/2^9 (step calculation)
    val thousand= new FP(1000)
    val oneKilo= new FP(1024)
    val quarterKilo= new FP(1024,2)
    val quarterKilo2= new FP(256)
    val quarterKilo3= new FP(64,-2)


    t1

    def t1 {
        myAssert(0==0)
        myAssert(new FP(0).toInt==0)
        myAssert(new FP(0).toInt==0)
        myAssert(new FP(0,1000).toInt==0)
        myAssert(new FP(1).toInt==1)
        myAssert(new FP(1,1).toInt==0)
        myAssert(new FP(1,1000).toInt==0)
        myAssert(thousand.toInt==1000)
        myAssert(quarterKilo.toInt==256)
        println(step)
        myAssert((quarterKilo+oneKilo).toInt==1280)
        myAssert((quarterKilo-oneKilo).toInt==(-768))
        myAssert((quarterKilo*oneKilo).toInt==262144)
        myAssert((oneKilo/quarterKilo).toInt==4)
        println("["+quarterKilo+"]==["+quarterKilo2+"]")
        myAssert(quarterKilo==quarterKilo2)
        println("["+quarterKilo+"]==["+quarterKilo3+"]")
        myAssert(quarterKilo==quarterKilo3)
        myAssert(quarterKilo/oneKilo==new FP(1,2))
        myAssert(quarterKilo.rebase.v==256)
        myAssert(quarterKilo.rebase.c==0)
        myAssert(quarterKilo3.rebase.v==256)
        myAssert(quarterKilo3.rebase.c==0)
        myAssert(step.rebase.v==838861)
        myAssert(step.rebase.c==29)
        myAssert(quarterKilo*4==oneKilo)
        println(new FP(1,-30))
        myAssert((quarterKilo+1)==new FP(257))
        myAssert(((quarterKilo*4)+1)>oneKilo)

        // throw exceptions
        try {
            println(new FP(1,-30)*2)
        } catch {
        case e: Exception => println(tag(1)+e)
        }   
        try {
            un/zero
        } catch {
        case e: Exception => println(tag(1)+e)
        }

        myAssert((quarterKilo-oneKilo).toInt==(-769))
    }

    println("there!")
}