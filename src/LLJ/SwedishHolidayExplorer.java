package LLJ;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SwedishHolidayExplorer implements HolidayExplorer {

    private static class MonDay {
        public int month;
        public int day;

        public MonDay(int month, int day) {
            this.month = month;
            this.day = day;
        }
    }

    @Override
    public String getHoliday(Calendar cal) {
        return getHoliday(cal, ",");
    }

    @Override
    public String getHoliday(Calendar cal, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String s : getHolidayList(cal)) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(s);
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    @Override
    public List<String> getHolidayList(Calendar cal) {
        List<String> result = new ArrayList<String>();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (month == Calendar.JANUARY) {
            if (day == 1) {
                result.add("Nyårsdagen");
            } else if (day == 6) {
                result.add("Trettondagen");
            }
        } else if (month == Calendar.JUNE) {
            if (cal.get(Calendar.DAY_OF_MONTH) == 6) {
                result.add("Nationaldagen");
            }

            // Midsommardagen är den lördag som infaller under tiden den 20..26 juni
            Calendar c = new GregorianCalendar(year, Calendar.JUNE, 20);
            int msDay = (20 + (Calendar.SATURDAY - c.get(Calendar.DAY_OF_WEEK)));
            if (day == msDay) {
                result.add("Midsommardagen");
            } else if (day == msDay - 1) {
                result.add("Midsommarafton");
            }
        } else if (month == Calendar.DECEMBER) {
            if (day == 24) {
                result.add("Julafton");
            } else if (day == 25) {
                result.add("Juldagen");
            } else if (day == 26) {
                result.add("Annandagen");
            }
        }

        MonDay easterMd = easterday(year);
        Calendar easternDay = DateUtils.getCalendar(year, easterMd.month, easterMd.day);
        if (month == easternDay.get(Calendar.MONTH) && day == easternDay.get(Calendar.DAY_OF_MONTH)) {
            result.add("Påskdagen");
        }

        Calendar easternEvening = (Calendar) easternDay.clone();
        easternEvening.add(Calendar.DAY_OF_YEAR, -1);
        if (month == easternEvening.get(Calendar.MONTH) && day == easternEvening.get(Calendar.DAY_OF_MONTH)) {
            result.add("Påskafton");
        }

        Calendar easternFriday = (Calendar) easternDay.clone();
        easternFriday.add(Calendar.DAY_OF_YEAR, -2);
        if (month == easternFriday.get(Calendar.MONTH) && day == easternFriday.get(Calendar.DAY_OF_MONTH)) {
            result.add("Långfredag");
        }

        Calendar easternMonday = (Calendar) easternDay.clone();
        easternMonday.add(Calendar.DAY_OF_YEAR, 1);
        if (month == easternMonday.get(Calendar.MONTH) && day == easternMonday.get(Calendar.DAY_OF_MONTH)) {
            result.add("Annandag påsk");
        }

        {
            Calendar c = new GregorianCalendar(year, easterMd.month, easterMd.day);
            c.add(Calendar.DAY_OF_MONTH, 5 * 7 + 4);
            if (month == c.get(Calendar.MONTH) && day == c.get(Calendar.DAY_OF_MONTH)) {
                result.add("Kristi himmelsfärdsdag");
            }
        }

        {
            Calendar c = new GregorianCalendar(year, easterMd.month, easterMd.day);
            c.add(Calendar.DAY_OF_MONTH, 7 * 7);
            if (month == c.get(Calendar.MONTH) && day == c.get(Calendar.DAY_OF_MONTH)) {
                result.add("Pingstdagen");
            }
        }

        // Alla helgona är den lördag som infaller under tiden 31:a oktober..6:e november
        {
            Calendar ah = new GregorianCalendar(year, Calendar.OCTOBER, 31);
            int ahd = (Calendar.SATURDAY - ah.get(Calendar.DAY_OF_WEEK));
            if (ahd == 0 && month == Calendar.OCTOBER && day == 31 || month == Calendar.NOVEMBER && day == ahd) {
                result.add("Alla helgons dag");
            }
        }

        return result;
    }

    @Override
    public List<Holiday> getHolidays(int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_YEAR, 1);
        List<Holiday> result = new ArrayList<Holiday>();
        do {
            for (String s : getHolidayList(c)) {
                result.add(new Holiday((Calendar) c.clone(), s));
            }
            c.add(Calendar.DAY_OF_YEAR, 1);
        } while (c.get(Calendar.YEAR) == year);
        return result;
    }

    /**
     * Calculate Easter day
     *
     * @param y Year, four digits
     * @return Month (1..11) and day (1..31) in month of Easter this year
     *         <p/>
     *         See http://aa.usno.navy.mil/faq/docs/easter.html
     */
    private static MonDay easterday(int y) {
        int c = y / 100;
        int n = y - 19 * (y / 19);
        int k = (c - 17) / 25;
        int i = c - c / 4 - (c - k) / 3 + 19 * n + 15;
        i = i - 30 * (i / 30);
        i = i - (i / 28) * (1 - (i / 28) * (29 / (i + 1))
                * ((21 - n) / 11));
        int j = y + y / 4 + i + 2 - c + c / 4;
        j = j - 7 * (j / 7);
        int l = i - j;
        int m = 3 + (l + 40) / 44; // month
        int d = l + 28 - 31 * (m / 4); // day

        return new MonDay(m - 1/*Jan=0 in Calendar*/, d);
    }
}