package rocks.athrow.android_habit_tracker_app;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class HabitsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public HabitsListFragment() {
    }

    public static final String LOG_TAG = HabitsListFragment.class.getSimpleName();
    Cursor mCursor;
    private HabitsListAdapter habitsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        habitsAdapter = new HabitsListAdapter(getContext(), mCursor);

        Log.e(LOG_TAG, "onCreateView -> " + true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        View recyclerView = rootView.findViewById(R.id.list);
        if ( recyclerView != null ) {
            setupRecyclerView((RecyclerView) recyclerView);
        }
        return rootView;
    }
    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /** DetailFragmentCallback for when an item has been selected.*/
        void onItemSelected(Uri dateUri);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        Log.e(LOG_TAG, "onActivityCreated -> " + true);
    }

    /**
     * setupRecyclerView
     * Method to set the adapter on the RecyclerView
     * @param recyclerView the recycler view
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.e(LOG_TAG, "setupRecyclerView -> " + true);
        Log.e(LOG_TAG, "recyclerView -> " + recyclerView);
        recyclerView.setAdapter(habitsAdapter);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        cursorLoader = new CursorLoader (
                getActivity(),
                HabitsContract.HabitsEntry.buildHabits(),
                null,
                null,
                null,
                null
        );


        Log.e(LOG_TAG, "onCreateLoader -> " + true);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Log.e(LOG_TAG, "onLoadFinished -> " + true);

        habitsAdapter.changeCursor(data);
        habitsAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
