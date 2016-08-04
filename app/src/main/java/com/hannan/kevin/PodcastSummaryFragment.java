package com.hannan.kevin;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannan.kevin.api.PodcastFetchService;
import com.hannan.kevin.provider.DatabaseContract;


public class PodcastSummaryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, PodcastRecyclerAdapter.PodcastRecyclerAdapterOnClickHandler {

    private static final String TAG = "PodcastSummaryFragment";

    public final static String PODCAST_ID = "com.hannan.kevin.podcastsummaryfragment.podcastid";
    public static final int PODCAST_LOADER = 0;

    PodcastRecyclerAdapter podcastRecyclerAdapter;
    RecyclerView recyclerView;
    Callback callback;
    CollapsingToolbarLayout toolbarLayout;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        updatePodcasts();
        View rootView = inflater.inflate(R.layout.fragment_podcast_summary, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_podcasts);

        //Set layout manager?
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        podcastRecyclerAdapter = new PodcastRecyclerAdapter(getActivity(), this);

        recyclerView.setAdapter(podcastRecyclerAdapter);
        recyclerView.addItemDecoration(new PodcastRecyclerAdapter.SimpleDividerItemDecoration(getActivity()));

        // loader
        getLoaderManager().initLoader(PODCAST_LOADER, null, this);

        toolbarLayout = (CollapsingToolbarLayout) rootView
                .findViewById(R.id.collapsing_toolbar_layout);

        return rootView;

    }

    // On attaching, create reference to containing activity
    // through Callback interface
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
}
