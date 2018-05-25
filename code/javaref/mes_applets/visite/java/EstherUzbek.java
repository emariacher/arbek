import java.applet.Applet;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.event.*;
import java.net.*;
import java.util.*;

public class EstherUzbek extends Applet implements Runnable,
                                                   MouseListener,
                                                   MouseMotionListener,
                                                   EstherInclude {
    MediaTracker tracker;
    Image bg;
    Image anim[] = new Image[MAX_LIEU];
    Lieu  lieu[] = new Lieu[MAX_LIEU];
    int   index = MAX_LIEU, oldIndex;
    int   montreIndex, oldMontreIndex;
    int   maxIndex = MAX_LIEU;
    Thread animator;
    boolean stopAnimation=false;

    public void init() {
        tracker = new MediaTracker(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // get images
        try {
            bg = getImage(getDocumentBase(), getParameter("bg"));
            String zob = getParameter("zob");
            if(zob!=null) {
                maxIndex=0;
                for (StringTokenizer t = new StringTokenizer(zob, "!") ; t.hasMoreTokens() ; ) {
                    lieu[maxIndex] = new Lieu(t.nextToken(), lieu, maxIndex);
                    if(!lieu[maxIndex].imageSource.equals("rien")) anim[maxIndex] = getImage(getDocumentBase(), lieu[maxIndex].imageSource);
                    maxIndex++;
                }
            }
            tracker.addImage(bg, MAX_LIEU);
            for(int i=0;i<maxIndex;i++) if(!lieu[i].imageSource.equals("rien")) tracker.addImage(anim[i], i);
        } catch(Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    // Start the animation thread.
    public void start() {
        animator = new Thread(this);
        animator.start();
    }

    // Stop the animation thread.
    public void stop() {
        animator.stop();
        animator = null;
    }

    // Run the animation thread.
    // First wait for the background image to fully load and paint.
    // Then wait for all of the animation frames to finish loading.
    // Finally loop and increment the animation frame index.
    public void run() {
        try {
            tracker.waitForID(MAX_LIEU);
        } catch (InterruptedException e) {
            return;
        }
        Thread me = Thread.currentThread();
        while (animator == me) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            if((!stopAnimation)&&(montreIndex<maxIndex)&&(lieu[montreIndex]!=null)) montreIndex = lieu[montreIndex].nextIndex;
            if(montreIndex!=oldMontreIndex) repaint();
            oldMontreIndex = montreIndex;
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0,0,size().width, size().height);
        g.setColor(Color.black);
        g.drawString("Carte & Monuments d\'Ouzbekistan",10,10);

        if(tracker.statusID(MAX_LIEU, false) == MediaTracker.COMPLETE) {
            g.drawImage(bg, 20, 20, this);
        } else {
            g.setColor(Color.red);
            g.drawString(getParameter("bg") + ": Chargement en cours...", 20, bg.getHeight(this) + 50);
        }
        if(index < maxIndex) {
            g.setColor(Color.black);
            g.drawString(lieu[index].nom, 20, bg.getHeight(this) + 30);
            if(lieu[montreIndex].laius!=null) {
                g.setColor(Color.blue);
                g.drawString(lieu[montreIndex].laius, 30, bg.getHeight(this) + 40);
            }
            if(anim[montreIndex]!=null) {
                try { tracker.waitForID(montreIndex); } catch(InterruptedException e) { }
                if(tracker.statusID(montreIndex, false) == MediaTracker.COMPLETE) {
                    g.drawImage(anim[montreIndex], 20, bg.getHeight(this) + 50, this);
                    // System.out.println("  index:" + index + ", montreIndex:" + montreIndex + ", " + lieu[montreIndex].toString());
                } else {
                    g.setColor(Color.red);
                    g.drawString(lieu[montreIndex].imageSource + ": Chargement en cours...", 20, bg.getHeight(this) + 50);
                }
            } else {
                g.setColor(Color.red);
                g.drawString("Pas de photo.", 20, bg.getHeight(this) + 50);
                g.drawString("Cette appliquette est la propriete d\'Eric Mariacher.", 20, bg.getHeight(this) + 60);
            }
        }
    }


//**********************************************************************************
    void trouveLeLieuLePlusProche(MouseEvent evt) {
        int x = evt.getX(); int y = evt.getY();
        index = maxIndex;
        for(int i=0; i < maxIndex; i++) {
            Lieu l = lieu[i];
            double dist = (l.x - x) * (l.x - x) + (l.y - y) * (l.y - y);
            if (dist < (l.influence * l.influence)) {
                index = i;
                break;
            }
        }
    }

    //***** mouse motion listener
    public void mouseMoved(MouseEvent evt) {
        // System.out.println("[31m mouseMoved. [34m" + evt);
        trouveLeLieuLePlusProche(evt);
        if(index!=oldIndex) {
            montreIndex = index;
            repaint();
        }
        oldIndex=index;
    }

    public void mouseDragged(MouseEvent evt) {
        if(BRINGUP) System.out.println("[31m mouseDragged. [34m" + evt);
        repaint();
    }

    //***** mouse listener
    public void mousePressed(MouseEvent evt) { stopAnimation=true; }
    public void mouseReleased(MouseEvent evt) {
        // System.out.println("[31m mouseReleased. [34m" + evt);
        trouveLeLieuLePlusProche(evt);
        if(index!=oldIndex) {
            montreIndex = index;
            repaint();
        }
        oldIndex=index;
        stopAnimation=false;
    }

    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
//**********************************************************************************
}
