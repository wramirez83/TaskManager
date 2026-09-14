package co.edu.poli.taskmanager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {

    private static final Locale ES = Locale.forLanguageTag("es-CO");

    private DateUtils() {
    }

    public static long startOfDay(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long endOfDay(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startOfDay(millis));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    public static String greetingDate() {
        String raw = new SimpleDateFormat("EEE d MMM yyyy", ES).format(new Date());
        return raw.substring(0, 1).toUpperCase(ES) + raw.substring(1);
    }

    public static String shortDate(long millis) {
        if (millis <= 0) {
            return "Sin fecha";
        }
        return new SimpleDateFormat("d MMM yyyy", ES).format(new Date(millis));
    }

    public static String taskDate(long millis) {
        if (millis <= 0) {
            return "Sin fecha";
        }
        long start = startOfDay(System.currentTimeMillis());
        long tomorrow = start + 24L * 60L * 60L * 1000L;
        String time = new SimpleDateFormat("HH:mm", ES).format(new Date(millis));
        if (millis >= start && millis < endOfDay(start)) {
            return "Hoy, " + time;
        }
        if (millis >= tomorrow && millis < endOfDay(tomorrow)) {
            return "Mañana, " + time;
        }
        return new SimpleDateFormat("d MMM, HH:mm", ES).format(new Date(millis));
    }

    public static boolean isToday(long millis) {
        if (millis <= 0) {
            return false;
        }
        long start = startOfDay(System.currentTimeMillis());
        return millis >= start && millis < endOfDay(start);
    }
}
