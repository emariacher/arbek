import java.util.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public abstract class Tableau implements Runnable, BInclude {
    int    w,h;             // dimensions
    Vector obstacle = null;
    Vector bille    = null;
    Thread thread;
    SimpleDateFormat formatter = new SimpleDateFormat(" MM|dd HH:mm:ss:SSSS ");
    int boumBille, prevBoumBille, boumObstacle;
    double incrementTemps = 1;
    boolean bascule = true;
    // Sort sort = new Sort();

    Tableau(int w, int h, double dynamicitePlancher) {
        this.w = w;
        this.h = h;
        thread = new Thread(this);

        obstacle = new Vector();
        bille    = new Vector();

        // Rectangular(double x, double y, double w, double h)
        // Obstacle(Rectangular r, double dynamicite)
        // Bille(int rayon, Rectangular rect, Color couleur, double dynamicite)

        // 4 murs entourant le tout
        obstacle.addElement(new Obstacle(new Rectangular(w-MURDENCEINTE,     0,   0,   h), 0.90)); // vertical
        obstacle.addElement(new Obstacle(new Rectangular(MURDENCEINTE,       0,   0,   h), 0.90)); // vertical   ralentit (a droite)
        obstacle.addElement(new Obstacle(new Rectangular(0,       MURDENCEINTE,   w,   0), 0.00)); // horizontal ralentit/annule (en haut)
        obstacle.addElement(new Obstacle(new Rectangular(0,     h-MURDENCEINTE,   w,   0), dynamicitePlancher)); // horizontal plancher
    }

    public void nextTableau() {
        boumBille = 0;
        boumObstacle = 0;

        if(bascule) {
            for(int bi = 0 ; bi < bille.size(); bi++) {
                for(int bii = bi+1 ; bii < bille.size(); bii++) {
                    if(((Bille)bille.elementAt(bi)).choc((Bille)bille.elementAt(bii))) boumBille++;;
                }
                for(int bo = 0 ; bo < obstacle.size(); bo++) {
                    if(((Bille)bille.elementAt(bi)).choc((Obstacle)obstacle.elementAt(bo))) boumObstacle++;
                }
                ((Bille)bille.elementAt(bi)).next(incrementTemps);
                // System.out.println(((Bille)bille.elementAt(bi)).toString());
            }
        } else {
            for(int bi = bille.size()-1 ; bi >= 0; bi--) {
                for(int bii = bi+1 ; bii < bille.size(); bii++) {
                    if(((Bille)bille.elementAt(bi)).choc((Bille)bille.elementAt(bii))) boumBille++;;
                }
                for(int bo = 0 ; bo < obstacle.size(); bo++) {
                    if(((Bille)bille.elementAt(bi)).choc((Obstacle)obstacle.elementAt(bo))) boumObstacle++;
                }
                ((Bille)bille.elementAt(bi)).next(incrementTemps);
                // System.out.println(((Bille)bille.elementAt(bi)).toString());
            }
        }

        // supprime les billes hors murs d'enceintes
        for(int bi = bille.size()-1 ; bi > 0; bi--) {
                 if((((Bille)bille.elementAt(bi)).rect.x<MURDENCEINTE)&&(((Bille)bille.elementAt(bi)).prect.x<MURDENCEINTE))
                bille.removeElementAt(bi);
            else if((((Bille)bille.elementAt(bi)).rect.x>(w-MURDENCEINTE))&&(((Bille)bille.elementAt(bi)).prect.x>(w-MURDENCEINTE)))
                bille.removeElementAt(bi);
            else if((((Bille)bille.elementAt(bi)).rect.y<MURDENCEINTE)&&(((Bille)bille.elementAt(bi)).prect.y<MURDENCEINTE))
                bille.removeElementAt(bi);
            else if((((Bille)bille.elementAt(bi)).rect.y>(h-MURDENCEINTE))&&(((Bille)bille.elementAt(bi)).prect.y>(h-MURDENCEINTE)))
                bille.removeElementAt(bi);

        }

        // sort.sort(bille);

        for(int bi = 0 ; bi < bille.size(); bi++) {
            double maxy = 0, miny = h, maxx = 0, minx = w;
            int rencontre = 0;
            for(int bii = 0 ; bii < bille.size(); bii++) {
                if(((Bille)bille.elementAt(bi)).rencontre((Bille)bille.elementAt(bii))) {
                    if(bi!=bii) {
                        maxx = Math.max(maxx, ((Bille)bille.elementAt(bii)).rect.x);
                        minx = Math.min(minx, ((Bille)bille.elementAt(bii)).rect.x);
                        maxy = Math.max(maxy, ((Bille)bille.elementAt(bii)).rect.y);
                        miny = Math.min(miny, ((Bille)bille.elementAt(bii)).rect.y);
                        rencontre++;
                    }
                }
            }
            if(rencontre>0) {
                // System.out.println(((Bille)bille.elementAt(bi)).toString() + " " + Service.format(maxx) + " " + Service.format(minx) + " " + Service.format(maxy) + " " + Service.format(miny));
                if(maxx<=((Bille)bille.elementAt(bi)).rect.x) { ((Bille)bille.elementAt(bi)).rect.x += 2; }
                if(minx>=((Bille)bille.elementAt(bi)).rect.x) { ((Bille)bille.elementAt(bi)).rect.x -= 2; }
                if(maxy<=((Bille)bille.elementAt(bi)).rect.y) { ((Bille)bille.elementAt(bi)).rect.y += 2; }
                if(miny>=((Bille)bille.elementAt(bi)).rect.y) { ((Bille)bille.elementAt(bi)).rect.y -= 2; }
            }
            if(rencontre>2) {
                // System.out.print("r" + rencontre);
                ((Bille)bille.elementAt(bi)).rect.w = 0;
                ((Bille)bille.elementAt(bi)).rect.h = 0;
            }
            // if(rencontre>0) System.out.println(" bi=" + bi);
        }

        bascule = !bascule;
        if((boumBille>(prevBoumBille+3))&&(incrementTemps>0.10)) {
            incrementTemps = incrementTemps * 0.99;
            prevBoumBille  = boumBille;
        } else if(boumBille<(prevBoumBille-4)) {
            incrementTemps = incrementTemps * 1.01;
            prevBoumBille  = boumBille;
        }
        // Date stuff
        // Date date =new Date();
        // System.out.println("nextTableau :" + bille.size() + " billes (chocs:" + boumBille + ") & " +
        //                    obstacle.size() + " obstacles (chocs:" + boumObstacle + ") incrementTemps=" + Service.format(incrementTemps) + ". " + formatter.format(date));

        try { thread.sleep(200); } catch(Exception e) { }
    }

    public abstract void run();
    public void start() { thread.start(); }
    public void stop()  { thread.stop(); }
}
