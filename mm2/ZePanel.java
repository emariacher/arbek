import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Description of the Class
 *
 * @author emariach
 * @created April 12, 2007
 */
public class ZePanel extends Panel
        implements Runnable,
        MMInclude, MouseListener {
    Color couleur[] = new Color[NBRE_COULEUR];
    Color backgroundColor = Color.lightGray;
    boolean Moused = false;
    boolean afficheSolution = false;
    Moteur moteur;
    Thread thread;
    private Image image;
    private Dimension dim;
    private Graphics graphics;
    int x_inc, y_inc, x_diametre, y_diametre;
    int xMoused, yMoused, indexCouleurMoused;


    /**
     * Constructor for the ZePanel object
     *
     * @param moteur Description of the Parameter
     */
    ZePanel(Moteur moteur) {
        this.moteur = moteur;
        thread = new Thread(this);
        try {
            couleur[0] = backgroundColor;
            couleur[1] = Color.red;
            couleur[2] = Color.green;
            couleur[3] = Color.yellow;
            couleur[4] = Color.blue;
            couleur[5] = Color.magenta;
            couleur[6] = Color.cyan;
            couleur[7] = Color.orange;
            couleur[8] = Color.black;
            couleur[9] = Color.white;
        } catch (Exception e) {
        }
    }


    /**
     * Description of the Method
     *
     * @param g Description of the Parameter
     */
    public void update(Graphics g) {
        String trace = new String("0 ");
        try {
            //******* debut
            Dimension d = getSize();
            if ((image == null) || (d.width != dim.width) || (d.height != dim.height)) {
                image = createImage(d.width, d.height);
                dim = d;
                graphics = image.getGraphics();
                graphics.setFont(getFont());
            }
            graphics.setColor(backgroundColor);
            graphics.fillRect(0, 0, d.width, d.height);

            //******* dessine
            int lign;

            //******* dessine
            int pion;
            x_inc = (d.width - 20) / ((NBRE_PION * 2) + 1);
            y_inc = (d.height - 20) / (MAX_REPONSE + 1);
            x_diametre = x_inc * 2 / 3;
            y_diametre = y_inc * 2 / 3;

            // dessine un rectangle autour des pions que l'utilisateur peut choisir.
            graphics.setColor(Color.black);
            graphics.drawRect((x_inc * ((NBRE_PION * 2) + 1)) - 3, 2, x_inc, (y_inc * NBRE_COULEUR) - 2);

            // dessine des pions que l'utilisateur peut choisir
            for (pion = 0; pion < NBRE_COULEUR; pion++) {
                trace = trace + "1";
                graphics.setColor(couleur[pion]);
                graphics.fillOval((x_inc * ((NBRE_PION * 2) + 1)), (y_inc * pion) + 5,
                        x_diametre, y_diametre);
                graphics.setColor(Color.white);
                graphics.drawOval((x_inc * ((NBRE_PION * 2) + 1)), (y_inc * pion) + 5,
                        x_diametre, y_diametre);

            }

            // dessine le but que l'utilisateur/la machine doit atteindre.
            for (pion = 0; pion < NBRE_PION; pion++) {
                trace = trace + "2";
                if ((moteur.automatique) || (afficheSolution)) {
                    graphics.setColor(couleur[moteur.but.pion[pion]]);
                } else {
                    graphics.setColor(Color.darkGray);
                }
                graphics.fillOval((x_inc * pion) + 10, 10, x_diametre, y_diametre);
                graphics.setColor(Color.black);
                graphics.drawOval((x_inc * pion) + 10, 10, x_diametre, y_diametre);
            }

            // dessine les differents essais proposes par l'utilisateur/la machine et les reponses correspondantes.
            int limite;
            if ((moteur.automatique) || (!moteur.pasTrouve)) {
                limite = moteur.inc;
            } else {
                limite = moteur.inc + 1;
            }
            for (lign = 0; lign < limite; lign++) {
                for (pion = 0; pion < NBRE_PION; pion++) {
                    trace = trace + "3";
                    if (moteur.ligne[lign].pion[pion] != INVALID) {
                        graphics.setColor(couleur[moteur.ligne[lign].pion[pion]]);
                        graphics.fillOval((x_inc * pion) + 10,
                                (y_inc * (lign + 1)) + 10,
                                x_diametre, y_diametre);
                        graphics.setColor(Color.white);
                        graphics.drawOval((x_inc * pion) + 10,
                                (y_inc * (lign + 1)) + 10,
                                x_diametre, y_diametre);
                    } else {
                        graphics.setColor(Color.darkGray);
                        graphics.drawOval((x_inc * pion) + 10,
                                (y_inc * (lign + 1)) + 10,
                                1, 1);
                    }
                }
                if (moteur.reponse[lign] == null) {
                    break;
                }
                graphics.setColor(Color.black);
                for (pion = 0; pion < moteur.reponse[lign].noir; pion++) {
                    trace = trace + "4";
                    graphics.fillOval((x_inc * (pion + NBRE_PION)) + 10,
                            (y_inc * (lign + 1)) + 10,
                            x_diametre / 2, y_diametre / 2);
                }
                graphics.setColor(Color.white);
                for (pion = 0; pion < moteur.reponse[lign].blanc; pion++) {
                    trace = trace + "5";
                    graphics.fillOval((x_inc * (pion + NBRE_PION + moteur.reponse[lign].noir)) + 10,
                            (y_inc * (lign + 1)) + 10,
                            x_diametre / 2, y_diametre / 2);
                }
            }

            // si l'utilisateur a selectionne un pion pour l'inserer dans sa ligne d'essai, dessine le.
            if (Moused) {
                trace = trace + "6";
                graphics.setColor(couleur[indexCouleurMoused]);
                graphics.fillOval(xMoused, yMoused, x_diametre, y_diametre);
                graphics.setColor(Color.darkGray);
                graphics.drawOval(xMoused, yMoused, x_diametre, y_diametre);
            }

            //******* fin
            g.drawImage(image, 0, 0, null);
        } catch (Exception e) {
            System.err.println(e + " " + trace);
            e.printStackTrace();
        }
    }


    /**
     * Main processing method for the ZePanel object
     */
    public void run() {
        while (true) {
            if (moteur.automatique) {
                if (moteur.changement) {
                    repaint();
                }
            } else {
                repaint();
            }
            try {
                thread.sleep(200);
            } catch (Exception e) {
            }
        }
    }


//**********************************************************************************

    /**
     * Description of the Method
     *
     * @param x    Description of the Parameter
     * @param y    Description of the Parameter
     * @param drop Description of the Parameter
     * @return Description of the Return Value
     */
    int trouveObjetLePlusProche(int x, int y, boolean drop) {
        int pion;

        if (drop) {
            for (pion = 0; pion < NBRE_PION; pion++) {
                if (x < ((x_inc * pion) + 10 - x_diametre)) {
                    break;
                }
            }
            return (pion - 1);
        } else {
            if (x > (x_inc * NBRE_PION * 2)) {
                for (pion = 0; pion < NBRE_COULEUR; pion++) {
                    if (y < (y_inc * pion)) {
                        break;
                    }
                }
                return (pion - 1);
            } else if ((x < (x_inc * NBRE_PION)) &&
                    (y > (y_inc * moteur.inc)) &&
                    (y < (y_inc * (moteur.inc + 1)))) {
                for (pion = 0; pion < NBRE_PION; pion++) {
                    if (x < ((x_inc * pion) + 10 - x_diametre)) {
                        break;
                    }
                }
                return (moteur.ligne[moteur.inc - 1].pion[pion - 1]);
            } else if ((x < (x_inc * NBRE_PION)) &&
                    (y > (y_inc * moteur.inc))) {
                for (pion = 0; pion < NBRE_PION; pion++) {
                    if (x < ((x_inc * pion) + 10 - x_diametre)) {
                        break;
                    }
                }
                int zub = moteur.ligne[moteur.inc].pion[pion - 1];
                moteur.ligne[moteur.inc].pion[pion - 1] = INVALID;
                return (zub);
            } else {
                return INVALID;
            }
        }
    }


    public synchronized void mousePressed(MouseEvent evt) {
        xMoused = evt.getX();
        yMoused = evt.getY();
        System.out.println("[31m mousePressed. [34m" + evt);
        indexCouleurMoused = trouveObjetLePlusProche(xMoused, yMoused, false);
        if ((!moteur.automatique) && (indexCouleurMoused != INVALID)) {
            Moused = true;
            repaint();
        }
    }

    public synchronized void mouseReleased(MouseEvent evt) {
        xMoused = evt.getX();
        yMoused = evt.getY();
        System.out.println("[31m mouseReleased. [34m" + evt);
        int indexPion = trouveObjetLePlusProche(xMoused, 0, true);
        if ((!moteur.automatique) && (Moused)) {
            if (xMoused < ((x_inc * NBRE_PION) + 20)) {
                // user friendlier
                moteur.ligne[moteur.inc].pion[indexPion] = indexCouleurMoused;
            }
            Moused = false;
            repaint();
        }
    }


    public synchronized void mouseDragged(MouseEvent evt) {
        xMoused = evt.getX();
        yMoused = evt.getY();
        System.out.println("[31m mouseDragged. [34m" + evt);
        if (!moteur.automatique) {
            repaint();
        }
    }

    public synchronized void mouseClicked(MouseEvent evt) {
        System.out.println("[31m mouseClicked. [34m" + evt);
    }

    public synchronized void mouseExited(MouseEvent evt) {
        System.out.println("[31m mouseExited. [34m" + evt);
    }

    public synchronized void mouseEntered(MouseEvent evt) {
        System.out.println("[31m mouseEntered. [34m" + evt);
    }


//**********************************************************************************

}

