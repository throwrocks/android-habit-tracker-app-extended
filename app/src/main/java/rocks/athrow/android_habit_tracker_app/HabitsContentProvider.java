package rocks.athrow.android_habit_tracker_app;

/**
 * Created by josel on 6/11/2016.
 */

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

        import android.annotation.TargetApi;
        import android.content.ContentProvider;
        import android.content.ContentValues;
        import android.content.UriMatcher;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteQueryBuilder;
        import android.support.annotation.NonNull;
        import android.util.Log;

/**
 * Created by josel on 3/10/2016.
 */
public class HabitsContentProvider extends ContentProvider {
    private static final String LOG_TAG = "HabitsContentProvider";
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SQLiteOpenHelper mOpenHelper;

    private static final int HABITS = 100;
    private static final int HABIT_NAME = 101;


    // Declare the query builders
    private static final SQLiteQueryBuilder sHabitsQueryBuilder;

    // Set the tables for each query builder
    static {
        sHabitsQueryBuilder = new SQLiteQueryBuilder();
        sHabitsQueryBuilder.setTables(
                HabitsContract.HabitsEntry.HABITS_TABLE_NAME
        );

    }

    //sHabitsByName build the uri to get a habit by name
    private static final String sHabitsByName =
            HabitsContract.HabitsEntry.HABITS_TABLE_NAME +
                    "." + HabitsContract.HabitsEntry.habitName + " = ? ";

    /**
     * getHabitsByName
     * @param uri the country uri
     * @param projection the fields requested
     * @param sortOrder the order by parameter
     * @return the countries cursor
     */
    private Cursor getHabitsByName(Uri uri, String[] projection, String sortOrder) {
        Cursor returnCursor;

        String habitsSelection = sHabitsByName;
        //---------------------------------------------------------
        // Countries Cursor
        //---------------------------------------------------------
        String name = HabitsContract.HabitsEntry.getHabitsFromUri(uri);
        String[] habitsSelectionArgs = {
                name
        };
        Cursor habitsCursor = sHabitsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                habitsSelection,
                habitsSelectionArgs,
                null,
                null,
                sortOrder + " DESC"
        );
        habitsCursor.moveToFirst();

        returnCursor = habitsCursor;
        return returnCursor;
    }


    /**
     * buildUriMatcher
     * @return the uri matching the request
     */
    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = HabitsContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, HabitsContract.PATH_HABITS, HABITS);
        matcher.addURI(authority, HabitsContract.PATH_HABIT_NAME + "/habit/*", HABIT_NAME);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        //Log.e(LOG_TAG, "onCreate -> " + true);
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        //Log.e(LOG_TAG, "getType -> " + uri);
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        //Log.e(LOG_TAG, "getType -> " + true);
        switch (match) {
            // Student: Uncomment and fill out these two cases
            case HABITS:
                return HabitsContract.HabitsEntry.CONTENT_TYPE;
            case HABIT_NAME:
                return HabitsContract.HabitsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        //Log.e(LOG_TAG, "query -> " + uri);

        switch (sUriMatcher.match(uri)) {

            case HABITS:
            {
                //Log.e(LOG_TAG, "COUNTRIES -> " + true);

                retCursor = mOpenHelper.getReadableDatabase().query(
                        HabitsContract.HabitsEntry.HABITS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                //Log.e(LOG_TAG, "retCursor.getCount() -> " + retCursor.getCount());
                break;
            }
            case HABIT_NAME:
            {
                //Log.e(LOG_TAG, "query -> " + uri);
                retCursor = getHabitsByName(uri, projection, sortOrder);
                break;
            }
            default:
                //Log.e(LOG_TAG, "unknown uri -> " + uri);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // This causes the cursor to register a content observer
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case HABITS:
            {
                //Log.e(LOG_TAG, "Insert -> " + values.getAsString("id"));
                long _id = db.insert(HabitsContract.HabitsEntry.HABITS_TABLE_NAME, null, values);
                if (_id > 0) returnUri = HabitsContract.HabitsEntry.buildHabitsUri(_id);
                else throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case HABITS:
                rowsDeleted = db.delete(
                        HabitsContract.HabitsEntry.HABITS_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }



    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case HABITS:
                //Log.e(LOG_TAG, "updateCountries " + true);
                rowsUpdated = db.update(HabitsContract.HabitsEntry.HABITS_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HABITS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value: values) {
                        Log.e(LOG_TAG, "Lets bulk!");
                        long _id = db.insert(HabitsContract.HabitsEntry.HABITS_TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
