package com.hannan.kevin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
