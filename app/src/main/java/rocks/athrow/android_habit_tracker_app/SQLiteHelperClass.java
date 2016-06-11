package rocks.athrow.android_habit_tracker_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by josel on 6/8/2016.
 */
public class SQLiteHelperClass
{
    private final String LOG_TAG = SQLiteHelperClass.class.getSimpleName();

    public static final int VERSION_NUMBER = 1;
    public static final String TABLE_HABITS = "habits";

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_COUNT = "count";

    private SQLiteDatabase db;
    public SQLiteOpenHelper sqlHelper;

    public SQLiteHelperClass(Context context)
    {
        sqlHelper =
                new SQLiteOpenHelper(context, "HabitsDatabase", null,
                        VERSION_NUMBER)
                {
                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        //drop table on upgrade
                        db.execSQL("DROP TABLE IF EXISTS "
                                + TABLE_HABITS);
                        // Create tables again
                        onCreate(db);
                    }

                    @Override
                    public void onCreate(SQLiteDatabase db) {
                     Log.e(LOG_TAG, "onCreate: " + true);
                        // creating table during onCreate
                        String createContactsTable =
                                "CREATE TABLE "
                                        + TABLE_HABITS + "("
                                        + FIELD_ID + " INTEGER PRIMARY KEY,"
                                        + FIELD_NAME + " TEXT,"
                                        + FIELD_COUNT + " INTEGER" + ")";
                        try {
                            db.execSQL(createContactsTable);
                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public synchronized void close()
                    {
                        super.close();
                        Log.d("TAG", "Database closed");
                    }

                    @Override
                    public void onOpen(SQLiteDatabase db)
                    {
                        super.onOpen(db);
                        Log.d("TAG", "Database opened");
                    }
                };
    }

    /**
     *
     * Method to insert data to the database.
     *
     */
    public void insertIntoDatabase(ContentValues contentValues)
    {
        db = sqlHelper.getWritableDatabase();

        db.insert(TABLE_HABITS, null, contentValues);


    }

    /**
     *
     * Method to read data from the database.
     *
     * Here we are reading the entire table.
     */
    public Cursor getDataFromDatabase()
    {
        int count;
        db = sqlHelper.getReadableDatabase();
        // Use of normal query to fetch data
        // fetching the whole table
        Cursor cr = db. query(TABLE_HABITS, null, null,
                null, null, null, null);
        int id;
        if(cr != null) {
            count = cr.getCount();
            cr.moveToFirst();
            Log.d("DATABASE", "count is : " + count);
            id = cr.getInt(cr.getColumnIndex(FIELD_ID));
        }

        // Use of raw query to fetch data
        // fetching the whole table
        cr = db.rawQuery("select * from " + TABLE_HABITS, null);
        if(cr != null) {
            count = cr.getCount();
            Log.d("DATABASE", "count is : " + count);
        }

        return cr;
    }

    /**
     *
     * Method to delete data from the database.
     *
     * Here we are deleting data based on name of
     * the record. All the records with this name will
     * be deleted.
     */
    public void delete(String id)
    {
        String whereClause = FIELD_ID + "=?";
        String[] whereArgs = new String[]{id};
        db = sqlHelper.getWritableDatabase();
        db.delete(TABLE_HABITS, whereClause, whereArgs);
    }

    /**
     *
     * Method to update data in database
     *
     * Here we are modifying the data based on name
     * of the record. All the records with this name will
     * be updated.
     */
    public void update(String id, ContentValues contentValues)
    {
        String whereClause = FIELD_ID + "=?";
        String[] whereArgs = new String[]{id};
        db = sqlHelper.getWritableDatabase();
        db.update(TABLE_HABITS, contentValues, whereClause, whereArgs);
    }

}
