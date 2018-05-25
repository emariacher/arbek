
public class Resistor {
	public Resistor(double d) {
		d_ohms=d;
		d_ohms_orig=d;
	}

	double d_ohms;
	double d_ohms_orig;

	public void add(Resistor r) {
		d_ohms +=r.d_ohms;
		d_ohms_orig += r.d_ohms_orig;
	}

	public String toString() {
		if(d_ohms==d_ohms_orig) {
			return new String(String.format("%1$6.2f ",d_ohms));
		} else {
			return new String(String.format("%1$6.2f (%2$6.2f) ",d_ohms, d_ohms_orig));		
		}
	}
}
