import java.util.*;

public class Region implements Include {
	int couleur=ZOB;          // couleur
	int hcase;            // case_start h
	int wcase;            // case_start w
	int ccase;            // carre_start in case_start
	List<Region> voisin; // liste des voisins
	List<Region> vqp; // liste des voisins interessants (quartes potentiels)
	int quarteCtr;
	int quarteVoisinCtr;
	int id=ZOB;


	Region(int id, int hcase, int wcase, int ccase) {
		this.id=id;
		this.hcase      = hcase;
		this.wcase      = wcase;
		this.ccase      = ccase;
		voisin          = new ArrayList<Region>();
		vqp             = new ArrayList<Region>();
		quarteCtr       = 0;
		quarteVoisinCtr = 0;
	}


	public String toString(int i, boolean verbose) {
		String zurg = new String();
		if(verbose) zurg = "regiont[" + i + "]: " + couleur + ", Voisins: " + voisin.toString() + ", vqp: " + vqp.toString() +
		", present dans " + quarteCtr + " quartes, et voisin " + quarteVoisinCtr + " fois de quartes.";
		else        zurg = "regiont[" + i + "]: " + couleur + ", vqp: " + vqp.toString() +
		", present dans " + quarteCtr + " quartes, et voisin " + quarteVoisinCtr + " fois de quartes.";
		return zurg;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("regiont[id:" +id+"][h:"+ hcase+",w:"+wcase+",c:"+ccase + "]: " + couleur);
		//		sb.append(", Voisins: " + voisin.toString() + ", vqp: " + vqp.toString());
		//		sb.append(", present dans " + quarteCtr + " quartes, et voisin " + quarteVoisinCtr + " fois de quartes.");
		return sb.toString();
	}
	
	public String toString2() {
		StringBuffer sb = new StringBuffer();
		sb.append("[regiont_id:" +id+"]");
		//		sb.append(", Voisins: " + voisin.toString() + ", vqp: " + vqp.toString());
		//		sb.append(", present dans " + quarteCtr + " quartes, et voisin " + quarteVoisinCtr + " fois de quartes.");
		return sb.toString();
	}

	public void addVoisin(Region r) {
		/* cherche si le voisin est deja reference et ajoute le si necessaire */

		if((!voisin.contains(r))&&(!equals(r))) {
			voisin.add(r);
			vqp.add(r);
			System.out.print(r.toString() + ", ");
		}
	}

	public int donneCouleursVoisins() {
		boolean couleurConsommee[] = new boolean[4];
		for(int j=0; j<4; j++) couleurConsommee[j] = false;

		Iterator<Region> irv=voisin.iterator();
		while(irv.hasNext()){
			Region v = irv.next();
			couleurConsommee[v.couleur]=true;
		}
		int nbreCouleurVoisins = 0;
		for(int j=0; j<4; j++) {
			if(couleurConsommee[j]) {
				nbreCouleurVoisins++;
			}
		}
		return nbreCouleurVoisins;
	}


}
