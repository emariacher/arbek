package git2surround

import org.scalastuff.scalabeans.Enum
import org.scalastuff.scalabeans.Preamble._

case class errorCode private (rc: Int)
object errorCode extends Enum[errorCode] {
	val OK = errorCode(0)
			val noSuchDirectory = errorCode(100)
			val notGitRepository = errorCode(128)
			val unknownError = errorCode(808)
			val someError = errorCode(1000)
			val emptyList = errorCode(1001)
			val baselineNotFound = errorCode(1002)
			val gitFileChanged = errorCode(1003)
			val gitEmptyDir = errorCode(1004)
			val sscmFileExists = errorCode(1005)
			val alreadyBackUped = errorCode(1006)
			val inactive42Long = errorCode(1007)
			val labelDoesNotExists = errorCode(1008)
			val checkedOutFiles = errorCode(1009)
			val TimeoutException = errorCode(2001)
}