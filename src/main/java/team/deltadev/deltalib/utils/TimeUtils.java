package team.deltadev.deltalib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeUtils {

    private static final Map<String, String> TIME_UNITS = new HashMap<>();

    static {
        TIME_UNITS.put("s", "second");
        TIME_UNITS.put("m", "minute");
        TIME_UNITS.put("h", "hour");
        TIME_UNITS.put("d", "day");
        TIME_UNITS.put("w", "week");
        TIME_UNITS.put("M", "month");
        TIME_UNITS.put("y", "year");
        TIME_UNITS.put("ms", "millisecond");
    }

    /**
     * Converts a short time unit (e.g., "1d", "2h") to a full-formatted string (e.g., "1 day", "2 hours").
     *
     * @param shortTime The short time unit string.
     * @return The full-formatted time string.
     */
    public static String formatShortTime(String shortTime) {
        if (shortTime == null || shortTime.length() < 2) {
            throw new IllegalArgumentException("Invalid short time format.");
        }

        String unit = shortTime.substring(shortTime.length() - 2);
        if (!TIME_UNITS.containsKey(unit)) {
            unit = shortTime.substring(shortTime.length() - 1);
        }

        String valueStr = shortTime.substring(0, shortTime.length() - unit.length());

        int value;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in short time format.", e);
        }

        String unitFull = TIME_UNITS.get(unit);
        if (unitFull == null) {
            throw new IllegalArgumentException("Invalid time unit in short time format.");
        }

        return value + " " + unitFull + (value > 1 ? "s" : "");
    }

    /**
     * Converts a duration string to its equivalent in seconds.
     *
     * @param durationStr The duration string (e.g., "1d", "2h").
     * @return The duration in seconds.
     */
    public static long convertToSeconds(String durationStr) {
        if (durationStr == null || durationStr.length() < 2) {
            throw new IllegalArgumentException("Invalid duration format.");
        }

        String unit = durationStr.substring(durationStr.length() - 2);
        if (!TIME_UNITS.containsKey(unit)) {
            unit = durationStr.substring(durationStr.length() - 1);
        }

        String valueStr = durationStr.substring(0, durationStr.length() - unit.length());

        int value;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in duration format.", e);
        }

        switch (unit) {
            case "s": return value;
            case "m": return value * 60;
            case "h": return value * 3600;
            case "d": return value * 86400;
            case "w": return value * 604800;
            case "M": return value * 2592000;
            case "y": return value * 31536000;
            default: throw new IllegalArgumentException("Invalid time unit in duration format.");
        }
    }

    /**
     * Converts seconds to a full-formatted time string (e.g., "1 day", "2 hours").
     *
     * @param seconds The time in seconds.
     * @return The full-formatted time string.
     */
    public static String formatSeconds(long seconds) {
        if (seconds < 60) {
            return seconds + " second" + (seconds == 1 ? "" : "s");
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minute" + (minutes == 1 ? "" : "s");
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " hour" + (hours == 1 ? "" : "s");
        } else if (seconds < 604800) {
            long days = seconds / 86400;
            return days + " day" + (days == 1 ? "" : "s");
        } else if (seconds < 2592000) {
            long weeks = seconds / 604800;
            return weeks + " week" + (weeks == 1 ? "" : "s");
        } else if (seconds < 31536000) {
            long months = seconds / 2592000;
            return months + " month" + (months == 1 ? "" : "s");
        } else {
            long years = seconds / 31536000;
            return years + " year" + (years == 1 ? "" : "s");
        }
    }

    /**
     * Pauses the current thread for a specified duration in milliseconds.
     *
     * @param milliseconds The duration to pause in milliseconds.
     */
    public static void sleepMillis(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Pauses the current thread for a specified duration in seconds.
     *
     * @param seconds The duration to pause in seconds.
     */
    public static void sleepSeconds(long seconds) {
        sleepMillis(seconds * 1000);
    }

    /**
     * Formats a Date object to a string with the specified pattern.
     *
     * @param date    The Date object.
     * @param pattern The pattern to format the date.
     * @return The formatted date string.
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * Parses a date string to a Date object with the specified pattern.
     *
     * @param dateString The date string to parse.
     * @param pattern    The pattern to parse the date string.
     * @return The corresponding Date object.
     */
    public static Date parseDate(String dateString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a duration string to its equivalent in milliseconds.
     *
     * @param durationStr The duration string (e.g., "1d", "2h").
     * @return The duration in milliseconds.
     */
    public static long convertToMilliseconds(String durationStr) {
        if (durationStr == null || durationStr.length() < 2) {
            throw new IllegalArgumentException("Invalid duration format.");
        }

        String unit = durationStr.substring(durationStr.length() - 2);
        if (!TIME_UNITS.containsKey(unit)) {
            unit = durationStr.substring(durationStr.length() - 1);
        }

        String valueStr = durationStr.substring(0, durationStr.length() - unit.length());

        int value;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in duration format.", e);
        }

        switch (unit) {
            case "s": return value * 1000L;
            case "m": return value * 60000L;
            case "h": return value * 3600000L;
            case "d": return value * 86400000L;
            case "w": return value * 604800000L;
            case "M": return value * 2592000000L;
            case "y": return value * 31536000000L;
            case "ms": return value;
            default: throw new IllegalArgumentException("Invalid time unit in duration format.");
        }
    }

    /**
     * Converts milliseconds to a full-formatted time string (e.g., "1 day", "2 hours").
     *
     * @param milliseconds The time in milliseconds.
     * @return The full-formatted time string.
     */
    public static String formatMilliseconds(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + " millisecond" + (milliseconds == 1 ? "" : "s");
        } else if (milliseconds < 60000) {
            long seconds = milliseconds / 1000;
            return seconds + " second" + (seconds == 1 ? "" : "s");
        } else if (milliseconds < 3600000) {
            long minutes = milliseconds / 60000;
            return minutes + " minute" + (minutes == 1 ? "" : "s");
        } else if (milliseconds < 86400000) {
            long hours = milliseconds / 3600000;
            return hours + " hour" + (hours == 1 ? "" : "s");
        } else if (milliseconds < 604800000) {
            long days = milliseconds / 86400000;
            return days + " day" + (days == 1 ? "" : "s");
        } else if (milliseconds < 2592000000L) {
            long weeks = milliseconds / 604800000;
            return weeks + " week" + (weeks == 1 ? "" : "s");
        } else if (milliseconds < 31536000000L) {
            long months = milliseconds / 2592000000L;
            return months + " month" + (months == 1 ? "" : "s");
        } else {
            long years = milliseconds / 31536000000L;
            return years + " year" + (years == 1 ? "" : "s");
        }
    }
}
