package git2surround
import java.io.FilenameFilter

import java.io.File
import java.text.ParsePosition;
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import scala.collection.immutable.ListSet

	def parseGitDate(s: String): Date = {
}

class TaggedCommit(L: MyLog, l: List[String]) extends Ordering[TaggedCommit] { 
	case r_tagv(s_tagTmp) => s_tag = s_tagTmp; L.myPrintln("    TAG["+s_tag+"] + BRANCH(ES)");
	case r_tagp(s_tagTmp) => s_tag = s_tagTmp; L.myPrintln("    TAG["+s_tag+"]");
	});

	def this(L: MyLog, l: List[String], a: Int) {
		this(L,List[String]())
		if(l.size==3) {
			s_tag = l(0);
			date = TaggedCommit.formatIn.parse(l(1), new ParsePosition(0));
			s_gitRepos = l(2)
		}
	};
	override def equals(that: Any): Boolean = {
			val that_tg = that.asInstanceOf[TaggedCommit];
			((s_tag==that_tg.s_tag)&&(date==that_tg.date))
	};

	override def toString(): String = {

	def git2Surround(gr: gitBranch) {