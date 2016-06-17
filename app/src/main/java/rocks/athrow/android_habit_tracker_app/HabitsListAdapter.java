package rocks.athrow.android_habit_tracker_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by josel on 6/9/2016.
 */
public class HabitsListAdapter extends RecyclerView.Adapter<HabitsListAdapter.ViewHolder> {

    // Set local variables
    private final String LOG_TAG = HabitsListAdapter.class.getSimpleName();
    private final Context mContext;
    private Cursor mCursor;



    public HabitsListAdapter(Context context, Cursor habitsCursor){
        mContext = context;
        mCursor = habitsCursor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView viewHabitName;
        public final TextView viewHabitDateAdded;
        public final TextView viewHabitCount;
        public final TextView viewHabitFrequency;
        public final Button viewHabitAddButton;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.habit_item);
            viewHabitName = (TextView) view.findViewById(R.id.habit_name);
            viewHabitDateAdded = (TextView) view.findViewById(R.id.habit_date_added);
            viewHabitCount = (TextView) view.findViewById(R.id.habit_count);
            viewHabitFrequency = (TextView) view.findViewById(R.id.habit_frequency);
            viewHabitAddButton = (Button) view.findViewById(R.id.habit_add_button);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(LOG_TAG, "onCreateViewHolder -> " + true);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habits_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Log.e(LOG_TAG, "onBindviewHolder -> " + true);
        mCursor.moveToPosition(position);

        final String habitId = mCursor.getString(0);
        final String habitName = mCursor.getString(1);
        final String habitCountString = mCursor.getString(2);
        final int habitCountInt = mCursor.getInt(2);
        final String habitDateAdded = mCursor.getString(3);


        holder.viewHabitName.setText(habitName);
        holder.viewHabitDateAdded.setText(habitDateAdded);
        holder.viewHabitCount.setText(habitCountString);
        holder.viewHabitFrequency.setText("100%");

        holder.viewHabitAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e(LOG_TAG,"count " + habitId);
                //Log.e(LOG_TAG,"count " + habitCountInt);
                // Increase the habit count by 1
                final int newCount = habitCountInt + 1;
                // Create a new ContentValues Object to store the data that will be updated
                final ContentValues contentValues = new ContentValues();
                String[] selectionArgs = new String[]{habitId};
                contentValues.put("count", newCount);
                contentValues.put("date_last_done","");
                mContext.getContentResolver().update(
                        HabitsContract.HabitsEntry.CONTENT_URI,
                        contentValues,
                        "_ID=?",
                        selectionArgs
                );
            }
        });
    }

    @Override
    public int getItemCount() {

        if ( mCursor != null ) {
            return mCursor.getCount();
        }else{
            return 0;
        }
    }
    /**
     * changeCursor
     * Called from the fragment to change the cursor once the data is loaded
     */
    public void changeCursor(Cursor cursor) {
        Log.e(LOG_TAG, "changeCursor -> " + cursor.getCount());
        mCursor = cursor;
    }
}
