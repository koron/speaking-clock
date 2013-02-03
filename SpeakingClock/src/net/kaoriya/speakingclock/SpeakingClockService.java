package net.kaoriya.speakingclock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public final class SpeakingClockService extends Service
    implements SpeakingClock
{
    private class Monitor extends BroadcastReceiver {

        private final IntentFilter filter = new IntentFilter();

        Monitor() {
            this.filter.addAction(Intent.ACTION_SCREEN_ON);
        }

        public void onReceive(Context context, Intent intent) {
            SpeakingClockService.this.onReceive(context, intent);
        }

        IntentFilter getFilter() {
            return this.filter;
        }
    }

    private Monitor monitor;

    private SpeakTask speakTask = null;

    // @implements Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        uninstallMonitor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        installMonitor();
        return START_STICKY;
    }

    private void installMonitor() {
        if (this.monitor == null) {
            Log.v(TAG, "installMonitor");
            this.monitor = new Monitor();
            registerReceiver(this.monitor, this.monitor.getFilter());
        }
    }

    private void uninstallMonitor() {
        if (this.monitor != null) {
            Log.v(TAG, "uninstallMonitor");
            unregisterReceiver(this.monitor);
            this.monitor = null;
        }
    }

    private void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "onReceive: " + action);
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            speak(context);
        }
    }

    private synchronized boolean speak(Context context)
    {
        if (this.speakTask != null) {
            return false;
        }
        this.speakTask = new SpeakTask(this) {
            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                SpeakingClockService.this.speakTask = null;
            }
        };
        this.speakTask.execute(Clock.getSpeakText());
        return true;
    }
}
