package kebra

;
import kebra.MyLog._
import java.util.concurrent.TimeUnit
import java.util.concurrent.FutureTask
import scala.util.Random
import java.io._
import scala.collection.immutable.ListSet;
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import language.postfixOps

object ScalaBatsh {
	def exec(Tlvl: Int, s_cmd: String): (Int,List[String],List[String]) = execRemovePwd(Tlvl, s_cmd, "Cowabunga!")

			def execRemovePwd(Tlvl: Int, s_cmd: String, s_pwd: String): (Int,List[String],List[String]) = {
		myErrPrintln(MyLog.tag(Tlvl)+getClass.getName+".exec(<i>"+s_cmd.replaceAll(s_pwd,"##hidden##")+"</i>)")
		val s_filename = "zz"+new Random().nextInt(1000)+".bat";
		val fw = new FileWriter(s_filename, false) ; fw.write(s_cmd+"\n") ; fw.close
		var ls = List.empty[String];
		var lse = List.empty[String];
		var i_rc = i_unknownError;
		try {
			val theTask = new FutureTask(new Runnable {
				def run {
					MyLog.waiting(1 second);
					val p = Runtime.getRuntime.exec(s_filename)
							var input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream))
					Stream.continually(input.readLine).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => ls = ls ::: List(l.replaceAll(s_pwd,"##hidden##")))
					var error = new java.io.BufferedReader(new java.io.InputStreamReader(p.getErrorStream))
					Stream.continually(error.readLine).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => lse = lse ::: List(l.replaceAll(s_pwd,"##hidden##")))
					i_rc = p.exitValue
				};
			}, null);
			new Thread(theTask).start;
			theTask.get(long_timeOut, TimeUnit.MILLISECONDS);

			myPrintln(ls.mkString("    i  ","\n    i  ","\n"))
			if(!lse.isEmpty) myErrPrintln(lse.mkString("  e  ","\n  e  ","\n"))
			(new File(s_filename)).delete
		} catch {
		case e: Exception => myErrPrintDln("execBat Exception: "+ e)
		}
		i_rc = parse4Errors(i_rc,ls,lse)
				myErrPrintln("  rc: {"+i_rc+"}")
				(i_rc,ls,lse)
	}

	final val i_unknownError = 808

			final val long_timeOut = 10000L


			def parse4Errors(l:(Int,List[String],List[String])): Int = l._1

			def createDirectoryIfNeeded(L: MyLog, directoryName: String): Int = {
		val theDir = new File(directoryName);
		var i_rc = 0;

		// if the directory does not exist, create it
		if (!theDir.exists) {
			myErrPrintln("["+MyLog.tag(2)+"] creating directory: " + directoryName);
			if(!theDir.mkdirs) {
				i_rc = 1;
			}
		} else {
			myErrPrintln("["+MyLog.tag(2)+"] already exist directory: " + directoryName);
		}
		return i_rc;
	}

	def deleteDirectoryIfNeeded(L: MyLog, directoryName: String): Int = {
		val theDir = new File(directoryName);
		var i_rc = 0;

		// if the directory does not exist, create it
		if (theDir.exists) {
			myErrPrintln("["+MyLog.tag(2)+"] deleting directory: " + directoryName);
			if(!theDir.delete) {
				i_rc = 1;
			}
		} else {
			myErrPrintln("["+MyLog.tag(2)+"] did not exist directory: " + directoryName);
		}
		return i_rc;
	}

	/*def cleanDirectory(L: MyLog, parentDir: String, dir: String): Int = {
		var i_rc = 0;
		myErrPrintln(MyLog.func(1)+"["+MyLog.tag(2)+"] ["+parentDir+File.separatorChar+dir+"]")
		if((new File(parentDir+File.separatorChar+dir)).exists) {
			MyLog.checkException(L, ScalaBatsh.exec(5,"cd "+parentDir)._1,"Parent Directory does not exists["+parentDir+"].");
			i_rc = ScalaBatsh.exec(5,"cd "+parentDir+"\nrm -rf "+dir)._1;
		};
		i_rc;
	}
	def createDirectory(L: MyLog, parentDir: String, dir: String): Int = {
		var i_rc = 0;
		myErrPrintln(MyLog.func(1)+"["+MyLog.tag(2)+"] ["+parentDir+File.separatorChar+dir+"]")
		if(!(new File(parentDir+File.separatorChar+dir)).exists) {
			MyLog.checkException(L, ScalaBatsh.exec(5,"cd "+parentDir)._1,"Parent Directory does not exists["+parentDir+"].");
			i_rc = ScalaBatsh.exec(5,"cd "+parentDir+"\nmkdir "+dir)._1;
		}
		i_rc;
	}*/
}