/*
 * $Log$
 * emariacher - Tuesday, November 17, 2009 11:29:55 AM
 * cleaning html err area
 * emariacher - Tuesday, November 17, 2009 10:45:31 AM
 * err textarea is now in html.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JLabel;

class Log {
	ErrorPane err             = null;
	JLabel LogLabel           = null;
	MyFile logfile               = null;
	MyFile errfile               = null;
	String working_directory  = null;
	String name_no_ext        = null;
	ArrayList<MyFile> files        = new ArrayList<MyFile>();

	Log(ErrorPane err, JLabel LogLabel) {
		this.err = err;
		this.LogLabel = LogLabel;
	}

	Log(Log L) {
		this.err = L.err;
		this.LogLabel = L.LogLabel;
		this.files = L.files;
		this.logfile = L.logfile;
		this.errfile = L.errfile;
	}


	/**  Constructor for the Log object */
	Log() { }

	String initLogFiles() throws Exception {

		logfile = new MyFile(working_directory + File.separatorChar+"log_" + 
				printToday("ddMMMyy_HH_mm") + ".log");
		files.add(logfile);
		errfile = new MyFile(working_directory + File.separatorChar+"out_"+name_no_ext.replaceAll("\\W", "_")+"_" + 
				printToday("ddMMMyy_HH_mm") + ".htm");
		files.add(errfile);
		System.err.println("initLogFiles():" + working_directory);
		return working_directory;
	}

	String printToday(String fmt) throws Exception {
		Calendar today  = Calendar.getInstance();
		return printZisday(today, fmt);
	}

	String printToday() throws Exception {
		return printToday("ddMMMyy");
	}

	String printZisday(Calendar zisday, String fmt) throws Exception {
		return printZisday(zisday.getTime(), fmt);
	}

	String printZisday(Date date, String fmt) throws Exception {
		SimpleDateFormat datefmt2  = new SimpleDateFormat(fmt);
		String sdate2              = new String(datefmt2.format(date));
		return sdate2;
	}

	String printZisday(Date date) throws Exception {
		return printZisday(date, "ddMMMyy");
	}

	String printZisday(Calendar zisday) throws Exception {
		return printZisday(zisday.getTime());
	}

	void copyCal(Calendar out, Calendar in) {
		out.setTime(in.getTime());
	}

	String knowPath(String filename) throws Exception {
		File fil              = new File(filename);
		String name           = fil.getName();
		String CanonicalPath  = fil.getCanonicalPath();
		int index             = CanonicalPath.indexOf(name);
		String ext            = getExtension(fil);
		if((ext != null) && (name.lastIndexOf(ext) > 1)) {
			name_no_ext = name.substring(0, name.lastIndexOf(ext) - 1);
		}
		working_directory = new String(CanonicalPath.substring(0, index));
		return working_directory;
	}

	void closeFiles() throws Exception {
		logfile               = null;
		errfile               = null;
		Iterator<MyFile> i = files.iterator();
		while(i.hasNext()){
			MyFile mf = i.next();
			mf.clean();
			myErrPrintln(mf.getName()+" file closed.");
		}
	}

	void displayLogMsg(String s) throws Exception {
		if(LogLabel != null) {
			LogLabel.setText(s);
		}
		myPrintln(s);
	}

	void myPrint(String s) throws Exception {
		if(logfile != null) {
			logfile.writeFile(s);
		}
		System.out.print(s);
	}

	void myPrintln(String s) throws Exception {
		myPrint(s + "\n");
	}

	void myErrPrint(String s) throws Exception {
		if(err != null) {
			err.appendErr(s);
			if(errfile != null) {
				errfile.writeFile(s);
			}
			myPrint(s);
			if(logfile != null) {
				logfile.writeFile(s);
			}
		} else {
			System.err.print(s);
		}
	}

	
	void myErrPrintln(String s) throws Exception {
		myErrPrint(s + "\n");
	}

	MyFile addXmlHeaderAndFooter(File i) throws Exception {
		BufferedReader inputf  = new BufferedReader(new FileReader(i));
		MyFile newFile          = new MyFile(new String("zob" + (new Random()).nextInt()));
		String linef;
		newFile.writeFile("<eric_mariacher>");
		while((linef = inputf.readLine()) != null) {
			newFile.writeFile(linef.replaceAll("&", "&amp;") + "\n");
		}
		newFile.writeFile("</eric_mariacher>");
		newFile.clean();
		return newFile;
	}

	private String rootName;
	private String extName;

	void setRootName(String file_name) {
		rootName = new String(file_name.substring(0, file_name.indexOf(".")));
		extName = new String(file_name.substring(file_name.indexOf(".")));
	}

	String getRootName() {
		return rootName;
	}

	String getExtName() {
		return extName;
	}

	private String getExtension(File f) {
		String ext  = null;
		String s    = f.getName();
		int i       = s.lastIndexOf('.');

		if(i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public MyFile getMyFile(String pathname) throws IOException, Exception{
		MyFile f = new MyFile(pathname);
		files.add(f);
		return f;
	}
	
	public MyFile getMyUniqueFile(String pathname, String extension) throws IOException, Exception{
		return getMyFile(working_directory + File.separatorChar+pathname + 
				printToday("ddMMMyy_HH_mm") + "."+extension);
	}

	
	class MyFile extends File {

		FileOutputStream fos;
		File dir;
		String CanonicalPath  = null;
		private static final long serialVersionUID = 1L;

		public MyFile(String pathname) throws IOException, Exception {
			super(pathname);
			int index  = pathname.lastIndexOf(File.separatorChar);
			if(index > 0) {
				CanonicalPath = pathname.substring(0, index);
			}
			if(CanonicalPath != null) {
				if(CanonicalPath.length() != 0) {
					dir = new File(CanonicalPath);
					dir.mkdirs();
				}
			}
			try  {
				fos = new FileOutputStream(this);
			} catch(FileNotFoundException e) {
				System.err.println("Cannot write to file[" +
						getCanonicalPath() + "]");
				throw new Exception("Cannot write to file[" +
						getCanonicalPath() + "]");
			}
		}

		public void clean() throws Exception {
			fos.close();
			System.out.println("Fichier " + getName() + " generated.");
		}

		void writeFile(String s) {
			try {
				fos.write(s.getBytes());
			} catch(Exception e) {
				System.err.println(e + " " + getName());
				e.printStackTrace();
			}
		}
	}


}
