package rocks.athrow.android_habit_tracker_app;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HabitsListActivity extends AppCompatActivity implements HabitsListFragment.Callback {
    private final String LOG_TAG = HabitsListActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.habits_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addHabit();
                }
            });
        }
    }


    public void addHabit() {

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.habit_entry_dialog, null);

        AlertDialog newHabitDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setMessage("New Habit")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Context context = getApplicationContext();
                        int toastDuration = Toast.LENGTH_SHORT;
                        String toastText;

                        EditText viewNewHabitName = (EditText) view.findViewById(R.id.new_habit_name);
                        String habitName = viewNewHabitName.getText().toString();
                        String[] projection = new String[]{"_ID "};
                        String[] selectionArgs = new String[]{habitName};
                        Cursor queryResult;

                        queryResult = getApplicationContext().getContentResolver().query(
                                HabitsContract.HabitsEntry.CONTENT_URI,
                                projection,
                                "name=?",
                                selectionArgs,
                                null
                        );

                        int cursorCount;

                        if (queryResult != null) {
                            queryResult.moveToFirst();
                            cursorCount = queryResult.getCount();
                            queryResult.close();

                            if (cursorCount > 0) {
                                toastText = habitName + " already exists";
                                Toast toast = Toast.makeText(context, toastText, toastDuration);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {

                                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                                Date date = new Date();
                                System.out.println(dateFormat.format(date));
                                Calendar cal = Calendar.getInstance();
                                String habitDateAdded = dateFormat.format(cal.getTime());

                                int habitCount = 0;
                                final ContentValues contentValues = new ContentValues();
                                contentValues.put("name", habitName);
                                contentValues.put("count", habitCount);
                                contentValues.put("date_added", habitDateAdded);

                                getApplicationContext().getContentResolver().insert(
                                        HabitsContract.HabitsEntry.CONTENT_URI,
                                        contentValues
                                );

                                toastText = "Habit Added!";
                                Toast toast = Toast.makeText(context, toastText, toastDuration);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                        else{
                            toastText = "Oops, something wrong. Please try again.";
                            Toast toast = Toast.makeText(context, toastText, toastDuration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
        // Automatically pop up the keyboard
        // See http://stackoverflow.com/questions/2403632/android-show-soft-keyboard-automatically-when-focus-is-on-an-edittext
        newHabitDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onItemSelected(Uri dateUri) {

    }
}

