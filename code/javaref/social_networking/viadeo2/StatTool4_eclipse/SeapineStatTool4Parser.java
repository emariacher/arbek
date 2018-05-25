
import java.io.*;
import java.io.Reader.*;
import java.util.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 26, 2006
 */
public class SeapineStatTool4Parser extends Thread implements DefVar {

	/**
	 *  Constructor for the SeapineStatToolParser object
	 *
	 *@param  zt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	public SeapineStatTool4Parser(SeapineStatTool4 zt) throws Exception {
		this.zt = zt;
		L = new Log(zt.err, zt.AnalyzeLabel);
		x = new SeapineStat(zt, L);
		L.knowPath(zt.SeapineExportTxtFile.getCanonicalPath());
		x.initLogFiles2();
		start();
	}


	Log L;


	/**
	 *  Main processing method for the SeapineStatTool4Parser object
	 */
	public void run() {
		try {
			// PARSE...
			L.myErrPrintln("parsing");
			saxz = new saxzefile(L.f.getFile(L.addXmlHeaderAndFooter(zt.SeapineExportTxtFile)), L, false);
			// ANALYZE
			L.myErrPrintln("running");
			x.buildChildListsXmltag(saxz);
			zt.AnalyzeLabel.setText(x.L.f.getName(x.xlsfile_bug) + " Xls Report Built!");
			x.closeLogFiles2();
		} catch (Exception e) {
			zt.AnalyzeLabel.setText("*******Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				L.myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					L.err.append(new String(buf, 0, len));
				}

			} catch (Exception ze) {}
		} finally {

		}
	}


	saxzefile saxz;
	SeapineStat x;
	SeapineStatTool4 zt;
}

