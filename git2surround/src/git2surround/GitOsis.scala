package git2surround

import java.io.File

import scala.collection.immutable.ListSet

class GitOsis(val L: MyLog, val s_workingDirectory: 
		String, val s_gitServer: String, val s_gitRepos: String) {
	val r_group = """\[group (.+)\]""".r;
	val r_writable = """writable = (.+)""".r;
	val r_members = """members = (.+)""".r;
	val r_other = """\s+(.+)""".r;
	var l_groups = ListSet.empty[(String,String,Int)];
	var l_repos = ListSet.empty[String];
	var s_group = "";
	val i_notYetInitialized = 0;
	val i_4writable = 1;
	val i_4members = 2;
	var i_state = i_notYetInitialized;

	var i_rc = errorCode.OK;
	L.myErrPrintln("</pre><h2>***GitOsis("+s_gitRepos+")***</h2><pre>")

	Git2Surround.checkDirExistsGitInited(L, s_workingDirectory);

	// clean directory
	i_rc = ScalaBatsh.cleanDirectory(L, 29, s_workingDirectory, s_gitRepos.substring(0,s_gitRepos.indexOf(".")));

	// clone repository
	i_rc = ScalaBatsh.execrcdir(L, 33, errorCode.OK, "git clone "+s_gitServer+":"+s_gitRepos, s_workingDirectory)._1;
		
	// build tag list
	if(i_rc == errorCode.OK) {
		val l_lines = scala.io.Source.fromFile(s_workingDirectory+File.separatorChar+
				s_gitRepos.substring(0,s_gitRepos.indexOf("."))+File.separatorChar+"gitosis.conf").getLines;
		l_lines.foreach((l: String) => l match {
		case r_group(s_groupTmp) => s_group = s_groupTmp
		case r_writable(s_line) => l_groups ++= s_line.split(' ').toList.map((s_group,_,i_4writable)); i_state = i_4writable
		case r_members(s_line) => l_groups ++= s_line.split(' ').toList.map((s_group,_,i_4members)); i_state = i_4members
		case r_other(s_line) => l_groups ++= s_line.split(' ').toList.map((s_group,_,i_state));
		case _ =>
		}
		);
		L.myErrPrintln(l_groups.mkString("***l_groups0:\n  ","\n  ",""));
		l_groups = l_groups.filter((l: (String,String,Int)) => l._3 == i_4writable && l._1 != "gitosis-admin");
		val r_filter1 = """(tools/.+)""".r;
		val r_filter2 = """(fw/.+)""".r;
		val r_filter3 = """(personal/.+)""".r;
		l_repos = l_groups.filter((l: (String,String,Int)) => l._2 match {
		case r_filter1(s) => true
		case r_filter2(s) => true
		case r_filter3(s) => true
		case _ => false
		}).map(_._2);
		L.myErrPrintln(l_repos.mkString("***l_repos:\n  ","\n  ",""));
	};
}
