package com.hannan.kevin;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hannan.kevin.api.PodcastFetchService;
import com.hannan.kevin.data.DatabaseContract;


public class PodcastSummaryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, PodcastRecyclerAdapter.PodcastRecyclerAdapterOnClickHandler,
        LoginInterface {

    private static final String TAG = "PodcastSummaryFragment";

    public final static String PODCAST_ID = "com.hannan.kevin.podcastsummaryfragment.podcastid";
    public static final int PODCAST_LOADER = 0;

    PodcastRecyclerAdapter podcastRecyclerAdapter;
    RecyclerView recyclerView;
    Callback callback;
    Toolbar toolbar;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        public void onItemSelected(Uri uri);
    }

    public PodcastSummaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_podcast_summary, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_podcasts);

        //Set layout manager?
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        podcastRecyclerAdapter = new PodcastRecyclerAdapter(getActivity(), this);

        recyclerView.setAdapter(podcastRecyclerAdapter);
        recyclerView.addItemDecoration(new PodcastRecyclerAdapter.SimpleDividerItemDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);

        // loader
         getLoaderManager().initLoader(PODCAST_LOADER, null, this);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinator_layout);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout);

        collapsingToolbar.setTitle("Podcast Player");

        // approach from Stack Overflow:
        // http://stackoverflow.com/questions/31738831/how-to-change-collapsingtoolbarlayout-typeface-and-size
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // toolbar
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        // Integrating admob
        MobileAds.initialize(getActivity(), getResources().getString(R.string.app_id));

        AdView mAdView = (AdView) rootView.findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;

    }

    // On attaching, create reference to containing activity
    // through Callback interface
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.v(TAG, "on attach");
        callback = (Callback)activity;
    }

    // To handle the click on a view holder, defined in
    // interface in PodcastRecyclerAdapter. Takes data from
    // the adapter and passing to the containing activity
    @Override
    public void onClick(Uri uri) {
        callback.onItemSelected(uri);

    }

    private void updatePodcasts() {
        Intent intent = new Intent(getActivity(), PodcastFetchService.class);
        getActivity().startService(intent);
        Log.v(TAG, "update podcast");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),                                  // parent activity context
                DatabaseContract.PodcastTable.allPodcasts(),    // Table to query
                null,                                           // projection
                null,                                           // selection clause
                null,                                           // selection argument
                null                                            // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(TAG, "onloadFinished()");
        podcastRecyclerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //adapter.swapCursor(null);
        podcastRecyclerAdapter.swapCursor(null);
    }

    public void onLogin() {
        updatePodcasts();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.v(TAG, "onOpionsItemSelected");

        switch(item.getItemId()) {
            case R.id.action_settings:
                Log.v(TAG, "action settings");
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
