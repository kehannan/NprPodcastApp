package com.hannan.kevin;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by kehannan on 7/6/16.
 */
public class PodcastAdapter extends CursorAdapter {

    private static final String TAG = "PodcastAdapter";

    public static final int COL_TITLE = 1;
    public static final int COL_IMAGE = 3;

    private Context context;

    public PodcastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View mItem = LayoutInflater.from(context).inflate(R.layout.podcast_list_item, parent, false);
        return mItem;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvTitle = (TextView)view.findViewById(R.id.podcast_title);
        ImageView ivImage = (ImageView)view.findViewById(R.id.podcast_image);

        String imageHref = cursor.getString(COL_IMAGE);
        boolean isNull = (imageHref == null);
        Log.v(TAG, "image Href is null? " + isNull);
        Log.v(TAG, "Href " + imageHref);

        if (imageHref != null) {
            Picasso.with(context).load(imageHref).into(ivImage);
        }

        String title = cursor.getString(COL_TITLE);

        tvTitle.setText(title);

    }
}
