package rocks.athrow.android_habit_tracker_app;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        //public final TextView viewhabitId;
        public final TextView viewHabitName;
        public final TextView viewHabitCount;
        public final TextView viewHabitDateAdded;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.habit_item);
            viewHabitName = (TextView) view.findViewById(R.id.habit_name);
            viewHabitCount = (TextView) view.findViewById(R.id.habit_count);
            viewHabitDateAdded = (TextView) view.findViewById(R.id.habit_date_added);


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

        //String habitId = mCursor.getString(1);
        String habitName = mCursor.getString(1);
        String habitCount = mCursor.getString(2);
        String habitDateAdded = mCursor.getString(3);


        holder.viewHabitName.setText(habitName);
        holder.viewHabitCount.setText(habitCount);
        holder.viewHabitDateAdded.setText(habitDateAdded);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = "Toast";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(mContext, text, duration);
                toast.show();
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
