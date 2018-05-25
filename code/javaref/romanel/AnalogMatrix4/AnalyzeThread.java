import java.io.*;

public class AnalyzeThread extends Thread {
	public AnalyzeThread(AnalogMatrix1 z) throws Exception {
		this.z = z;
		L = new Log(null, z.AnalyzeLabel);
		L.knowPath(z.f_MainMatrixFile.getCanonicalPath());
		L.initLogFiles();
		start();
	}

	AnalogMatrix1 z;
	Log L;
	KbdMatrix km;



	public void run() {
		try {
			// ANALYZE
			z.graphPanel.L=L;
			L.myErrPrintln("Main Matrix File: "+z.f_MainMatrixFile.getName());
			km = new KbdMatrix(z.f_MainMatrixFile, L);
			z.graphPanel.km=km;
			L.myErrPrintln(km.toString());
			L.myErrPrintln(km.toStringKeyCode());
			//			km.addToGraph(z.graphPanel);
			km.readMembrane(z.f_MembraneFile, z.graphPanel);
			if(z.f_ADtableFile!=null) {
				km.readADtable(z.f_ADtableFile);
			}
			z.AnalyzeLabel.setText(L.errfile.getName() +" written!");
			int i=0;
			while(i++<1000){
				sleep(1000);
				z.update(z.getGraphics());
			}
			L.closeFiles();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			z.AnalyzeLabel.setText("******A*Errors: not Built!********");
			try {
				PipedInputStream piErr = new PipedInputStream();
				PipedOutputStream poErr = new PipedOutputStream(piErr);
				e.printStackTrace(new PrintStream(poErr));
				System.setErr(new PrintStream(poErr, true));
				e.printStackTrace();
				e.printStackTrace(new PrintStream(poErr));
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
					L.myErrPrintln(new String(buf, 0, len));
					L.myPrintln(new String(buf, 0, len));
				}

			} catch (Exception ze) {}
		} finally {}
	}

}
