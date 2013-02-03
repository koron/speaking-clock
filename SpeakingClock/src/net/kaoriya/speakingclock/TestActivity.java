package net.kaoriya.speakingclock;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TestActivity extends Activity
{
    public static String TAG = "SpeakingClock";

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
}
