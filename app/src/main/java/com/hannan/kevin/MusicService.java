package com.hannan.kevin;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kehannan on 8/20/16.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "MuscService";
    public static final String AUDIO_HREF = "audio_href";

    private String audio_href;
    private MediaPlayer mp;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        audio_href =(String) intent.getExtras().get(AUDIO_HREF);

        Log.v(TAG, audio_href);

        setup();
        return START_NOT_STICKY;
    }

    private void setup() {
        loadClip();
    }

    private void loadClip() {
        try {
            mp = new MediaPlayer();

            mp.setOnPreparedListener(this);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(this, Uri.parse(audio_href));
            mp.prepareAsync(); // prepare async to not block main thread
        }
        catch (Throwable t) {
            throwPlayerException(t);
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        play();
        mp.setOnCompletionListener(this);
    }

    private void play() {
        mp.start();
    }

    private void throwPlayerException(Throwable t) {
        Log.v(TAG, "exception " + t.toString());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
