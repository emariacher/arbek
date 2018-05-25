import java.awt.*;

class Bille implements BInclude {
    // proprietes statiques
    int    rayon;      // rayon
    Color  couleur;
    double dynamicite; // pour savoir si la bille est accelere ou ralentie sur un choc avec une autre bille

    // proprietes dynamiques
    Rectangular rect; // coordonnees & vitesse

    // variables locales
    Rectangular prect; // coordonnees & vitesse precedentes

    Bille(int rayon, Rectangular rect, Color couleur, double dynamicite) {
        this.rayon      = rayon;
        this.rect       = rect;
        this.prect      = new Rectangular(rect.x, rect.y, rect.w, rect.h);
        this.couleur    = couleur;
        this.dynamicite = dynamicite;
    }

    Bille(Bille b) {
         rayon      = b.rayon;
         rect       = b.rect;
         prect      = b.prect;
         couleur    = b.couleur;
         dynamicite = b.dynamicite;
    }

    public String toString() {
        return(new String(" Bille " + Service.getColorName(couleur) +
                          " r=" + rayon + " dyn=" + Service.format(dynamicite) +
                          " " +rect.toString() + " " + rect.rect2Pol().toString()));
    }

    public boolean choc(Obstacle o) {
        // y a t'il choc avec un obstacle?
               if(o.r.w==0) { // mur vertical
                   if(o.hIn(rect.y)&&o.hIn(prect.y)&&o.wTraverse(rect.x,prect.x)) {
                       rect.w = -o.choc(prect.w);
                       rect.x  = prect.x;
                       // System.out.println("Choc sur mur vertical!");
                       return true;
                   }
        } else if(o.r.h==0) { // plancher horizontal
                   if(o.wIn(rect.x)&&o.wIn(prect.x)&&o.hTraverse(rect.y,prect.y)) {
                       rect.h = -o.choc(prect.h);
                       rect.y  = prect.y;
                       // System.out.println("Choc sur plancher horizontal!");
                       return true;
                   }
        } else {
            System.err.println("Choc pas encore traite!");
        }
        return false;
    }


    public boolean rencontre(Bille b) {
        // y a t'il choc/recouvrement avec une bille?
        if(Service.dcarre(rect.x,b.rect.x)>30) return false; // plus rapide
        if((Service.dcarre(rect.x,b.rect.x)+Service.dcarre(rect.y,b.rect.y))<=Service.carre(rayon + b.rayon)) return true;
        return false;
    }


    public boolean choc(Bille b) {
        if(rencontre(b)) {
            Polar b2b;
            Rectangular rb2b;
            // System.out.println("[31mChoc billes![30m");
            // calcule le vecteur reliant le centre des 2 billes
            b2b = (new Rectangular(rect.x, rect.y, rect.x-b.rect.x, rect.y-b.rect.y)).rect2Pol();

            // System.out.println(" " + this.toString() + "[31m>-<[30m" + b.toString());
            // System.out.println(" [34m" + b2b.toString() + "[30m");

            // calcule les vecteurs des 2 billes
            Polar pzis = rect.rect2Pol();
            Polar pb   = b.rect.rect2Pol();

            // symetrise les vecteurs des 2 billes par rapport au vecteur reliant le centre des 2 billes
            pzis.angle = (2 * b2b.angle) - pzis.angle;
            pb.angle   = (2 * b2b.angle) - pb.angle;

            // accelere ou ralentit les 2 billes suivant besoin (dynamicite)
            // pi * R * R * V = pi * r * r * v
            pzis.rayon =   dynamicite * pzis.rayon * Service.carre(b.rayon/rayon);
              pb.rayon = b.dynamicite *   pb.rayon * Service.carre(rayon/b.rayon);

            // retrofite ca en rectangulaire sur les 2 billes
            rect   = pzis.pol2Rect();
            b.rect = pb.pol2Rect();

            // securite pour que les billes ne se recouvrent pas de facon permanente
            // (cela arrive en general quand la dynamicite des billes < 1)
                 if(b.rect.x>(rect.x+1)) { b.rect.x+=1; rect.x-=1; }
            else if(b.rect.x<(rect.x-1)) { b.rect.x-=1; rect.x+=1; }
                 if(b.rect.y>(rect.y+1)) { b.rect.y+=1; rect.y-=1; }
            else if(b.rect.y<(rect.y-1)) { b.rect.y-=1; rect.y+=1; }

            // System.out.println(" " + this.toString() + "[31m<->[30m" + b.toString() + "[34m");
            // b2b = (new Rectangular(rect.x, rect.y, rect.x-b.rect.x, rect.y-b.rect.y)).rect2Pol();
            // System.out.println(" [34m" + b2b.toString() + "[30m");
            return true;
        }
        return false;
    }

    public void next(double temps) {
        // bouge de la
        prect.x  = rect.x;
        prect.y  = rect.y;
        prect.w = rect.w;
        prect.h = rect.h;
        rect.x  += rect.w * temps;
        rect.y  += rect.h * temps;
        rect.w += XACCELERATION * temps;
        rect.h += YACCELERATION * temps;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(couleur);
        graphics.fillOval((int)(rect.x)-rayon, (int)(rect.y)-rayon, rayon*2, rayon*2);
        graphics.setColor(Color.black);
        graphics.drawOval((int)(rect.x)-rayon, (int)(rect.y)-rayon, rayon*2, rayon*2);
        graphics.drawLine((int)rect.x, (int)rect.y, (int)(rect.x + rect.w), (int)(rect.y + rect.h));
    }
}
