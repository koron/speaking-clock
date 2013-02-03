package net.kaoriya.speakingclock;

import java.util.Calendar;

public final class Clock
{
    public static String getSpeakText() {
        return getSpeakText(Calendar.getInstance());
    }

    public static String getSpeakText(Calendar c) {
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        return String.format("%1$dじ%2$dふんです", hour, min);
    }
}
