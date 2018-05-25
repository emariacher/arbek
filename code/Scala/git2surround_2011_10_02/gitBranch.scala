package git2surround

import java.util.Date
import java.io.File
class gitBranch(val gitRepos: GitRepository, val s_branch: String) {
	val g2s = gitRepos.g2s;
	val L = g2s.L;
	val s_gitRepos = gitRepos.s_gitRepos;
	var ll_commits = List.empty[TaggedCommit];
	var ll_taggedCommits = List.empty[TaggedCommit];
	var i_rc = errorCode.OK;
	var i_backupedTag = 0;
	L.myPrintDln("************************************************************************************************"); 
	L.myErrPrintDln("</pre><h2>***"+MyLog.func(1)+"(repository: "+s_gitRepos+", branch: "+s_branch+")****************************************</h2><pre>"); 
	L.myPrintDln("************************************************************************************************"); 
	val s_dir = g2s.s_workingDirectory + File.separatorChar + s_gitRepos.substring(s_gitRepos.lastIndexOf("/")+1);
	
	// checkout branch
	i_rc = ScalaBatsh.execrcdir(L, i_rc, "git branch",s_dir)._1;
	i_rc = ScalaBatsh.execrcdir(L, i_rc, "git checkout "+s_branch,s_dir)._1;
	val gitbranch = ScalaBatsh.execrcdir(L, i_rc, "git branch",s_dir);
	i_rc = gitbranch._1;
	val l_branch = gitbranch._2.filter(_.indexOf("* ")==0);
	L.myErrPrintDln(l_branch.mkString("</pre>current branch:<ul>\n  <li>","</li>\n  <li>","</li></ul><pre>"));
	
	// build tag list
	if(i_rc == errorCode.OK) {
		val gitlog = ScalaBatsh.execrcdir(L,i_rc,"git log --decorate --date=iso",s_dir);
		i_rc = gitlog._1;
		var l_groups = gitlog._2.filter(_.indexOf("commit ")==0) zip gitlog._2.filter(_.indexOf("Date:   ")==0);
		ll_commits = l_groups.map {(l: (String, String)) => new TaggedCommit(L, List(l._1,l._2,"Repository: "+s_gitRepos,"Branch: "+s_branch))} .sorted(TaggedCommit);
		ll_taggedCommits = ll_commits.filter(_.s_tag.indexOf("NO TAG")<0);
		if(ll_taggedCommits.isEmpty) {
			i_rc = errorCode.emptyList
		};
		if(ll_commits.last.lastCommit(new Date()).toInt > g2s.i_period2scan) {
			i_rc = errorCode.inactive42Long;
		};
		
	};
	if(i_rc == errorCode.OK) {
		// prepare Surround stuff
		i_rc = GitRepository.buildSurroundBaseline(L, g2s, s_gitRepos);
	};
	if(i_rc == errorCode.OK) {
		L.myErrPrintDln(MyLog.tag(1)+ll_taggedCommits.mkString("</pre>[repository: "+s_gitRepos+", branch: "+s_branch+"] l_taggedCommits:<ul>\n  <li>","</li>\n  <li>","</li></ul><pre>"));
		if(!ll_commits.isEmpty) {
			L.myErrPrintDln("</pre>[repository: "+s_gitRepos+", branch: "+s_branch+"] last commit: "+ll_commits.last.toString+"<pre>");
		};
		if(!ll_taggedCommits.isEmpty) {
			L.myErrPrintDln("</pre>[repository: "+s_gitRepos+", branch: "+s_branch+"] last tagged commit: "+ll_taggedCommits.last.toString+"<pre>");
		};
		// process the new tags		
		ll_taggedCommits.foreach(_.git2Surround(this));
		
		i_backupedTag = ll_taggedCommits.filter(_.b_backuped).size;
	};
	L.myErrPrintDln(s_gitRepos+"__"+s_branch+", rc: {"+errorCode.nameOf(i_rc)+"}");
	
	override def toString(): String = {
		L.myPrintDln("****[repository: "+s_gitRepos+", branch: "+s_branch+"] ll_commits:\n"+ll_commits.mkString("\n  "));
		var s = "";
		i_rc match {
			case errorCode.OK          => s = "<font color=\"green\">";
			case errorCode.gitEmptyDir => s = "<font color=\"blue\">";
			case errorCode.emptyList   => s = "<font color=\"red\">";
			case errorCode.inactive42Long   => s = "<font color=\"grey\">";
			case _ => s = "<font color=\"red\">";
		};
		s = s + s_gitRepos + "---"+s_branch+", rc: {"+errorCode.nameOf(i_rc)+"}";
		if(!ll_commits.isEmpty && !ll_taggedCommits.isEmpty) {
			L.myPrintDln("****[repository: "+s_gitRepos+", branch: "+s_branch+"] last Commit: ["+ll_taggedCommits.last+"], "+ll_commits.indexOf(ll_taggedCommits.last));
			val i_diff = ll_commits.size-ll_commits.indexOf(ll_taggedCommits.last)-1;
//			s = s + ", commits: " + (ll_commits.size-1) + ", taggedCommits: " + ll_taggedCommits.size;
			i_diff match {
				case 0         => s = s + "</font><font color=\"darkgreen\">";
				case inf2ten() => s = s + "</font><font color=\"orange\">";
				case _         => s = s + "</font><font color=\"red\">";
			};
			s = s + ", "+i_backupedTag+" backups performed, [c: "+ll_commits.size+"/tc: "+ll_taggedCommits.size+"/diff: "+i_diff+"/"+
			(ll_commits.last-ll_taggedCommits.last)+"d/"+ll_commits.last.lastCommit(new Date())+"d]";
		} else if(!ll_commits.isEmpty) {
			s = s + " [c: "+ll_commits.size+"/"+ll_commits.last.lastCommit(new Date())+"d]";
		};
		s + "</font>"
	};
};
object inf2ten {
	def unapply(x : Int): Boolean = x<10;
};