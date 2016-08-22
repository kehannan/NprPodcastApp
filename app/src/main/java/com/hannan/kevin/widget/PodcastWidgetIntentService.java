package com.hannan.kevin.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import com.hannan.kevin.R;
import com.hannan.kevin.MusicService;
import com.hannan.kevin.api.PodcastFetchService;
import com.hannan.kevin.provider.DatabaseContract;

/**
 * Created by kehannan on 8/20/16.
 */
public class PodcastWidgetIntentService extends IntentService {

    private static final String TAG = "PodcastWidgetIntentServ";

    public PodcastWidgetIntentService() {
        super ("PodcastWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.v(TAG, "onHandleIntent");

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                PodcastWidgetProvider.class));

        Cursor data = getContentResolver().query(
                DatabaseContract.PodcastTable.allPodcasts(),
                null,
                null,
                null,
                null
        );

        if (data == null) {
            updatePodcasts();
        }

        data.moveToFirst();

        //Extract data from cursor
        String title = data.getString(1);
        String audio_href = data.getString(2);

//        Intent updateWidgetIntent = new Intent(this, PodcastWidgetProvider.class);
//        updateWidgetIntent.putExtra("title", title);
//        updateWidgetIntent.putExtra(MusicService.AUDIO_HREF, audio_href);
//
//        PendingIntent pUpdateWidgetIntent =
//                PendingIntent.getBroadcast(this, 0, updateWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        try {
//            pUpdateWidgetIntent.send();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        for (int appWidgetId : appWidgetIds) {

            Log.v(TAG, "in addwidgetid for loop");

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);

            views.setTextViewText(R.id.title_widget, title);

            Intent updateWidgetIntent = new Intent(this, PodcastWidgetProvider.class);
            updateWidgetIntent.putExtra(MusicService.AUDIO_HREF, audio_href);

            PendingIntent pUpdateWidgetIntent =
                PendingIntent.getBroadcast(this, 0, updateWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//            Intent playPauseIntent = new Intent(this, MusicService.class);
//            playPauseIntent.putExtra(MusicService.AUDIO_HREF, audio_href);

//            PendingIntent playPausePendingIntent =
//                    PendingIntent.getService(this, 0, playPauseIntent, 0);


            views.setOnClickPendingIntent(R.id.play_pause_button_widget, pUpdateWidgetIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void updatePodcasts() {
        startService(new Intent(PodcastWidgetIntentService.this, PodcastFetchService.class));
                Log.v(TAG, "update podcast");
    }
}
