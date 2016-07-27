package com.hannan.kevin;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannan.kevin.provider.DatabaseContract;


public class PodcastDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "PodcastDetailFragment";
    private Uri mUri;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve bundle .
        Bundle args = getArguments();

        Uri uri = Uri.parse(args.getString(PodcastSummaryFragment.PODCAST_ID));

        Log.v(TAG, "podcast id " + uri.toString());

        View rootView = inflater.inflate(R.layout.fragment_podcast_detail, container, false);

        TextView podcast_id_tv = (TextView)rootView.findViewById(R.id.podcast_id);
        podcast_id_tv.setText(uri.toString());

        return rootView;
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

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
