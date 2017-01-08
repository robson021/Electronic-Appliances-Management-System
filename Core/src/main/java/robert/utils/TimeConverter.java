package robert.utils;

public class TimeConverter {

    public static long convertMillisToMinutes(long timeInMillis) {
        return timeInMillis / 1000 / 60;
    }

    public static long convertMillisToMinutes(long from, long to) {
        return convertMillisToMinutes(to - from);
    }
}
