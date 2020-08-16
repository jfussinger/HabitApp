package com.example.android.habitapp;

//https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

//https://stackoverflow.com/questions/33627915/java-lang-nullpointerexception-attempt-to-invoke-virtual-method-on-a-null-objec

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.habitapp.data.HabitContract.HabitEntry;
import com.example.android.habitapp.data.HabitDbHelper;

/**
 * Displays list of habits that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        DbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
        readData();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_DAYOFWEEK,
                HabitEntry.COLUMN_HABIT_TIMEOFDAY,
                HabitEntry.COLUMN_HABIT_FREQUENCY};

        // Perform a query on the provider using the ContentResolver.
        // Use the {@link HabitEntry#CONTENT_URI} to access the pet data.
        Cursor cursor = getContentResolver().query(
                HabitEntry.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                null,                   // Selection criteria
                null,                   // Selection criteria
                null);                  // The sort order for the returned rows

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The habits table contains <number of rows in Cursor> habits.
            // _id - name - day of week - time of day - frequency
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The habits table contains " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_DAYOFWEEK + " - " +
                    HabitEntry.COLUMN_HABIT_TIMEOFDAY + " - " +
                    HabitEntry.COLUMN_HABIT_FREQUENCY + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int dayOfWeekColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DAYOFWEEK);
            int timeOfDayColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TIMEOFDAY);
            int frequencyColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_FREQUENCY);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentdayOfWeek = cursor.getString(dayOfWeekColumnIndex);
                int currenttimeOfDay = cursor.getInt(timeOfDayColumnIndex);
                int currentFrequency = cursor.getInt(frequencyColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentdayOfWeek + " - " +
                        currenttimeOfDay + " - " +
                        currentFrequency));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //Method for Read database

    private HabitDbHelper DbHelper;

    private Cursor readData(){

        // Create and/or open a database to read from it
        SQLiteDatabase db = DbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_DAYOFWEEK,
                HabitEntry.COLUMN_HABIT_TIMEOFDAY,
                HabitEntry.COLUMN_HABIT_FREQUENCY};

        // Perform a query on the habits table
        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        //query database
        return cursor;

    }

    /**
     * Helper method to insert hardcoded habit data into the database. For debugging purposes only.
     */
    private void insertHabit() {

        // Create a ContentValues object where column names are the keys,
        // and music habit attributes are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, "Music");
        values.put(HabitEntry.COLUMN_HABIT_DAYOFWEEK, "Monday");
        values.put(HabitEntry.COLUMN_HABIT_TIMEOFDAY, HabitEntry.TIMEOFDAY_MORNING);
        values.put(HabitEntry.COLUMN_HABIT_FREQUENCY, 1);

        // Insert a new row for Music in the database, returning the ID of that new row.
        // The first argument for db.insert() is the habits table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Music.
        Uri newUri = getContentResolver().insert(HabitEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertHabit();
                displayDatabaseInfo();
                readData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

