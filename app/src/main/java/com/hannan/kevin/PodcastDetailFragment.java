package com.hannan.kevin;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PodcastDetailFragment extends Fragment {

    private static final String TAG = "PodcastDetailFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve bundle .
        Bundle args = getArguments();

        int podcastId = args.getInt(PodcastSummaryFragment.PODCAST_ID);

        Log.v(TAG, "podcast id " + podcastId);

        View rootView = inflater.inflate(R.layout.fragment_podcast_detail, container, false);

        TextView podcast_id_tv = (TextView)rootView.findViewById(R.id.podcast_id);
        podcast_id_tv.setText(Integer.toString(podcastId));

        return rootView;
    }


    }
