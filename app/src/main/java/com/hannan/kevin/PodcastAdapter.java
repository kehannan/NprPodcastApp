package com.hannan.kevin;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by kehannan on 7/6/16.
 */
public class PodcastAdapter extends CursorAdapter {

    public static final int COL_TITLE = 1;

    public PodcastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View mItem = LayoutInflater.from(context).inflate(R.layout.podcast_list_item, parent, false);
        return mItem;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvTitle = (TextView)view.findViewById(R.id.podcast_title);
        String title = cursor.getString(COL_TITLE);

        tvTitle.setText(title);

    }
}
