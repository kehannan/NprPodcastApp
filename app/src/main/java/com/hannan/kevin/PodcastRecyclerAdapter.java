package com.hannan.kevin;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannan.kevin.provider.DatabaseContract;
import com.squareup.picasso.Picasso;


/**
 * Created by kehannan on 7/17/16.
 */
public class PodcastRecyclerAdapter
        extends RecyclerView.Adapter<PodcastRecyclerAdapter.PodcastAdapterViewHolder> {

    private static final String TAG = "PodcastRecyclerAdapter";

    private Cursor mCursor;
    private Context context;
    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_IMAGE = 3;

    // to hold reference to containing fragment
    public PodcastRecyclerAdapterOnClickHandler mClickHandler;

    public PodcastRecyclerAdapter(Context context, PodcastRecyclerAdapterOnClickHandler vh) {
        this.context = context;
        mClickHandler = vh;
    }

    // interface to pass click back to containing Fragment
    public static interface PodcastRecyclerAdapterOnClickHandler {
        public void onClick(Uri uri);
    }

    // Custom view holder to hold podcast items
    public class PodcastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivIcon;
        public TextView tvTitle;

        public PodcastAdapterViewHolder(View view) {
            super(view);

            ivIcon = (ImageView) view.findViewById(R.id.podcast_image);
            tvTitle = (TextView) view.findViewById(R.id.podcast_title);

            // Setting click listener on the view
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            // get podcast ID ans send to fragment
            Log.v(TAG, DatabaseContract.buildIndividualPodcast(mCursor.getInt(COL_ID)).toString());
            mClickHandler.onClick(DatabaseContract.buildIndividualPodcast(mCursor.getInt(COL_ID)));
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
