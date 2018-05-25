
class Polar {
    double x,y;
    double angle, rayon;

    Polar(double x, double y, double angle, double rayon) {
        this.x     = x;
        this.y     = y;
        if(angle<0) this.angle += 2 * Math.PI;
        else        this.angle  = angle;
        this.rayon = rayon;
    }

    public String toString() {
        return(new String("P(" + Service.format(x) + "," + Service.format(y) + ")->[" + Service.format(angle) + " rad (" + Service.format(angle/Math.PI) + "*PI)," + Service.format(rayon) + "]"));
    }


    public Rectangular pol2Rect() {
        if(angle<0) angle += 2 * Math.PI;
        return(new Rectangular(x, y, Math.sin(angle) * rayon, Math.cos(angle) * rayon));
    }
}
