package LLJ;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * If you ask me, ISO 8601 should be the only permitted date format on this planet.
 */
public class DateUtils {
    public static final String DATE_FORMAT_ISO8601 = "yyyy-MM-dd";

    private static SimpleDateFormat dfDate = new SimpleDateFormat(DATE_FORMAT_ISO8601);

    private DateUtils() {
    }

    /**
     * Get a string with date of c on the format yyyy-MM-dd.
     *
     * @param c Calendar object
     * @return String representing date of c
     */
    public static String formatDateIso8601(Calendar c) {
        return dfDate.format(c.getTime());
    }

    /**
     * Get Calendar object for date.
     *
     * @param year  Year yyyy
     * @param month Month 1-12
     * @param day   Day of month 1-31
     * @return Calendar object
     */
    public static Calendar getCalendar(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c;
    }
}
