
//import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  Description of the Class
 *
 *@author     Eric Mariacher
 *@created    November 30, 2005
 */
public class UtilsDate {
  /**
   *  Gets the quarter attribute of the CalendarStuff object
   *
   *@param  c  Description of the Parameter
   *@return    The quarter value
   */
  int getYear(Calendar c) {
    return c.get(Calendar.YEAR);
  }


  int getQuarter(Calendar c) {
    int mois  = c.get(Calendar.MONTH);
    return getQuarter(mois);
  }

  int getMonth(Calendar c) {
    return c.get(Calendar.MONTH);
  }


  /**
   *  Gets the quarter attribute of the CalendarStuff object
   *
   *@param  mois  Description of the Parameter
   *@return       The quarter value
   */
  int getQuarter(int mois) {
    return ((mois / 3) + 1);
  }



  /**
   *  Description of the Method
   *
   *@param  quarter  Description of the Parameter
   *@return          Description of the Return Value
   */
  int get1stMonth(int quarter) {
    return (quarter - 1) * 3;
  }


  /**
   *  Gets the lastMonth attribute of the CalendarStuff object
   *
   *@param  quarter  Description of the Parameter
   *@return          The lastMonth value
   */
  int getLastMonth(int quarter) {
    return get1stMonth(quarter) + 2;
  }


  /**
   *  Gets the month2WeekSpan attribute of the CalendarStuff object
   *
   *@param  annee  Description of the Parameter
   *@param  mois   Description of the Parameter
   *@return        The month2WeekSpan value
   */
  int getMonth2WeekSpan(int annee, int mois) {
    return getLastWeek(annee, mois) - get1stWeek(annee, mois) + 1;
  }


  /**
   *  Description of the Method
   *
   *@param  annee  Description of the Parameter
   *@param  mois   Description of the Parameter
   *@return        Description of the Return Value
   */
  Calendar get1erLundi(int annee, int mois) {
    Calendar c  = Calendar.getInstance();
    c.set(annee, mois, 1);
    while(c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
      c.roll(Calendar.DAY_OF_MONTH, true);
    }
    return c;
  }

  Calendar get1erLundiDaysAgo(long daysAgo) {
	    Calendar c  = Calendar.getInstance();
	    c.setTimeInMillis(c.getTimeInMillis() - (1000*3600*24*daysAgo));
	    while(c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
	      c.roll(Calendar.DAY_OF_MONTH, true);
	    }
	    return c;
	  }
  Calendar get1erJourDaysAgo(long daysAgo) {
	    Calendar c  = Calendar.getInstance();
	    int i_today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	    c.setTimeInMillis(c.getTimeInMillis() - (1000*3600*24*daysAgo));
	    while(c.get(Calendar.DAY_OF_WEEK) != i_today) {
	      c.roll(Calendar.DAY_OF_MONTH, true);
	    }
	    return c;
	  }

	Calendar get1erJour(int annee, int mois) {
		Calendar c = Calendar.getInstance();
	    int i_today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		c.set(annee, mois, 1);
		while (c.get(Calendar.DAY_OF_WEEK) != i_today) {
			c.roll(Calendar.DAY_OF_MONTH, true);
		}
		return c;
	}


  /**
   *  Description of the Method
   *
   *@param  annee  Description of the Parameter
   *@param  mois   Description of the Parameter
   *@return        Description of the Return Value
   */
  Calendar getDernierLundi(int annee, int mois) {
    Calendar c  = Calendar.getInstance();
    c.set(annee, mois, 1);
    c.set(annee, mois, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    while(c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
      c.roll(Calendar.DAY_OF_MONTH, false);
    }
    return c;
  }


  /**
   *  Description of the Method
   *
   *@param  annee  Description of the Parameter
   *@param  mois   Description of the Parameter
   *@return        Description of the Return Value
   */
  int get1stWeek(int annee, int mois) {
    Calendar c  = get1erLundi(annee, mois);
    return c.get(Calendar.WEEK_OF_YEAR);
  }


  /**
   *  Description of the Method
   *
   *@param  annee  Description of the Parameter
   *@param  mois   Description of the Parameter
   *@return        Description of the Return Value
   */
  int getLastWeek(int annee, int mois) {
    Calendar c  = getDernierLundi(annee, mois);
    return c.get(Calendar.WEEK_OF_YEAR);
  }


  /**
   *  Description of the Method
   *
   *@param  c  Description of the Parameter
   *@return    Description of the Return Value
   */
  String printXLS(Calendar c) {
    SimpleDateFormat formatOut  = new SimpleDateFormat("ddMMMyyyy");
    Date date                   = c.getTime();
    return new String("<Cell ss:StyleID=\"head\"><Data ss:Type=\"String\">" + formatOut.format(date) + "</Data></Cell>");
  }


  /**
   *  Description of the Method
   *
   *@param  date  Description of the Parameter
   *@return       Description of the Return Value
   */
  String print(Date date) {
    if(date == null) {
      return new String("null_date1");
    }
    SimpleDateFormat formatOut  = new SimpleDateFormat("ddMMMyyyy");
    return new String(formatOut.format(date));
  }


  /**
   *  Description of the Method
   *
   *@param  c  Description of the Parameter
   *@return    Description of the Return Value
   */
  String print(Calendar c) {
    if(c == null) {
      return new String("null_date2");
    }
    return print(c.getTime());
  }
}

