import java.util.*;
import java.awt.*;

class TableauBoite extends Tableau implements Runnable, BInclude {

    TableauBoite(int w, int h, double dynamicitePlancher) {
        super(w, h, dynamicitePlancher);

        // Rectangular(double x, double y, double w, double h)
        // Obstacle(Rectangular r, double dynamicite)
        // Bille(int rayon, Rectangular rect, Color couleur, double dynamicite)

        // petits murs additionnels
        obstacle.addElement(new Obstacle(new Rectangular(w/2,    200,   0, 150), 0.90)); // vertical   ralentit
        obstacle.addElement(new Obstacle(new Rectangular(w-200,  h/4, 100,   0), 0.90)); // horizontal ralentit
        obstacle.addElement(new Obstacle(new Rectangular(w-300,  h/2,  75,   0), 0.90)); // horizontal ralentit

        bille.addElement(new Bille(7,  new Rectangular(w/2, 21, 15,  0), Color.yellow,  1.02)); // accelere
        bille.addElement(new Bille(6,  new Rectangular(21,  21,  1,  0), Color.red,     1.02));
        bille.addElement(new Bille(5,  new Rectangular(21,  35,  5,  0), Color.blue,    0.98)); // ralentit
        bille.addElement(new Bille(5,  new Rectangular(21,  50, 10,  3), Color.green,   0.98));
        bille.addElement(new Bille(5,  new Rectangular(w/3, 21,  0, 10), Color.cyan,    0.97));
        bille.addElement(new Bille(8,  new Rectangular(w/4, 21,  0,  0), Color.magenta, 0.95));
    }

    public void run() { while(true) nextTableau(); }
}
