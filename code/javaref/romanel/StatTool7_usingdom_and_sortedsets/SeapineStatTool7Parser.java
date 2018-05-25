
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    January 26, 2006
 */
public class SeapineStatTool7Parser extends Thread implements DefVar {

	/**
	 *  Constructor for the SeapineStatToolParser object
	 *
	 *@param  zt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	public SeapineStatTool7Parser(SeapineStatTool7 zt) throws Exception {
		this.zt = zt;
		L = new Log(zt.err, zt.AnalyzeLabel);
		x = new SeapineStat(this);
		L.knowPath(zt.SeapineReportHtmlFile.getCanonicalPath());
		x.initLogFiles();
		start();
	}


	Log L;


	/**
	 *  Main processing method for the SeapineStatTool5Parser object
	 */
	public void run() {
		try {
			// PARSE...
			L.myErrPrintln("parsing");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(zt.SeapineReportHtmlFile);
			// ANALYZE
			L.myErrPrintln("running");
			x.buildChildListsXmltag(dom);
			zt.AnalyzeLabel.setText(x.xlsfile_bug.getName() + " Xls Report Built!");
			L.closeFiles();
		} catch (Exception e) {
			zt.AnalyzeLabel.setText("*******Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				L.myPrintln(e.toString());
				L.myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					L.err.append(new String(buf, 0, len));
					L.myPrintln(new String(buf));

				}

			} catch (Exception ze) {}
		} finally {

		}
	}


	Document dom;
	SeapineStat x;
	SeapineStatTool7 zt;
}

