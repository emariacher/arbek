package git2surround
import java.io.FilenameFilter

import java.io.File
import java.text.ParsePosition;
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import scala.collection.immutable.ListSet
object TaggedCommit extends Ordering[TaggedCommit] {
	def parseGitDate(s: String): Date = {		formatIn.parse(s, new ParsePosition(0));	};	val formatIn  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");	val r_date = """Date:(.+)""".r;		def compare (x: TaggedCommit, y: TaggedCommit): Int = {		x.date .compareTo(y.date);	}
}

class TaggedCommit(L: MyLog, l: List[String]) extends Ordering[TaggedCommit] {	var b_backuped = false;	val r_tagv = """.+tag: (\w+),.*""".r;	val r_tagp = """.+tag: (\w+)\).*""".r;	val r_author = """Author:(.+)""".r;	val r_repos = """Repository: (.+)""".r;	val r_branch = """Branch: (.+)""".r;	var s_tag = "NO TAG";	var s_comment = "--rien--";	var date = new Date();	var s_gitRepos = "NO TAG";	var s_gitBranch = "NO BRANCH";	l.foreach((s: String) => s match {
	case r_tagv(s_tagTmp) => s_tag = s_tagTmp; L.myPrintln("    TAG["+s_tag+"] + BRANCH(ES)");
	case r_tagp(s_tagTmp) => s_tag = s_tagTmp; L.myPrintln("    TAG["+s_tag+"]");	case TaggedCommit.r_date(s_dateTmp) => date = TaggedCommit.parseGitDate(s_dateTmp);	case r_repos(s_reposTmp) => s_gitRepos = s_reposTmp;	case r_branch(s_branchTmp) => s_gitBranch = s_branchTmp;	case r_author(s_authorTmp) => 	case _ => s_comment = s
	}	)

	def this(L: MyLog, l: List[String], a: Int) {
		this(L,List[String]())
		if(l.size==3) {
			s_tag = l(0);
			date = TaggedCommit.formatIn.parse(l(1), new ParsePosition(0));
			s_gitRepos = l(2)
		}
	}
	override def equals(that: Any): Boolean = {
			val that_tg = that.asInstanceOf[TaggedCommit];
			((s_tag==that_tg.s_tag)&&(date==that_tg.date))
	}

	override def toString(): String = {			s_gitRepos + " " + s_gitBranch + " " + s_tag + " " + MyLog.printZisday(date, "ddMMMyy_HH:mm:ss") + " " + s_comment	};	def getCheckoutBranch(L: MyLog, line: Int, i_rci: errorCode, s_tag: String, s_dirgit: String): errorCode = {		var i_rc = i_rci;		if(i_rc == errorCode.OK) {			i_rc = ScalaBatsh.execrcdir(L, line, i_rc, "git branch",s_dirgit)._1;			var gitStatus = ScalaBatsh.execrcdir(L, line, i_rc,"git status", s_dirgit);			i_rc = gitStatus._1;			if(gitStatus._2.exists(_.indexOf("# On branch "+s_tag)<0)) {								i_rc = ScalaBatsh.execrcdir(L, line, i_rc,"git checkout -B br_"+s_tag+" "+s_tag, s_dirgit)._1;				i_rc = ScalaBatsh.execrcdir(L, line, i_rc, "git branch",s_dirgit)._1;			};		};		i_rc;	}
	
	def git2Surround(gr: gitBranch) {		L.myErrPrintln("</pre><h3>*2*"+this.getClass.getName+".git2Surround("+s_gitRepos + "__" + s_gitBranch + "__" + s_tag+")***</h3><pre>");		var i_rc = errorCode.OK;		val s_dir_short = s_gitRepos.substring(s_gitRepos.lastIndexOf("/")+1) + "_" + s_tag;		val s_dir = gr.g2s.s_workingDirectory + File.separatorChar + s_dir_short;		val s_dirSurround_short = s_gitRepos.substring(s_gitRepos.lastIndexOf("/")+1) + "_surround";		val s_dirSurround = gr.g2s.s_workingDirectory + File.separatorChar + s_dirSurround_short;		val s_label = s_tag + "__" + s_gitBranch + "__" + s_gitRepos.substring(s_gitRepos.lastIndexOf("/")+1);		val s_dirgit = gr.g2s.s_workingDirectory + File.separatorChar + s_gitRepos.substring(s_gitRepos.lastIndexOf("/")+1);				// check that label does not exist in Surround, otherwise that means tag has already been saved		i_rc = ScalaBatsh.execrcNoPwd(L,i_rc,"sscm addlabel "+s_label+" -b"+s_gitRepos+" -p"+gr.g2s.sscmMainLine+			" -d"+s_label+gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd)._1;		if(i_rc == errorCode.OK) {			i_rc = getCheckoutBranch(L, 117, errorCode.OK, s_tag, s_dirgit);					i_rc = ScalaBatsh.cleanDirectory(L, 142, gr.g2s.s_workingDirectory, s_dirSurround_short);			i_rc = ScalaBatsh.createDirectory(L, 142, gr.g2s.s_workingDirectory, s_dirSurround_short);			L.myPrintln(this.getClass.getName+" * "+s_gitRepos.substring(s_gitRepos.lastIndexOf("/")+1)+" * "+s_dirSurround_short+" * "+s_dirSurround);			i_rc = ScalaBatsh.execrcNoPwd(L,i_rc,"sscm workdir "+s_dirSurround+" "+gr.g2s.sscmMainLine+" -b"+s_gitRepos+" -r -o"+				gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd)._1;			i_rc = ScalaBatsh.execrcNoPwd(L,i_rc,"sscm workdirinfo "+s_dirSurround+" -r", gr.g2s.s_uid_pwd)._1;			i_rc = ScalaBatsh.execrcNoPwdDir(L,146,i_rc,"sscm uncheckout * -b"+s_gitRepos+" -p"+gr.g2s.sscmMainLine+" -r -fleave"+				gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd,s_dirSurround)._1;			i_rc = ScalaBatsh.execrcNoPwdDir(L,146,i_rc,"sscm checkout * -b"+s_gitRepos+" -p"+gr.g2s.sscmMainLine+" -r -f -wreplace -c-"+				gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd,s_dirSurround)._1;			i_rc = ScalaBatsh.cleanDirectory(L, 142, gr.g2s.s_workingDirectory, s_dirSurround_short);					// copy dir			i_rc = ScalaBatsh.execrc(L,i_rc,"cp -r "+s_dirgit+" "+s_dirSurround)._1;			// remove git info			i_rc = ScalaBatsh.execrcdir(L,112,i_rc,"rm -rf .git",s_dirSurround)._1;					// add or checkin files			i_rc = ScalaBatsh.execrcNoPwdDir(L,158,i_rc,"sscm add * -l"+s_label+" -b"+s_gitRepos+" -p"+gr.g2s.sscmMainLine+" -r -cc"+s_tag+				gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd, s_dirSurround)._1;			i_rc = ScalaBatsh.execrcNoPwdDir(L,160,i_rc,"sscm checkin * -l"+s_label+" -b"+s_gitRepos+" -p"+gr.g2s.sscmMainLine+" -r -cc"+s_tag+				gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd, s_dirSurround)._1;			i_rc = ScalaBatsh.execrcNoPwdDir(L,160,i_rc,"sscm label * -l"+s_label+" -b"+s_gitRepos+" -p"+gr.g2s.sscmMainLine+" -o -r -cc"+s_tag+				gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd, s_dirSurround)._1;					};		val b_alreadyBranched = gr.g2s.l_lsbranch.exists((s: String) => s.indexOf(s_label)>=0);		if(!b_alreadyBranched) {			i_rc = ScalaBatsh.execrcNoPwdDir(L,160,i_rc,"sscm mkbranch "+s_label+" "+gr.g2s.sscmMainLine+" -b"+s_gitRepos+" -l"+s_label+" -ssnapshot -cc"+s_tag+					gr.g2s.s_sscmAccess, gr.g2s.s_uid_pwd, s_dirSurround)._1;		} else {			i_rc = errorCode.alreadyBackUped;		};				if(i_rc==errorCode.OK) {			b_backuped = true;			L.myErrPrintln("<b><font color=\"blue\">*2*"+this.getClass.getName+"**Surround Back-up Done for: "+s_dir_short+"</font></b>");		};		L.myErrPrintln("  "+this.getClass.getName+" "+s_dir_short+", rc="+i_rc+" {"+errorCode.nameOf(i_rc)+"}");	};	def compare (x: TaggedCommit, y: TaggedCommit): Int = {		x.date .compareTo(y.date);	};	def - (tc: TaggedCommit) = ((date.getTime - tc.date.getTime) / (1000*3600*24)).toInt;	def lastCommit(d: Date) = ((d.getTime - date.getTime) / (1000*3600*24)).toInt;}