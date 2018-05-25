
import java.awt.*;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    March 24, 2005
 */
class TableauPluie extends Tableau implements Runnable, BInclude {
  int nombrebilles  = 150;


  /**
   *  Constructor for the TableauPluie object
   *
   *@param  w                   Description of the Parameter
   *@param  h                   Description of the Parameter
   *@param  dynamicitePlancher  Description of the Parameter
   */
  TableauPluie(int w, int h, double dynamicitePlancher) {
    super(w, h, dynamicitePlancher);

          // petits murs additionnels
    obstacle.addElement(new Obstacle(new Rectangular(w / 2, 200, 0, 150), 0.10)); // vertical   ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w - 200, h / 4, 100, 0), 0.10)); // horizontal ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w - 300, h / 2, 75, 0), 0.10)); // horizontal ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w / 3, h - 200, w / 3, 0), 0.10)); // horizontal ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w / 4, h - 100, w / 2, 0), 0.10)); // horizontal ralentit
  }


  /**
   *  Constructor for the TableauPluie object
   *
   *@param  w                   Description of the Parameter
   *@param  h                   Description of the Parameter
   *@param  dynamicitePlancher  Description of the Parameter
   *@param  nombrebilles        Description of the Parameter
   */
  TableauPluie(int w, int h, double dynamicitePlancher, int nombrebilles) {
    super(w, h, dynamicitePlancher);
          // petits murs additionnels
    obstacle.addElement(new Obstacle(new Rectangular(w / 2, 200, 0, 150), 0.10)); // vertical   ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w - 200, h / 4, 100, 0), 0.10)); // horizontal ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w - 300, h / 2, 75, 0), 0.10)); // horizontal ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w / 3, h - 200, w / 3, 0), 0.10)); // horizontal ralentit
    obstacle.addElement(new Obstacle(new Rectangular(w / 4, h - 100, w / 2, 0), 0.10)); // horizontal ralentit
    this.nombrebilles = nombrebilles;
  }


  /**  Main processing method for the TableauPluie object */
  public void run() {
    int cpt  = 0;
    while (true) {
      if ((bille.size() < nombrebilles) && ((cpt % 300) < 150)) {
        if ((cpt % 7) == 0) {
          bille.addElement(new Bille(5, new Rectangular((w / 9) * ((cpt % 8) + 1), 21, 1 - (cpt % 3), 0), Color.blue, 0.50));
        }
        if ((cpt % 9) == 0) {
          bille.addElement(new Bille(10, new Rectangular((w / 7) * ((cpt % 6) + 1), 21, 1 - (cpt % 3), 0), Color.green, 0.70));
        }
        if ((cpt % 40) == 31) {
          bille.addElement(new Bille(5, new Rectangular((w / 5) * ((cpt % 4) + 1), 21, 1 - (cpt % 3), 0), Color.red, 1.05));
        }
      }
      nextTableau();
      cpt++;
    }
  }
}

