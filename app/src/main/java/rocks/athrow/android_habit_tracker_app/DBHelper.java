package rocks.athrow.android_habit_tracker_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by josel on 6/8/2016.
 */
class DBHelper extends SQLiteOpenHelper {

    // The database version
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "habits.db";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_HABITS_TABLE  =
                "CREATE TABLE " +
                        HabitsContract.HabitsEntry.HABITS_TABLE_NAME + " (" +
                        HabitsContract.HabitsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        HabitsContract.HabitsEntry.habitName + " TEXT NOT NULL, " +
                        HabitsContract.HabitsEntry.habitCount + " INTEGER NOT NULL, " +
                        HabitsContract.HabitsEntry.habitDateAdded + " TEXT NOT NULL, " +
                        HabitsContract.HabitsEntry.habitDateLastDone + " TEXT NULL " +

                        ")";
        sqLiteDatabase.execSQL(SQL_CREATE_HABITS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HabitsContract.HabitsEntry.HABITS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}