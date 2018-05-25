import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MyFile extends File {

	FileOutputStream fos;
	File dir;
	String CanonicalPath  = null;
	Log L;
	private static final long serialVersionUID = 1L;

	public MyFile(String pathname, Log L) throws IOException, Exception {
		super(pathname);
		this.L=L;
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
			L.myErrPrintln("Cannot write to file[" +
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
