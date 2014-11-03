package LLJ;

import java.util.Calendar;
import java.util.List;

/**
 * Explores holidays for a year or given a date.
 */
public interface HolidayExplorer {
    /**
     * Check if a given date is a holiday or not
     *
     * @param cal       Year, month and day to be checked
     * @return null if no holiday or a description of the holidays, if more than one, separated with ","
     */
    String getHoliday(Calendar cal);

    /**
     * Check if a given date is a holiday or not
     *
     * @param cal       Year, month and day to be checked
     * @param separator String to separate holiday strings if more than one
     * @return null if no holiday or a description of the holiday
     */
    String getHoliday(Calendar cal, String separator);

    /**
     * Check holidays for a given date.
     *
     * @param cal Year, month and day to be checked
     * @return List of holiday on that day or empty list if none
     */
    List<String> getHolidayList(Calendar cal);

    /**
     * Get all holidays for the given year.
     *
     * @param year 4-digit year
     * @return List of holidays
     */
    List<Holiday> getHolidays(int year);
}
