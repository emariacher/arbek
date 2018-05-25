import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


@SuppressWarnings("serial")
public class Carte extends Panel implements Runnable, Include {
	/** Un vieux r&ecirc;ve de dix-huit ans... */
	Colorie colorie;
	List<Region> regiont;      /** @see Region toutes les regions */
	TableCase tableCase;
	Log    log[]       = new Log[MAXR];         // logging
	int    regiontqp[] = new int[MAXR];         // les regions faisant potentiellement partie d'un quarte
	int    quarte[]    = new int[4];            // les quarte
	int    quartet[][] = new int[MAXR][4];      // les quartes ( r0, r1, r2, r3)
	int    nbre_quarte        = 0;
	int    nbre_regiontqp     = 0;
	int    nbre_region        = 0;
	int    logIndex           = 0;
	int    backMax            = 0;
	int    backOcc            = 0;
	int    backOccSeed        = 0;
	int    logIndexLimit      = ZOB;
	int    seed;
	String forTrace        = new String();
	String mainForTrace    = new String();
	int couleurVoisins[]   = new int[5];
	int couleurPossibles[] = new int[4];
	final int default_wait = 500;
	boolean unLog = false;


	private Image     offscreen     = null;
	private Dimension offscreensize = null;
	private Graphics  offgraphics   = null;
	public boolean threadSuspended;


	Carte(Colorie colorie) {
		this.colorie = colorie;
		Service.trace("init!");
		Calendar date    = Calendar.getInstance();
		seed         = date.get(Calendar.SECOND) + (date.get(Calendar.MINUTE)*60) + (date.get(Calendar.HOUR)*24*60);
	}



	public String getCarteBackInfo() {
		return(new String("Max back occurences = " + backMax + " seed: " + backOccSeed + " H=" + MAXH + ", W=" + MAXW));
	}


	public void run() {
		try {
			Service.trace("run!");
			while(true) {
				do {
					System.out.println("***********************seed: " + seed + " H=" + MAXH + ", W=" + MAXW);
				} while(!firstpass(seed++));
				updateMap();
//				secondpass();
				updateMap(1000 + (backOcc*1000));
			}
		} catch(Exception e) {
			System.err.println(mainForTrace);
			e.printStackTrace();
			// throw new Exception("AOXOMOXOA");
		}
	}


	public void updateMapNoSuspend(int sleep_time) throws Exception {
		Service.trace("updateMapNoSuspend");
		repaint();
		Thread.sleep(sleep_time);
	}


	public void updateMapNoSuspend() throws Exception {
		Service.trace("updateMapNoSuspend");
		repaint();
		Thread.sleep(default_wait);
	}


	public void updateMap(int sleep_time) throws Exception {
		Service.trace("updateMap");
		if(!colorie.runFreely) {
			//colorie.thread.suspend();
			threadSuspended = !threadSuspended;

			if (!threadSuspended)
				notify();

		}
		repaint();
		Thread.sleep(sleep_time);
	}


	public void updateMap() throws Exception {
		Service.trace("updateMap");
		if(!colorie.runFreely) {
//			colorie.thread.suspend();
			threadSuspended = !threadSuspended;

			if (!threadSuspended)
				notify();

		}
		repaint();
		Thread.sleep(default_wait);
	}


	public void update(Graphics g) {
		Service.trace("update");
		Dimension d = getSize();
		if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
			offscreen     = createImage(d.width, d.height);
			offscreensize = d;
			offgraphics   = offscreen.getGraphics();
			offgraphics.setFont(getFont());
		}
		tableCase.peint(offgraphics);
		g.drawImage(offscreen, 0, 0, null);
	}


	void colorieTable() {
		for(int h=0;h<MAXH;h++) {
			for(int w=0;w<MAXW;w++) {
				for(int c=0;c<4;c++) {
					if(tableCase.tc[h][w].carre[c]==null) {
						Region r = new Region(nbre_region, h, w, c);
						regiont.add(r);
						tableCase.tc[h][w].carre[c]   = r;
						tableCase.epandage(r);
						nbre_region++;
					}
				}
			}
		}
	}


	public void trouveVoisins() {
		Iterator<Region> ir=regiont.iterator();
		while(ir.hasNext()){
			Region r = ir.next();
			for(int h=0;h<MAXH;h++) {
				for(int w=0;w<MAXW;w++) {
					for(int c=0;c<4;c++) {
						Case zeCase = tableCase.tc[h][w];
						if(zeCase.carre[c].equals(r)) {
//							zeCase.carreTraite[c] = true;
							/* cherche les frontieres de ce carre et ajoute le voisin si besoin est */
							Service.trace(2,"  [" + h + "][" + w + "].carre[" + c + "]:");
							if(zeCase.frontiere[c]) {
								if(!zeCase.carre[(c+3)%4].equals(r)) {
									//                                regiont[regiont_index].addVoisin(zeCase.carre[(c+3)%4]);
									r.addVoisin(zeCase.carre[(c+3)%4]);
									Service.trace(1,"    +3[" + h + "][" + w + "].frontiere[" + c + "]: " +
											zeCase.carre[(c+3)%4] + " add!");
								} else { /* si c'est la meme region sucre la frontiere */
									zeCase.frontiere[c] = false;
									Service.trace(1,"    +3[" + h + "][" + w + "].frontiere[" + c + "]: " +
											zeCase.carre[c] + " sucree!");
								}
							}
							if(zeCase.frontiere[(c+1)%4]) {
								if(!zeCase.carre[(c+1)%4].equals(r)) {
									r.addVoisin(zeCase.carre[(c+1)%4]);
									Service.trace(1,"    +1[" + h + "][" + w + "].frontiere[" + ((c+1)%4) + "]: " +
											zeCase.carre[(c+1)%4] + " add!");
								} else { /* si c'est la meme region sucre la frontiere */
									zeCase.frontiere[(c+1)%4] = false;
									Service.trace(1,"    +1[" + h + "][" + w + "].frontiere[" + ((c+1)%4) + "]: " +
											zeCase.carre[c] + " sucree!");
								}
							}
						}
					}
				}
			}
			r.addVoisin(r);
			r.voisin.remove(r); /* add only to vqp */
			Service.trace(0,"regiont[" + r.toString() + "].voisin: " + r.voisin.toString());
		}
	}


	public boolean firstpass(int seed) throws Exception {
		mainForTrace = "colorie! ";

		tableCase   = new TableCase(this);
		tableCase.genereTableCase(seed);
		regiont   = new ArrayList<Region>();

		// reset les regions
		nbre_quarte    = 0;
		nbre_regiontqp = 0;
		nbre_region    = 0;
		logIndex       = 0;
		for(int i=0; i<MAXR; i++) {
			Service.trace(0,i + " ");
			//            regiont.set(i,null);
			log[i]       = null;
			regiontqp[i] = ZOB;
			for(int j=0; j<4; j++) quartet[i][j] = ZOB;
		}


		mainForTrace = mainForTrace + "1 ";
		colorieTable();


		mainForTrace = mainForTrace + "2 ";
//		tableCase.resetCarreTraite();
//		trouveVoisins();

		/*boolean faitQuelquechose = true;
		while(faitQuelquechose) {
			faitQuelquechose = false;
			for(int j=0; j<nbre_region; j++) {
				if(regiont.get(j).vqp==null) {
					for(int k=0; k<nbre_region; k++) {
						if(regiont.get(k).removeVqp(j)) faitQuelquechose = true;
					}
				}
			}
		}*/


		mainForTrace = mainForTrace + "3 ";
		// for(int i=0; i<nbre_region; i++) Service.trace(regiont[i].toString(i));
		boolean auMoins1QuartePotentiel = false;
		for(int i=0; i<nbre_region; i++) {
			if(regiont.get(i).vqp!=null) auMoins1QuartePotentiel = true;
		}


		mainForTrace = mainForTrace + "4 ";
		if(auMoins1QuartePotentiel) {
			for(int i=0; i<nbre_region; i++) {
				if(regiont.get(i).vqp!=null) {
					regiontqp[nbre_regiontqp++] = i;
					Service.trace(regiont.get(i).toString(i,false));
				}
			}
		}
		updateMapNoSuspend(10000);
//		System.exit(8888);
		return auMoins1QuartePotentiel;
	}


	public boolean trouveQuarte(int ri1, int ri2, int ri3, int ri4) {
		int cpt=0;

		// System.out.print("(" + ri1 + "," +  ri2 + "," +  ri3 + "," +  ri4 + "): ");
		for(int i=0; i<nbre_regiontqp; i++) {
			if((regiont.get(regiontqp[i]).vqp.contains(new Integer(ri1)))&&
					(regiont.get(regiontqp[i]).vqp.contains(new Integer(ri2)))&&
					(regiont.get(regiontqp[i]).vqp.contains(new Integer(ri3)))&&
					(regiont.get(regiontqp[i]).vqp.contains(new Integer(ri4)))) {
				quarte[cpt++] = regiontqp[i];
				// System.out.print(regiontqp[i] + ", ");
			}
		}
		if(cpt>=4) return true;
		else       return false;
	}


	/*public boolean trouveLesQuarte() {
		boolean found = false;
		for(int i=0; i<nbre_regiontqp; i++) {
			for(int j=0; j<(regiont.get(regiontqp[i]).vqp.size()-3); j++) {
				if(((Integer)regiont.get(regiontqp[i]).vqp.elementAt(j)).intValue()>=regiontqp[i]) {
					for(int k=(j+1); k<(regiont.get(regiontqp[i]).vqp.size()-2); k++) {
						for(int l=(k+1); l<(regiont.get(regiontqp[i]).vqp.size()-1); l++) {
							for(int m=(l+1); m<regiont.get(regiontqp[i]).vqp.size(); m++) {
								if(trouveQuarte(((Integer)regiont.get(regiontqp[i]).vqp.elementAt(j)).intValue(),
										((Integer)regiont.get(regiontqp[i]).vqp.elementAt(k)).intValue(),
										((Integer)regiont.get(regiontqp[i]).vqp.elementAt(l)).intValue(),
										((Integer)regiont.get(regiontqp[i]).vqp.elementAt(m)).intValue())) {
									for(int u=0; u<4; u++) {
										quartet[nbre_quarte][u] = quarte[u];
										regiont.get(quarte[u]).quarteCtr++;
									}
									Service.trace(0,nbre_quarte + " (" + quartet[nbre_quarte][0] + "," +
											quartet[nbre_quarte][1] + "," + quartet[nbre_quarte][2] + "," +
											quartet[nbre_quarte][3] + ")");
									found = true;
									nbre_quarte++;
								}
							}
						}
					}
				}
			}
		}
		return found;
	}*/

/*	public void updateQuarteVoisinCtr() {
		for(int i=0; i<nbre_region; i++) {
			for(int j=0; j<regiont.get(i).voisin.size(); j++) {
				boolean found = false;
				for(int k=0; k<nbre_regiontqp; k++) {
					if(regiont.get(regiontqp[k]).vqp.contains(regiont.get(i).voisin.elementAt(j))) found = true;
				}
				if(found) regiont.get(i).quarteVoisinCtr++;
			}
		}
	}*/



	public void unLog() throws Exception {
		if(logIndex==0) return;
		Service.trace(0,"GOING BACK! " + log[logIndex].toString(logIndex));
		regiont.get(log[logIndex].regionIndex).couleur = 9;
		updateMapNoSuspend();
		regiont.get(log[logIndex].regionIndex).couleur = ZOB;
		updateMap();
		log[logIndex].Reset();
	}


	public int trouveRegionSansChoix(int maxNbreCouleurVoisins, int findMax) throws Exception {
		/** <OL>
		 *   <LI> Trouve et colorie les régions dont le nombre de voisins est égal &agrave; 3.
		 *   <LI> Trouve le nombre maximum de voisisns pour une région non coloriée.
		 *   <LI> Colorie la région dont l'index est égal &agrave; <pre>maxNbreCouleurVoisins</pre>.
		 *   </OL>
		 *   <P>S'occupe aussi des "culs de sac".
		 */
		int maxQuarteCtr       = -1;
		int maxQuarteVoisinCtr = -1;
		int regionIndex        = ZOB;
		mainForTrace = mainForTrace + findMax;
		if(logIndex>=logIndexLimit) return 998;
		for(int i=0; i<nbre_region; i++) {
			if(regiont.get(i).couleur==ZOB) {
				if(unLog) return 997;
				forTrace = "couleurs des voisins: ";

				Service.trace("*****" + i + "***** " + maxNbreCouleurVoisins + ", " + findMax);
				Service.trace(regiont.get(i).toString(i,true));
				int nbreCouleurVoisins = regiont.get(i).donneCouleursVoisins();

				Service.trace("  nbreCouleurVoisins=" + nbreCouleurVoisins);
				int nbreCouleursDifferentes = 4-nbreCouleurVoisins;

				forTrace = forTrace + "\n   nbreCouleursDifferentes=" + nbreCouleursDifferentes;
				if((nbreCouleursDifferentes == 3)||((i==maxNbreCouleurVoisins)&&(findMax==2))) {
					log[logIndex] = new Log(i, couleurPossibles);
					regiont.get(i).couleur = log[logIndex].nextCouleur();
					Service.trace("[30m !" + findMax + "! " + regiont.get(i).toString(i,false) + "[34m");
					Service.trace(0,log[logIndex].toString(logIndex));
					logIndex++;
					updateMap();
					if(logIndex>=logIndexLimit) Service.trace(forTrace);
					return 999;
				} else if(nbreCouleursDifferentes > 3) {
					backOcc++;
					System.out.print(backOcc + " GOING BACK ");
					regiont.get(i).couleur = 9;
					updateMapNoSuspend();
					regiont.get(i).couleur = ZOB;
					updateMap();
					Service.trace(0,regiont.get(i).toString(i,false) + " a des voisins de " + nbreCouleursDifferentes + " couleurs differentes.\n" + forTrace);
					int back_cpt = 0;
					logIndex--;
					while(!log[logIndex].choix) {
						if(unLog) return 997;
						unLog();
						logIndex--;
						back_cpt++;
					}
					System.out.println(back_cpt + " STEPS!");
					regiont.get(log[logIndex].regionIndex).couleur = log[logIndex].nextCouleur();
					updateMap();
					Service.trace(0,"GOING BACK! NEXT COULEUR: " + regiont.get(log[logIndex].regionIndex).toString(log[logIndex].regionIndex,false));
					Service.trace(0,"     " + log[logIndex].toString(logIndex));
					logIndex++;
					return 999;
				}
				if(findMax!=0) {
					if(nbreCouleursDifferentes==maxNbreCouleurVoisins) {
						if(regiont.get(i).quarteCtr>maxQuarteCtr) {
							maxQuarteCtr = regiont.get(i).quarteCtr;
							maxQuarteVoisinCtr = regiont.get(i).quarteVoisinCtr;
							regionIndex  = i;
						} else if((regiont.get(i).quarteCtr==maxQuarteCtr)&&(regiont.get(i).quarteVoisinCtr>=maxQuarteVoisinCtr)) {
							maxQuarteVoisinCtr = regiont.get(i).quarteVoisinCtr;
							regionIndex  = i;
						}
					}
				} else maxNbreCouleurVoisins = Math.max(nbreCouleursDifferentes, maxNbreCouleurVoisins);
			}
		}
		Service.trace(" " + maxNbreCouleurVoisins);
		if(findMax==0) return maxNbreCouleurVoisins;
		else           return regionIndex;
	}


	public boolean secondpass() throws Exception {
		int maxNbreCouleurVoisins = 1;
		int region_index;
		backOcc = 0;

//		trouveLesQuarte();
//		updateQuarteVoisinCtr();

		// colorie le 1er quarte
		if(nbre_quarte>0) {
			for(int i=0; i<4; i++) {
				for(int j=0; j<5; j++) couleurVoisins[j] = ZOB;
				couleurVoisins[0] = i;
				log[logIndex] = new Log(quartet[0][i], couleurVoisins);
				regiont.get(quartet[0][i]).couleur = log[logIndex].nextCouleur();
				updateMap();
				Service.trace(log[logIndex].toString(logIndex));
				logIndex++;
			}
		} else {
			for(int j=0; j<4; j++) couleurVoisins[j] = j;
			log[logIndex] = new Log(0, couleurVoisins);
			regiont.get(0).couleur = log[logIndex].nextCouleur();
			Service.trace(log[logIndex].toString(logIndex));
			logIndex++;
		}

		mainForTrace = mainForTrace + "5 ";
		// colorie la suite
		while(maxNbreCouleurVoisins>0) {
			int compteur = 0;
			while((maxNbreCouleurVoisins=trouveRegionSansChoix(0, 0))==3) {
				Service.trace(0,"***** maxNbreCouleurVoisins=" + maxNbreCouleurVoisins + " (" + compteur + ").");
				if(compteur++>=10) break;
			}
			if(compteur>=10) break;
			if(maxNbreCouleurVoisins==999) continue;
			if(maxNbreCouleurVoisins==998) break;
			if(unLog) continue;
			Service.trace(0,"***** plus de regions avec 3 voisins");
			region_index=trouveRegionSansChoix(maxNbreCouleurVoisins, 1);
			if(region_index==999) continue;
			if(region_index==998) break;
			if(unLog) continue;
			Service.trace(0,"***** " + region_index + " a " + maxNbreCouleurVoisins + " NbreCouleurVoisins.");
			int rc = trouveRegionSansChoix(region_index, 2);
			if(rc==999) continue;
			if(rc==998) break;
			if(unLog) continue;
			Service.trace(0,"***** " + region_index + " vient d\'etre coloriee.");
		}
		if(backOcc>backMax) {
			backMax     = backOcc;
			backOccSeed = seed-1;
			System.out.println("Max back occurences = " + backMax + " seed: " + backOccSeed + " H=" + MAXH + ", W=" + MAXW);

		}

		return true;
	}
}
