package net.kaoriya.speakingclock;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class TestActivity extends Activity implements SpeakingClock
{
    private final ArrayList<View> speakButtons = new ArrayList<View>();

    private TimePicker picker;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        this.speakButtons.add(findViewById(R.id.speak_clock));
        this.speakButtons.add(findViewById(R.id.speak_pick_clock));
        this.picker = (TimePicker)findViewById(R.id.time_picker);
        this.picker.setIs24HourView(true);
        this.picker.setCurrentHour(
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    public void onClickSpeakClock(final View view) {
        speak(Clock.getSpeakText());
    }

    public void onClickSpeakPickClock(final View view) {
        int hour = this.picker.getCurrentHour();
        int min = this.picker.getCurrentMinute();
        speak(Clock.getSpeakText(hour, min));
    }

    public void onClickStartMonitor(View view) {
        startService(new Intent(this, SpeakingClockService.class));
    }

    public void onClickStopMonitor(View view) {
        stopService(new Intent(this, SpeakingClockService.class));
    }

    private void speak(String text) {
        SpeakTask task = new SpeakTask(this) {
            @Override
            public void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                setSpeakEnabled(true);
            }
        };
        setSpeakEnabled(false);
        task.execute(text);
    }

    private void setSpeakEnabled(boolean value) {
        for (View view : this.speakButtons) {
            view.setEnabled(value);
        }
    }
}
