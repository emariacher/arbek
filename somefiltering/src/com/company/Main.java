package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Eric Mariacher 6apr2009
 * Do some list manipulation without doing any loop (for, while, etc...) but using SortedSet
 * http://eric-mariacher.blogspot.com/2009/04/java-list-management-and-filetring.html
 */

public class Main {
    static ArrayList<People> l_europeans = new ArrayList<People>(Arrays.asList(
            new People("Eric", "France"),
            new People("Martine", "France"),
            new People("John", "Great-Britain"),
            new People("Martha", "Great-Britain"),
            new People("Carine", "France"),
            new People("Gerd", "Deutschland"),
            new People("Giuseppe", "Italia"),
            new People("Martha", "Deutschland")));

    Main() {
    }

    public static void main(String[] args) {
        System.out.println("Eric Mariacher");
        System.out.println("European people: " + l_europeans);
        SortedSet<People> l_countries = new TreeSet<People>(new People.compareCountry());
        l_countries.addAll(l_europeans);
        System.out.println("European countries: " + l_countries);
        Main s = new Main();
        SortedSet<People> l_listByCountry = new TreeSet<People>(s.new listByCountry());
        l_listByCountry.addAll(l_countries);
    }

    public class listByCountry implements Comparator<People> {
        SortedSet<People> l_thisCountry1;
        SortedSet<People> l_thisCountry2;

        @Override
        public int compare(People o1, People o2) {
            l_thisCountry1 = new TreeSet<People>(new People.compareCountry2(o1));
            l_thisCountry1.addAll(l_europeans);
            l_thisCountry1.remove(l_thisCountry1.first());
            System.out.println("People from: [" + o1.s_Nationality + "]: " + l_thisCountry1);
            if (!o2.b_done) {
                l_thisCountry2 = new TreeSet<People>(new People.compareCountry2(o2));
                l_thisCountry2.addAll(l_europeans);
                l_thisCountry2.remove(l_thisCountry2.first());
                System.out.println("People from: [" + o2.s_Nationality + "]: " + l_thisCountry2);
            }
            return 0;
        }
    }
}