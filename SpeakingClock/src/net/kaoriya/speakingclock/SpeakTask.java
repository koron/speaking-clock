package net.kaoriya.speakingclock;

import android.os.AsyncTask;
import android.content.Context;
import android.speech.tts.TextToSpeech;

public class SpeakTask extends AsyncTask<String, Void, Boolean>
    implements TextToSpeech.OnInitListener
{
    public static final String TAG = "SpeakTask";

    public static final long PREPARE_TIMEOUT = 3000;
    public static final long SPEAK_TIMEOUT = 10000;

    private enum EngineStatus { NONE, PREPARED, FAILED }

    private final Context context;

    private final Object lockObj = new Object();

    private TextToSpeech tts = null;

    private volatile EngineStatus status = EngineStatus.NONE;

    public SpeakTask(Context context)
    {
        this.context = context;
    }

    public Boolean doInBackground(String... params)
    {
        String text = params[0];
        if (text != null && text.length() > 0 && waitPrepared()) {
            this.tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            waitSpoken();
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public void onPreExecute()
    {
        super.onPreExecute();
        this.tts = new TextToSpeech(this.context.getApplicationContext(),
                this);
    }

    @Override
    public void onPostExecute(Boolean result)
    {
        if (this.tts != null) {
            try {
                this.tts.shutdown();
            } catch (Exception e) {
            }
            this.tts = null;
        }
    }

    // @implements TextToSpeech.OnInitListener
    public void onInit(int status)
    {
        synchronized (this.lockObj) {
            this.status = (status == TextToSpeech.SUCCESS)
                ? EngineStatus.PREPARED : EngineStatus.FAILED;
            this.lockObj.notifyAll();
        }
    }

    private boolean waitPrepared()
    {
        if (this.status == EngineStatus.NONE) {
            synchronized (this.lockObj) {
                try {
                    this.lockObj.wait(PREPARE_TIMEOUT);
                } catch (InterruptedException e) {
                }
            }
        }
        return this.status == EngineStatus.PREPARED ? true : false;
    }

    private boolean waitSpoken()
    {
        long start = System.currentTimeMillis();
        while (this.tts.isSpeaking() &&
                (System.currentTimeMillis() - start) < SPEAK_TIMEOUT)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        return !this.tts.isSpeaking();
    }
}
