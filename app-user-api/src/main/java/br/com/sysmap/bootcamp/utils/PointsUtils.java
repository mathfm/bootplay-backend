package br.com.sysmap.bootcamp.utils;

import java.time.DayOfWeek;

public class PointsUtils {
    public static long getPoints(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> 7L;
            case TUESDAY -> 6L;
            case WEDNESDAY -> 2L;
            case THURSDAY -> 10L;
            case FRIDAY -> 15L;
            case SATURDAY -> 20L;
            default -> 25L;
        };
    }
}
