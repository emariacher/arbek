import java.awt.*;
import java.util.*;

class Case implements Include {
    boolean frontiere[]    = new boolean[4];
    int     carre[]        = new int[4];
    boolean carreTraite[] = new boolean[4];

    public void genereCase(Case  caset[][], int h, int w, Random hasard) {
      int cpt=0;

      for(int i=0; i<4; i++) {
          frontiere[i]    = false; /* pas de frontiere */
          carre[i]        = ZOB;    /* pas de couleur */
          carreTraite[i] = false;
      }


      /* ce qui est impose par h-1 & w-1 */
      if(h>0) frontiere[0] = caset[h-1][w].frontiere[2];
      if(w>0) frontiere[1] = caset[h][w-1].frontiere[3];

      /* ce qui est libre                */
      if((Math.abs(hasard.nextInt())%2)==1) frontiere[2] = true;
      if((Math.abs(hasard.nextInt())%2)==1) frontiere[3] = true;

      /* interdiction de 1 ou 4 trait(s) */
      for(int i=0; i<4; i++) if(frontiere[i]) cpt++;
      switch(cpt) {
          case 0: frontiere[2] = true;
                  frontiere[3] = true;
                  break;
          case 1: if(!(frontiere[2]||frontiere[3])) {
                      frontiere[(Math.abs(hasard.nextInt())%2)+2] = true;
                  } else {
                      frontiere[2] = true;
                      frontiere[3] = true;
                  }
                  break;
          case 2: frontiere[(Math.abs(hasard.nextInt())%2)+2] = true;
                  break;
          case 4: frontiere[(Math.abs(hasard.nextInt())%2)+2] = false;
                  break;
         default: break;
      }
    }

    public void peintCase(int h, int w, Dimension d, Graphics g, Region rt[]) {
      double startx, starty, lengthx, lengthy;
      startx = ((w + 0.5) * d.width)  / MAXW;
      starty = ((h + 0.5) * d.height) / MAXH;
      lengthx = d.width  / (MAXW * 2);
      lengthy = d.height / (MAXH * 2);

      for(int i=0; i<4; i++) {
          int couleur = ZOB;

          if(carre[i]!=ZOB) couleur = rt[carre[i]].couleur;

          if(couleur!=ZOB) {
              switch(couleur) {
                  case 0: g.setColor(Color.yellow); break;
                  case 1: g.setColor(Color.green);  break;
                  case 2: g.setColor(Color.red);    break;
                  case 3: g.setColor(Color.cyan);   break;
                  case 9: g.setColor(Color.black);  break;
                 default: g.setColor(Color.white);  break;
              }
          } else g.setColor(Color.white);
          switch(i) {
              case 0: g.fillRect((int)(startx - lengthx), (int)(starty - lengthy),
                                 (int)lengthx, (int)lengthy);
                      g.setColor(Color.blue);
                      // g.drawString("0", (int)(startx - (lengthx/2)), (int)(starty - (lengthy/2)));
                      if(traceEnabled) g.drawString(carre[i] + " ", (int)(startx - (lengthx/2)), (int)(starty - (lengthy/2)));
                      break;
              case 1: g.fillRect((int)(startx - lengthx), (int)starty,
                                 (int)lengthx, (int)lengthy);
                      g.setColor(Color.blue);
                      // g.drawString("1", (int)(startx - (lengthx/2)), (int)(starty + (lengthy/2)));
                      if(traceEnabled) g.drawString(carre[i] + " ", (int)(startx - (lengthx/2)), (int)(starty + (lengthy/2)));
                      break;
              case 2: g.fillRect((int)startx, (int)starty,
                                 (int)lengthx, (int)lengthy);
                      g.setColor(Color.blue);
                      // g.drawString("2", (int)(startx + (lengthx/2)), (int)(starty + (lengthy/2)));
                      if(traceEnabled) g.drawString(carre[i] + " ", (int)(startx + (lengthx/2)), (int)(starty + (lengthy/2)));
                      break;
              case 3: g.fillRect((int)startx, (int)(starty - lengthy),
                                 (int)lengthx, (int)lengthy);
                      g.setColor(Color.blue);
                      // g.drawString("3", (int)(startx + (lengthx/2)), (int)(starty - (lengthy/2)));
                      if(traceEnabled) g.drawString(carre[i] + " ", (int)(startx + (lengthx/2)), (int)(starty - (lengthy/2)));
                      break;
          }
      }

      g.setColor(Color.black);
      if(frontiere[0]) g.drawLine((int)startx, (int)starty, (int)startx            , (int)(starty - lengthy));
      if(frontiere[1]) g.drawLine((int)startx, (int)starty, (int)(startx - lengthx), (int)starty);
      if(frontiere[2]) g.drawLine((int)startx, (int)starty, (int)startx            , (int)(starty + lengthy));
      if(frontiere[3]) g.drawLine((int)startx, (int)starty, (int)(startx + lengthx), (int)starty);

      /*if(frontiere[0]) g.drawString("0", (int)startx, (int)(starty - lengthy + 5));
      if(frontiere[1]) g.drawString("1", (int)(startx - lengthx + 5), (int)starty);
      if(frontiere[2]) g.drawString("2", (int)startx, (int)(starty + lengthy - 5));
      if(frontiere[3]) g.drawString("3", (int)(startx + lengthx - 5), (int)starty);
      g.drawString(h + "," + w , (int)startx, (int)starty);*/
    }

}

