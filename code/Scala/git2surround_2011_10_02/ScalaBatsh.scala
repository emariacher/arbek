package git2surround
import java.util.concurrent.TimeUnit

import java.util.concurrent.FutureTask

import scala.util.Random
import java.io._
import scala.collection.immutable.ListSet

object ScalaBatsh {
	def execrc(L: MyLog, i_rc: errorCode, s_cmd: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			exec(L, 4, s_cmd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---"+MyLog.tag(2)+"["+s_cmd+"] has not been executed because previous rc: {"+errorCode.nameOf(i_rc)+"}")
			(i_rc,List[String](),List[String]())
		}
	}
	def execrcdir(L: MyLog, i_rc: errorCode, s_cmd: String, s_dir: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			exec(L, 4, "cd "+s_dir+"\n"+s_cmd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---"+MyLog.tag(2)+"["+s_cmd+"] in dir ["+s_dir+"] has not been executed because previous rc: {"+errorCode.nameOf(i_rc)+"}")
			(i_rc,List[String](),List[String]())
		}
	}
	def execrcNoPwd(L: MyLog, i_rc: errorCode, s_cmd: String, s_pwd: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			execNoPwd(L, 3,s_cmd, s_pwd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---"+MyLog.tag(2)+"["+s_cmd.replaceAll(s_pwd,"##hidden##")+"] has not been executed because previous rc: {"+errorCode.nameOf(i_rc)+"}")
			(i_rc,List[String](),List[String]())
		}
	}
	def execrcNoPwdDir(L: MyLog, i_rc: errorCode, s_cmd: String, s_pwd: String, s_dir: String): (errorCode,List[String],List[String]) = {
		if(i_rc == errorCode.OK) {
			execNoPwd(L, 3,"cd "+s_dir+"\n"+s_cmd, s_pwd)
		} else {
			L.myErrPrintln("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;---"+MyLog.tag(2)+"["+s_cmd.replaceAll(s_pwd,"##hidden##")+"] in dir ["+s_dir+"] has not been executed because previous rc: {"+errorCode.nameOf(i_rc)+"}")
			(i_rc,List[String](),List[String]())
		}
	}
	def exec(L: MyLog, Tlvl: Int, s_cmd: String): (errorCode,List[String],List[String]) = execNoPwd(L, Tlvl, s_cmd, "Cowabunga!")

			def execNoPwd(L: MyLog, Tlvl: Int, s_cmd: String, s_pwd: String): (errorCode,List[String],List[String]) = {
		L.myErrPrintln(MyLog.tag(Tlvl)+"ScalaBatsh.exec(<i>"+s_cmd.replaceAll(s_pwd,"##hidden##")+"</i>)")
		val s_filename = "zz"+new Random().nextInt(1000)+".bat";		val fw = new FileWriter(s_filename, false) ; fw.write(s_cmd+"\n") ; fw.close()
		var ls = List[String]();
		var lse = List.empty[String];
		var i2_rc = i_unknownError;		try {
			val theTask = new FutureTask(new Runnable() {
				def run() {					MyLog.waiting(1000);					val p = Runtime.getRuntime().exec(s_filename)
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
		case e: Exception => L.myErrPrintDln("execBat Exception: "+ e)
		}
		val i_rc = parse4Errors(i2_rc,ls,lse)
				L.myErrPrintln("  rc: {"+errorCode.nameOf(i_rc)+"}")
				(i_rc,ls,lse)
	}

	final val i_unknownError = 808

			final val long_timeOut = 100000L


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
			errorCode.DevResBusy
		} else if(l._3.exists(_.indexOf("File already exists!")>=0)) {			errorCode.OK		} else if(l._3.exists(_.indexOf("Repository already exists!")>=0)) {			errorCode.ReposExists		} else if(l._3.exists(_.indexOf("MS-DOS style path detected:")>=0)) {			errorCode.RelaunchTheProgram		} else if(l._3.exists(_.indexOf("Branch already exists!")>=0)) {			errorCode.OK		} else if(l._3.exists(_.indexOf("check-in error: Local directory")>=0)) {			errorCode.OK		} else if(l._3.exists(_.indexOf("Unable to check in file")>=0)) {			errorCode.UnableCheckIn		} else if(l._3.exists(_.indexOf("Invalid branch!")>=0)) {			errorCode.scmInvalidBranch		} else if(l._3.exists(_.indexOf("fatal: Unable to create")>=0)) {			errorCode.fatalGitError		} else if(l._3.exists(_.indexOf("Please, commit your changes or stash them before you can switch branches.")>=0)) {
			errorCode.gitFileChanged
		} else if(l._3.exists(_.indexOf("warning: You appear to have cloned an empty repository.")>=0)) {			errorCode.gitEmptyDir		} else if(l._3.exists(_.indexOf("Either the username or password you entered is not valid.")>=0)) {			errorCode.SurroundBadUiDPwd		} else if(l._1!=0) {			errorCode.unknownError		} else if(!l._3.isEmpty) {
			println(l._3.mkString("SomeError:\n  e  ","\n  e  ","\n"))
			errorCode.someError
		} else if(l._2.exists(_.startsWith("fatal: Not a git repository"))) {
			errorCode.notGitRepository
		} else if(l._2.exists(_.indexOf("Total listed files: 0")>=0)) {			errorCode.labelDoesNotExists		} else {
			errorCode.OK
		}
	}

	def createDirectoryIfNeeded(L: MyLog, directoryName: String): Int = {
		val theDir = new File(directoryName);
		var i_rc = 0;

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			L.myErrPrintln("["+MyLog.tag(2)+"] creating directory: " + directoryName);
			if(!theDir.mkdirs()) {
				i_rc = 1;
			}
		} else {
			L.myErrPrintln("["+MyLog.tag(2)+"] already exist directory: " + directoryName);
		}
		return i_rc;
	}

	def deleteDirectoryIfNeeded(L: MyLog, directoryName: String): Int = {
		val theDir = new File(directoryName);
		var i_rc = 0;

		// if the directory does not exist, create it
		if (theDir.exists()) {
			L.myErrPrintln("["+MyLog.tag(2)+"] deleting directory: " + directoryName);
			if(!theDir.delete()) {
				i_rc = 1;
			}
		} else {
			L.myErrPrintln("["+MyLog.tag(2)+"] did not exist directory: " + directoryName);
		}
		return i_rc;
	}

	def cleanDirectory(L: MyLog, parentDir: String, dir: String): errorCode = {
		var i_rc = errorCode.OK;
		L.myErrPrintln(MyLog.func(1)+"["+MyLog.tag(2)+"] ["+parentDir+File.separatorChar+dir+"]")
		if((new File(parentDir+File.separatorChar+dir)).exists()) {
			MyLog.checkException(L, ScalaBatsh.exec(L, 5,"cd "+parentDir)._1,"Parent Directory does not exists["+parentDir+"].");			i_rc = ScalaBatsh.execrc(L, i_rc, "cd "+parentDir+"\nrm -rf "+dir)._1;		};		if(i_rc==errorCode.DevResBusy) {			i_rc = errorCode.OK;		};
		i_rc;
	}
	def createDirectory(L: MyLog, parentDir: String, dir: String): errorCode = {
		var i_rc = errorCode.OK;		L.myErrPrintln(MyLog.func(1)+"["+MyLog.tag(2)+"] ["+parentDir+File.separatorChar+dir+"]")
		if(!(new File(parentDir+File.separatorChar+dir)).exists()) {
			MyLog.checkException(L, ScalaBatsh.exec(L, 5,"cd "+parentDir)._1,"Parent Directory does not exists["+parentDir+"].");			i_rc = ScalaBatsh.execrc(L, i_rc, "cd "+parentDir+"\nmkdir "+dir)._1;		}
		i_rc;
	}
}