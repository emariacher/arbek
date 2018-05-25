import java.util.*;
import java.awt.*;

public class TableCase implements Include {
	Carte  carte;
	Case   tc[][]   = new Case[MAXH][MAXW];

	int    cfi[][]  = {{3,1,ZOB,ZOB},{ZOB,0,2,ZOB},{ZOB,ZOB,1,3},{0,ZOB,ZOB,2}}; // quadrants/carres dans la même case
	int    cfe[][]  = {{1,3,ZOB,ZOB},{ZOB,2,0,ZOB},{ZOB,ZOB,3,1},{2,ZOB,ZOB,0}}; // quadrants/carres dans les cases adjacentes


	TableCase(Carte carte) {
		this.carte  = carte;
	}


	public void repandCarre(int h, int w, int start_carre) {
		Case root = tc[h][w];
		String debug = new String("[[" + h + "][" + w + "].carre[" + start_carre + "]: " + root.carre[start_carre]+"]");

		// carres internes a la case
		int i = start_carre;
		if((!root.frontiere[i])&&(root.carre[cfi[start_carre][i]]==null)) {
			root.carre[cfi[start_carre][i]] = root.carre[start_carre];
			debug = debug + " i0 " + i + " " + cfi[start_carre][i] + ",";
		}
		i = (start_carre+1)%4;
		if((!root.frontiere[i])&&(root.carre[cfi[start_carre][i]]==null)) {
			root.carre[cfi[start_carre][i]] = root.carre[start_carre];
			debug = debug + " i1 " + i + " " + cfi[start_carre][i] + ",";
		}

		// carres externes a la case
		if(h>0) {
			Case nord = tc[h-1][w];
			if((cfe[start_carre][i]!=ZOB)&&(nord.carre[cfe[start_carre][i]]==null)) {
				nord.carre[cfe[start_carre][i]] = root.carre[start_carre];
				debug = debug + " e nord,";
			}
		}
		if(w>0) {
			Case ouest = tc[h][w-1];
			if((cfe[start_carre][i]!=ZOB)&&(ouest.carre[cfe[start_carre][i]]==null)) {
				ouest.carre[cfe[start_carre][i]] = root.carre[start_carre];
				debug = debug + " e ouest,";
			}
		}
		if(h<(MAXH-1)) {
			Case sud = tc[h+1][w];
			if((cfe[start_carre][i]!=ZOB)&&(sud.carre[cfe[start_carre][i]]==null)) {
				sud.carre[cfe[start_carre][i]] = root.carre[start_carre];
				debug = debug + " e sud,";
			}
		}
		if(w<(MAXW-1)) {
			Case est = tc[h][w+1];
			if((cfe[start_carre][i]!=ZOB)&&(est.carre[cfe[start_carre][i]]==null)) {
				est.carre[cfe[start_carre][i]] = root.carre[start_carre];
				debug = debug + " e est,";
			}
		}
		//		root.carreTraite[start_carre] = true;
		Service.trace(debug);
	}


	public void epandage(Region r) {
		Service.trace("coloriage " + r.toString2() + " ");
		for(int h=0;h<MAXH;h++) {
			for(int w=0;w<MAXW;w++) {
				for(int c=0;c<4;c++) {
					if(tc[h][w].carre[c]==null) {
						continue;
					}
					/*if((tc[h][w].carre[c].equals(r))&&
							(!tc[h][w].carreTraite[c])) {*/
					if(tc[h][w].carre[c].equals(r)) {
						repandCarre(h, w, c);
					}
				}
			}
		}
		Service.trace("coloriage " + r.toString() + " termine!");
	}


/*	public void resetCarreTraite() {
		for(int h=0;h<MAXH;h++) {
			for(int w=0;w<MAXW;w++) {
				for(int c=0;c<4;c++) {
					tc[h][w].carreTraite[c] = false;
				}
			}
		}
	}*/


	public void peint(Graphics g) {
		Dimension d = carte.getSize();

		for(int h=0; h<MAXH; h++) {
			for(int w=0; w<MAXW; w++) {
				if(tc[h][w]==null) { h=ZOB; break; } // ne peint pas les cases non initialisées
				tc[h][w].peintCase(h, w, d, g);
			}
		}
	}


	public void genereTableCase(int seed) throws Exception {
		Random  hasard  = new Random(seed);
		Service.trace("colorie!");

		// reset les carres si necessaire
		for(int h=0; h<MAXH; h++) {
			for(int w=0; w<MAXW; w++) {
				tc[h][w] = null;
			}
		}

		// genere les frontieres
		for(int h=0; h<MAXH; h++) {
			for(int w=0; w<MAXW; w++) {
				Case zeCase = new Case();
				Service.trace("[" + h + "][" + w + "]: ");
				Case nord=null;
				Case ouest=null;
				if(h>0) nord=tc[h-1][w];
				if(w>0) ouest=tc[h][w-1];
				zeCase.genereCase(nord, ouest, h, w, hasard);
				carte.updateMap(20+(40*w/MAXW)+(40*h/MAXH));
				Service.trace("  - " + zeCase.frontiere[0] + zeCase.frontiere[1] + zeCase.frontiere[2] + zeCase.frontiere[3]);
				tc[h][w] = zeCase;
			}
		}
	}
}
