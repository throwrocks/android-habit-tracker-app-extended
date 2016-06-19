package rocks.athrow.android_habit_tracker_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by josel on 6/9/2016.
 */
public class HabitsListAdapter extends RecyclerView.Adapter<HabitsListAdapter.ViewHolder> {

    // Set local variables
    private final String LOG_TAG = HabitsListAdapter.class.getSimpleName();
    private final Context mContext;
    private Cursor mCursor;

    public HabitsListAdapter(Context context, Cursor habitsCursor) {
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habits_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Create a DateFormat object to format dates used in calculations
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        mCursor.moveToPosition(position);
        // Get the habit data
        final String habitId = mCursor.getString(0);
        final String habitName = mCursor.getString(1);
        final String habitCountString = mCursor.getString(2);
        final int habitCountInt = mCursor.getInt(2);
        final String habitDateAdded = mCursor.getString(3);
        // Get today's date to calculate frequency %
        Calendar cal = Calendar.getInstance();
        String dateToday = dateFormat.format(cal.getTime());
        final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
        // Initialize variables to calculate and display frequency &
        int daysSinceToday;
        double frequency;
        String frequencyString = "0%";
        // If this habit has been completed, let's calculate the frequency %
        if (habitCountInt > 0) {
            try {
                Date dateHabitDateAdded = dateFormat.parse(habitDateAdded);
                Date dateDateToday = dateFormat.parse(dateToday);
                daysSinceToday = (int) ((dateDateToday.getTime() - dateHabitDateAdded.getTime()) / DAY_IN_MILLIS) + 1;
                // Calculate the frequency
                frequency = (double) habitCountInt / (double) daysSinceToday;
                // Format the frequency into #% String
                DecimalFormat df = new DecimalFormat("#%");
                frequencyString = df.format(frequency);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        // Update the views
        holder.viewHabitName.setText(habitName);
        holder.viewHabitDateAdded.setText(habitDateAdded);
        holder.viewHabitCount.setText(habitCountString);
        holder.viewHabitFrequency.setText(frequencyString);
        // Set the onClickListener on the Add button (to handle increasing the count)
        holder.viewHabitAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast variables
                int toastDuration = Toast.LENGTH_SHORT;
                String toastText;

                // Get today's date
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Calendar cal = Calendar.getInstance();
                String dateToday = dateFormat.format(cal.getTime());

                // Check if this habit was already completed today
                String[] projection = new String[]{"date_last_done"};
                String[] selectionArgs = new String[]{habitId};
                Cursor queryResult;
                queryResult = mContext.getContentResolver().query(
                        HabitsContract.HabitsEntry.CONTENT_URI,
                        projection,
                        "_ID=?",
                        selectionArgs,
                        null
                );
                String dateLastDone;
                if (queryResult != null) {
                    queryResult.moveToFirst();
                    dateLastDone = queryResult.getString(0);
                    queryResult.close();
                    // Only increase the count if the habit hasn't been completed today
                    if (dateLastDone == null || !dateLastDone.equals(dateToday)) {
                        // Increase the habit count by 1
                        final int newCount = habitCountInt + 1;
                        // Create a new ContentValues Object to store the data that will be updated
                        final ContentValues contentValues = new ContentValues();
                        selectionArgs = new String[]{habitId};
                        contentValues.put("count", newCount);
                        contentValues.put("date_last_done", dateToday);
                        mContext.getContentResolver().update(
                                HabitsContract.HabitsEntry.CONTENT_URI,
                                contentValues,
                                "_ID=?",
                                selectionArgs
                        );
                        toastText = "Good job!";
                        Toast toast = Toast.makeText(mContext, toastText, toastDuration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        // If the habit was completed today, don't add a count
                    } else {
                        queryResult.close();
                        toastText = "You already completed this Habit today.";
                        Toast toast = Toast.makeText(mContext, toastText, toastDuration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                    // If there was a problem retrieving the cursor provide some feedback
                } else {
                    toastText = "Oops, something wrong. Please try again.";
                    Toast toast = Toast.makeText(mContext, toastText, toastDuration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        //
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Toast variables
                int toastDuration = Toast.LENGTH_SHORT;
                String toastText;
                toastText = "Long Click";
                Toast toast = Toast.makeText(mContext, toastText, toastDuration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {

        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    /**
     * changeCursor
     * Called from the fragment to change the cursor once the data is loaded
     */
    public void changeCursor(Cursor cursor) {
        mCursor = cursor;
    }
}
