package com.hannan.kevin;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import android.support.v7.graphics.Palette;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PodcastDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, MediaPlayer.OnCompletionListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener {

    private static final String TAG = "PodcastDetailFragment";

    // UI elements

    private LinearLayout mLinearLayout;

    Toolbar toolbar;
    private ImageView mPhotoView;
    private TextView mProgramView;
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mDateView;
    private TextView mDurationView;

    private SeekBar songProgressBar;
    private ImageButton play_pause;
    private ImageButton forward30;
    private ImageButton back30;

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    private Util utils;
    private Uri mUri;
    private static final int PODCAST_DETAIL_LOADER = 0;
    private static int THIRTY_SECONDS = 30 * 1000; // in milliseconds
    private MediaPlayer mp;
    private Uri audioHref;

    View rootView;

    private boolean isReady = false;

    public PodcastDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve bundle.
        Bundle args = getArguments();

        mUri = Uri.parse(args.getString(PodcastSummaryFragment.PODCAST_ID));

        rootView = inflater.inflate(R.layout.fragment_podcast_detail, container, false);

        // loader
        getLoaderManager().initLoader(PODCAST_DETAIL_LOADER, null, this);

        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.detail_frag);

        mPhotoView = (ImageView) rootView.findViewById(R.id.photo);
        mProgramView = (TextView) rootView.findViewById(R.id.program);
        mTitleView = (TextView) rootView.findViewById(R.id.title);
        mDescriptionView = (TextView) rootView.findViewById(R.id.description);
        mDateView = (TextView) rootView.findViewById(R.id.podcast_date);
        mDurationView = (TextView) rootView.findViewById(R.id.duration);

        songProgressBar = (SeekBar)rootView.findViewById(R.id.songProgressBar);
        play_pause = (ImageButton)rootView.findViewById(R.id.play_pause_button);
        forward30 = (ImageButton)rootView.findViewById(R.id.forward_30);
        back30 = (ImageButton)rootView.findViewById(R.id.back_30);

        utils = new Util();

        // toolbar
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                getActivity().onBackPressed();
            }
        });

        play_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mp.isPlaying()){
                    pause();
                } else {
                    play();
                }
            }
        });

        forward30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                forward30();
            }
        });

        back30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                back30();
            }
        });

        songProgressBar.setOnSeekBarChangeListener(this);

        rootView.setVisibility(View.INVISIBLE);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mp.stop();
    }

    private void play() {

        if (isReady) {
            mp.start();

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
            play_pause.setImageResource(R.drawable.ic_pause);
        } else {
            showWarning();
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress handler
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    private void forward30() {

        if (isReady) {
            int currentPosition = mp.getCurrentPosition();
            mp.seekTo(currentPosition + THIRTY_SECONDS);
        } else {
            showWarning();
        }
    }

    private void back30() {
        int currentPosition = mp.getCurrentPosition();
        mp.seekTo(currentPosition - THIRTY_SECONDS);
    }

    private void pause() {
        mp.pause();
        play_pause.setImageResource(R.drawable.ic_play_arrow);
    }

    private void stop() {
        mp.stop();

        try {
            mp.prepare();
            mp.seekTo(0);
        }
        catch (Throwable t) {
            throwPlayerException(t);
        }
    }

    private void setup() {
        loadClip();
    }

    private void loadClip() {
        try {
            mp = new MediaPlayer();

            mp.setOnPreparedListener(this);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(getActivity(), audioHref);
            mp.prepareAsync(); // prepare async to not block main thread
        }
        catch (Throwable t) {
            throwPlayerException(t);
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        //mp.start();

        isReady = true;
        mp.setOnCompletionListener(this);
    }

    private void throwPlayerException(Throwable t) {
        Log.v(TAG, "exception " + t.toString());
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

        mDurationView.setText(Util.formatDuration(data.getString(5)));

        audioHref = Uri.parse(data.getString(2));

        setup();

        String imageHref = data.getString(3);
        if (imageHref != null) {
            Picasso.with(getActivity()).load(imageHref)
  //               .into(mPhotoView);
            .into(myTarget);
        }

        // Converting date format
        //mDateView.setText(data.getString(6));

        SimpleDateFormat sourceFormat  = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        SimpleDateFormat desiredFormat = new SimpleDateFormat("MMM d ''yy");
        String formattedDate = null;

        try {

            Log.v(TAG, data.getString(6).substring(0,10));
            Date date = sourceFormat.parse(
                    data.getString(6).substring(0,10));
            formattedDate = desiredFormat.format(date.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mDateView.setText(formattedDate);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    // Custom Target used by Picasso to set Image and
    // set background/view colors
    private Target myTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mPhotoView.setImageBitmap(bitmap);
            setColors(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.v(TAG, "Bitmap failed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    // Sets color of views based on Palette on the image.
    // Takes the image (bitmap) as input
    public void setColors(Bitmap bitmap) {

        try {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                // Use the first theme that is non-null and set the colors
                // of the article bar.
                @Override
                public void onGenerated(Palette palette) {

                    if (palette.getDarkVibrantSwatch() !=null) {
                        setContentColors(palette.getDarkVibrantSwatch());

                    } else if(palette.getLightVibrantSwatch() !=null) {
                        setContentColors(palette.getLightVibrantSwatch());

                    } else if (palette.getLightMutedSwatch() !=null) {
                        setContentColors(palette.getLightMutedSwatch());

                    } else if (palette.getDarkMutedSwatch() != null) {
                        setContentColors(palette.getDarkMutedSwatch());

                    } else if (palette.getVibrantSwatch() != null) {
                        setContentColors(palette.getVibrantSwatch());

                    } else if (palette.getMutedSwatch() != null) {
                        setContentColors(palette.getMutedSwatch());
                    }
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "error in creating palette");
        }
    }

    // Set the colors of the views in the article bar based on the
    // supplied swatch. Also sets the up arrow color.
    private void setContentColors(Palette.Swatch swatch) {
       // articleBar.setBackgroundColor(swatch.getRgb());

        int bodyTextColor = swatch.getBodyTextColor();

        // set color of text
        mLinearLayout.setBackgroundColor(swatch.getRgb());
        mProgramView.setTextColor(bodyTextColor);
        mTitleView.setTextColor(swatch.getTitleTextColor());
        mDescriptionView.setTextColor(bodyTextColor);
        mDurationView.setTextColor(bodyTextColor);
        mDateView.setTextColor(bodyTextColor);

        // set color of buttons
        play_pause.setColorFilter(new PorterDuffColorFilter(bodyTextColor, PorterDuff.Mode.SRC_IN));
        forward30.setColorFilter(new PorterDuffColorFilter(bodyTextColor, PorterDuff.Mode.SRC_IN));
        back30.setColorFilter(new PorterDuffColorFilter(bodyTextColor, PorterDuff.Mode.SRC_IN));
        songProgressBar.getProgressDrawable().setColorFilter(
                new PorterDuffColorFilter(bodyTextColor, PorterDuff.Mode.SRC_IN));

        toolbar.getNavigationIcon().setColorFilter(
                new PorterDuffColorFilter(bodyTextColor, PorterDuff.Mode.SRC_IN));

        setOverflowIconColor(bodyTextColor);

        rootView.setVisibility(View.VISIBLE);
    }

    private void setOverflowIconColor(int color) {
        Drawable overflowIcon = toolbar.getOverflowIcon();

        if (overflowIcon != null) {
            Drawable newIcon = overflowIcon.mutate();
            newIcon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            toolbar.setOverflowIcon(newIcon);
        }
    }


    private void showWarning(){
        Snackbar snackbar = Snackbar.make(
                rootView, "The podcast is still loading. Try again in a few seconds.", Snackbar.LENGTH_SHORT);
        snackbar.show();

    }
}
