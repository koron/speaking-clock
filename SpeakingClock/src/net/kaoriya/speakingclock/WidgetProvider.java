package net.kaoriya.speakingclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public final class WidgetProvider extends AppWidgetProvider
    implements SpeakingClock
{
    // @implements AppWidgetProvider
    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.startService(new Intent(context, SpeakingClockService.class));
    }

    // @implements AppWidgetProvider
    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.stopService(new Intent(context, SpeakingClockService.class));
    }

    // @implements AppWidgetProvider
    public void onUpdate(
            Context context,
            AppWidgetManager manager,
            int[] ids)
    {
        super.onUpdate(context, manager, ids);
        context.startService(new Intent(context, SpeakingClockService.class));
    }

}
