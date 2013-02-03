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
        if (min == 0) {
            return String.format("%1$dじちょうどです", hour);
        } else {
            String minText = "ふん";
            switch (min % 10) {
                case 0: case 1: case 3: case 4: case 6:
                    minText = "ぷん";
                    break;
            }
            return String.format("%1$dじ%2$d%3$sです", hour, min, minText);
        }
    }
}
