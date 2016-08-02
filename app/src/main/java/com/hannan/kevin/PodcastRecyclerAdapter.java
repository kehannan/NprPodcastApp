package com.hannan.kevin;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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

    // constants for the view types
//    private static final int VIEW_TYPE_COUNT = 2;
//    private static final int VIEW_TYPE_IMAGE = 0;
//    private static final int VIEW_TYPE_NO_IMAGE = 1;

    private Cursor mCursor;
    private Context context;
    public static final int COL_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_IMAGE = 3;
    public static final int COL_PROGRAM = 4;
    public static final int COL_DURATION = 5;

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
        public TextView tvProgram;
        public TextView tvDuration;

        public PodcastAdapterViewHolder(View view) {
            super(view);

            ivIcon = (ImageView) view.findViewById(R.id.podcast_image);
            tvProgram = (TextView) view.findViewById(R.id.program);
            tvTitle = (TextView) view.findViewById(R.id.podcast_title);
            tvDuration = (TextView) view.findViewById(R.id.duration);

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

    public static class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
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
        } else {
//            Picasso.with(context)
//                    .cancelRequest(podcastAdapterViewHolder.ivIcon);
            podcastAdapterViewHolder.ivIcon.setImageDrawable(null);
        }
        podcastAdapterViewHolder.tvTitle.setText(mCursor.getString(COL_TITLE));
        podcastAdapterViewHolder.tvProgram.setText(mCursor.getString(COL_PROGRAM));
        podcastAdapterViewHolder.tvDuration.setText(
                Util.formatDuration(mCursor.getString(COL_DURATION)));
        Log.v(TAG, "prog " + mCursor.getString(COL_PROGRAM));
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
