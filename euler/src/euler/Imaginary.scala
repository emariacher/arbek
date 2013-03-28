package euler

import Math._

class Im(val r: Double, val i: Double) {

	def isGaussian: Boolean = (r-r.toInt==0)&(i-i.toInt==0)

			override def toString: String = {
			var signum="+"
					if(i<0.0) {
						signum="-"
					}
			if(!isGaussian) {
				"%4.3f+".format(r)+signum+"i%4.3f".format(abs(i))
			} else if(i==0.0){
				r.toInt.toString
			} else {
				r.toInt.toString+signum+"i"+abs(i).toInt.toString
			}
	}

	def *(d: Double): Im = new Im(r*d,i*d)
	def *(bi: BigInt): Im = new Im(r*bi.toDouble,i*bi.toDouble)

	override def hashCode: Int = toString.hashCode
	override def equals(o: Any): Boolean = toString == o.asInstanceOf[Im].toString

}

class ImBi(val r: BigInt, val i: BigInt) {
			override def toString: String = {
			var signum="+"
					var isignum = 1
					if(i<0) {
						signum="-"
								isignum = -1
					}
			if(i==0){
				r.toInt.toString
			} else {
				r.toInt.toString+signum+"i"+(i*isignum).toInt.toString
			}
	}

	def *(bi: BigInt): ImBi = new ImBi(r*bi,i*bi)

	override def hashCode: Int = toString.hashCode
	override def equals(o: Any): Boolean = toString == o.asInstanceOf[ImBi].toString

}

