package com.company;

import java.util.Comparator;

/**
 * @author Eric Mariacher 6apr2009
 */

public class People {
    String s_FirstName;
    String s_Nationality;
    boolean b_done = false;

    public People(String firstName, String nationality) {
        s_FirstName = firstName;
        s_Nationality = nationality;
    }

    private boolean sameNationality(String s) {
        return s_Nationality.equals(s);
    }

    public String toString() {
        return new String(s_FirstName + " from " + s_Nationality);
    }

    public static class compareCountry implements Comparator<People> {
        @Override
        public int compare(People p1, People p2) {
            return p1.s_Nationality.compareTo(p2.s_Nationality);
        }
    }

    public static class compareCountry2 implements Comparator<People> {
        String s_this_nationality;

        public compareCountry2(People o1) {
            s_this_nationality = o1.s_Nationality;
            o1.b_done = true;
        }

        @Override
        public int compare(People o1, People o2) {
            int i_result;
            if ((o1.sameNationality(s_this_nationality)) && (o2.sameNationality(s_this_nationality))) {
                i_result = o1.s_FirstName.compareTo(o2.s_FirstName);
            } else if (o1.sameNationality(s_this_nationality)) {
                i_result = 1;
            } else if (o2.sameNationality(s_this_nationality)) {
                i_result = -1;
            } else {
                i_result = 0;
            }
            return i_result;
        }
    }
}