package com.example.android.habitapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Habit app.
 */
public final class HabitContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private HabitContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.habitapp";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.habits/habits/ is a valid path for
     * looking at habit data. content://com.example.android.habits/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_HABITS = "habits";

    /**
     * Inner class that defines constant values for the habits database table.
     * Each entry in the table represents a single habit.
     */
    public static final class HabitEntry implements BaseColumns {

        /** The content URI to access the habit data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HABITS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of habits.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HABITS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single habit.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HABITS;



        /** Name of database table for habits */
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the habit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_NAME ="name";

        /**
         * Day of the the week habit was performed.
         *
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_DAYOFWEEK = "dayOfWeek";

        /**
         * Time of day the habit the habit was performed.
         *
         * The only possible values are {@link #TIMEOFDAY_MORNING}, {@link #TIMEOFDAY_AFTERNOON},
         * or {@link #TIMEOFDAY_EVENING}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_TIMEOFDAY = "timeOfDay";

        /**
         * Frequency of the habit.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_FREQUENCY = "frequency";

        /**
         * Possible values for the time of day of the habit.
         */
        public static final int TIMEOFDAY_MORNING = 0;
        public static final int TIMEOFDAY_AFTERNOON = 1;
        public static final int TIMEOFDAY_EVENING = 2;

        /**
         * Returns whether or not the given timeOfDay is {@link #TIMEOFDAY_MORNING}, {@link #TIMEOFDAY_AFTERNOON},
         * or {@link #TIMEOFDAY_EVENING}.
         */
        public static boolean isValidTimeOfDay(int timeOfDay) {
            if (timeOfDay == TIMEOFDAY_MORNING || timeOfDay == TIMEOFDAY_AFTERNOON || timeOfDay == TIMEOFDAY_EVENING) {
                return true;
            }
            return false;
        }
    }

}
