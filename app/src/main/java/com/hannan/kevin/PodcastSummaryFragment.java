package com.hannan.kevin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hannan.kevin.api.PodcastFetchService;
import com.hannan.kevin.provider.DatabaseContract;


public class PodcastSummaryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "PodcastSummaryFragment";

    public static final int PODCAST_LOADER = 0;
    PodcastAdapter adapter;

    public PodcastSummaryFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        updatePodcasts();
        View rootView = inflater.inflate(R.layout.fragment_podcast_summary, container, false);
        ListView podcasts_list = (ListView) rootView.findViewById(R.id.podcast_list);

        adapter = new PodcastAdapter(getActivity(), null, 0);
        podcasts_list.setAdapter(adapter);

        // loader
        getLoaderManager().initLoader(PODCAST_LOADER, null, this);

        return rootView;

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
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}
