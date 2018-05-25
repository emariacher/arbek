package git2surround
import java.io.File
import java.io.FileWriter
import scala.collection.immutable.ListSet
import scala.swing.TextField
import scala.swing.PasswordField

object Git2Surround {
	def main(args: Array[String]): Unit = {
			println("Hello World!")			
			val afile = new File("..\\out\\placeholder.g2s")
			val L = new MyLog(this.getClass.getName,afile,"htm")
			var l_parameters = List[String]();
			scala.io.Source.fromFile(afile.getName).getLines.foreach((s: String) => l_parameters +: s);
			L.myErrPrintln(l_parameters.mkString("l_parameters from "+afile.getCanonicalPath()+":\n  ","\n  ",""));
			val g2s = new Git2Surround(L, l_parameters)		
	};
	def checkDirExistsGitInited(L: MyLog, s_dir: String): String = {
			val lastSlash = s_dir.lastIndexOf(File.separatorChar);
			val parentDir = s_dir.substring(0,lastSlash);
			val dir = s_dir.substring(lastSlash+1);
			ScalaBatsh.cleanDirectory(L, parentDir, dir);
			ScalaBatsh.createDirectory(L, parentDir, dir);
			MyLog.checkException(L, ScalaBatsh.exec(L, 4,"ls "+s_dir)._1, s_dir+" is not a valid directory.");
			ScalaBatsh.execrcdir(L, errorCode.OK, "git init", s_dir);
			MyLog.checkException(L, ScalaBatsh.execrcdir(L, errorCode.OK, "git status --porcelain", s_dir)._1, s_dir+" is not git inited.");
			parentDir+"\\logprev.log";
	}
}
class Git2Surround(val L: MyLog, val l_args: List[String]) {
	val s_gitServer = l_args(0);
	val s_workingDirectory = l_args(1);
	val s_SCMAdd = l_args(2);
	val l_uid_pwd = l_args(3).split(':').toList;
	L.createGui(List(("uid",l_uid_pwd(0),new TextField),("pwd","",new PasswordField)));
	val s_pwd = L.Gui.getAndClose.apply(1)._2
	val s_uid_pwd = l_uid_pwd(0) + ":"+ s_pwd;
//	val s_uid_pwd = l_uid_pwd(0) + ":"+ List.fromString(l_uid_pwd(1),' ').reverse.mkString("","","");
	val s_sscmAccess = " -y"+s_uid_pwd+" -z"+s_SCMAdd;
	val sscmMainLine = l_args(4);
	val i_period2scan = l_args(5).toInt;

	var i_rc = errorCode.OK;
	val lsbranch = ScalaBatsh.execrcNoPwd(L,i_rc,"sscm lsbranch -p"+sscmMainLine+" -a"+s_sscmAccess, s_uid_pwd);
	i_rc = lsbranch._1;
	MyLog.checkException(L,i_rc,"sscm command executed correctly");
	val l_lsbranch = lsbranch._2;

	L.myErrPrintln("<html><body>\n")
	L.myErrPrintln("<h2>jar: " + L.getJarFileCompilationDate() +"</h2>");
	L.myErrPrintln("<h3>Do not process repositories older than " + i_period2scan +" days</h3><pre>");
	Git2Surround.checkDirExistsGitInited(L, s_workingDirectory);
	var l_gitRepositories = ListSet.empty[GitRepository];
	var fw_logprev = new FileWriter("a");

	// get log file from git of previous Git2Surround jobs
	val go = new GitOsis(L,s_workingDirectory, s_gitServer, "gitosis-admin.git");
	i_rc = go.i_rc
			if(i_rc==errorCode.OK) {
				var l_s_gitRepos = go.l_repos;
				l_gitRepositories = l_s_gitRepos.map(new GitRepository(this, _));
				L.myErrPrintln(l_gitRepositories.mkString("</pre>l_gitRepositories:<ul>\n  <li>","</li>\n  <li>","</li></ul><pre>"));
			} else {
				L.myErrPrintln("Gitosis stuff failed!")
			}
	L.myErrPrintln("</pre><h1>RC="+i_rc+"</h1></body></html>\n")
L.closeFiles
}