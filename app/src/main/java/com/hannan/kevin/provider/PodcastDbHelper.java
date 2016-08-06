package com.hannan.kevin.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hannan.kevin.provider.DatabaseContract.PodcastTable;

/**
 * Created by khannan on 7/3/16.
 */
public class PodcastDbHelper extends SQLiteOpenHelper{

    private static final String TAG = "PodcastDbHelper";

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "podcast.db";

    public PodcastDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Create table to hold podcasts
        final String SqlCreateScoresTable = "CREATE TABLE "
                + PodcastTable.PODCAST_TABLE
                + " (" + PodcastTable._ID + " INTEGER PRIMARY KEY, "
                + PodcastTable.TITLE + " TEXT, "
                + PodcastTable.AUDIO_HREF + " TEXT, "
                + PodcastTable.IMAGE_HREF + " TEXT, "
                + PodcastTable.PROGRAM + " TEXT, "
                + PodcastTable.DURATION + " TEXT, "
                + PodcastTable.PROG_DATE + " TEXT, "
                + PodcastTable.DESCRIPTION + " TEXT, "
                + PodcastTable.BRICK_HREF + " TEXT "
                + " );";

        Log.v(TAG, "SQL " + SqlCreateScoresTable);

        sqLiteDatabase.execSQL(SqlCreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + PodcastTable.PODCAST_TABLE);
    }
}
