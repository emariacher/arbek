/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    20 décembre 2010
 *$Log$
 *emariacher - Friday, January 07, 2011 4:24:36 PM
 *emariacher - Tuesday, January 04, 2011 5:20:47 PM
 *remove conjugation
 *emariacher - Monday, December 20, 2010 3:03:04 PM
 */

class Filter {
	public static String filter(String s_in) {
		// add space at string end
		String s_out = new String(s_in + " ");
		// replace all non word character: [a-zA-Z_0-9] by space and put everything to lower case
		s_out = s_out.replaceAll("\\W", " ").toLowerCase();
		// remove single characters
		s_out = s_out.replaceAll(" \\w ", " ");
		// remove following words
		String s_remove = new String("a an and is or the of for to in not after when by at " +
				"from be goes does it that should wrong vs are needs non which " +
				"like did on off potential impact new old some kind short must have until but " +
				"both than dont put done with seen do any this too enter into few my one we " +
				"as br if there give only instead while no without ");
		String a_s[] = s_remove.split(" ");
		for( String s : a_s) {
			s_out = s_out.replaceAll(" "+s+" ", " ");
		}
		// remove verb conjugation / conjugaison
		s_out = s_out.replaceAll("ing ", " ");
		s_out = s_out.replaceAll("ed ", " ");
		s_out = s_out.replaceAll("s ", " ");
		return s_out;
	}

}