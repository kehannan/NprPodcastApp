package com.hannan.kevin;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by kehannan on 7/17/16.
 */
public class PodcastRecyclerAdapter
        extends RecyclerView.Adapter<PodcastRecyclerAdapter.PodcastAdapterViewHolder> {

    private static final String TAG = "PodcastRecyclerAdapter";

    private Cursor mCursor;
    private Context context;
    public static final int COL_TITLE = 1;
    public static final int COL_IMAGE = 3;

    public PodcastRecyclerAdapter(Context context) {
        this.context = context;
    }

    public class PodcastAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivIcon;
        public TextView tvTitle;

        public PodcastAdapterViewHolder(View view) {
            super(view);

            ivIcon = (ImageView) view.findViewById(R.id.podcast_image);
            tvTitle = (TextView) view.findViewById(R.id.podcast_title);
        }
    }

    @Override
    public PodcastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.podcast_list_item_recycler, viewGroup, false);

        return new PodcastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PodcastAdapterViewHolder podcastAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);

        String imageHref = mCursor.getString(COL_IMAGE);

        boolean isNull = (imageHref == null);
        Log.v(TAG, "image Href is null? " + isNull);
        Log.v(TAG, "Href " + imageHref);

        if (imageHref !=null ) {
            Picasso.with(context).load(imageHref)
                    .fit()
                    .into(podcastAdapterViewHolder.ivIcon);
        }

        podcastAdapterViewHolder.tvTitle.setText(mCursor.getString(COL_TITLE));
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        Log.v(TAG, "swapCursor()");
    }

    public Cursor getCursor() {
        return mCursor;
    }


}
