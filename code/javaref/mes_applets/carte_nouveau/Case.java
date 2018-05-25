import java.awt.*;
import java.util.*;

class Case implements Include {
	int hcase;            // case_start h
	int wcase;            // case_start w
	boolean frontiere[]    = new boolean[4];
	Region     carre[]        = new Region[4]; // une case a 4 quadrants/carres 0 1
	//                               3 2
	//    boolean carreTraite[] = new boolean[4];

	public void genereCase(Case  auDessus, Case deGauche, int h, int w, Random hasard) {
		int cpt=0;
		hcase=h;
		wcase=w;

		for(int i=0; i<4; i++) {
			frontiere[i]    = false; /* pas de frontiere */
			carre[i]        = null;    /* pas de couleur */
			//          carreTraite[i] = false;
		}


		/* ce qui est impose par h-1 & w-1 */
		if(auDessus!=null) frontiere[0] = auDessus.frontiere[2];
		if(deGauche!=null) frontiere[1] = deGauche.frontiere[3];

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
		Service.trace("genereCase:"+toString());
	}

	public void peintCase(int h, int w, Dimension d, Graphics g) {
		double startx, starty, lengthx, lengthy;
		startx = ((w + 0.5) * d.width)  / MAXW;
		starty = ((h + 0.5) * d.height) / MAXH;
		lengthx = d.width  / (MAXW * 2);
		lengthy = d.height / (MAXH * 2);

		for(int i=0; i<4; i++) {
			if(carre[i]==null){
				continue;
			}
			int couleur = carre[i].couleur;
			
			switch(couleur) {
			case 0: g.setColor(Color.yellow); break;
			case 1: g.setColor(Color.green);  break;
			case 2: g.setColor(Color.red);    break;
			case 3: g.setColor(Color.cyan);   break;
			case 9: g.setColor(Color.black);  break;
			case ZOB: g.setColor(Color.lightGray);  break;
			default: g.setColor(Color.white);  break;
			}
			switch(i) {
			case 0: g.fillRect((int)(startx - lengthx), (int)(starty - lengthy),
					(int)lengthx, (int)lengthy);
			g.setColor(Color.blue);
			// g.drawString("0", (int)(startx - (lengthx/2)), (int)(starty - (lengthy/2)));
			if(traceEnabled) g.drawString(carre[i].id + " ", (int)(startx - (lengthx/2)), (int)(starty - (lengthy/2)));
			break;
			case 1: g.fillRect((int)(startx - lengthx), (int)starty,
					(int)lengthx, (int)lengthy);
			g.setColor(Color.blue);
			// g.drawString("1", (int)(startx - (lengthx/2)), (int)(starty + (lengthy/2)));
			if(traceEnabled) g.drawString(carre[i].id + " ", (int)(startx - (lengthx/2)), (int)(starty + (lengthy/2)));
			break;
			case 2: g.fillRect((int)startx, (int)starty,
					(int)lengthx, (int)lengthy);
			g.setColor(Color.blue);
			// g.drawString("2", (int)(startx + (lengthx/2)), (int)(starty + (lengthy/2)));
			if(traceEnabled) g.drawString(carre[i].id + " ", (int)(startx + (lengthx/2)), (int)(starty + (lengthy/2)));
			break;
			case 3: g.fillRect((int)startx, (int)(starty - lengthy),
					(int)lengthx, (int)lengthy);
			g.setColor(Color.blue);
			// g.drawString("3", (int)(startx + (lengthx/2)), (int)(starty - (lengthy/2)));
			if(traceEnabled) g.drawString(carre[i].id + " ", (int)(startx + (lengthx/2)), (int)(starty - (lengthy/2)));
			break;
			}
		}

		g.setColor(Color.black);
		if(frontiere[0]) g.drawLine((int)startx, (int)starty, (int)startx            , (int)(starty - lengthy));
		if(frontiere[1]) g.drawLine((int)startx, (int)starty, (int)(startx - lengthx), (int)starty);
		if(frontiere[2]) g.drawLine((int)startx, (int)starty, (int)startx            , (int)(starty + lengthy));
		if(frontiere[3]) g.drawLine((int)startx, (int)starty, (int)(startx + lengthx), (int)starty);

		Service.trace("  peintCase:"+toString());
		/*if(frontiere[0]) g.drawString("0", (int)startx, (int)(starty - lengthy + 5));
      if(frontiere[1]) g.drawString("1", (int)(startx - lengthx + 5), (int)starty);
      if(frontiere[2]) g.drawString("2", (int)startx, (int)(starty + lengthy - 5));
      if(frontiere[3]) g.drawString("3", (int)(startx + lengthx - 5), (int)starty);
      g.drawString(h + "," + w , (int)startx, (int)starty);*/
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[h:"+ hcase+",w:"+wcase+"] ");
		if(frontiere[0]) sb.append("0V ");
		if(frontiere[1]) sb.append("1< ");
		if(frontiere[2]) sb.append("2A ");
		if(frontiere[3]) sb.append("3> ");
		return sb.toString();
	}
}

