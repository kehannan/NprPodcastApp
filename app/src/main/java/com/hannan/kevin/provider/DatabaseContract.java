package com.hannan.kevin.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by khannan on 7/2/16.
 */
public class DatabaseContract {


    //URI data
    public static final String CONTENT_AUTHORITY = "com.hannan.kevin.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PODCASTS = "podcasts";

    public static final class PodcastTable implements BaseColumns {

        public static final String PODCAST_TABLE = "podcasts_table";

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String AUDIO_HREF = "audio_href";
        public static final String IMAGE_HREF = "image_href";

        public static Uri allPodcasts() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PODCASTS).build();
        }

    }
}
