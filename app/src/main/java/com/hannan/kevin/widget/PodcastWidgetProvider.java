package com.hannan.kevin.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.hannan.kevin.R;

import com.hannan.kevin.MusicService;

/**
 * Created by kehannan on 8/19/16.
 */
public class PodcastWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "PodcastWidgetProvider";

    MusicService musicService;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {



        context.startService(new Intent(context, PodcastWidgetIntentService.class));

        // Get all widget ids
//        ComponentName thisWidget = new ComponentName(context,
//                PodcastWidgetProvider.class);
//        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the update service
//        Intent intent = new Intent(context.getApplicationContext(),
//                UpdateWidgetService.class);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
//        context.startService(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        ComponentName thisWidget = new ComponentName(context,
                PodcastWidgetProvider.class);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        String audio_href = intent.getStringExtra(MusicService.AUDIO_HREF);
        String play_state =  intent.getStringExtra(MusicService.PLAY_STATE);

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

            }
        }

        Log.v(TAG, "onReceive " + audio_href);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}
