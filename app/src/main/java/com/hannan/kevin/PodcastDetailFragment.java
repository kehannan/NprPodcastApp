package com.hannan.kevin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PodcastDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, MediaPlayer.OnCompletionListener {

    private static final String TAG = "PodcastDetailFragment";
    private Uri mUri;
    private static final int PODCAST_DETAIL_LOADER = 0;
    private static int THIRTY_SECONDS = 30 * 1000; // in milliseconds

    TextView podcast_title_tv;

    private ImageButton play_pause;
    private ImageButton forward30;
    private ImageButton back30;
    private ImageButton pause;
    private ImageButton stop;
    private MediaPlayer mp;

    private Uri audioHref;
    private Uri imageHref;

    private ImageView mPhotoView;
    private TextView mProgramView;
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mDateView;
    private TextView mDurationView;

    CollapsingToolbarLayout toolbarLayout;


    public PodcastDetailFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve bundle .
        Bundle args = getArguments();

        mUri = Uri.parse(args.getString(PodcastSummaryFragment.PODCAST_ID));

        Log.v(TAG, "podcast id " + mUri.toString());

        View rootView = inflater.inflate(R.layout.fragment_podcast_detail, container, false);


        // loader
        getLoaderManager().initLoader(PODCAST_DETAIL_LOADER, null, this);

        mPhotoView = (ImageView) rootView.findViewById(R.id.photo);
        mProgramView = (TextView) rootView.findViewById(R.id.program);
        mTitleView = (TextView) rootView.findViewById(R.id.title);
        mDescriptionView = (TextView) rootView.findViewById(R.id.description);
        mDateView = (TextView) rootView.findViewById(R.id.podcast_date);
        mDurationView = (TextView) rootView.findViewById(R.id.duration);

        play_pause = (ImageButton)rootView.findViewById(R.id.play_pause_button);
        forward30 = (ImageButton)rootView.findViewById(R.id.forward_30);
        back30 = (ImageButton)rootView.findViewById(R.id.back_30);

        // set color of buttons
        play_pause.setColorFilter(ContextCompat.getColor(getActivity(), R.color.DarkGrey));
        forward30.setColorFilter(ContextCompat.getColor(getActivity(), R.color.DarkGrey));
        back30.setColorFilter(ContextCompat.getColor(getActivity(), R.color.DarkGrey));

        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (mp.isPlaying()){
                    pause();
                    play_pause.setImageResource(R.drawable.ic_pause);
                } else {
                    play();
                    play_pause.setImageResource(R.drawable.ic_play_arrow);
                }
            }
        });

        forward30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                forward30();
            }
        });

        toolbarLayout = (CollapsingToolbarLayout) rootView
                .findViewById(R.id.collapsing_toolbar_layout);

        // Implementing up arrow
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);

        return rootView;
    }

    private void play() {
        mp.start();

        int currentPosition = mp.getCurrentPosition();
        int duration = mp.getDuration();

        Log.v(TAG, "current position" + currentPosition);
        Log.v(TAG, "duration" + duration);
        play_pause.setEnabled(true);
    }

    private void forward30() {
        Log.v(TAG, "forward30");
        int currentPosition = mp.getCurrentPosition();
        mp.seekTo(currentPosition + THIRTY_SECONDS);
    }

    private void stop() {

       mp.stop();
//        stop.setEnabled(false);
//
//        try {
//            mp.prepare();
//            mp.seekTo(0);
//            play_pause.setEnabled(true);
//        }
//        catch (Throwable t) {
//            goBlooey(t);
//        }
    }

    private void pause() {
        mp.pause();

    }

    private void setup() {
        loadClip();
        play_pause.setEnabled(true);
        forward30.setEnabled(true);
    }

    private void loadClip() {
        try {
            mp=MediaPlayer.create(getActivity(), audioHref);
            mp.setOnCompletionListener(this);
        }
        catch (Throwable t) {
            goBlooey(t);
        }
    }

    private void goBlooey(Throwable t) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder
                .setTitle("Exception!")
                .setMessage(t.toString())
                .setPositiveButton("OK", null)
                .show();
    }


    // Loader to get selected podcast
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),                                  // parent activity context
                mUri,                                           // Table to query
                null,                                           // projection
                null,                                           // selection clause
                null,                                           // selection argument
                null                                            // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "onLoadFinished()");

        data.moveToFirst();

        mProgramView.setText(data.getString(4));
        mTitleView.setText(data.getString(1));
        mDescriptionView.setText(data.getString(7));
        mDateView.setText(data.getString(6));
        mDurationView.setText(data.getString(5));

        audioHref = Uri.parse(data.getString(2));

        String imageHref = data.getString(3);
        if (imageHref != null) {
            Picasso.with(getActivity()).load(imageHref)
                    .fit()
                    .into(mPhotoView);
        }
        setup();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }
}
