import java.util.*;
import java.awt.*;

public class TableCase implements Include {
    Carte  carte;
    Region regiont[];
    Case   tc[][]   = new Case[MAXH][MAXW];

    int    cfi[][]  = {{3,1,ZOB,ZOB},{ZOB,0,2,ZOB},{ZOB,ZOB,1,3},{0,ZOB,ZOB,2}};
    int    cfe[][]  = {{1,3,ZOB,ZOB},{ZOB,2,0,ZOB},{ZOB,ZOB,3,1},{2,ZOB,ZOB,0}};


    TableCase(Carte carte, Region regiont[]) {
        this.carte  = carte;
        this.regiont = regiont;
    }


    public void repandCarre(int h, int w, int start_carre) {
        String debug = new String("[" + h + "][" + w + "].carre[" + start_carre + "]: ");

        if(tc[h][w].carre[start_carre]==ZOB) {
          System.err.println("[" + h + "][" + w + "].carre[" + start_carre + "]==ZOB.");
          tc[h][w].carre[200000]=56;
          return;
        }

        try {
        // carres internes a la case
        int i = start_carre;
        if((!tc[h][w].frontiere[i])&&(tc[h][w].carre[cfi[start_carre][i]]==ZOB)) {
            tc[h][w].carre[cfi[start_carre][i]] = tc[h][w].carre[start_carre];
            debug = debug + " i0 " + i + " " + cfi[start_carre][i] + ",";
        }
        i = (start_carre+1)%4;
        if((!tc[h][w].frontiere[i])&&(tc[h][w].carre[cfi[start_carre][i]]==ZOB)) {
            tc[h][w].carre[cfi[start_carre][i]] = tc[h][w].carre[start_carre];
            debug = debug + " i1 " + i + " " + cfi[start_carre][i] + ",";
        }

        // carres externes a la case
        if(h>0) {
            i=0;
            if((cfe[start_carre][i]!=ZOB)&&(tc[h-1][w].carre[cfe[start_carre][i]]==ZOB)) {
                tc[h-1][w].carre[cfe[start_carre][i]] = tc[h][w].carre[start_carre];
                debug = debug + " e" + i + ",";
            }
        }
        if(w>0) {
            i=1;
            if((cfe[start_carre][i]!=ZOB)&&(tc[h][w-1].carre[cfe[start_carre][i]]==ZOB)) {
                tc[h][w-1].carre[cfe[start_carre][i]] = tc[h][w].carre[start_carre];
                debug = debug + " e" + i + ",";
            }
        }
        if(h<(MAXH-1)) {
            i=2;
            if((cfe[start_carre][i]!=ZOB)&&(tc[h+1][w].carre[cfe[start_carre][i]]==ZOB)) {
                tc[h+1][w].carre[cfe[start_carre][i]] = tc[h][w].carre[start_carre];
                debug = debug + " e" + i + ",";
            }
        }
        if(w<(MAXW-1)) {
            i=3;
            if((cfe[start_carre][i]!=ZOB)&&(tc[h][w+1].carre[cfe[start_carre][i]]==ZOB)) {
                tc[h][w+1].carre[cfe[start_carre][i]] = tc[h][w].carre[start_carre];
                debug = debug + " e" + i + ",";
            }
        }
        } catch (Exception e) {
            System.out.println(e);
        }
        tc[h][w].carreTraite[start_carre] = true;
        Service.trace(debug);
    }


    public void epandage(int region_id) {
        boolean epandage_pas_fini = true;
        Service.trace("coloriage " + region_id + " ");
        while(epandage_pas_fini) {
            epandage_pas_fini = false;
            for(int h=0;h<MAXH;h++) {
                for(int w=0;w<MAXW;w++) {
                    for(int c=0;c<4;c++) {
                        if((tc[h][w].carre[c]==region_id)&&
                           (!tc[h][w].carreTraite[c])) {
                            repandCarre(h, w, c);
                            epandage_pas_fini = true;
                        }
                    }
                }
            }
            if(epandage_pas_fini) Service.trace("*");
        }
        Service.trace("coloriage " + region_id + " termine!");
    }


    public void resetCarreTraite() {
        for(int h=0;h<MAXH;h++) {
            for(int w=0;w<MAXW;w++) {
                for(int c=0;c<4;c++) {
                    tc[h][w].carreTraite[c] = false;
                }
            }
        }
    }


    public void peint(Graphics g) {
        Dimension d = carte.size();

        for(int h=0; h<MAXH; h++) {
            for(int w=0; w<MAXW; w++) {
                if(tc[h][w]==null) { h=ZOB; break; }
                tc[h][w].peintCase(h, w, d, g, regiont);
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
                try {
                    Service.trace("[" + h + "][" + w + "]: ");
                    zeCase.genereCase(tc, h, w, hasard);
                    carte.updateMap(20+(40*w/MAXW)+(40*h/MAXH));
                    Service.trace("  - " + zeCase.frontiere[0] + zeCase.frontiere[1] + zeCase.frontiere[2] + zeCase.frontiere[3]);
                    tc[h][w] = zeCase;
                } catch(Exception e) {
                    System.err.println("[" + h + "][" + w + "]: " + "  - " + zeCase.frontiere[0] + zeCase.frontiere[1] + zeCase.frontiere[2] + zeCase.frontiere[3]);
                    throw e;
                }
            }
        }
    }
}
