package rocks.athrow.android_habit_tracker_app;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by josel on 6/10/2016.
 */
public class HabitsContract {

    // The content authority
    public static final String CONTENT_AUTHORITY = "rocks.athrow20.android_habit_tracker_app";
    //The content uri for the top level authority
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // The path to the habits database
    public static final String PATH_HABITS = "habits";
    // The path to the habit table
    public static final String PATH_HABIT_NAME = "habit/";




    /**
     * Habit Entry
     * The inner class that defines the contents of the habits table
     */

    public static final class HabitsEntry implements BaseColumns {
        // This is the complete path to the countries database
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_HABITS)
                .build();
        // Returns multiple records
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HABITS;
        // Returns a single record
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HABIT_NAME;

        // The internal id is used by all tables
        public static final String HABITS_TABLE_NAME = "habits";

        //The habits table fields
        public static final String habitId = "id";
        public static final String habitName = "name";
        public static final String habitCount = "count";
        public static Uri buildHabitsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        // Build the habits Uri
        public static Uri buildHabits() {
            return CONTENT_URI.buildUpon().build();
        }

        // Get the habit name from the uri
        public static String getHabitsFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}