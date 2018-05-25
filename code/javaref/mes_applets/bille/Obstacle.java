import java.awt.*;

class Obstacle implements BInclude {
    Rectangular r;     // coordonnees
    double dynamicite; // pour savoir si la bille est accelere ou ralentie sur un choc avec cet obstacle

    Obstacle(Rectangular r, double dynamicite) {
        this.r = r;
        this.dynamicite = dynamicite;
    }

    public boolean hIn(double by)                  { return((by>r.y)&&(by<(r.y+r.h))); }
    public boolean wIn(double bx)                  { return((bx>r.x)&&(bx<(r.x+r.w))); }
    public boolean wTraverse(double bx,double bpx) { return((bx<r.x)!=(bpx<r.x)); }
    public boolean hTraverse(double by,double bpy) { return((by<r.y)!=(bpy<r.y)); }
    public double choc(double v)                   { return(v * dynamicite); }

    public void draw(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.drawLine((int)r.x, (int)r.y, (int)(r.x+r.w), (int)(r.y+r.h));
    }
}
