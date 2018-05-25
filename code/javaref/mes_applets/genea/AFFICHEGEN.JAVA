/*---------------------------------------------------------------------------*\
 *
 * Copyright (c) 1998-2000 Eric Mariacher. All Rights Reserved.
 *
 * Eric Mariacher grants you ("Licensee") a non-exclusive, royalty free
 * and license to use
 * this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Eric Mariacher.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. ERIC MARIACHER AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL ERIC MARIACHER OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF ERIC MARIACHER HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.

     Author: E.Mariacher
   Creation: 8 Juin 1998
   Abstract: Genealogie
 Assumption: -1- not Y10K ready!
             -2- branches must be given going from the oldest generation to the youngest
                 (no order required if 2 branches are of the same generation).

\*---------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------*\
 *
 * Copyright (c) 1994-1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
\*---------------------------------------------------------------------------*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.SimpleDateFormat;

/*---------------------------------------------------------------------------*\
      Class: afficheGen
   Abstract: ?
\*---------------------------------------------------------------------------*/
class afficheGen extends Panel
               implements Runnable, MouseListener, MouseMotionListener {

      final int           SOURIS_INFO = 1;
      final int           SOURIS_NO   = 0;

            int           nklampin_affiche;

    private Thread        relaxer;
    private boolean       random;
            Klampin       pique;
    public  int           borne_inf;
    public  int           borne_sup;
            int           compteur_global_generation = 0;
            Dimension     d = getSize();
            int           compteur_affichage = 0;
            int           klampin_par_generation[];
            int           annee_graphique;
            boolean       freeze = false;
            String        vivante = "vivante";
            String        vivant  = "vivant";
            int           indexDuKlampinCentre;
            int           oldIndexDuKlampinCentre;
            menuGen       mg;
            dbGen         db;
            String        StrMatch = null;
            String        bonne_recherche            = null;
            String        mauvaise_recherche         = null;
            int           LIMITE_DE_CALCUL_GRAPHIQUE = 3;
            boolean       limite_atteinte            = false;
            int           moyenne_par_generation     = 1;
            int           nombre_generation_affiche  = 1;

    afficheGen(menuGen mg, dbGen db) {
        this.mg = mg;
        this.db = db;
        addMouseListener(this);
        addMouseMotionListener(this);
        klampin_par_generation  = new int[db.MAX_GENE+1];
        indexDuKlampinCentre    = db.MAX_KLAMPIN;
        oldIndexDuKlampinCentre = db.MAX_KLAMPIN;
    }


    private int annee_vers_abcisse(int annee) {
        if(borne_sup > borne_inf) return (((annee - borne_inf) * ( d.width - 100 ) / (borne_sup - borne_inf)) + 50);
        else                      return (d.width/2);
    }
    private int annee_vers_ordonnee(int annee) {
        if(borne_sup > borne_inf) return (((annee - borne_inf) * ( d.height - 20 ) / (borne_sup - borne_inf)) + 10);
        else                      return (d.height/2);
    }
    boolean afficheKlampin(int i) {
      // System.out.println(db.klampin[i].naissance + "-" + borne_sup + "/" + db.klampin[i].deces + "-" + borne_inf );
      if(db.klampin[i].naissance>borne_sup) return false;
      if(db.klampin[i].deces<borne_inf)     return false;
      return db.klampin[i].affiche;
    }
    boolean afficheLien(int i) {
      if((afficheKlampin(db.liens[i].de)) && (afficheKlampin(db.liens[i].vers))) return true;
      else                                                                       return false;
    }

    public void run() {
        Thread thisThread = Thread.currentThread();
        while (relaxer == thisThread) {
            relax();
            if (random && (Math.random() < 0.03)) {
                Klampin n = db.klampin[(int)(Math.random() * db.nklampin)];
                n.x += 100*Math.random() - 50;
                n.y += 100*Math.random() - 50;
            }
            // firechangeLaLangueEvent();
            compteur_affichage++;
            try {
                Thread.sleep(250);
            } catch (Exception e) {
                break;
            }
        }
    }


    private synchronized void relax() {
        // System.out.println("relax " + (new SimpleDateFormat(" MM|dd HH:mm:ss:SSSS ")).format(new Date()));
        if(!freeze) {
            for (int i = 0 ; i < db.nliens ; i++) {
                if(afficheLien(i)||(!limite_atteinte)) {
                    Lien e          = db.liens[i];
                    double vx       = db.klampin[e.vers].x - db.klampin[e.de].x;
                    double vy       = db.klampin[e.vers].y - db.klampin[e.de].y;
                    double longueur = Math.sqrt(vx * vx + vy * vy);
                    double f        = (db.liens[i].longueur - longueur) / (longueur * 3) ; // do not diminish 3
                    double dx       = f * vx;
                    double dy       = f * vy;

                    if(dx>0) {
                      db.klampin[e.vers].dx +=  dx+1;
                      db.klampin[e.de].dx   += -dx-1;
                    } else {
                      db.klampin[e.vers].dx +=  dx-1;
                      db.klampin[e.de].dx   += -dx+1;
                    }
                    if(dy>0) {
                      db.klampin[e.vers].dy +=  dy+1;
                      db.klampin[e.de].dy   += -dy-1;
                    } else {
                      db.klampin[e.vers].dy +=  dy-1;
                      db.klampin[e.de].dy   += -dy+1;
                    }
                }
            }

            for (int i = 0 ; i < db.nklampin ; i++) {
                if((!afficheKlampin(i))&&(limite_atteinte)) continue;
                Klampin n1 = db.klampin[i];
                double dx = 0;
                double dy = 0;

                for (int j = 0 ; j < db.nklampin ; j++) {
                    if((!afficheKlampin(j))&&(limite_atteinte)) continue;
                    if (i == j)                                 continue;
                    Klampin n2 = db.klampin[j];
                    double vx       = n1.x - n2.x;
                    double vy       = n1.y - n2.y;
                    double longueur = vx * vx + vy * vy;
                    if (longueur == 0) {
                        dx += Math.random();
                        dy += Math.random();
                    } else if (longueur < 500*100) { // no change 100->500
                        dx += vx / longueur;
                        dy += vy / longueur;
                    }
                }
                double dlongueur = dx * dx + dy * dy;
                if (dlongueur > 0) {
                    dlongueur = Math.sqrt(dlongueur) / 2;
                    n1.dx += dx / dlongueur;
                    n1.dy += dy / dlongueur;
                }
            }

            d = getSize();
            int minx=d.width;
            int miny=d.height;
            int maxx=0;
            int maxy=0;
            int minxindex=0;
            int maxxindex=0;
            int minyindex=0;
            int maxyindex=0;
            // System.out.println("*********** " + moyenne_par_generation + " = " + nklampin + "/" + (compteur_global_generation+2));
            for (int i = 0 ; i < db.nklampin ; i++) {
                if((!afficheKlampin(i))&&(limite_atteinte)) continue;
                Klampin n = db.klampin[i];
                n.x += Math.max(-50, Math.min(50, n.dx));
                n.y += Math.max(-10, Math.min(10, n.dy));
                int offset = 0;
                if (moyenne_par_generation<klampin_par_generation[n.generation])
                     offset += (klampin_par_generation[n.generation]*klampin_par_generation[n.generation]);
                else offset -= ((moyenne_par_generation-klampin_par_generation[n.generation])*(moyenne_par_generation-klampin_par_generation[n.generation]));
                if (n.x > (annee_vers_abcisse(n.deces)+offset)) {
                  // System.out.println(n.prenom + " (x1) " + n.x + ">" + (annee_vers_abcisse(n.deces)+offset));
                  n.x = annee_vers_abcisse(n.deces)+(offset);
                } else if (n.x < (annee_vers_abcisse(n.naissance)-offset)) {
                  // System.out.println(n.prenom + " (x2) " + n.x + "<" + (annee_vers_abcisse(n.naissance)-offset) + " (" + annee_vers_abcisse(n.naissance) + "," + offset + "," + n.generation + "," + klampin_par_generation[n.generation] + ")");
                  n.x = annee_vers_abcisse(n.naissance)-(offset);
                }
                if (n.x > (d.width-50)) {
                    // System.out.println(n.prenom + " (x3) " + n.x + ">" + (d.width-50));
                    n.x = d.width-50;
                } else if (n.x < 50) {
                    // System.out.println(n.prenom + " (x4) " + n.x + "<50");
                    n.x = 50;
                }
                if (n.y < 10) {
                    // System.out.println(n.prenom + " (y5) " + n.y + "<10");
                    n.y = 10;
                } else if (n.y > (d.height-10)) {
                    // System.out.println(n.prenom + " (y6) " + n.y + ">" + (d.height-10));
                    n.y = d.height-10;
                }
                n.dx /= 2;
                n.dy /= 2;
                // if(n.x < minx) minxindex = i;
                // if(n.x > maxx) maxxindex = i;
                if(n.y < miny) { minyindex = i; miny = n.y; }
                if(n.y > maxy) { maxyindex = i; maxy = n.y; }
            }
            int north,south;
            north = d.height-maxy;
            south = miny;
            if((compteur_affichage%16)==10) {
                for (int i = 0 ; i < db.nklampin ; i++) {
                  db.klampin[i].y +=north/8;
                }
            } else if((compteur_affichage%16)==15) {
                for (int i = 0 ; i < db.nklampin ; i++) {
                  db.klampin[i].y -=south/8;
                }
            }
        }
        repaint();
    }

    private Image     offscreen;
    private Dimension offscreensize;
    private Graphics  offgraphics;


    private final Color background   = Color.white;
    // private final Color klampinColor = Color.yellow;
    private Color geneColor[]        = new Color[6]; // colors for generations
    private final Color lienColor    = Color.red;
    private final Color badColor     = background;

    private void peintRectOval(Graphics g, int x, int y, int w, int h, int type) {
      if((type&1)==1) {
        g.fillOval(x - w/2, y - h / 2, w, h);
        g.setColor(Color.black);
        g.drawOval(x - w/2, y - h / 2, w-1, h-1);
      } else {
        g.fillRect(x - w/2, y - h / 2, w, h);
        g.setColor(Color.black);
        g.drawRect(x - w/2, y - h / 2, w-1, h-1);
      }
    }

    private void paintKlampin(Graphics g, Klampin n, FontMetrics fm) {
        int x = (int)n.x;
        int y = (int)n.y;
        g.setColor( geneColor[n.generation%6] );

          int h = fm.getHeight() + 4;
          int w = fm.stringWidth(n.prenom) + 10;
          if(n.souris != SOURIS_INFO) {
            peintRectOval(g,x,y,w,h,n.type);
            g.drawString(n.prenom, x - (w-10)/2, (y - (h-4)/2) + fm.getAscent());
          } else {
            peintRectOval(g,x,y,w,4*h,n.type);
            g.drawString(n.prenom                     , x - (w-10)/2, (y - (2*(h-4))) + fm.getAscent());
            g.drawString(Integer.toString(n.naissance), x - (w-10)/2, (y - (h-4))     + fm.getAscent());
            if(n.deces==9999) {
               if((n.type&1)==1) g.drawString(vivante                  , x - (w-10)/2,  y              + fm.getAscent());
               else              g.drawString(vivant                   , x - (w-10)/2,  y              + fm.getAscent());
            } else               g.drawString(Integer.toString(n.deces), x - (w-10)/2,  y              + fm.getAscent());
            if(n.commentaire!=null)  g.drawString(n.commentaire, x - (w-10)/2, (y + (h-4))     + fm.getAscent());
          }
       if(n != pique ) n.souris=SOURIS_NO;
    }


    public synchronized void update(Graphics g) {
        // System.out.println("update " + (new SimpleDateFormat(" MM|dd HH:mm:ss:SSSS ")).format(new Date()));
        geneColor[0] = Color.yellow;
        geneColor[1] = Color.magenta;
        geneColor[2] = Color.red;
        geneColor[3] = Color.cyan;
        geneColor[4] = Color.green;
        geneColor[5] = Color.lightGray;
        Dimension d = getSize();
        if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(getFont());
        }

        // offgraphics.setColor(getBackground());
        offgraphics.setColor(background);
        offgraphics.fillRect(0, 0, d.width, d.height);

        // 1er filtre d'affichage
        if(indexDuKlampinCentre != db.MAX_KLAMPIN) {
            if(indexDuKlampinCentre != oldIndexDuKlampinCentre) db.zaddGen.select(indexDuKlampinCentre);
            mg.statusText.setBackground(geneColor[db.klampin[indexDuKlampinCentre].generation%6]);
            mg.statusText.setForeground(Color.black);
            mg.statusText.setText(db.klampin[indexDuKlampinCentre].toString());
        } else if(StrMatch!=null) {
            int resultat = db.zaddGen.montre(StrMatch);
            mg.statusText.setBackground(Color.black);
            if(resultat==0) {
                mg.statusText.setForeground(Color.red);
                mg.statusText.setText(mauvaise_recherche + StrMatch.toUpperCase());
                db.zaddGen.resetLesKlampin(true);
            } else {
                mg.statusText.setForeground(Color.white);
                mg.statusText.setText(resultat + bonne_recherche + StrMatch.toUpperCase());
            }
        } else {
            mg.statusText.setForeground(Color.black);
            db.zaddGen.resetLesKlampin(true);
        }

        oldIndexDuKlampinCentre = indexDuKlampinCentre;

        // display liens
        offgraphics.setColor( lienColor );
        annee_graphique = d.width/(db.fin-db.debut);
        for (int i = 0 ; i < db.nliens ; i++) {
          if(afficheLien(i)) {
            if((db.liens[i].type&2)==0) db.liens[i].longueur = annee_graphique * (db.klampin[db.liens[i].vers].naissance - db.klampin[db.liens[i].de].naissance);
            int xoffset, yoffset, xmilieu, ymilieu;
            int x1 = db.klampin[db.liens[i].de].x;
            int y1 = db.klampin[db.liens[i].de].y;
            int x2 = db.klampin[db.liens[i].vers].x;
            int y2 = db.klampin[db.liens[i].vers].y;
            offgraphics.drawLine(x1,y1,x2,y2); // main line
            xoffset = (y1-y2)/20;
            yoffset = (x2-x1)/20;
            xmilieu = (x1 + x2)/2;
            ymilieu = (y1 + y2)/2;
            offgraphics.drawLine(xmilieu-xoffset,ymilieu-yoffset,xmilieu+(yoffset*2),ymilieu-(xoffset*2)); // arrow
            offgraphics.drawLine(xmilieu+xoffset,ymilieu+yoffset,xmilieu+(yoffset*2),ymilieu-(xoffset*2)); // arrow
          }
        }

        // display klampin
        FontMetrics fm = offgraphics.getFontMetrics();

        // fin de traitement/ debut du prochain traitement
        nklampin_affiche = 0;
        nombre_generation_affiche = 0;
        for(int i=0;i<db.MAX_GENE+1;i++) klampin_par_generation[i]=0;
        int last_generation = -1;
        for(int i = 0 ; i < db.nklampin ; i++) {
            if(afficheKlampin(i)) {
                paintKlampin(offgraphics, db.klampin[i], fm);
                klampin_par_generation[db.klampin[i].generation]++;
                if(db.klampin[i].generation>last_generation) {
                    last_generation = db.klampin[i].generation;
                    nombre_generation_affiche++;
                }

                nklampin_affiche++;
            }
        }
        if(nklampin_affiche>LIMITE_DE_CALCUL_GRAPHIQUE) limite_atteinte = true;
        else                                            limite_atteinte = false;

        if(nombre_generation_affiche!=0) moyenne_par_generation = nklampin_affiche/nombre_generation_affiche;
        else                             moyenne_par_generation = 0;
        // System.out.println(limite_atteinte + " " + nklampin_affiche + "/" + nombre_generation_affiche + "=" + moyenne_par_generation);

        g.drawImage(offscreen, 0, 0, null);
    }


//**********************************************************************************
    //***** find nearest klampin
    int findNearestKlampin(int x, int y) {
        double bestdist = Double.MAX_VALUE;
        int indexKlampin = 9999;
        for (int i = 0 ; i < db.nklampin ; i++) {
            if(afficheKlampin(i)) {
                Klampin n = db.klampin[i];
                double dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
                if (dist < bestdist) {
                    bestdist     = dist;
                    indexKlampin = i;
                }
            }
        }
        // System.out.println("[31m" + indexKlampin + "/" + nklampin + "[34m");
        return indexKlampin;
    }

    //***** mouse listener
    public void mousePressed(MouseEvent evt) {
        int x = evt.getX(); int y = evt.getY();
        // System.out.println("[31m mousePressed. [34m" + evt);
        if (!evt.isAltDown() && !evt.isMetaDown()) { // Bouton Gauche
          pique   = db.klampin[findNearestKlampin(x,y)];
          pique.x = x;
          pique.y = y;
          pique.souris = SOURIS_INFO;
          mg.statusText.setBackground(Color.white);
          mg.statusText.setText(pique.toString());
          repaint();
        } else {                                     // Bouton Droit
          int i = findNearestKlampin(x,y);
          // System.out.println("Bouton Droit: klampin[" + i + "]->" + klampin[i].prenom + "/" + indexDuKlampinCentre);
          if(i == indexDuKlampinCentre) {
              indexDuKlampinCentre = db.MAX_KLAMPIN;
              mg.statusText.setBackground(Color.white);
              mg.statusText.setText("");
              StrMatch = null;
              mg.inputText.setText("");
          } else {
              indexDuKlampinCentre = i;
          }
        }
    }

    public void mouseReleased(MouseEvent evt) {
        int x = evt.getX(); int y = evt.getY();
        // System.out.println("[31m mouseReleased. [34m" + evt);
        if (!evt.isAltDown() && !evt.isMetaDown() && (pique!=null))
        {
          pique.x = x;
          pique.y = y;
          pique   = null;
        } else {
          pique   = null;
        }
        repaint();
    }

    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }

    //***** mouse motion listener
    public void mouseMoved(MouseEvent evt) { }

    public void mouseDragged(MouseEvent evt) {
        int x = evt.getX(); int y = evt.getY();
        // System.out.println("[31m mouseDragged. [34m" + evt);
        if (!evt.isAltDown() && !evt.isMetaDown() && (pique!=null))
        {
          pique.x = x;
          pique.y = y;
        }
        repaint();
    }
//**********************************************************************************



    public void start() {
        // System.out.println("[30m afficheGenStart [34m");
        relaxer = new Thread(this);
        relaxer.start();
    }
    public void stop() {
        // System.out.println("[30m afficheGenStop [34m");
        relaxer = null;
    }

    public int changeLaLangue(Vector vector, int index) {
      int i = index;

      vivante            = vector.elementAt(i++).toString();
      vivant             = vector.elementAt(i++).toString();
      bonne_recherche    = vector.elementAt(i++).toString();
      mauvaise_recherche = vector.elementAt(i).toString();
      return i;
    }
}

