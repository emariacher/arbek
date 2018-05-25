import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    September 14, 2005
 */
class Viadeo2 extends Thread {

	String binaryFile = null;

	String currep = null;
	List defectIn = new ArrayList();
	List defectOut = new ArrayList();

	MesFichiers f = new MesFichiers();
	List files = new ArrayList();

	String s_buildNumber = null;
	String working_directory = null;
	File defvardiff;
	FileOutputStream outputf;



	/**
	 *  Constructor for the BuildRelease2 object
	 *
	 *@param  zt             Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	public Viadeo2(Viadeo2HMI zt) throws Exception {
		this.zt = zt;
		this.err = zt.err;
		initLogFiles();
		start();
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	void closeLogFiles() throws Exception {
		f.cleanfos(logfile);
		f.cleanfos(errfile);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  s              Description of the Parameter
	 *@return                Description of the Return Value
	 *@exception  Exception  Description of the Exception
	 */
	String crunchwebpage(String s) throws Exception {
		myErrPrintln("crunchwebpage");
		String slocal = new String(s.toLowerCase().replaceAll("<[/]*strong>", ""));
		slocal = new String(slocal.replaceAll("[\\s]*\\.[\\s]*", ".").replaceAll("[\\s|\\p{Punct}]+dot[\\s|\\p{Punct}]+", "."));
		slocal = new String(slocal.replaceAll("[\\s|\\p{Punct}]+at[\\s|\\p{Punct}]+", "@").replaceAll("[\\s|\\p{Punct}]+@[\\s|\\p{Punct}]+", "@"));
		slocal = new String(slocal.replaceAll("[\\s|\\p{Punct}]+a[\\s|\\p{Punct}]+", "@"));
		Pattern p = Pattern.compile("[\\w|\\.|\\-|\\_]+\\@[\\w|\\.]+");
		Matcher m = p.matcher(slocal);
		myPrintln(s);
		myPrintln("\n***********\n************\n*********\n*********\n" + slocal);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String email = new String(m.group().replaceAll("\\.com\\.\\.\\.", "\\.com").replaceAll("\\.\\.\\.", "\\."));
			if (m.group().lastIndexOf(".") > m.group().lastIndexOf("@")) {
				myErrPrintln("mail: " + email);
				if ((sb.indexOf(email) < 0)&&(zt.alreadyInvited.indexOf(email) < 0)) {
					sb.append(email + ", ");
				}
			}
		}
		return sb.toString();
	}


	JTextArea err;
	int errfile;



	/**
	 *  Description of the Method
	 *
	 *@exception  Exception  Description of the Exception
	 */
	void initLogFiles() throws Exception {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat datefmt = new SimpleDateFormat("ddMMMyy_HH_mm");
		SimpleDateFormat datefmt2 = new SimpleDateFormat("ddMMMyy");
		String sdate = new String(datefmt.format(date));
		logfile = f.addFichier("logfile_" + sdate + ".log");
		errfile = f.addFichier("errfile_" + sdate + ".log");
	}


	int logfile;


	/**
	 *  Description of the Method
	 *
	 *@param  s              Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void myErrPrint(String s) throws Exception {
		if (err != null) {
			err.append(s);
			f.writeFile(errfile, s);
			myPrint(s);
			f.writeFile(logfile, s);
		}
		System.out.print(s);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  s              Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void myErrPrintln(String s) throws Exception {
		myErrPrint(s + "\n");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  s              Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void myPrint(String s) throws Exception {
		f.writeFile(logfile, s);
		System.out.print(s);
	}


	/**
	 *  Description of the Method
	 *
	 *@param  s              Description of the Parameter
	 *@exception  Exception  Description of the Exception
	 */
	void myPrintln(String s) throws Exception {
		myPrint(s + "\n");
	}


	/**
	 *  Description of the Method
	 */
	public void run() {
		try {
			myErrPrintln("running");
			myPrintln("yo1[" + zt.alreadyInvited + "]");
			String emailist = new String(crunchwebpage(zt.webpage.getText()));
			zt.email_list.setText(emailist);
			myErrPrintln("runned");
			zt.alreadyInvited = zt.alreadyInvited.concat(emailist);
			outputf = new FileOutputStream(zt.defvardiff);
			outputf.write((new String(zt.alreadyInvited + "\n")).getBytes());
			outputf.write((new String("\n")).getBytes());
			outputf.close();
			closeLogFiles();
		} catch (Exception e) {
			zt.AnalyzeLabel.setText("*******Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				myErrPrintln(e.toString());
				int len = 0;
				byte[] buf = new byte[1024];
				while (true) {
					len = piErr.read(buf);
					if (len == -1) {
						break;
					}
					err.append(new String(buf, 0, len));
				}
			} catch (Exception ze) {}
		} finally {}
	}


	Viadeo2HMI zt;

}
// -- end class BuildRelease2

