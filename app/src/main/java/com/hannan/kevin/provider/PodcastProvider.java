package com.hannan.kevin.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by khannan on 7/2/16.
// */


public class PodcastProvider extends ContentProvider {

    private static PodcastDbHelper sPodcastDbHelper;

    private static final String TAG = "PodcastProvider";

    // URIMatcher constants
    static final int PODCASTS = 100;
    static final int INDIVIDUAL_PODCAST = 101;

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static SQLiteQueryBuilder sQueryBuilder;

    // Selections
    private static final String sPodcastSelection =
            DatabaseContract.PodcastTable.PODCAST_TABLE +
                    "." + DatabaseContract.PodcastTable._ID + " = ? ";

    static UriMatcher buildUriMatcher() {

        sQueryBuilder = new SQLiteQueryBuilder();

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY; // com.hannan.kevin.provider

        matcher.addURI(authority, DatabaseContract.PODCASTS, PODCASTS); //authority, podcasts, 100
        matcher.addURI(authority, DatabaseContract.PODCASTS + "/#", INDIVIDUAL_PODCAST);

        Log.v(TAG, "authority (in build matcher) " + authority);
        Log.v(TAG, "path " + DatabaseContract.PODCASTS);
        return matcher;
    }

    private Cursor getIndividualPodcast(Uri uri) {
        int id = DatabaseContract.getIdFromUri(uri);

        return sPodcastDbHelper.getReadableDatabase().query(
                DatabaseContract.PodcastTable.PODCAST_TABLE,
                null,
                sPodcastSelection,
                new String[] {String.valueOf(id)},
                null,
                null,
                null
        );

//        return sQueryBuilder.query(sPodcastDbHelper.getReadableDatabase(),
//                DatabaseContract.PodcastTable.PODCAST_TABLE,
//                null,                               // projection
//                sPodcastSelection,                  // where clause
//                new String[] {String.valueOf(id)},  // where arguments
//                null,
//                null,
//                null
//        );
    }

    @Override
    public boolean onCreate() {

        sPodcastDbHelper = new PodcastDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        final int match = sUriMatcher.match(uri);
        Log.v(TAG, "match int " + match);
        switch (match) {
            case PODCASTS:
                retCursor = sPodcastDbHelper.getReadableDatabase().query(
                        DatabaseContract.PodcastTable.PODCAST_TABLE,
                        null,null,null,null,null,null);
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return retCursor;

            case INDIVIDUAL_PODCAST:
                retCursor = getIndividualPodcast(uri);
                return retCursor;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        Log.v(TAG, "bulk insert()");
        Log.v(TAG, "Uri " + uri.toString());

        final SQLiteDatabase db = sPodcastDbHelper.getWritableDatabase();
        db.delete(DatabaseContract.PodcastTable.PODCAST_TABLE,null,null);

        final int match = sUriMatcher.match(uri);
        Log.v(TAG, "match int " + match);
        switch (match) {
            case PODCASTS:

                Log.v(TAG, "in PODCASTS switch");
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DatabaseContract.PodcastTable.PODCAST_TABLE, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
