package com.hannan.kevin;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class PodcastDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, MediaPlayer.OnCompletionListener {

    private static final String TAG = "PodcastDetailFragment";
    private Uri mUri;
    private static final int PODCAST_DETAIL_LOADER = 0;
    private static int THIRTY_SECONDS = 30 * 1000; // in milliseconds

    TextView podcast_title_tv;

    private ImageButton play_pause;
    private ImageButton forward30;
    private ImageButton pause;
    private ImageButton stop;
    private MediaPlayer mp;

    private Uri audioHref;

    public PodcastDetailFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve bundle .
        Bundle args = getArguments();

        mUri = Uri.parse(args.getString(PodcastSummaryFragment.PODCAST_ID));

        Log.v(TAG, "podcast id " + mUri.toString());

        View rootView = inflater.inflate(R.layout.fragment_podcast_detail, container, false);

        //TextView podcast_id_tv = (TextView)rootView.findViewById(R.id.podcast_id);
        //podcast_title_tv = (TextView)rootView.findViewById(R.id.podcast_title);
        //podcast_id_tv.setText(mUri.toString());

        // loader
        getLoaderManager().initLoader(PODCAST_DETAIL_LOADER, null, this);

        play_pause=(ImageButton)rootView.findViewById(R.id.play_pause_button);
        forward30 = (ImageButton)rootView.findViewById(R.id.forward_30);



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

        String title = data.getString(1);
//        podcast_title_tv.setText(title);

        Log.v(TAG, data.getString(2));
        audioHref = Uri.parse(data.getString(2));
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
