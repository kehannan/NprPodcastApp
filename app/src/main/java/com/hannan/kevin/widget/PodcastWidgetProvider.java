package com.hannan.kevin.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kehannan on 8/19/16.
 */
public class PodcastWidgetProvider extends AppWidgetProvider {

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
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}
