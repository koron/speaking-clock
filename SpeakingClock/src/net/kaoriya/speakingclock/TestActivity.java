package net.kaoriya.speakingclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends Activity implements SpeakingClock
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }

    public void onClickSpeakClock(final View view)
    {
        SpeakTask task = new SpeakTask(this) {
            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                view.setEnabled(true);
            }
        };
        view.setEnabled(false);
        task.execute(Clock.getSpeakText());
    }

    public void onClickStartMonitor(View view)
    {
        startService(new Intent(this, SpeakingClockService.class));
    }

    public void onClickStopMonitor(View view)
    {
        stopService(new Intent(this, SpeakingClockService.class));
    }
}
