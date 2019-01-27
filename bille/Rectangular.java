
class Rectangular {
    double x,y,w,h;

    Rectangular(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public String toString() {
        return(new String("R(" + Service.format(x) + "," + Service.format(y) + ")->v[" + Service.format(w) + "," + Service.format(h) + "]"));
    }


    public Polar rect2Pol() {
        double angle = Math.atan2(h, w);
        if(angle<0) angle += 2 * Math.PI;
        return(new Polar(x, y, angle, Math.sqrt(Service.carre(w)+Service.carre(h))));
    }
}
