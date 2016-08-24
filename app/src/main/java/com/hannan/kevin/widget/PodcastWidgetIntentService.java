package com.hannan.kevin.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import com.hannan.kevin.MusicService;
import com.hannan.kevin.R;
import com.hannan.kevin.api.PodcastFetchService;
import com.hannan.kevin.data.DatabaseContract;

/**
 * Created by kehannan on 8/20/16.
 */
public class PodcastWidgetIntentService extends IntentService {

    private static final String TAG = "PodcastWidgetIntentServ";
    public static final int COL_TITLE = 1;
    public static final int COL_AUDIO_HREF = 2;


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
        String title = data.getString(COL_TITLE);
        String audio_href = data.getString(COL_AUDIO_HREF);

        for (int appWidgetId : appWidgetIds) {

            Log.v(TAG, "in addwidgetid for loop");

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);

            views.setTextViewText(R.id.title_widget, title);
            views.setImageViewResource(R.id.play_pause_button_widget, R.drawable.ic_play_arrow);
            views.setInt(R.id.play_pause_button_widget, "setColorFilter", Color.WHITE);

            Intent updateWidgetIntent = new Intent(this, PodcastWidgetProvider.class);
            updateWidgetIntent.putExtra(MusicService.AUDIO_HREF, audio_href);

            PendingIntent pUpdateWidgetIntent =
                PendingIntent.getBroadcast(this, 0, updateWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.play_pause_button_widget, pUpdateWidgetIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void updatePodcasts() {
        startService(new Intent(PodcastWidgetIntentService.this, PodcastFetchService.class));
                Log.v(TAG, "update podcast");
    }
}
