import java.util.Date;
import java.util.Random;

/**
 * Description of the Class
 *
 * @author emariach
 * @created April 12, 2007
 */
public class Moteur implements Runnable, MMInclude {
    Ligne ligne[] = new Ligne[MAX_REPONSE];
    Reponse reponse[] = new Reponse[MAX_REPONSE];
    int inc = 0;
    boolean changement = false;
    // utilise par ZePanel
    boolean runFreely = true;
    // utilise par pushbuttons
    boolean step = false;
    // utilise par pushbuttons
    boolean automatique = true;
    Random hasard;
    Ligne but;
    Date date;
    boolean pasTrouve;
    Thread thread;
    // utilise par pushbuttons


    /**
     * Constructor for the Moteur object
     *
     * @param graine Description of the Parameter
     */
    Moteur(int graine) {
        hasard = new Random(graine);
        but = new Ligne(hasard.nextInt());
        System.out.println("-- [31m" + but.toString() + "[30m");
        thread = new Thread(this);
    }


    public void reset() {
        int lign;
        /*ligne = new Ligne[MAX_REPONSE];
        reponse = new Reponse[MAX_REPONSE];*/
        for (lign = 0; lign < MAX_REPONSE; lign++) {
            if (ligne[lign] == null) {
                break;
            }
            ligne[lign].invalide();
            if (reponse[lign] != null) {
                reponse[lign].noir = 0;
                reponse[lign].blanc = 0;
            }
        }

        inc = 0;
        but = new Ligne(hasard.nextInt());
    }

    /**
     * Main processing method for the Moteur object
     */
    public void run() {
        try {
            Reponse resultat;
            while (true) {
                pasTrouve = true;

                int lign;

                int pion;
                reset();

                System.out.println("-- [31m" + but.toString() + "[30m");

                while ((pasTrouve) && (inc < MAX_REPONSE)) {
                    hypothese();

                    // soumet cet essai
                    resultat = but.compare(ligne[inc]);
                    reponse[inc] = resultat;
                    if (pasTrouve) {
                        pasTrouve = !resultat.trouve();
                        System.out.println(" [34m" + inc + ":" + ligne[inc].toString() + " " + reponse[inc].toString() + "[30m");
                        stopOuEncore(1000);
                    }
                    inc++;
                }
                stopOuEncore(5000);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }


    /**
     * Description of the Method
     *
     * @throws Exception Description of the Exception
     */
    private void hypothese() throws Exception {
        changement = false;
        if (automatique) {
            int u = 0;
            int limit = 200 * inc * inc * inc;
            Ligne essai = new Ligne(0);
            while (true) {
                // if(u++>limit) break;
                // genere 1 essai
                essai = new Ligne(hasard.nextInt());

                // System.out.println("       " + essai.toString());

                // verifie la pertinence de cet essai avec les essais precedents
                int i = 0;
                while ((i < inc) && (essai.valide(ligne[i], reponse[i]))) {
                    i++;
                }
                if (i == inc) {
                    break;
                }
            }
            System.out.println(" " + u + "/" + limit);
            ligne[inc] = essai;
        } else {
            ligne[inc] = new Ligne(0);
            ligne[inc].invalide();
            step = false;
            while ((!step) || (ligne[inc].estInvalide() && pasTrouve)) {
                if (automatique) {
                    break;
                }
                thread.sleep(500);
            }
        }
    }


    /**
     * Description of the Method
     *
     * @param sleep_time Description of the Parameter
     * @throws Exception Description of the Exception
     */
    private void stopOuEncore(int sleep_time) throws Exception {
        changement = true;
        if ((automatique) || (!pasTrouve)) {
            if (runFreely) {
                thread.sleep(sleep_time);
            } else {
                step = false;
                while (!step) {
                    thread.sleep(500);
                }
            }
        }
    }


    /**
     * Description of the Method
     */
    public void start() {
        thread.start();
    }
}

