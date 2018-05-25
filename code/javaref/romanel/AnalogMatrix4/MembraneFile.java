import java.io.IOException;


public class MembraneFile {
	Log L;
	GraphPanel gp;
	private Log.MyFile csvfile_membraneOut;

	public MembraneFile(Log l, GraphPanel graphPanel, int i_seed) throws IOException, Exception {
		L = l;
		this.gp = graphPanel;
		csvfile_membraneOut = L.getMyFile(gp.main.f_MembraneFile.getCanonicalPath() + 
				"_"+i_seed + "_.csv");
		for (int i = 0 ; i < gp.nedges ; i++) {
			csvfile_membraneOut.writeFile(gp.edges[i].toStringCsv()+"\n");
		}
		csvfile_membraneOut.clean();
	}

	public MembraneFile(Log l, GraphPanel graphPanel, double d_percent) throws IOException, Exception {
		L = l;
		this.gp = graphPanel;
		csvfile_membraneOut = L.getMyFile(gp.main.f_MembraneFile.getCanonicalPath() + 
				"_"+L.printToday("ddMMMyy_HH_mm_")+String.format("%1$6.3f",d_percent) + "_.csv");
		for (int i = 0 ; i < gp.nedges ; i++) {
			csvfile_membraneOut.writeFile(gp.edges[i].toStringCsv()+"\n");
		}
		csvfile_membraneOut.clean();
	}

	
}
