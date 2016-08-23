package com.hannan.kevin;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by kehannan on 8/20/16.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "MusicService";
    public static final String AUDIO_HREF = "audio_href";

    public static final String IS_PLAYING = "is_playing";

    public static final String PAUSE_INTENT = "com.hannan.kevin.PAUSE_INTENT";
    public static final String PAUSE = "pause";
    public static final String PLAY = "play";

    private String audio_href;
    private MediaPlayer mp;

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");

        EventBus.getDefault().register(this);

        BroadcastReceiver onNotice = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.v(TAG, "onReceive");

                String extra = intent.getAction();
                Log.v(TAG, extra);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(PAUSE_INTENT);

        registerReceiver(onNotice, filter);
    }

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
        isPlayingCallback();
    }

    private void throwPlayerException(Throwable t) {
        Log.v(TAG, "exception " + t.toString());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    private void isPlayingCallback() {
        Intent intent = new Intent(IS_PLAYING);
        sendBroadcast(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        Log.v(TAG, event.message);

        if (event.message.equals(PAUSE)) {
            mp.pause();
        }

        if (event.message.equals(PLAY)) {
            play();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
