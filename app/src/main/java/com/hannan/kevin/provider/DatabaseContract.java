package com.hannan.kevin.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by khannan on 7/2/16.
 */
public class DatabaseContract {

    private static final String TAG = "DatabaseContract";


    //URI data
    public static final String CONTENT_AUTHORITY = "com.hannan.kevin.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PODCASTS = "podcasts";

    public static final class PodcastTable implements BaseColumns {

        public static final String PODCAST_TABLE = "podcasts_table";

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String PROGRAM = "program";
        public static final String AUDIO_HREF = "audio_href";
        public static final String IMAGE_HREF = "image_href";
        public static final String DURATION = "duration";
        public static final String PROG_DATE = "date";
        public static final String DESCRIPTION = "description";
        public static final String BRICK_HREF = "brick_href";

        public static Uri allPodcasts() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PODCASTS).build();
        }
    }

    public static Uri buildIndividualPodcast (int id) {
        Uri uri = PodcastTable.allPodcasts();
        return ContentUris.withAppendedId(uri, id);
    }

    public static int getIdFromUri(Uri uri) {
        Log.v(TAG, (uri.getPathSegments().get(1)));
        return (Integer.parseInt(uri.getPathSegments().get(1)));
    }
}
