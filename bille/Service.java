import java.math.BigDecimal;
import java.awt.Color;

class Service {

    static public BigDecimal format(double d) {
        BigDecimal n = new BigDecimal(d);
        return n.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    static public double carre(double i)     { return(i*i); }
    static public double dcarre(double i, double j) { return(carre(i-j)); }

    static public String getColorName(Color couleur) {
             if(couleur.equals(Color.red))     return new String("red");
        else if(couleur.equals(Color.blue))    return new String("blue");
        else if(couleur.equals(Color.green))   return new String("green");
        else if(couleur.equals(Color.cyan))    return new String("cyan");
        else if(couleur.equals(Color.magenta)) return new String("magenta");
        else if(couleur.equals(Color.yellow))  return new String("yellow");
        else if(couleur.equals(Color.white))   return new String("white");
        else if(couleur.equals(Color.black))   return new String("black");
        else if(couleur.equals(Color.orange))  return new String("orange");
        else if(couleur.equals(Color.pink))    return new String("pink");
        else                                   return new String("(" + couleur.getRed()  + "," + couleur.getGreen()  + "," + couleur.getBlue()  + ")");
    }
}
