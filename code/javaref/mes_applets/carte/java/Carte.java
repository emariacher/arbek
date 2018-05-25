import java.awt.*;
import java.lang.*;
import java.applet.Applet;
import java.util.*;

public class Carte extends Panel implements Runnable, Include {
    /** Un vieux r&ecirc;ve de dix-huit ans... */
    Colorie colorie;
    Region regiont[]   = new Region[MAXR];      /** @see Region toutes les regions */
    TableCase tableCase   = new TableCase(this, regiont);
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
    int couleurConsommee[] = new int[4];
    final int default_wait = 500;
    boolean unLog = false;


    private Image     offscreen     = null;
    private Dimension offscreensize = null;
    private Graphics  offgraphics   = null;


    Carte(Colorie colorie) {
        this.colorie = colorie;
        Service.trace("init!");
        Date date    = new Date();
        seed         = date.getSeconds() + (date.getMinutes()*60) + (date.getHours()*24*60);
    }



    public String getCarteBackInfo() {
        return(new String("[31m Max back occurences = " + backMax + " seed: " + backOccSeed + "[34m H=" + MAXH + ", W=" + MAXW));
    }


    public void run() {
        try {
            Service.trace("run!");
            while(true) {
                do {
                    System.out.println("[31m seed: " + seed + "[34m H=" + MAXH + ", W=" + MAXW);
                } while(!firstpass(seed++));
                updateMap();
                secondpass();
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
        colorie.thread.sleep(sleep_time);
    }


    public void updateMapNoSuspend() throws Exception {
        Service.trace("updateMapNoSuspend");
        repaint();
        colorie.thread.sleep(default_wait);
    }


    public void updateMap(int sleep_time) throws Exception {
        Service.trace("updateMap");
        if(!colorie.runFreely) colorie.thread.suspend();
        repaint();
        colorie.thread.sleep(sleep_time);
    }


    public void updateMap() throws Exception {
        Service.trace("updateMap");
        if(!colorie.runFreely) colorie.thread.suspend();
        repaint();
        colorie.thread.sleep(default_wait);
    }


    public void update(Graphics g) {
        Service.trace("update");
        Dimension d = size();
        if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
            offscreen     = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics   = offscreen.getGraphics();
            offgraphics.setFont(getFont());
        }
        tableCase.peint(offgraphics);
        g.drawImage(offscreen, 0, 0, null);
    }


    public boolean trouve_1ere_case_non_coloriee(int regiont_index) {
        for(int h=0;h<MAXH;h++) {
            for(int w=0;w<MAXW;w++) {
                for(int c=0;c<4;c++) {
                    if(tableCase.tc[h][w].carre[c]==ZOB) {
                        regiont[regiont_index] = new Region(ZOB, h, w, c);
                        tableCase.tc[h][w].carre[c]   = regiont_index;
                        Service.trace(0,"[" + h + "][" + w + "].carre[" + c + "]: " + regiont_index);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void trouveVoisins(int regiont_index) {
        for(int h=0;h<MAXH;h++) {
            for(int w=0;w<MAXW;w++) {
                for(int c=0;c<4;c++) {
                    if(tableCase.tc[h][w].carre[c]==regiont_index) {
                        tableCase.tc[h][w].carreTraite[c] = true;
                        /* cherche les frontieres de ce carre et ajoute le voisin si besoin est */
                        Service.trace(0,"  [" + h + "][" + w + "].carre[" + c + "]:");
                        if(tableCase.tc[h][w].frontiere[c]) {
                            if(regiont_index!=tableCase.tc[h][w].carre[(c+3)%4]) {
                                regiont[regiont_index].addVoisin(tableCase.tc[h][w].carre[(c+3)%4]);
                                Service.trace("    +3[" + h + "][" + w + "].frontiere[" + c + "]: " +
                                                    tableCase.tc[h][w].carre[(c+3)%4] + " add!");
                            } else { /* si c'est la meme region sucre la frontiere */
                                tableCase.tc[h][w].frontiere[c] = false;
                                Service.trace(0,"    +3[" + h + "][" + w + "].frontiere[" + c + "]: " +
                                                    tableCase.tc[h][w].carre[c] + " sucree!");
                            }
                        }
                        if(tableCase.tc[h][w].frontiere[(c+1)%4]) {
                            if(regiont_index!=tableCase.tc[h][w].carre[(c+1)%4]) {
                                regiont[regiont_index].addVoisin(tableCase.tc[h][w].carre[(c+1)%4]);
                                Service.trace(0,"    +1[" + h + "][" + w + "].frontiere[" + ((c+1)%4) + "]: " +
                                                    tableCase.tc[h][w].carre[(c+1)%4] + " add!");
                            } else { /* si c'est la meme region sucre la frontiere */
                                tableCase.tc[h][w].frontiere[(c+1)%4] = false;
                                Service.trace(0,"    +1[" + h + "][" + w + "].frontiere[" + ((c+1)%4) + "]: " +
                                                    tableCase.tc[h][w].carre[c] + " sucree!");
                            }
                        }
                    }
                }
            }
        }
        regiont[regiont_index].addVoisin(regiont_index);
        regiont[regiont_index].voisin.removeElement(new Integer(regiont_index)); /* add only to vqp */
        Service.trace(0,"regiont[" + regiont_index + "].voisin: " + regiont[regiont_index].voisin.toString());
    }


    public boolean firstpass(int seed) throws Exception {
        Random  hasard  = new Random(seed);
        mainForTrace = "colorie! ";

        tableCase.genereTableCase(seed);

        // reset les regions
        nbre_quarte    = 0;
        nbre_regiontqp = 0;
        nbre_region    = 0;
        logIndex       = 0;
        for(int i=0; i<MAXR; i++) {
            Service.trace(0,i + " ");
            regiont[i]   = null;
            log[i]       = null;
            regiontqp[i] = ZOB;
            for(int j=0; j<4; j++) quartet[i][j] = ZOB;
        }


        mainForTrace = mainForTrace + "1 ";
        while(trouve_1ere_case_non_coloriee(nbre_region)) {
            tableCase.epandage(nbre_region);
            nbre_region++;
        }

        mainForTrace = mainForTrace + "2 ";
        tableCase.resetCarreTraite();
        for(int i=0; i<nbre_region; i++) trouveVoisins(i);
        for(int i=0; i<nbre_region; i++) regiont[i].removeVqp(ZOB);

        boolean faitQuelquechose = true;
        while(faitQuelquechose) {
            faitQuelquechose = false;
            for(int j=0; j<nbre_region; j++) {
                if(regiont[j].vqp==null) {
                    for(int k=0; k<nbre_region; k++) {
                        if(regiont[k].removeVqp(j)) faitQuelquechose = true;
                    }
                }
            }
        }


        mainForTrace = mainForTrace + "3 ";
        // for(int i=0; i<nbre_region; i++) Service.trace(regiont[i].toString(i));
        boolean auMoins1QuartePotentiel = false;
        for(int i=0; i<nbre_region; i++) {
            if(regiont[i].vqp!=null) auMoins1QuartePotentiel = true;
        }


        mainForTrace = mainForTrace + "4 ";
        if(auMoins1QuartePotentiel) {
            for(int i=0; i<nbre_region; i++) {
                if(regiont[i].vqp!=null) {
                    regiontqp[nbre_regiontqp++] = i;
                    Service.trace(regiont[i].toString(i,false));
                }
            }
        }
        return auMoins1QuartePotentiel;
    }


    public boolean trouveQuarte(int ri1, int ri2, int ri3, int ri4) {
        int cpt=0;

        // System.out.print("(" + ri1 + "," +  ri2 + "," +  ri3 + "," +  ri4 + "): ");
        for(int i=0; i<nbre_regiontqp; i++) {
            if((regiont[regiontqp[i]].vqp.contains(new Integer(ri1)))&&
               (regiont[regiontqp[i]].vqp.contains(new Integer(ri2)))&&
               (regiont[regiontqp[i]].vqp.contains(new Integer(ri3)))&&
               (regiont[regiontqp[i]].vqp.contains(new Integer(ri4)))) {
                quarte[cpt++] = regiontqp[i];
                // System.out.print(regiontqp[i] + ", ");
            }
        }
        if(cpt>=4) return true;
        else       return false;
    }


    public boolean trouveLesQuarte() {
        boolean found = false;
        for(int i=0; i<nbre_regiontqp; i++) {
            for(int j=0; j<(regiont[regiontqp[i]].vqp.size()-3); j++) {
                if(((Integer)regiont[regiontqp[i]].vqp.elementAt(j)).intValue()>=regiontqp[i]) {
                    for(int k=(j+1); k<(regiont[regiontqp[i]].vqp.size()-2); k++) {
                        for(int l=(k+1); l<(regiont[regiontqp[i]].vqp.size()-1); l++) {
                            for(int m=(l+1); m<regiont[regiontqp[i]].vqp.size(); m++) {
                                if(trouveQuarte(((Integer)regiont[regiontqp[i]].vqp.elementAt(j)).intValue(),
                                                ((Integer)regiont[regiontqp[i]].vqp.elementAt(k)).intValue(),
                                                ((Integer)regiont[regiontqp[i]].vqp.elementAt(l)).intValue(),
                                                ((Integer)regiont[regiontqp[i]].vqp.elementAt(m)).intValue())) {
                                    for(int u=0; u<4; u++) {
                                        quartet[nbre_quarte][u] = quarte[u];
                                        regiont[quarte[u]].quarteCtr++;
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
    }

    public void updateQuarteVoisinCtr() {
        for(int i=0; i<nbre_region; i++) {
            for(int j=0; j<regiont[i].voisin.size(); j++) {
                 boolean found = false;
                 for(int k=0; k<nbre_regiontqp; k++) {
                     if(regiont[regiontqp[k]].vqp.contains(regiont[i].voisin.elementAt(j))) found = true;
                 }
                 if(found) regiont[i].quarteVoisinCtr++;
            }
        }
    }


    public int donneCouleursPossibles() {
        for(int k=0; k<4; k++) {
            couleurPossibles[k] = ZOB;
        }
        int nbreCouleursDifferentes = 0;
        forTrace = forTrace + "\n   couleurConsommees: ";
        for(int k=0; k<4; k++) {
           forTrace = forTrace + " [" + k + ": " + couleurConsommee[k] + "]";
           if(couleurConsommee[k] == 1) {
               couleurPossibles[nbreCouleursDifferentes] = k;
               // forTrace = forTrace + " [30m couleurPossibles[" + nbreCouleursDifferentes + "]=" + couleurPossibles[nbreCouleursDifferentes] + " [34m";
               nbreCouleursDifferentes++;
           }
        }
        nbreCouleursDifferentes = 4 - nbreCouleursDifferentes;
        return nbreCouleursDifferentes;
    }


    public int donneCouleursVoisins(int i) {
        int nbreCouleurVoisins = 0;
        for(int j=0; j<4; j++) couleurConsommee[j] = 1;
        for(int j=0; j<regiont[i].voisin.size(); j++) {
            if(regiont[((Integer)regiont[i].voisin.elementAt(j)).intValue()].couleur!=ZOB) {
                couleurConsommee[regiont[((Integer)regiont[i].voisin.elementAt(j)).intValue()].couleur] = ZOB;
                forTrace = forTrace + " [" + j + ": " + regiont[((Integer)regiont[i].voisin.elementAt(j)).intValue()].couleur + "]";
            }
        }
        return nbreCouleurVoisins;
    }


    public void unLog() throws Exception {
        if(logIndex==0) return;
        Service.trace(0,"GOING BACK! " + log[logIndex].toString(logIndex));
        regiont[log[logIndex].regionIndex].couleur = 9;
        updateMapNoSuspend();
        regiont[log[logIndex].regionIndex].couleur = ZOB;
        updateMap();
        log[logIndex].Reset();
    }


    public int trouveRegionSansChoix(int maxNbreCouleurVoisins, int findMax) throws Exception {
        /** <OL>
        *   <LI> Trouve et colorie les r&eacute;gions dont le nombre de voisins est &eacute;gal &agrave; 3.
        *   <LI> Trouve le nombre maximum de voisisns pour une r&eacute;gion non colori&eacute;e.
        *   <LI> Colorie la r&eacute;gion dont l'index est &eacute;gal &agrave; <pre>maxNbreCouleurVoisins</pre>.
        *   </OL>
        *   <P>S'occupe aussi des "culs de sac".
        */
        int maxQuarteCtr       = -1;
        int maxQuarteVoisinCtr = -1;
        int regionIndex        = ZOB;
        mainForTrace = mainForTrace + findMax;
        if(logIndex>=logIndexLimit) return 998;
        for(int i=0; i<nbre_region; i++) {
            if(regiont[i].couleur==ZOB) {
                if(unLog) return 997;
                forTrace = "couleurs des voisins: ";

                Service.trace("*****" + i + "***** " + maxNbreCouleurVoisins + ", " + findMax);
                Service.trace(regiont[i].toString(i,true));
                int nbreCouleurVoisins = donneCouleursVoisins(i);

                Service.trace("  nbreCouleurVoisins=" + nbreCouleurVoisins);
                int nbreCouleursDifferentes = donneCouleursPossibles();

                forTrace = forTrace + "\n   nbreCouleursDifferentes=" + nbreCouleursDifferentes;
                if((nbreCouleursDifferentes == 3)||((i==maxNbreCouleurVoisins)&&(findMax==2))) {
                    log[logIndex] = new Log(i, couleurPossibles);
                    regiont[i].couleur = log[logIndex].nextCouleur();
                    Service.trace("[30m !" + findMax + "! " + regiont[i].toString(i,false) + "[34m");
                    Service.trace(0,log[logIndex].toString(logIndex));
                    logIndex++;
                    updateMap();
                    if(logIndex>=logIndexLimit) Service.trace(forTrace);
                    return 999;
                } else if(nbreCouleursDifferentes > 3) {
                    backOcc++;
                    System.out.print(backOcc + " GOING BACK ");
                    regiont[i].couleur = 9;
                    updateMapNoSuspend();
                    regiont[i].couleur = ZOB;
                    updateMap();
                    Service.trace(0,"[31m" + regiont[i].toString(i,false) + " a des voisins de " + nbreCouleursDifferentes + " couleurs differentes. [34m \n" + forTrace);
                    int back_cpt = 0;
                    logIndex--;
                    while(!log[logIndex].choix) {
                        if(unLog) return 997;
                        unLog();
                        logIndex--;
                        back_cpt++;
                    }
                    System.out.println(back_cpt + " STEPS!");
                    regiont[log[logIndex].regionIndex].couleur = log[logIndex].nextCouleur();
                    updateMap();
                    Service.trace(0,"GOING BACK! NEXT COULEUR: " + regiont[log[logIndex].regionIndex].toString(log[logIndex].regionIndex,false));
                    Service.trace(0,"     " + log[logIndex].toString(logIndex));
                    logIndex++;
                    return 999;
                }
                if(findMax!=0) {
                    if(nbreCouleursDifferentes==maxNbreCouleurVoisins) {
                        if(regiont[i].quarteCtr>maxQuarteCtr) {
                            maxQuarteCtr = regiont[i].quarteCtr;
                            maxQuarteVoisinCtr = regiont[i].quarteVoisinCtr;
                            regionIndex  = i;
                        } else if((regiont[i].quarteCtr==maxQuarteCtr)&&(regiont[i].quarteVoisinCtr>=maxQuarteVoisinCtr)) {
                            maxQuarteVoisinCtr = regiont[i].quarteVoisinCtr;
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

        trouveLesQuarte();
        updateQuarteVoisinCtr();

        // colorie le 1er quarte
        if(nbre_quarte>0) {
            for(int i=0; i<4; i++) {
                for(int j=0; j<5; j++) couleurVoisins[j] = ZOB;
                couleurVoisins[0] = i;
                log[logIndex] = new Log(quartet[0][i], couleurVoisins);
                regiont[quartet[0][i]].couleur = log[logIndex].nextCouleur();
                updateMap();
                Service.trace(log[logIndex].toString(logIndex));
                logIndex++;
            }
        } else {
            for(int j=0; j<4; j++) couleurVoisins[j] = j;
            log[logIndex] = new Log(0, couleurVoisins);
            regiont[0].couleur = log[logIndex].nextCouleur();
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
            System.out.println("[31m Max back occurences = " + backMax + " seed: " + backOccSeed + "[34m H=" + MAXH + ", W=" + MAXW);

        }

        return true;
    }
}
