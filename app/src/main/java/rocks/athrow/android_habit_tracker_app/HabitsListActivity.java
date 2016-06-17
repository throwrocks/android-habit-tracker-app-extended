package rocks.athrow.android_habit_tracker_app;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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

public class HabitsListActivity extends AppCompatActivity implements HabitsListFragment.Callback{
    private final String LOG_TAG = HabitsListActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.habits_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if ( fab != null ) {
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


        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setView(view)
                .setMessage("New Habit")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {


                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        //get current date time with Date()
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));

                        //get current date time with Calendar()
                        Calendar cal = Calendar.getInstance();
                        String habitDateAdded = dateFormat.format(cal.getTime());
                        //System.out.println(dateFormat.format(cal.getTime()));


                        EditText viewNewHabitName = (EditText) view.findViewById(R.id.new_habit_name);
                        String habitName = viewNewHabitName.getText().toString();
                        int habitCount = 0;
                        final ContentValues contentValues = new ContentValues();
                        contentValues.put("name",habitName);
                        contentValues.put("count",habitCount);
                        contentValues.put("date_added",habitDateAdded);

                        Log.e(LOG_TAG, "onClick: " + true);
                        getApplicationContext().getContentResolver().insert(
                                HabitsContract.HabitsEntry.CONTENT_URI,
                                contentValues
                        );
                        Log.e(LOG_TAG, "Content: " + contentValues.get("name"));
                        Context context = getApplicationContext();
                        CharSequence text = "Habit Added!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        //close();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
        // Automatically pop up the keyboard
        // See http://stackoverflow.com/questions/2403632/android-show-soft-keyboard-automatically-when-focus-is-on-an-edittext
        alertbox.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onItemSelected(Uri dateUri) {

    }
}

