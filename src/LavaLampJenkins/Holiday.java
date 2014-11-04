package LavaLampJenkins;

import java.util.Calendar;

public class Holiday {
    private Calendar date;
    private String name;

    public Holiday(Calendar date, String name) {
        this.date = date;
        this.name = name;
    }

    public Calendar getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "date=" + DateUtils.formatDateIso8601(date) +
                ", name='" + name + '\'' +
                '}';
    }
}
