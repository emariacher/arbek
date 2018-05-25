package git2surround
import java.util.concurrent.TimeUnit

import java.util.concurrent.FutureTask

import scala.util.Random
import java.io._
import scala.collection.immutable.ListSet

object ScalaBatsh {
	def execrc(L: MyLog, i_rc: errorCode, s_cmd: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			exec(L, s_cmd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---["+s_cmd+"] has not been executed because previous rc="+i_rc)
			(i_rc,List[String](),List[String]())
		}
	}
	def execrcdir(L: MyLog, line: Int, i_rc: errorCode, s_cmd: String, s_dir: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			exec(L, "cd "+s_dir+"\n"+s_cmd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---["+s_cmd+"] in dir ["+s_dir+"] has not been executed because previous rc="+i_rc)
			(i_rc,List[String](),List[String]())
		}
	}
	def execrcNoPwd(L: MyLog, i_rc: errorCode, s_cmd: String, s_pwd: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			execNoPwd(L, s_cmd, s_pwd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---["+s_cmd.replaceAll(s_pwd,"##hidden##")+"] has not been executed because previous rc="+i_rc)
			(i_rc,List[String](),List[String]())
		}
	}
	def execrcNoPwdDir(L: MyLog, line: Int, i_rc: errorCode, s_cmd: String, s_pwd: String, s_dir: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			execNoPwd(L,  "cd "+s_dir+"\n"+s_cmd, s_pwd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---["+s_cmd.replaceAll(s_pwd,"##hidden##")+"] in dir ["+s_dir+"] has not been executed because previous rc="+i_rc)
			(i_rc,List[String](),List[String]())
		}
	}
	def exec(L: MyLog, s_cmd: String): (errorCode,List[String],List[String]) = execNoPwd(L, s_cmd, "Cowabunga!")

	def execNoPwd(L: MyLog, s_cmd: String, s_pwd: String): (errorCode,List[String],List[String]) = {
		L.myErrPrintln("ScalaBatsh.exec(<i>"+s_cmd.replaceAll(s_pwd,"##hidden##")+"</i>)")
		val s_filename = "zz"+new Random().nextInt(1000)+".bat"
		val fw = new FileWriter(s_filename, true) ; fw.write(s_cmd+"\n") ; fw.close()
		var ls = List[String]();
		var lse = List.empty[String];
		var i2_rc = i_unknownError;
		try {
			val theTask = new FutureTask(new Runnable() {
				def run() {
					val p = Runtime.getRuntime().exec(s_filename)
					var input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()))
					Stream.continually(input.readLine()).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => ls = ls ::: List(l.replaceAll(s_pwd,"##hidden##")))
					var error = new java.io.BufferedReader(new java.io.InputStreamReader(p.getErrorStream()))
					Stream.continually(error.readLine()).takeWhile(_ != null).filter(_.length>1).foreach((l: String) => lse = lse ::: List(l.replaceAll(s_pwd,"##hidden##")))
					i2_rc = p.exitValue()
				};
			}, null);
			new Thread(theTask).start();
			theTask.get(long_timeOut, TimeUnit.MILLISECONDS);

			L.myPrintln(ls.mkString("    i  ","\n    i  ","\n"))
			if(!lse.isEmpty) L.myErrPrintln(lse.mkString("  e  ","\n  e  ","\n"))
			(new File(s_filename)).delete()
		} catch {
		case e: Exception => println("execBat Exception: "+ e)
		}
		val i_rc = parse4Errors(i2_rc,ls,lse)
		L.myErrPrintln("  rc="+i_rc+" {"+errorCode.nameOf(i_rc)+"}")
		(i_rc,ls,lse)
	}

	final val i_unknownError = 808
	
	final val long_timeOut = 50000L
	

	def parse4Errors(l:(Int,List[String],List[String])): errorCode = {
		if(l._3.exists(_.startsWith("The system cannot find the path specified."))) {
			errorCode.noSuchDirectory
		} else if(l._3.exists(_.startsWith("The filename, directory name, or volume label syntax is incorrect."))) {
			errorCode.noSuchDirectory
		} else if(l._3.exists(_.startsWith(" * branch"))) {
			errorCode.OK
		} else if(l._3.exists(_.startsWith("TimeoutException"))) {
			errorCode.TimeoutException
		} else if(l._3.exists(_.startsWith("No files are checked in because files are not checked out."))) {
			errorCode.OK
		} else if(l._3.exists(_.indexOf("Switched to")>=0)) {
			errorCode.OK
		} else if(l._3.exists(_.indexOf("Already on")>=0)) {
			errorCode.OK
		} else if(l._3.exists(_.indexOf("Device or resource busy")>=0)) {
			errorCode.OK
		} else if(l._3.exists(_.indexOf("File already exists!")>=0)) {			errorCode.OK		} else if(l._3.exists(_.indexOf("Please, commit your changes or stash them before you can switch branches.")>=0)) {
			errorCode.gitFileChanged
		} else if(l._3.exists(_.indexOf("warning: You appear to have cloned an empty repository.")>=0)) {
			errorCode.gitEmptyDir
		} else if(l._1!=0) {			errorCode.unknownError
		} else if(!l._3.isEmpty) {
			println(l._3.mkString("SomeError:\n  e  ","\n  e  ","\n"))
			errorCode.someError
		} else if(l._2.exists(_.startsWith("fatal: Not a git repository"))) {
			errorCode.notGitRepository
		} else if(l._2.exists(_.indexOf("Total listed files: 0")>=0)) {			errorCode.labelDoesNotExists		} else {
			errorCode.OK
		}
	}

	def createDirectoryIfNeeded(L: MyLog, line: Int, directoryName: String): Int = {
		val theDir = new File(directoryName);
		var i_rc = 0;

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			L.myErrPrintln("["+line+"] creating directory: " + directoryName);
			if(!theDir.mkdirs()) {
				i_rc = 1;
			}
		} else {
			L.myErrPrintln("["+line+"] already exist directory: " + directoryName);
		}
		return i_rc;
	}
	
	def deleteDirectoryIfNeeded(L: MyLog, line: Int, directoryName: String): Int = {
		val theDir = new File(directoryName);
		var i_rc = 0;

		// if the directory does not exist, create it
		if (theDir.exists()) {
			L.myErrPrintln("["+line+"] deleting directory: " + directoryName);
			if(!theDir.delete()) {
				i_rc = 1;
			}
		} else {
			L.myErrPrintln("["+line+"] did not exist directory: " + directoryName);
		}
		return i_rc;
	}
	
	def cleanDirectory(L: MyLog, line: Int, parentDir: String, dir: String): errorCode = {
		var i_rc = errorCode.OK;
		ScalaBatsh.exec(L, "echo cleanDirectory["+parentDir+File.separatorChar+dir+"]: "+line)
		if((new File(parentDir+File.separatorChar+dir)).exists()) {
//			i_rc = ScalaBatsh.exec(L, "echo line: "+line+"\ncd "+parentDir+"\nrmdir /S /Q "+dir)._1;
			i_rc = ScalaBatsh.exec(L, "cd "+parentDir)._1;			i_rc = ScalaBatsh.execrc(L, i_rc, "cd "+parentDir+"\nrm -rf "+dir)._1;		}
		i_rc;
	}
	def createDirectory(L: MyLog, line: Int, parentDir: String, dir: String): errorCode = {
		var i_rc = errorCode.OK;
		if(!(new File(parentDir+File.separatorChar+dir)).exists()) {
			i_rc = ScalaBatsh.exec(L, "echo line: "+line+"\ncd "+parentDir)._1;			i_rc = ScalaBatsh.execrc(L, i_rc, "echo line: "+line+"\ncd "+parentDir+"\nmkdir "+dir)._1;		}
		i_rc;
	}
}