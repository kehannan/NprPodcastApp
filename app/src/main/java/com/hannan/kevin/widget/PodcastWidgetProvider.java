package com.hannan.kevin.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.hannan.kevin.MessageEvent;
import com.hannan.kevin.MusicService;
import com.hannan.kevin.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kehannan on 8/19/16.
 */
public class PodcastWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "PodcastWidgetProvider";
    private static final String PLAY_PAUSE_ACTION = "play_pause_action";

    MusicService mMusicService;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, PodcastWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        ComponentName thisWidget = new ComponentName(context,
                PodcastWidgetProvider.class);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        String audio_href = intent.getStringExtra(MusicService.AUDIO_HREF);
        String play_pause_state =  intent.getStringExtra(PodcastWidgetProvider.PLAY_PAUSE_ACTION);

        Log.v(TAG, "play_pause_state " + play_pause_state);

        // if getting audio url, start music service
        if (audio_href != null) {

            Intent startMusicIntent = new Intent(context, MusicService.class);
            startMusicIntent.putExtra(MusicService.AUDIO_HREF, audio_href);

            PendingIntent startMusicPendingIntent =
                    PendingIntent.getService(context, 0, startMusicIntent, 0);

            try {
                startMusicPendingIntent.send();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int appWidgetId : appWidgetIds) {

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                views.setImageViewResource(R.id.play_pause_button_widget, R.drawable.ic_pause);
                appWidgetManager.updateAppWidget(appWidgetId, views);

                Intent clickListenerIntent = new Intent(context, PodcastWidgetProvider.class);
                clickListenerIntent.putExtra(PLAY_PAUSE_ACTION, "pause");

                PendingIntent clickListenerPendingIntent =
                        PendingIntent.getBroadcast(context, 0, clickListenerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.play_pause_button_widget, clickListenerPendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }

        if (play_pause_state != null ) {

            if (play_pause_state.equals("pause")) {
                Log.v(TAG, "paused");

                for (int appWidgetId : appWidgetIds) {

                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                    views.setImageViewResource(R.id.play_pause_button_widget, R.drawable.ic_play_arrow);
                    appWidgetManager.updateAppWidget(appWidgetId, views);

                    Intent clickListenerIntent = new Intent(context, PodcastWidgetProvider.class);
                    clickListenerIntent.putExtra(PLAY_PAUSE_ACTION, "play");

                    PendingIntent clickListenerPendingIntent =
                            PendingIntent.getBroadcast(context, 0, clickListenerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.play_pause_button_widget, clickListenerPendingIntent);
                }

                EventBus.getDefault().post(new MessageEvent(MusicService.PAUSE));

//                Intent pauseIntent = new Intent();
//                intent.setAction(MusicService.PAUSE_INTENT);
//
//                context.sendBroadcast(pauseIntent);


            }

            if (play_pause_state.equals("play")) {

                for (int appWidgetId : appWidgetIds) {

                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                    views.setImageViewResource(R.id.play_pause_button_widget, R.drawable.ic_pause);
                    appWidgetManager.updateAppWidget(appWidgetId, views);

                    Intent clickListenerIntent = new Intent(context, PodcastWidgetProvider.class);
                    clickListenerIntent.putExtra(PLAY_PAUSE_ACTION, "pause");

                    PendingIntent clickListenerPendingIntent =
                            PendingIntent.getBroadcast(context, 0, clickListenerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.play_pause_button_widget, clickListenerPendingIntent);
                }

                EventBus.getDefault().post(new MessageEvent(MusicService.PLAY));

            }
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}
