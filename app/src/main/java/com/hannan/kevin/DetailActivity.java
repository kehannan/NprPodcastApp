package com.hannan.kevin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        // create a bundle to send data payload
        Bundle args = new Bundle();

        // get the podcast id passed from the summary fragment
        // put it in the args to pass to the detailed fragment
        args.putString(PodcastSummaryFragment.PODCAST_ID,
                getIntent().getExtras().getString(PodcastSummaryFragment.PODCAST_ID));

        //Log.v(TAG, "podcast id " + getIntent().getExtras().getInt(PodcastSummaryFragment.PODCAST_ID));
        PodcastDetailFragment fragment = new PodcastDetailFragment();
        fragment.setArguments(args);

        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }
}
